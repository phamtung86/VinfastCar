package com.vinfast.ui.customer;

import com.vinfast.api.CustomerApi;
import javafx.scene.control.Alert;

import java.util.List;

public class Notifications {
    public static boolean validateName(String name) {
        if (name.trim().isEmpty()) {
            showAlert("Cảnh báo", "Tên khách hàng không được để trống! Vui lòng nhập lại");
            return false;
        }
        return true;
    }

    public static boolean validatePhone(String phone) {
        if (!phone.matches("\\d{10}")) {
            showAlert("Cảnh báo", "Số điện thoại phải có 10 chữ số! Vui lòng nhập lại");
            return false;
        }
        return true;
    }

    public static boolean validateEmail(String email) {
        if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$")) {
            showAlert("Cảnh báo", "Email không hợp lệ! Vui lòng nhập đúng định dạng (ví dụ: abc@gmail.com)");
            return false;
        }
        return true;
    }

    public static boolean validateAddress(String address) {
        if(address.trim().isEmpty()) {
            showAlert("Cảnh báo","Địa chỉ không được để trống! Vui lòng nhập lại");
            return false;
        }
        return true;
    }

    public static boolean validateCarId(String carIdText) {
        if (carIdText == null || carIdText.trim().isEmpty()) {
            showAlert("Cảnh báo", "Mã xe không được để trống.");
            return false;
        }

        try {
            Integer.parseInt(carIdText);
            return true;
        } catch (NumberFormatException e) {
            showAlert("Cảnh báo", "Mã xe không hợp lệ! Vui lòng nhập một số nguyên.");
            return false;
        }
    }

    public static boolean validatePaymentMethod(String payment) {
        if (payment == null || payment.trim().isEmpty()) {
            showAlert("Cảnh báo", "Vui lòng chọn phương thức thanh toán!");
            return false;
        }
        return true;
    }

    public static boolean validateStatus(String status) {
        if (status == null || status.trim().isEmpty()) {
            showAlert("Cảnh báo", "Vui lòng chọn trạng thái đơn hàng!");
            return false;
        }
        return true;
    }

    public static boolean isEmailExistOnline(String email) {
        if (CustomerApi.isEmailExist(email)) {
            showAlert("Cảnh báo", "Email này đã tồn tại trong hệ thống!");
            return true;
        }
        return false;
    }

    public static boolean isPhoneExistOnline(String phone) {
        if (CustomerApi.isPhoneExist(phone)) {
            showAlert("Cảnh báo", "Số điện thoại này đã tồn tại trong hệ thống!");
            return true;
        }
        return false;
    }

    private static void showAlert(String header, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING, message);
        alert.setHeaderText(header);
        alert.showAndWait();
    }
}
