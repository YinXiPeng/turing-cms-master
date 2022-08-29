package com.bonc.turing.cms.exercise.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ImportService {
    String importPoi(MultipartFile file) throws Exception;
}
