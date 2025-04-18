package com.vinfast.dto;

public class LibraryDTO {

    private int id;

    private String urlLink;

    private String title;

    private Integer carId;

    public LibraryDTO() {
    }

    public LibraryDTO(int id, String urlLink, String title, Integer carId) {
        this.id = id;
        this.urlLink = urlLink;
        this.title = title;
        this.carId = carId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUrlLink() {
        return urlLink;
    }

    public void setUrlLink(String urlLink) {
        this.urlLink = urlLink;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getCarId() {
        return carId;
    }

    public void setCarId(Integer carId) {
        this.carId = carId;
    }
}
