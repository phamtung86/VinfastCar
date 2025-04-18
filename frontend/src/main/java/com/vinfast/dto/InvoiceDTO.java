package com.vinfast.dto;


import java.math.BigDecimal;
import java.util.Date;


public class InvoiceDTO {

    private Long invoiceId;
    private OrderDTO order;
    private Date invoiceDate;
    private BigDecimal totalAmount;
    private String paymentMethod;
    private String status;
}
