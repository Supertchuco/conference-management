package com.manager.conferencemanagement.vo;

import lombok.Data;

@Data
public class FileProcessed {

    private String fileName;

    private String fileResult;

    public FileProcessed(String fileName, String fileResult){
        this.fileName = fileName;
        this.fileResult = fileResult;
    }

}
