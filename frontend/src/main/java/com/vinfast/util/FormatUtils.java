package com.vinfast.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class FormatUtils {
    private static final DecimalFormat formatter = new DecimalFormat("#,###");

    public static String formatPrice(long price) {
        return formatter.format(price) + " ₫";
    }

    public static String formatDateVietnamese(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy", Locale.forLanguageTag("vi")); // Định dạng ngày tháng theo kiểu tiếng Việt
        return sdf.format(date);
    }

    public static String formatCurrency(BigDecimal amount) {
        if (amount == null) {
            return "0 ₫";
        }
        // Định dạng tiền tệ (ví dụ: 1.000.000 VND)
        java.text.NumberFormat numberFormat = java.text.NumberFormat.getInstance(java.util.Locale.getDefault());
        return numberFormat.format(amount) + " ₫";
    }
}
