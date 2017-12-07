package com.manager.conferencemanagement;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.Resource;

@SpringBootApplication
public class ConferenceManagementApplication implements CommandLineRunner {

    @Resource
    com.manager.conferencemanagement.storage.StorageService storageService;

    public static void main(String[] args) {
        SpringApplication.run(ConferenceManagementApplication.class, args);
    }

    @Override
    public void run(String... args) {
        storageService.deleteAll();
        storageService.init();
    }
}
