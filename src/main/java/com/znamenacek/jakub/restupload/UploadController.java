package com.znamenacek.jakub.restupload;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;

@RestController
@RequestMapping(path = "/api")
public class UploadController {
    @Autowired
    private UploadService uploadService;


    @PostMapping(path = "/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile uploadFile){
        if(uploadFile.isEmpty()){
            return new ResponseEntity<>("File wasn't uploaded", HttpStatus.OK);
        }

        uploadService.saveUploadedFiles(Arrays.asList(uploadFile));


        return new ResponseEntity("Successfully uploaded - " +
                uploadFile.getOriginalFilename(), new HttpHeaders(), HttpStatus.OK);

    }


}
