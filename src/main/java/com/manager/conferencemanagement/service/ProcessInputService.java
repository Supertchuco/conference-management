package com.manager.conferencemanagement.service;


import com.manager.conferencemanagement.vo.Lecture;
import com.manager.conferencemanagement.vo.LecturesEventDay;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalTime;
import java.util.*;

@Service
public class ProcessInputService {

    @Autowired
    FileService fileService;

    public String processInput(MultipartFile file) throws IOException {
        String fileText = fileService.readFile(file);
        LinkedHashSet<Lecture> lectures = fileService.buildLectures(fileText);
        LecturesEventDay lecturesEventDay = new LecturesEventDay(lectures);
        Collection<LecturesEventDay> LecturesEventDayList = processEventDays(lecturesEventDay);
        return buildReport(LecturesEventDayList);
    }

    public Collection<LecturesEventDay> processEventDays(LecturesEventDay lecturesEventDay) {
        Collection<LecturesEventDay> LecturesEventDayListReturn = new ArrayList<>();

        while (lecturesEventDay.getAvailable().size() > 0) {

            if (!CollectionUtils.isEmpty(lecturesEventDay.getAvailable())) {
                lecturesEventDay = organizeLecturesPeriod(lecturesEventDay, 180, true);
            }

            if (!CollectionUtils.isEmpty(lecturesEventDay.getAvailable())) {
                lecturesEventDay = organizeLecturesPeriod(lecturesEventDay, 240, false);
            }


            LecturesEventDayListReturn.add(lecturesEventDay);
            if (lecturesEventDay.getAvailable().size() == 0) {
                break;
            } else {
                lecturesEventDay = new LecturesEventDay(lecturesEventDay.getAvailable());
            }
        }
        return LecturesEventDayListReturn;
    }


    private StringBuffer buildTrackEventInfo(StringBuffer stringBuffer, Collection<Lecture> lecturesList) {
        for (Lecture lecture : lecturesList) {
            stringBuffer.append(lecture.getStartTimeEvent()).append(" ").append(lecture.getName()).append(" ").append(lecture.getTime()).append("min").append(System.getProperty("line.separator"));
        }
        return stringBuffer;
    }

    private String buildReport(Collection<LecturesEventDay> LecturesEventDayList) {
        StringBuffer stringBuffer = new StringBuffer();
        int index = 1;
        for (LecturesEventDay lecturesEventDay : LecturesEventDayList) {
            stringBuffer.append("Track " + index).append(System.getProperty("line.separator"));
            stringBuffer = buildTrackEventInfo(stringBuffer, lecturesEventDay.getMorningPeriod());
            stringBuffer = buildTrackEventInfo(stringBuffer, lecturesEventDay.getAfternoonPeriod());
            index++;
        }

        return stringBuffer.toString();
    }

    public Lecture findLectureByMinorTime(Collection<Lecture> lecturesInput) {

        return lecturesInput.stream()
                .min(Comparator.comparingInt(Lecture::getTime))
                .get();
    }

    public Lecture findLectureByMaxTime(Collection<Lecture> lecturesInput) {

        return lecturesInput.stream()
                .max(Comparator.comparingInt(Lecture::getTime))
                .get();
    }

    public Lecture findLectureBySpecificTime(Collection<Lecture> lecturesInput, int specificTime) {

        return lecturesInput.stream().filter(o -> o.getTime() == specificTime).findFirst().orElse(null);

    }

    public Lecture findLectureByMinorNearTime(Collection<Lecture> lecturesInput, int inputTime) {
        Lecture lectureReturn = null;
        int minorLectureTime = findLectureByMinorTime(lecturesInput).getTime();

        while (inputTime >= minorLectureTime) {
            lectureReturn = findLectureBySpecificTime(lecturesInput, inputTime);
            if (Objects.isNull(lectureReturn)) {
                inputTime = inputTime - 15;
            } else {
                break;
            }
        }
        return lectureReturn;
    }

