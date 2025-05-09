package com.vinfast.service;


import com.vinfast.entity.Car;
import com.vinfast.form.CreateCarForm;

public interface ILibraryService {

    void createLibrary(Car car, CreateCarForm createCarForm);
}
