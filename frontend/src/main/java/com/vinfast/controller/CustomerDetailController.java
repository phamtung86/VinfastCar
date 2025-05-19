package com.vinfast.controller;

import com.vinfast.api.CustomerApi;
import com.vinfast.dto.CustomerDTO;
import com.vinfast.dto.CarDTO;
import com.vinfast.dto.OrderDTO;
import com.vinfast.ui.car.CarDetailView;
import com.vinfast.ui.customer.CustomerActionHandler;
import com.vinfast.util.FormatUtils;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class CustomerDetailController implements Initializable {
    @FXML private Label lblCustomerId;
    @FXML private Label lblCustomerName;
    @FXML private Label lblEmail;
    @FXML private Label lblPhone;
    @FXML private Label lblAddress;

    @FXML
    private TableView<OrderDTO> tableView;

    @FXML
    private TableColumn<OrderDTO, Integer> colSerial;

    @FXML
    private TableColumn<OrderDTO, Integer> colIdCar;

    @FXML
    private TableColumn<OrderDTO, String> colNameCar;

    @FXML
    private TableColumn<OrderDTO, Double> colOdo;

    @FXML
    private TableColumn<OrderDTO, Integer> colYear;

    @FXML
    private TableColumn<OrderDTO, String> colPurchaseDate;

    @FXML
    private TableColumn<OrderDTO, String> colPayment;

    @FXML
    private TableColumn<OrderDTO, String> colPrice;

    @FXML
    private TableColumn<OrderDTO, String> colOrderStatus;

    private ObservableList<OrderDTO> orderList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setTable();
        selectRow();
        setColumn();
    }

    public void setTable() {
        colSerial.setCellValueFactory(cellData -> new SimpleIntegerProperty(
                orderList.indexOf(cellData.getValue()) + 1).asObject());

        colIdCar.setCellValueFactory(cellData ->
                new SimpleIntegerProperty(cellData.getValue().getCar() != null ? cellData.getValue().getCar().getId() : 0).asObject());

        colNameCar.setCellValueFactory(cellData ->
                        new SimpleStringProperty(
                                cellData.getValue().getCar() != null ? cellData.getValue().getCar().getName() : "")
        );

        colOdo.setCellValueFactory(cellData ->
                new SimpleDoubleProperty(cellData.getValue().getCar().getOdo()).asObject());

        colYear.setCellValueFactory(cellData ->
                new SimpleIntegerProperty(cellData.getValue().getCar().getYear()).asObject());

        colPurchaseDate.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getOrderDate() != null
                        ? FormatUtils.formatDateVietnamese(cellData.getValue().getOrderDate())
                        : "")
        );

        colPayment.setCellValueFactory(cellData ->
                new SimpleStringProperty(
                        cellData.getValue().getPaymentMethod() != null
                                ? cellData.getValue().getPaymentMethod()
                                : "")
        );

        colPrice.setCellValueFactory(cellData ->
                new SimpleStringProperty(
                        cellData.getValue().getCar() != null
                                ? FormatUtils.formatPrice(Long.valueOf(cellData.getValue().getCar().getPrice()))
                                : ""
                )
        );

        colOrderStatus.setCellValueFactory(cellData ->
                new SimpleStringProperty(
                        cellData.getValue().getStatus() != null ? cellData.getValue().getStatus().toString() : ""
                )
        );

        tableView.setItems(orderList);
    }

    public void selectRow() {
        tableView.setRowFactory(tv -> {
            TableRow<OrderDTO> row = new TableRow<>();

            // Kiểm tra nếu hàng có dữ liệu (không phải là hàng trống)
            row.setOnMouseEntered(event -> {
                if (!row.isEmpty()) {  // Nếu hàng không trống
                    row.setStyle("-fx-cursor: hand;");
                }
            });

            // Đặt lại kiểu con trỏ khi chuột rời khỏi hàng
            row.setOnMouseExited(event -> {
                if (!row.isEmpty()) {  // Nếu hàng không trống
                    row.setStyle("-fx-cursor: default;");
                }
            });

            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    OrderDTO order = row.getItem();  // Lấy đối tượng OrderDTO từ dòng được chọn
                    if (order != null && order.getCar() != null) {
                        showCarDetail(order.getCar());  // Hiển thị chi tiết xe
                    }
                }
            });

            return row;
        });

    }

    public void setColumn() {
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        for (TableColumn<?, ?> column : tableView.getColumns()) {
            column.setMaxWidth(1f * Integer.MAX_VALUE);
        }

        colSerial.setStyle("-fx-alignment: CENTER;");
        colIdCar.setStyle("-fx-alignment: CENTER;");
        colNameCar.setStyle("-fx-alignment: CENTER;");
        colOdo.setStyle("-fx-alignment: CENTER;");
        colYear.setStyle("-fx-alignment: CENTER;");
        colPurchaseDate.setStyle("-fx-alignment: CENTER;");
        colPayment.setStyle("-fx-alignment: CENTER;");
        colPrice.setStyle("-fx-alignment: CENTER;");
        colOrderStatus.setStyle("-fx-alignment: CENTER;");
    }

    public void setCustomerDetail(CustomerDTO customer) {
        if (customer == null) return;

        lblCustomerId.setText(customer.getId() != 0 ? String.valueOf(customer.getId()) : "N/A");
        lblCustomerName.setText(customer.getName() != null ? customer.getName() : "N/A");
        lblEmail.setText(customer.getEmail() != null ? customer.getEmail() : "N/A");
        lblPhone.setText(customer.getPhone() != null ? customer.getPhone() : "N/A");
        lblAddress.setText(customer.getAddress() != null ? customer.getAddress() : "N/A");

        orderList.clear();
        if (customer.getOrders() != null) {
            orderList.addAll(customer.getOrders());
        }

        if (!orderList.isEmpty()) {
            tableView.getSelectionModel().selectFirst();
        } else {
            tableView.setPlaceholder(new javafx.scene.control.Label("Khách hàng chưa có đơn hàng."));
        }
    }

    private void showCarDetail(CarDTO car) {
        CarDetailView carDetailView = new CarDetailView();
        carDetailView.show(car);
    }

    @FXML
    private void handleAddOrder() {
        try {
            Long customerId = Long.parseLong(lblCustomerId.getText());
            if (customerId == 0) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Cảnh báo");
                alert.setHeaderText(null);
                alert.setContentText("Mã khách hàng không hợp lệ");
                alert.showAndWait();
                return;
            }

            // Hiển thị dialog thêm đơn hàng, trả về OrderDTO hoặc null nếu hủy
            OrderDTO newOrder = CustomerActionHandler.addOrder();
            if (newOrder == null) {
                return;
            }

            // Gửi yêu cầu thêm đơn hàng cho khách hàng lên API
            CustomerDTO updatedCustomer = CustomerApi.addOrderToCustomer(customerId, newOrder);
            if (updatedCustomer != null) {
                setCustomerDetail(updatedCustomer);

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Thông báo");
                alert.setHeaderText(null);
                alert.setContentText("Thêm đơn hàng thành công!");
                alert.showAndWait();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Lỗi");
                alert.setHeaderText(null);
                alert.setContentText("Thêm đơn hàng thất bại!");
                alert.showAndWait();
            }
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Lỗi");
            alert.setHeaderText(null);
            alert.setContentText("Mã khách hàng không hợp lệ");
            alert.showAndWait();
        }
    }

    @FXML
    private void handleUpdateOrder() {
        OrderDTO selectedOrder = tableView.getSelectionModel().getSelectedItem();
        if (selectedOrder == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Cảnh báo");
            alert.setHeaderText(null);
            alert.setContentText("Vui lòng chọn đơn hàng để cập nhật trạng thái.");
            alert.showAndWait();
            return;
        }

        // Lấy customerId từ label
        Long customerId;
        try {
            customerId = Long.parseLong(lblCustomerId.getText());
            if (customerId == 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Lỗi");
            alert.setHeaderText(null);
            alert.setContentText("Mã khách hàng không hợp lệ.");
            alert.showAndWait();
            return;
        }

        // Hiển thị dialog cập nhật trạng thái đơn hàng, trả về Optional<String> trạng thái mới
        Optional<String> newStatusOpt = CustomerActionHandler.updateOrderStatus(selectedOrder);

        if (newStatusOpt.isPresent()) {
            String newStatus = newStatusOpt.get();

            // Gọi API cập nhật trạng thái
            boolean success = CustomerApi.updateOrderStatus(customerId, selectedOrder.getId(), newStatus);

            if (success) {
                // Cập nhật lại trạng thái trong đối tượng và refresh TableView
                selectedOrder.setStatus(newStatus);
                tableView.refresh();

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Thành công");
                alert.setHeaderText(null);
                alert.setContentText("Cập nhật trạng thái đơn hàng thành công.");
                alert.showAndWait();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Lỗi");
                alert.setHeaderText(null);
                alert.setContentText("Cập nhật trạng thái thất bại. Vui lòng thử lại.");
                alert.showAndWait();
            }
        }
    }

    @FXML
    private void handleDeleteOrder() {
        OrderDTO selectedOrder = tableView.getSelectionModel().getSelectedItem();
        if (selectedOrder == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Cảnh báo");
            alert.setHeaderText(null);
            alert.setContentText("Vui lòng chọn đơn hàng để xóa.");
            alert.showAndWait();
            return;
        }

        Long customerId;
        try {
            customerId = Long.parseLong(lblCustomerId.getText());
            if (customerId == 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Lỗi");
            alert.setHeaderText(null);
            alert.setContentText("Mã khách hàng không hợp lệ.");
            alert.showAndWait();
            return;
        }

        boolean deleted = CustomerActionHandler.deleteOrder(customerId, selectedOrder);
        if (deleted) {
            orderList.remove(selectedOrder);
            tableView.refresh();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Thành công");
            alert.setHeaderText(null);
            alert.setContentText("Xóa đơn hàng thành công.");
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Thất bại");
            alert.setHeaderText(null);
            alert.setContentText("Xóa đơn hàng thát bại hoặc bị hủy.");
            alert.showAndWait();
        }
    }

}
