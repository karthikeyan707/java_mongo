package com.example.upload.controller;

import com.example.upload.service.FileStorageService;
import com.mongodb.client.gridfs.model.GridFSFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class FileController {

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private GridFsTemplate gridFsTemplate;

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @PostMapping("/upload")
    public String upload(@RequestParam("file") MultipartFile file, Model model) throws Exception {
        String id = fileStorageService.storeFile(file);
        model.addAttribute("fileId", id);
        return "index";
    }

    @GetMapping("/files/{id}")
    public ResponseEntity<?> getFile(@PathVariable String id) throws Exception {
        GridFSFile file = fileStorageService.getFile(id);
        GridFsResource resource = gridFsTemplate.getResource(file);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(file.getMetadata().getString("_contentType")))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + file.getFilename() + "\"")
                .body(new InputStreamResource(resource.getInputStream()));
    }
}
