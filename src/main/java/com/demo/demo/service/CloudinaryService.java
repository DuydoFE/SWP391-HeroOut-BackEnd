package com.demo.demo.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class CloudinaryService {
    private final Cloudinary cloudinary;

    public CloudinaryService() {
        this.cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "HeroOut",
                "api_key", "185993915989467",
                "api_secret", "XYxpo-av56Y23LcCPfecFYCwoUk"
        ));
    }

    public String uploadFile(MultipartFile file, String folder) throws IOException {
        Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap(
                "folder", folder
        ));
        return uploadResult.get("secure_url").toString();
    }
} 