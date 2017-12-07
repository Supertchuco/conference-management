package com.manager.conferencemanagement.service;


import com.manager.conferencemanagement.vo.Lecture;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.tomcat.util.http.fileupload.FileItem;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItem;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.util.LinkedHashSet;


@Service
public class FileService {

    public String readFile(MultipartFile file) throws IOException {
        ByteArrayInputStream stream = new ByteArrayInputStream(file.getBytes());
        return IOUtils.toString(stream, "UTF-8");
    }

    public LinkedHashSet<Lecture> buildLectures(String inputText) {
        LinkedHashSet<Lecture> lectures = new LinkedHashSet();
        String[] lines = StringUtils.split(inputText, "\r\n");
        for (String line : lines) {
            try {
                lectures.add(new Lecture(Integer.parseInt(StringUtils.substring(line, StringUtils.lastIndexOf(line, " "), StringUtils.lastIndexOf(line, "min")).trim()), StringUtils.substring(line, 0, StringUtils.lastIndexOf(line, " "))));
            } catch (Exception e) {
                System.out.println("error in this line: " + line);
            }

        }
        return lectures;
    }

    public File writeFile(String text, String fileName) throws IOException {
        BufferedWriter output = null;
        File file = null;
        try {
            file = new File(fileName);
            output = new BufferedWriter(new FileWriter(file));
            output.write(text);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (output != null) {
                output.close();
            }
        }
        return file;
    }

    public MultipartFile createMultipartFile(String text, String fileName) throws IOException {
        File file = writeFile(text, fileName);


        FileItem fileItem = new DiskFileItem("mainFile", Files.probeContentType(file.toPath()), false, file.getName(), (int) file.length(), file.getParentFile());

        try {
            InputStream input = new FileInputStream(file);
            OutputStream os = fileItem.getOutputStream();
            IOUtils.copy(input, os);
        } catch (IOException ex) {
            // do something.
        }

        return null;

    }

}
