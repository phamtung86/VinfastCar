package com.vinfast.util;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.vinfast.form.UploadImageForm;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.UUID;

@Service

public class ImageUtils {

    private final Cloudinary cloudinary;
    @Autowired
    public ImageUtils(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    public UploadImageForm uploadFile(MultipartFile file) throws IOException {
        if (file == null || file.getOriginalFilename() == null) {
            throw new IllegalArgumentException("File or filename cannot be null");
        }

        String publicValue = generatePublicValue(file.getOriginalFilename());

        String extension = getFileName(file.getOriginalFilename())[1];
        File fileUpload = convert(file);

        // Tải file lên Cloudinary và lấy kết quả
        Map uploadResult = cloudinary.uploader().upload(fileUpload, ObjectUtils.asMap("public_id", publicValue));
        cleanDisk(fileUpload);

        // Lấy public_id và tạo URL
        String publicId = (String) uploadResult.get("public_id");
        String url = cloudinary.url().generate(StringUtils.join(publicValue, ".", extension));

        return new UploadImageForm(url, publicId);
    }

    private File convert(MultipartFile file) throws IOException {
        assert file.getOriginalFilename() != null;
        File convFile = new File(StringUtils.join(generatePublicValue(file.getOriginalFilename()), getFileName(file.getOriginalFilename())[1]));
        try(InputStream is = file.getInputStream()) {
            Files.copy(is, convFile.toPath());
        }
        return convFile;
    }

    private void cleanDisk(File file) {
        try {
            Path filePath = file.toPath();
            Files.delete(filePath);
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public String generatePublicValue(String originalName){
        String fileName = getFileName(originalName)[0];
        return StringUtils.join(UUID.randomUUID().toString(), "_", fileName);
    }

    public String[] getFileName(String originalName) {
        return originalName.split("\\.");
    }

    public void deleteFile(String publicId) throws IOException {
        cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
    }
    public String extractPublicIdFromUrl(String url) {
        if (url == null || url.isEmpty()) return null;
        try {

            String[] parts = url.split("/");
            String fileWithExt = parts[parts.length - 1];
            return fileWithExt.substring(0, fileWithExt.lastIndexOf("."));
        } catch (Exception e) {
            return null;
        }
    }

}
