package com.manager.conferencemanagement.controller;

import com.manager.conferencemanagement.service.FileService;
import com.manager.conferencemanagement.service.ProcessInputService;
import com.manager.conferencemanagement.storage.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api")
public class RestUploadController {

    @Autowired
    StorageService storageService;

    @Autowired
    ProcessInputService processInputService;

    @Autowired
    FileService fileService;

    List<String> files = new ArrayList<String>();

    @PostMapping("/uploadfile")
    public String uploadFileMulti(
            @RequestParam("uploadfile") MultipartFile file) throws Exception {

        try {
            File fileProcessed = fileService.writeFile(processInputService.processInput(file), file.getOriginalFilename());
            storageService.store(fileProcessed);
            files.add(file.getOriginalFilename());
            return "You file was successfully uploaded and processed, please get file result - " + file.getOriginalFilename();
        } catch (Exception e) {
            throw new Exception("FAIL! Maybe You had uploaded the file before or the file's size > 500KB");
        }
    }

    @GetMapping("/getallfiles")
    public List<String> getListFiles() {
        List<String> lstFiles = new ArrayList<String>();

        try {
            lstFiles = files.stream()
                    .map(fileName -> MvcUriComponentsBuilder
                            .fromMethodName(RestUploadController.class, "getFile", fileName).build().toString())
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw e;
        }

        return lstFiles;
    }

    @GetMapping("/files/{filename:.+}")
    public ResponseEntity<Resource> getFile(@PathVariable String filename) {
        Resource file = storageService.loadFile(filename);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                .body(file);
    }
}
