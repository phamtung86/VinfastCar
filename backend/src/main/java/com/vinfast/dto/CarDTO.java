package com.vinfast.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.vinfast.entity.Car;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CarDTO {

    private Integer id;

    private String name;

    private int year;

    private String status;

    private double odo;

    private String original;

    private String style;

    private String gear;

    private String engine;

    private String colorOut;

    private String colorIn;

    private int slotSeats;

    private int slotDoor;

    private String driveTrain;

    private long price;

    private Car.CarStatus carStatus;

    private Integer inventoryId;

    private String inventoryName;

    private  List<LibraryDTO> libraries;

}
