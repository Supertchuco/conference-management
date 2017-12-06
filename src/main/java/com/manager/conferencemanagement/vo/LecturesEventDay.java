package com.manager.conferencemanagement.vo;

import lombok.Data;

import java.util.ArrayList;
import java.util.Collection;

@Data
public class LecturesEventDay {
    private Collection<Lecture> morningPeriod;
    private Collection<Lecture> afternoonPeriod;
    private Collection<Lecture> available;


    public LecturesEventDay(Collection<Lecture> available) {
        this.morningPeriod = new ArrayList<>();
        this.afternoonPeriod = new ArrayList<>();
        this.available = available;
    }

    public LecturesEventDay(LecturesEventDay lecturesEventDay, Collection<Lecture> availableLecture, Collection<Lecture> lecturesProcessed, boolean morningPeriod) {
        if (morningPeriod) {
            this.morningPeriod = lecturesProcessed;
            this.afternoonPeriod = lecturesEventDay.getAfternoonPeriod();
        } else {
            this.morningPeriod = lecturesEventDay.getMorningPeriod();
            this.afternoonPeriod = lecturesProcessed;
        }
        this.available = availableLecture;
    }
}
