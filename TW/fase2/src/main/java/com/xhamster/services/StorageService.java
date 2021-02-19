package com.xhamster.services;

import com.xhamster.exceptions.StorageException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class StorageService {

    @Value("${images.pathToSave}")
    private String pathToSave;
    
    @Value("${images.pathToLoad}")
    private String pathToLoad;

    public String uploadFile(MultipartFile file) {

        if (file.isEmpty()) {

            throw new StorageException("Failed to store empty file");
        }

        try {
            var fileName = file.getOriginalFilename();
            var is = file.getInputStream();

            Files.copy(is, Paths.get(pathToSave + fileName),
                    StandardCopyOption.REPLACE_EXISTING);
	    return pathToLoad + fileName;
        } catch (IOException e) {

            var msg = String.format("Failed to store file %s", file.getName());

            throw new StorageException(msg, e);
        }
    }
}

