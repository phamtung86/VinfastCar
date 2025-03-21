package com.vinfast.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BrandDTO {
    private int id;

    private String name;

    private List<CarDTO> cars;
}
