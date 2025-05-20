package com.vinfast.ui.order;

import com.vinfast.api.CarApi;
import com.vinfast.api.CustomerApi;
import com.vinfast.api.OrderApi;
import com.vinfast.dto.CarDTO;
import com.vinfast.dto.CustomerDTO;
import com.vinfast.dto.OrderDTO;
import com.vinfast.ui.alert.AlertNotice;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.*;
import java.util.function.Consumer;

public class OrderActionHandler {
    private final TableView<OrderDTO> orderTable;
    private final Consumer<List<OrderDTO>> updateTableCallback;
    private AlertNotice alertNotice = new AlertNotice();
    private final OrderApi orderApi = new OrderApi();
    private final CarApi carApi = new CarApi();
    private final List<String> paymethod = Arrays.asList("Bank", "Cash", "Installment");
    private final List<String> listStatus = Arrays.asList("Pending", "Processing", "Completed");


    public static List<CustomerDTO> getAllCus() throws IOException, InterruptedException {
        List<CustomerDTO> list = CustomerApi.getAllCustomers();
        return list;
    }
    public Map<String, CustomerDTO> getCustomerMap() throws IOException, InterruptedException {
        Map<String, CustomerDTO> map = new HashMap<>();
        List<CustomerDTO> customerDTOS = getAllCus();
        for (CustomerDTO customer : customerDTOS) {
            String display = customer.getId() + " " + customer.getName();
            map.put(display, customer);
        }
        return map;
    }




    private final List<CarDTO> carOption = carApi.getCarByStatus("AVAILABLE");

    public Map<String, CarDTO> getCarMap() {
        Map<String, CarDTO> carMap = new HashMap<>();
        for (CarDTO car : carOption) {
            String display = car.getId().toString() + " " + car.getName();
            carMap.put(display, car);
        }
        return carMap;
    }



