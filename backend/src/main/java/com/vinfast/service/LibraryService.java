package com.vinfast.service;

import com.vinfast.entity.Car;
import com.vinfast.entity.Library;
import com.vinfast.form.CreateCarForm;
import com.vinfast.form.UploadImageForm;
import com.vinfast.repository.LibraryRepository;
import com.vinfast.util.ImageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class LibraryService implements ILibraryService {

    @Autowired
    private ImageUtils imageUtils;
    @Autowired
    LibraryRepository libraryRepository;

    @Override
    public void createLibrary(Car car, CreateCarForm createCarForm) {
        System.out.println(car.getId());
        try {
            for (MultipartFile multipartFile : createCarForm.getImages()) {
                Library library = new Library();
                UploadImageForm uploadImageForm = imageUtils.uploadFile(multipartFile);
                library.setUrlLink(uploadImageForm.getUrl());
                library.setPublicId(uploadImageForm.getPublicId());
                library.setTitle(uploadImageForm.getUrl());
                library.setCar(car);
                libraryRepository.save(library);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
