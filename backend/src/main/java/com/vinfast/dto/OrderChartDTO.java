package com.vinfast.dto;

public class OrderChartDTO {
    private String date;
    private Long count;

    public OrderChartDTO(String date, Long count) {
        this.date = date;
        this.count = count;
    }

    public String getDate() {
        return date;
    }

    public Long getCount() {
        return count;
    }
}