    public OrderActionHandler(TableView<OrderDTO> orderTable, Consumer<List<OrderDTO>> updateTableCallback) {
        this.orderTable = orderTable;
        this.updateTableCallback = updateTableCallback;
    }
    public void handleAddOrder() throws IOException, InterruptedException {
        Dialog<OrderDTO> dialog = new Dialog<>();
        dialog.setTitle("Thêm Đơn Hàng Mới");
        dialog.setHeaderText("Nhập thông tin đơn hàng mới");

        // Dữ liệu map
        Map<String, CustomerDTO> cusMap = getCustomerMap();
        Map<String, CarDTO> carMap = getCarMap();

        // ComboBox
        ComboBox<String> customerComboBox = new ComboBox<>(FXCollections.observableArrayList(cusMap.keySet()));
        customerComboBox.setPromptText("Chọn khách hàng");

        ComboBox<String> carComboBox = new ComboBox<>(FXCollections.observableArrayList(carMap.keySet()));
        carComboBox.setPromptText("Chọn xe");

        // TextFields
        TextField totalAmountField = new TextField();
        totalAmountField.setPromptText("Tổng tiền");
        totalAmountField.setEditable(false); // Không cho người dùng chỉnh sửa tay

        ComboBox<String> paymentMethodCombobox = new ComboBox<>(FXCollections.observableArrayList(paymethod));
        paymentMethodCombobox.setPromptText("Phương thức thanh toán");

        ComboBox<String> statusCombobox = new ComboBox<>(FXCollections.observableArrayList(listStatus));
        statusCombobox.setPromptText("Trạng thái");

        // Khi chọn xe thì tự fill giá
        carComboBox.setOnAction(event -> {
            String selected = carComboBox.getValue();
            if (selected != null && carMap.containsKey(selected)) {
                CarDTO selectedCar = carMap.get(selected);
                totalAmountField.setText(String.valueOf(selectedCar.getPrice()));
            }
        });

// Giao diện GridPane đẹp hơn
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        // Thiết lập độ rộng cột
        ColumnConstraints col1 = new ColumnConstraints(120); // cột nhãn
        ColumnConstraints col2 = new ColumnConstraints(220); // cột input
        grid.getColumnConstraints().addAll(col1, col2);

        // Đặt độ rộng cố định cho các ComboBox và TextField
        customerComboBox.setPrefWidth(220);
        carComboBox.setPrefWidth(220);
        paymentMethodCombobox.setPrefWidth(220);
        statusCombobox.setPrefWidth(220);
        totalAmountField.setPrefWidth(220);

        // Thêm margin để đẹp hơn
        GridPane.setMargin(customerComboBox, new Insets(0, 0, 10, 0));
        GridPane.setMargin(carComboBox, new Insets(0, 0, 10, 0));
        GridPane.setMargin(paymentMethodCombobox, new Insets(0, 0, 10, 0));
        GridPane.setMargin(statusCombobox, new Insets(0, 0, 10, 0));
        GridPane.setMargin(totalAmountField, new Insets(0, 0, 10, 0));

        grid.add(new Label("Khách hàng:"), 0, 0);
        grid.add(customerComboBox, 1, 0);
        grid.add(new Label("Xe:"), 0, 1);
        grid.add(carComboBox, 1, 1);
        grid.add(new Label("Tổng tiền:"), 0, 2);
        grid.add(totalAmountField, 1, 2);
        grid.add(new Label("Phương thức thanh toán:"), 0, 3);
        grid.add(paymentMethodCombobox, 1, 3);
        grid.add(new Label("Trạng thái:"), 0, 4);
        grid.add(statusCombobox, 1, 4);
        dialog.getDialogPane().setContent(grid);
        ButtonType addButtonType = new ButtonType("Thêm", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);
        // Xử lý kết quả
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                try {
                    if (customerComboBox.getValue() == null || carComboBox.getValue() == null ||
                            paymentMethodCombobox.getValue().isEmpty() || statusCombobox.getValue().isEmpty()) {
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("Thiếu thông tin");
                        alert.setHeaderText("Vui lòng nhập đầy đủ thông tin!");
                        alert.showAndWait();
                        return null;
                    }

                    OrderDTO newOrder = new OrderDTO();
                    newOrder.setId(0L); // ID server tự tạo

                    String selectedCustomerStr = customerComboBox.getValue();
                    CustomerDTO selectedCustomer = cusMap.get(selectedCustomerStr);
                    newOrder.setCustomerId(Math.toIntExact(selectedCustomer.getId()));
                    newOrder.setCustomerName(selectedCustomer.getName());

                    String selectedCarStr = carComboBox.getValue();
                    CarDTO selectedCar = carMap.get(selectedCarStr);
                    newOrder.setCar(selectedCar);

                    newOrder.setTotalAmount(Long.parseLong(totalAmountField.getText()));
                    newOrder.setPaymentMethod(paymentMethodCombobox.getValue());
                    newOrder.setStatus(statusCombobox.getValue());
                    newOrder.setOrderDate(new Date());

                    return newOrder;
                } catch (Exception e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Lỗi");
                    alert.setHeaderText("Vui lòng kiểm tra lại dữ liệu nhập.");
                    alert.setContentText(e.getMessage());
                    alert.showAndWait();
                    return null;
                }
            }
            return null;
        });
        Optional<OrderDTO> result = dialog.showAndWait();
        result.ifPresent(order -> {
            new Thread(() -> {
                try {
                    orderApi.addOrder(order);

                    // (Tùy chọn) Cập nhật trạng thái xe thành SOLD
                    CarDTO carToUpdate = order.getCar();
                    carToUpdate.setStatus("RESERVED");
                    CarApi.updateCarStatus(Long.valueOf(carToUpdate.getId()),"RESERVED");

                    Platform.runLater(() -> {
                        reloadData();
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Thành công");
                        alert.setHeaderText("Thêm đơn hàng thành công");
                        alert.showAndWait();
                    });
                } catch (Exception ex) {
                    ex.printStackTrace();
                    Platform.runLater(() -> {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Lỗi");
                        alert.setHeaderText("Có lỗi xảy ra khi thêm đơn hàng");
                        alert.setContentText(ex.getMessage());
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