    public Lecture findLectureByMaxNearTime(Collection<Lecture> lecturesInput, int inputTime) {
        Lecture lectureReturn = null;
        int minorLectureTime = findLectureByMinorTime(lecturesInput).getTime();
        while (inputTime >= minorLectureTime) {
            lectureReturn = findLectureBySpecificTime(lecturesInput, inputTime);
            if (Objects.isNull(lectureReturn)) {
                inputTime--;
            } else {
                break;
            }
        }
        return lectureReturn;
    }

    public Lecture findLectureToRemoveTimePeriod(Collection<Lecture> lecturesInput, int lectureTime) {
        Lecture lectureReturn = findLectureBySpecificTime(lecturesInput, lectureTime);
        if (Objects.isNull(lectureReturn)) {
            lectureReturn = findLectureByMaxNearTime(lecturesInput, lectureTime);
        }
        return lectureReturn;
    }

    public Lecture findLectureToCompleteTimePeriod(Collection<Lecture> lecturesInput, int lectureTime) {
        Lecture lectureReturn = findLectureBySpecificTime(lecturesInput, lectureTime);
        if (Objects.isNull(lectureReturn)) {
            lectureReturn = findLectureByMinorNearTime(lecturesInput, lectureTime);
        }
        return lectureReturn;
    }

    public Lecture findLastLectureElement(Collection<Lecture> lecturesInput) {
        return lecturesInput.stream().reduce((first, second) -> second)
                .orElse(null);
    }

    public int calculateMorningMinutes() {
        LocalTime initialEventTime = LocalTime.of(9, 0);
        LocalTime lunchTime = LocalTime.of(12, 0);
        Long morningPeriod = Duration.between(initialEventTime, lunchTime).toMinutes();
        return morningPeriod.intValue();
    }

    public int calculateAfternoonMinutes() {
        LocalTime afterLunch = LocalTime.of(13, 0);
        LocalTime finalEventTime = LocalTime.of(17, 0);
        Long afternoon = Duration.between(afterLunch, finalEventTime).toMinutes();
        return afternoon.intValue();
    }

    public LecturesEventDay organizeLecturesPeriod(LecturesEventDay lecturesEventDay, int maxTime, boolean morningPeriod) {
        int total = 0;
        maxTime = (morningPeriod) ? maxTime : maxTime - 10;
        Collection<Lecture> outputLecture = new ArrayList<>();
        List<Lecture> availableLecture = new ArrayList<>(lecturesEventDay.getAvailable());
        LocalTime localTime = (morningPeriod) ? LocalTime.of(9, 00) : LocalTime.of(13, 00);
        boolean exitLoop = false;
        while (availableLecture.size() > 0 && exitLoop == false) {
            if (total == maxTime) {
                break;
            } else if (total > maxTime) {
                Lecture lectureTemp = findLectureToRemoveTimePeriod(outputLecture, total - maxTime);
                if (Objects.nonNull(lectureTemp)) {
                    lectureTemp.setStartTimeEvent(null);
                    outputLecture.remove(lectureTemp);
                    availableLecture.add(lectureTemp);
                    exitLoop = true;
                }
            } else {
                Lecture input = availableLecture.get(0);
                input.setStartTimeEvent(localTime);
                availableLecture.remove(input);
                outputLecture.add(input);
                total += input.getTime();
                localTime = localTime.plusMinutes(input.getTime());
                if (input.getTime() == 45 || input.getTime() == 15) {
                    Lecture inputAux = findLectureBySpecificTime(availableLecture, input.getTime());
                    if (Objects.nonNull(inputAux)) {
                        inputAux.setStartTimeEvent(localTime);
                        outputLecture.add(inputAux);
                        availableLecture.remove(inputAux);
                        total += inputAux.getTime();
                        localTime = localTime.plusMinutes(input.getTime());
                    }
                }
            }
        }

        Lecture lastLecture = findLastLectureElement(outputLecture);
        localTime = lastLecture.getStartTimeEvent().plusMinutes(lastLecture.getTime());
        outputLecture.add(new Lecture(60, (morningPeriod) ? "Lunch" : "Networking Event", localTime));
        return new LecturesEventDay(lecturesEventDay, availableLecture, outputLecture, morningPeriod);
    }

}
