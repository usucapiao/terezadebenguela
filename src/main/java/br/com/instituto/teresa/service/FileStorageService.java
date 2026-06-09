package br.com.instituto.teresa.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
public class FileStorageService {

    private static final Logger log = LoggerFactory.getLogger(FileStorageService.class);
    private static final String UPLOADS_PREFIX = "/uploads/";
    private static final String UPLOAD_DIR = "./uploads/";

    /**
     * Deletes the physical file only if the URL refers to an uploaded file
     * (starts with "/uploads/"). Static assets in /assets/ are never touched.
     */
    public void deleteIfUploaded(String url) {
        if (url == null || !url.startsWith(UPLOADS_PREFIX)) {
            return;
        }
        String filename = url.substring(UPLOADS_PREFIX.length());
        try {
            Files.deleteIfExists(Paths.get(UPLOAD_DIR + filename));
        } catch (IOException e) {
            log.warn("Não foi possível excluir o arquivo: {}", url, e);
        }
    }
}
