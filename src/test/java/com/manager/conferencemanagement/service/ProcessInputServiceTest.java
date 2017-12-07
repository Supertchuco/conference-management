package com.manager.conferencemanagement.service;

import com.manager.conferencemanagement.vo.Lecture;
import com.manager.conferencemanagement.vo.LecturesEventDay;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Collection;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProcessInputServiceTest {

    int[] inputTimes1 = new int[]{60, 15, 30, 45, 15};
    int[] inputTimes2 = new int[]{60, 15, 45, 15};
    int[] inputTimes3 = new int[]{30, 60, 30, 45};
    int[] inputTimes4 = new int[]{30, 15, 45, 15};

    @Autowired
    ProcessInputService processInputService;


    @Test
    public void findLectureByMinorTimeTest() {
        Assert.assertEquals(15, processInputService.findLectureByMinorTime(buildLectureCollection(inputTimes1)).getTime());
    }

    @Test
    public void findLectureByMaxTimeTest() {
        Assert.assertEquals(60, processInputService.findLectureByMaxTime(buildLectureCollection(inputTimes1)).getTime());
    }

    @Test
    public void findLectureBySpecificTimeTest() {
        Assert.assertEquals(15, processInputService.findLectureBySpecificTime(buildLectureCollection(inputTimes1), 15).getTime());
        Assert.assertEquals(45, processInputService.findLectureBySpecificTime(buildLectureCollection(inputTimes1), 45).getTime());
        Assert.assertEquals(null, processInputService.findLectureBySpecificTime(buildLectureCollection(inputTimes1), 55));
    }

    @Test
    public void findLectureByMinorNearTimeTest() {
        Assert.assertEquals(15, processInputService.findLectureByMinorNearTime(buildLectureCollection(inputTimes2), 30).getTime());
        Assert.assertEquals(45, processInputService.findLectureByMinorNearTime(buildLectureCollection(inputTimes2), 45).getTime());
        Assert.assertEquals(null, processInputService.findLectureByMinorNearTime(buildLectureCollection(inputTimes3), 15));
        Assert.assertEquals(45, processInputService.findLectureByMinorNearTime(buildLectureCollection(inputTimes4), 60).getTime());
    }

    @Test
    public void findLectureByMaxNearTimeTest() {
        Assert.assertEquals(45, processInputService.findLectureByMaxNearTime(buildLectureCollection(inputTimes2), 45).getTime());
        Assert.assertEquals(15, processInputService.findLectureByMaxNearTime(buildLectureCollection(inputTimes2), 30).getTime());
        Assert.assertEquals(45, processInputService.findLectureByMaxNearTime(buildLectureCollection(inputTimes4), 60).getTime());
    }

    @Test
    public void findLectureToRemoveTimePeriodTest() {
        Assert.assertEquals(60, processInputService.findLectureToRemoveTimePeriod(buildLectureCollection(inputTimes2), 60).getTime());
        Assert.assertEquals(45, processInputService.findLectureToRemoveTimePeriod(buildLectureCollection(inputTimes4), 60).getTime());
        Assert.assertEquals(null, processInputService.findLectureToRemoveTimePeriod(buildLectureCollection(inputTimes3), 15));
    }

    @Test
    public void findLectureToCompleteTimePeriodTest() {
        Assert.assertEquals(60, processInputService.findLectureToCompleteTimePeriod(buildLectureCollection(inputTimes2), 60).getTime());
        Assert.assertEquals(45, processInputService.findLectureToCompleteTimePeriod(buildLectureCollection(inputTimes4), 60).getTime());
        Assert.assertEquals(null, processInputService.findLectureToCompleteTimePeriod(buildLectureCollection(inputTimes3), 15));
    }

    @Test
    public void calculateMorningMinutesTest() {
        Assert.assertEquals(180, processInputService.calculateMorningMinutes());
    }

    @Test
    public void calculateAfternoonMinutesTest() {
        Assert.assertEquals(240, processInputService.calculateAfternoonMinutes());
    }

    private Collection<Lecture> buildLectureCollection(int times[]) {
        Collection<Lecture> collectionReturn = new ArrayList<>();
        for (int time : times) {
            collectionReturn.add(new Lecture(time, "Test - " + time));
        }
        return collectionReturn;
    }

    private LecturesEventDay buildLecturesInput() {
        Collection<Lecture> result = new ArrayList<>();
        result.add(new Lecture(60, "Writing Fast Tests Against Enterprise Rails"));
        result.add(new Lecture(45, " Overdoing it in Python"));
        result.add(new Lecture(30, "Lua for the Masses"));
        result.add(new Lecture(45, "Ruby Errors from Mismatched Gem Versions"));
        result.add(new Lecture(45, "Common Ruby Errors"));
        result.add(new Lecture(60, "Communicating Over Distance"));
        result.add(new Lecture(45, "Accounting-Driven Development"));
        result.add(new Lecture(30, "Woah"));
        result.add(new Lecture(30, "Sit Down and Write"));
        result.add(new Lecture(45, "Pair Programming vs Noise"));
        result.add(new Lecture(60, " Rails Magic"));
        result.add(new Lecture(60, "Ruby on Rails: Why We Should Move On"));
        result.add(new Lecture(45, "Clojure Ate Scala (on my project)"));
        result.add(new Lecture(30, "Programming in the Boondocks of Seattle"));
        result.add(new Lecture(30, "Ruby vs. Clojure for Back-End Development"));
        result.add(new Lecture(60, "Ruby on Rails Legacy App Maintenance"));
        result.add(new Lecture(30, "A World Without HackerNews"));
        result.add(new Lecture(30, "User Interface CSS in Rails Apps"));
        return new LecturesEventDay(result);
    }

}

