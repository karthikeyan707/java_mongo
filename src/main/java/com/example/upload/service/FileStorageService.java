package com.example.upload.service;

import com.mongodb.client.gridfs.model.GridFSFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class FileStorageService {

    @Autowired
    private GridFsTemplate gridFsTemplate;

    public String storeFile(MultipartFile file) throws IOException {
        return gridFsTemplate.store(
                file.getInputStream(),
                file.getOriginalFilename(),
                file.getContentType()
        ).toString();
    }

    public GridFSFile getFile(String id) {
        return gridFsTemplate.findOne(
                Query.query(Criteria.where("_id").is(id))
        );
    }
}
