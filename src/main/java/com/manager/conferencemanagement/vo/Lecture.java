package com.manager.conferencemanagement.vo;

import lombok.Data;

import java.time.LocalTime;

@Data
public class Lecture {
    int time;
    String name;
    LocalTime startTimeEvent;


    public Lecture(int time, String name) {
        this.time = time;
        this.name = name;
    }

    public Lecture(int time, String name, LocalTime startTimeEvent) {
        this.time = time;
        this.name = name;
        this.startTimeEvent = startTimeEvent;
    }

}
