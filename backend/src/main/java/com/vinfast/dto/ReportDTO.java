package com.vinfast.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportDTO {
    private Long id;
    private String reportType;
    private Date startDate;
    private Date endDate;
    private BigDecimal totalSales;
    private int totalOrders;
    private Date createdAt;
}
