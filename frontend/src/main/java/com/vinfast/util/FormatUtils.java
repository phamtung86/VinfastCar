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
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd MMMM yyyy", new Locale("vi", "VN")); // Định dạng ngày tháng theo kiểu tiếng Việt
        return sdf.format(date);
    }
}
