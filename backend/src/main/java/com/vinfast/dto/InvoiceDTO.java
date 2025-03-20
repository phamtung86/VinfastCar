package com.vinfast.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InvoiceDTO {

    private Long invoiceId;
    private OrderDTO order;
    private Date invoiceDate;
    private BigDecimal totalAmount;
    private String paymentMethod;
    private String status;
}
