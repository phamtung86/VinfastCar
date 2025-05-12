package com.vinfast.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class OrderChartDTO {
    private String date;
    private Long count;

    public OrderChartDTO() {
    }
    @JsonCreator
    public OrderChartDTO(@JsonProperty("count") Long count,@JsonProperty("date") String date) {
        this.count = count;
        this.date = date;
    }

    // Getters và setters
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }
}
