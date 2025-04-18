package com.vinfast.dto;

import java.math.BigDecimal;
import java.util.Date;

public class ReportDTO {
    private Long id;
    private String reportType;
    private Date startDate;
    private Date endDate;
    private BigDecimal totalSales;
    private int totalOrders;
    private Date createdAt;
}
