package com.spring.GramSevaPortal.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.spring.GramSevaPortal.DTO.ImageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@Service
public class ImageUploadService {

    @Autowired
    private Cloudinary cloudinary;

    @Autowired
    ImageData imageData;
    public ImageData upload(MultipartFile file) {
        try {
            Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
            imageData.setId(uploadResult.get("public_id").toString());
            imageData.setUrl(uploadResult.get("secure_url").toString());  // ✅ Return image URL
            return imageData;
        } catch (Exception e) {
            throw new RuntimeException("Image upload failed");
        }
    }

    public String uploadPdf(MultipartFile file) {
        try {
            Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
            return uploadResult.get("secure_url").toString();  // ✅ Return image URL
        } catch (Exception e) {
            throw new RuntimeException("Image upload failed");
        }
    }

    public String deleteImage(String publicId) {
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("invalidate", true);

            Map result = cloudinary.uploader().destroy(publicId, params);

            return result.get("result").toString();  // "ok" or "not found"
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete image: " + e.getMessage());
        }
    }
}

