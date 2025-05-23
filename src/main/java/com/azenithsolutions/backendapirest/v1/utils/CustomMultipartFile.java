package com.azenithsolutions.backendapirest.v1.utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class CustomMultipartFile implements MultipartFile {
    private final MultipartFile originalFile;
    private final String newFileName;

    public CustomMultipartFile(MultipartFile originalFile, String newFileName) {
        this.originalFile = originalFile;
        this.newFileName = newFileName;
    }

    @Override
    public String getName() {
        return originalFile.getName();
    }

    @Override
    public String getOriginalFilename() {
        return newFileName;
    }

    @Override
    public String getContentType() {
        return originalFile.getContentType();
    }

    @Override
    public boolean isEmpty() {
        return originalFile.isEmpty();
    }

    @Override
    public long getSize() {
        return originalFile.getSize();
    }

    @Override
    public byte[] getBytes() throws IOException {
        return originalFile.getBytes();
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return originalFile.getInputStream();
    }

    @Override
    public void transferTo(File dest) throws IOException, IllegalStateException {
        originalFile.transferTo(dest);
    }
}