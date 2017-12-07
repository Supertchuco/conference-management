package com.manager.conferencemanagement.service;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FileServiceTest {

    @Autowired
    FileService fileService;

    String correctFileString = "\uFEFFWriting Fast Tests Against Enterprise Rails 60min\r\n" +
            "Overdoing it in Python 45min\r\n" +
            "Lua for the Masses 30min\r\n" +
            "Ruby Errors from Mismatched Gem Versions 45min\r\n" +
            "Common Ruby Errors 45min\r\n" +
//
            "Communicating Over Distance 60min\r\n" +
            "Accounting-Driven Development 45min\r\n" +
            "Woah 30min\r\n" +
            "Sit Down and Write 30min\r\n" +
            "Pair Programming vs Noise 45min\r\n" +
            "Rails Magic 60min\r\n" +
            "Ruby on Rails: Why We Should Move On 60min\r\n" +
            "Clojure Ate Scala (on my project) 45min\r\n" +
            "Programming in the Boondocks of Seattle 30min\r\n" +
            "Ruby vs. Clojure for Back-End Development 30min\r\n" +
            "Ruby on Rails Legacy App Maintenance 60min\r\n" +
            "A World Without HackerNews 30min\r\n" +
            "User Interface CSS in Rails Apps 30min";

    String incorrectFileString = "Writing Fast Tests Against Enterprise Rails 60min\n" +
            "Overdoing it in Python 45min\n" +
            "Lua for the Masses 30min\n" +
            "Ruby Errors from Mismatched Gem Versions 45min\n" +
            "Common Ruby Errors 45min\n" +
            "Rails for Python Developers lightning\n" +
            "Communicating Over Distance 60min\n" +
            "Accounting-Driven Development 45min\n" +
            "Woah 30min\n" +
            "Sit Down and Write 30min\n" +
            "Pair Programming vs Noise 45min\n" +
            "Rails Magic 60min\n" +
            "Ruby on Rails: Why We Should Move On 60min\n" +
            "Clojure Ate Scala (on my project) 45min\n" +
            "Programming in the Boondocks of Seattle 30min\n" +
            "Ruby vs. Clojure for Back-End Development 30min\n" +
            "Ruby on Rails Legacy App Maintenance 60min\n" +
            "A World Without HackerNews 30min\n" +
            "User Interface CSS in Rails Apps 30min";

    String specificErrors = "Pair Programming vs Noise 45min\r\n" +
            "Programming in the Boondocks of Seattle 30min";

    @Test
    public void buildLecturesTest() {
        Assert.assertEquals(2, fileService.buildLectures(specificErrors).size());
        Assert.assertEquals(18, fileService.buildLectures(correctFileString).size());
        Assert.assertEquals(18, fileService.buildLectures(incorrectFileString).size());
        Assert.assertEquals(0, fileService.buildLectures("").size());
    }
}
