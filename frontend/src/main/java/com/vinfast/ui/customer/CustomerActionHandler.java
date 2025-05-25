package com.vinfast.ui.customer;

import com.vinfast.api.CustomerApi;
import com.vinfast.dto.CarDTO;
import com.vinfast.dto.CustomerDTO;
import com.vinfast.dto.OrderDTO;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.Node;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class CustomerActionHandler {

    public static void searchCustomers(String keyword, TableView<CustomerDTO> tableView) {
        new Thread(() -> {
            try {
                List<CustomerDTO> customers = CustomerApi.searchCustomers(keyword);
                Platform.runLater(() -> tableView.setItems(FXCollections.observableArrayList(customers)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public static void loadAllCustomers(TableView<CustomerDTO> tableView) {
        new Thread(() -> {
            try {
                List<CustomerDTO> customers = CustomerApi.getAllCustomers();
                Platform.runLater(() -> tableView.setItems(FXCollections.observableArrayList(customers)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public static CustomerDTO addCustomer() {
        Dialog<CustomerDTO> dialog = new Dialog<>();
        dialog.setTitle("Thêm khách hàng");
        dialog.setHeaderText("Nhập thông tin khách hàng mới");

        TextField name = new TextField();
        name.setPromptText("Tên khách hàng");

        TextField phone = new TextField();
        phone.setPromptText("Số điện thoại");

        TextField email = new TextField();
        email.setPromptText("Email");

        TextField address = new TextField();
        address.setPromptText("Địa chỉ");

        VBox vbox = new VBox(
                8,
                new Label("Tên khách hàng:"), name,
                new Label("Số điện thoại:"), phone,
                new Label("Email:"), email,
                new Label("Địa chỉ:"), address
        );
        vbox.setPadding(new Insets(13));
        dialog.getDialogPane().setContent(vbox);
        dialog.setWidth(200);

        ButtonType addButtonType = new ButtonType("Thêm", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        Button addButton = (Button) dialog.getDialogPane().lookupButton(addButtonType);
        addButton.addEventFilter(ActionEvent.ACTION, event -> {
            if (!Notifications.validateName(name.getText()) ||
                    !Notifications.validatePhone(phone.getText()) ||
                    Notifications.isPhoneExistOnline(phone.getText()) ||
                    !Notifications.validateEmail(email.getText()) ||
                    Notifications.isEmailExistOnline(email.getText()) ||
                    !Notifications.validateAddress(address.getText())) {
                event.consume(); // Chặn đóng nếu sai
            }
        });

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                CustomerDTO customerDTO = new CustomerDTO();
                customerDTO.setName(name.getText());
                customerDTO.setPhone(phone.getText());
                customerDTO.setEmail(email.getText());
                customerDTO.setAddress(address.getText());
                return customerDTO;
            }
            return null;
        });

        Optional<CustomerDTO> result = dialog.showAndWait();
        return result.orElse(null);
    }


    public static boolean updateCustomer(Long customerId) {
        CustomerDTO selectedCustomer = CustomerApi.getCustomerById(customerId);

        if (selectedCustomer == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Khách hàng không tồn tại!");
            alert.setHeaderText("Lỗi");
            alert.showAndWait();
            return false;
        }

        Dialog<CustomerDTO> dialog = new Dialog<>();
        dialog.setTitle("Cập nhật khách hàng");
        dialog.setHeaderText("Cập nhật thông tin cho khách hàng ID: " + customerId);

        // Tạo các trường nhập liệu và điền sẵn thông tin
        TextField name = new TextField(selectedCustomer.getName());
        TextField phone = new TextField(selectedCustomer.getPhone());
        TextField email = new TextField(selectedCustomer.getEmail());
        TextField address = new TextField(selectedCustomer.getAddress());

        name.setPrefWidth(200);
        phone.setPrefWidth(200);
        email.setPrefWidth(200);
        address.setPrefWidth(200);

        // Form layout
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10));
        grid.add(new Label("Tên khách hàng:"), 0, 0);
        grid.add(name, 1, 0);
        grid.add(new Label("Số điện thoại:"), 0, 1);
        grid.add(phone, 1, 1);
        grid.add(new Label("Email:"), 0, 2);
        grid.add(email, 1, 2);
        grid.add(new Label("Địa chỉ:"), 0, 3);
        grid.add(address, 1, 3);

        dialog.getDialogPane().setContent(grid);

        // Nút lưu và hủy
        ButtonType saveButtonType = new ButtonType("Lưu", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        Button saveButton = (Button) dialog.getDialogPane().lookupButton(saveButtonType);
        saveButton.addEventFilter(ActionEvent.ACTION, event -> {
            String newName = name.getText().trim();
            String newPhone = phone.getText().trim();
            String newEmail = email.getText().trim();
            String newAddress = address.getText().trim();

            boolean valid = true;

            if (!Notifications.validateName(newName)) {
                valid = false;
            }

            if (!Notifications.validatePhone(newPhone)) {
                valid = false;
            } else if (!newPhone.equals(selectedCustomer.getPhone()) && Notifications.isPhoneExistOnline(newPhone)) {
                valid = false;
            }

            if (!Notifications.validateEmail(newEmail)) {
                valid = false;
            } else if (!newEmail.equals(selectedCustomer.getEmail()) && Notifications.isEmailExistOnline(newEmail)) {
                valid = false;
            }

            if (!Notifications.validateAddress(newAddress)) {
                valid = false;
            }

            if (!valid) {
                event.consume(); // Ngăn đóng dialog nếu có lỗi
            }
        });

        Optional<CustomerDTO> result = dialog.showAndWait();
        if (result.isPresent()) {
            // Kiểm tra xem có thay đổi gì không
            boolean isChanged = false;

            String newName = name.getText().trim();
            String newPhone = phone.getText().trim();
            String newEmail = email.getText().trim();
            String newAddress = address.getText().trim();

            if (!newName.equals(selectedCustomer.getName())) {
                selectedCustomer.setName(newName);
                isChanged = true;
            }
            if (!newPhone.equals(selectedCustomer.getPhone())) {
                selectedCustomer.setPhone(newPhone);
                isChanged = true;
            }
            if (!newEmail.equals(selectedCustomer.getEmail())) {
                selectedCustomer.setEmail(newEmail);
                isChanged = true;
            }
            if (!newAddress.equals(selectedCustomer.getAddress())) {
                selectedCustomer.setAddress(newAddress);
                isChanged = true;
            }

            if (isChanged) {
                CustomerApi.updateCustomer(selectedCustomer);
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Lưu thông tin khách hàng thành công!");
                alert.setHeaderText("Thành công");
                alert.showAndWait();
                return true; // Trả về true nếu có thay đổi và cập nhật thành công
            }
        }
        return false; // Trả về false nếu không có thay đổi hoặc hủy
    }

    public static void deleteCustomer(TableView<CustomerDTO> tableView) {
        CustomerDTO selected = tableView.getSelectionModel().getSelectedItem();

        if (selected == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Cảnh báo");
            alert.setHeaderText("Chưa chọn khách hàng");
            alert.setContentText("Vui lòng chọn một khách hàng trước khi xóa.");
            alert.showAndWait();
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Xác nhận");
        alert.setHeaderText("Bạn có chắc chắn muốn xóa khách hàng này không?");
        alert.setContentText("Tên: " + selected.getName());

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                CustomerApi.deleteCustomer(selected.getId());
                tableView.getItems().remove(selected);
                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setTitle("Thành công");
                successAlert.setHeaderText("Đã xóa khách hàng");
                successAlert.setContentText("Khách hàng \"" + selected.getName() + "\" đã được xóa thành công.");
                successAlert.showAndWait();

            } catch (Exception e) {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("Lỗi");
                errorAlert.setHeaderText("Xóa thất bại");
                errorAlert.setContentText("Không thể xóa khách hàng. Vui lòng thử lại.");
                errorAlert.showAndWait();
            }
        }
    }

}
