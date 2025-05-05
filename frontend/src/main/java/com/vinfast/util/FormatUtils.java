package com.vinfast.util;

import java.text.DecimalFormat;

public class FormatUtils {
    private static final DecimalFormat formatter = new DecimalFormat("#,###");

    public static String formatPrice(long price) {
        return formatter.format(price) + " ₫";
    }
}
