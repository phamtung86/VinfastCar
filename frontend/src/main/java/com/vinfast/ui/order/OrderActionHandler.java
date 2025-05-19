package com.vinfast.ui.order;

import com.vinfast.api.CarApi;
import com.vinfast.api.OrderApi;
import com.vinfast.dto.CarDTO;
import com.vinfast.dto.LibraryDTO;
import com.vinfast.dto.OrderDTO;
import com.vinfast.ui.alert.AlertNotice;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import java.io.File;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.function.Consumer;

public class OrderActionHandler {
    private final TableView<OrderDTO> orderTable;
    private final Consumer<List<OrderDTO>> updateTableCallback;
    private AlertNotice alertNotice = new AlertNotice();
    private final OrderApi orderApi = new OrderApi();
    private final CarApi carApi = new CarApi();
    private final List<CarDTO> carOption = carApi.getCarByStatus("AVAILABLE");

    public OrderActionHandler(TableView<OrderDTO> orderTable, Consumer<List<OrderDTO>> updateTableCallback) {
        this.orderTable = orderTable;
        this.updateTableCallback = updateTableCallback;
    }

    public void handleEditOrder() {
        OrderDTO selectedOrder = orderTable.getSelectionModel().getSelectedItem();
        if (selectedOrder != null) {
            Dialog<OrderDTO> dialog = new Dialog<>();
            dialog.setTitle("Sửa Thông Tin Hóa Đơn");
            dialog.setHeaderText("Chỉnh sửa thông tin Hóa Đơn");

            ButtonType saveButtonType = new ButtonType("Lưu", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

            // Các trường nhập liệu
            TextField idField = new TextField(String.valueOf(selectedOrder.getId()));
            TextField nameCustomerField = new TextField(selectedOrder.getCustomerName());
            TextField idCustomerField = new TextField(String.valueOf(selectedOrder.getCustomerId()));
            TextField nameCarField = new TextField(selectedOrder.getCar().getName());
            TextField totalAmountField = new TextField(String.valueOf(selectedOrder.getTotalAmount()));

            // Chuyển Date -> LocalDate để hiển thị trong DatePicker
            LocalDate localDate = selectedOrder.getOrderDate().toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();
            DatePicker orderDatePicker = new DatePicker(localDate);

            TextField statusField = new TextField(selectedOrder.getStatus());

            // Giao diện nhập liệu
            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new Insets(20, 150, 10, 10));

            grid.add(new Label("ID hóa đơn:"), 0, 0);
            grid.add(idField, 1, 0);
            grid.add(new Label("Tên khách hàng:"), 0, 1);
            grid.add(nameCustomerField, 1, 1);
            grid.add(new Label("ID khách hàng:"), 0, 2);
            grid.add(idCustomerField, 1, 2);
            grid.add(new Label("Tên xe:"), 0, 3);
            grid.add(nameCarField, 1, 3);
            grid.add(new Label("Tổng tiền:"), 0, 4);
            grid.add(totalAmountField, 1, 4);
            grid.add(new Label("Ngày tạo:"), 0, 5);
            grid.add(orderDatePicker, 1, 5);
            grid.add(new Label("Trạng thái:"), 0, 6);
            grid.add(statusField, 1, 6);

            dialog.getDialogPane().setContent(grid);

            // Xử lý lưu
            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == saveButtonType) {
                    selectedOrder.setId(Long.parseLong(idField.getText()));
                    selectedOrder.setCustomerName(nameCustomerField.getText());
                    selectedOrder.setCustomerId(Integer.parseInt(idCustomerField.getText()));
                    selectedOrder.getCar().setName(nameCarField.getText());
                    selectedOrder.setTotalAmount(Long.parseLong(totalAmountField.getText()));

                    // Chuyển LocalDate -> Date
                    LocalDate selectedLocalDate = orderDatePicker.getValue();
                    Date date = Date.from(selectedLocalDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
                    selectedOrder.setOrderDate(date);

                    selectedOrder.setStatus(statusField.getText());
                    return selectedOrder;
                }
                return null;
            });

            Optional<OrderDTO> result = dialog.showAndWait();
            result.ifPresent(editedOrder -> {
                // Gọi API lưu nếu cần
                orderApi.editOrder(editedOrder);
                orderTable.refresh();
            });
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Chưa chọn hóa đơn");
            alert.setHeaderText(null);
            alert.setContentText("Vui lòng chọn một hóa đơn để sửa.");
            alert.showAndWait();
        }
    }
public void handleAddOrder() {
    Dialog<OrderDTO> dialog = new Dialog<>();
    dialog.setTitle("Thêm Đơn Hàng Mới");
    dialog.setHeaderText("Nhập thông tin đơn hàng mới");

    TextField customerNameField = new TextField();
    customerNameField.setPromptText("Tên khách hàng");

    TextField customerIdField = new TextField();
    customerIdField.setPromptText("ID khách hàng");

    ComboBox<CarDTO> carComboBox = new ComboBox<>(FXCollections.observableArrayList(carOption));
    carComboBox.setPromptText("Chọn xe");

    TextField totalAmountField = new TextField();
    totalAmountField.setPromptText("Tổng tiền");

    TextField paymentMethodField = new TextField();
    paymentMethodField.setPromptText("Phương thức thanh toán");

    TextField statusField = new TextField();
    statusField.setPromptText("Trạng thái");

    VBox vbox = new VBox(10, customerNameField, customerIdField, carComboBox, totalAmountField, paymentMethodField, statusField);
    vbox.setPadding(new Insets(10));

    dialog.getDialogPane().setContent(vbox);
    ButtonType addButtonType = new ButtonType("Thêm", ButtonBar.ButtonData.OK_DONE);
    dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

    dialog.setResultConverter(dialogButton -> {
        if (dialogButton == addButtonType) {
            try {
                OrderDTO newOrder = new OrderDTO();
                newOrder.setId(0L); // ID sẽ được tạo tự động từ server
                newOrder.setCustomerName(customerNameField.getText());
                newOrder.setCustomerId(Integer.parseInt(customerIdField.getText()));
                newOrder.setCar(carComboBox.getValue());
                newOrder.setTotalAmount(Long.parseLong(totalAmountField.getText()));
                newOrder.setPaymentMethod(paymentMethodField.getText());
                newOrder.setStatus(statusField.getText());
                newOrder.setOrderDate(new Date()); // Ngày hiện tại
                return newOrder;
            } catch (NumberFormatException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Lỗi nhập liệu");
                alert.setHeaderText("Vui lòng nhập đúng định dạng số trong các trường ID khách hàng và Tổng tiền.");
                alert.showAndWait();
                System.out.println(e);
            }
        }
        return null;
    });

    Optional<OrderDTO> result = dialog.showAndWait();
    result.ifPresent(order -> {
        // Gọi hàm thêm đơn hàng
        new Thread(() -> {
            try {
                orderApi.addOrder(order);
                Platform.runLater(() -> {
                    reloadData(); // Tải lại dữ liệu
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Thành công");
                    alert.setHeaderText("Thêm đơn hàng thành công");
                    alert.showAndWait();
                });
            } catch (Exception ex) {
                Platform.runLater(() -> {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Lỗi");
                    alert.setHeaderText("Có lỗi xảy ra khi thêm đơn hàng");
                    alert.showAndWait();
                });
            }
        }).start();
    });
}
    private void reloadData() {
        updateTableCallback.accept(null); // Gọi callback để reload dữ liệu
    }


}
