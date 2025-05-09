package com.vinfast.controller;

import com.vinfast.api.OrderApi;
import com.vinfast.dto.CarDTO;
import com.vinfast.dto.OrderDTO;
import com.vinfast.util.FormatUtils;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class SupportController implements Initializable {
    @FXML
    private TableView<OrderDTO> orderTable;
    @FXML
    private TableColumn<OrderDTO, Integer> idCol;
    @FXML
    private TableColumn<OrderDTO, String> inforCustomer;
    @FXML
    private TableColumn<OrderDTO, String> inforCar;
    @FXML
    private TableColumn<OrderDTO, Long > totalAmount;
    @FXML
    private TableColumn<OrderDTO, Date> createAt ;
    @FXML
    private TableColumn<OrderDTO, String>  status ;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadDataFormApi();
        setDataIntoTableView();
    }
    private void loadDataFormApi(){
        new Thread(()->{
            try {
                List<OrderDTO> orderDTOS = OrderApi.getAllOrder();
                System.out.println(orderDTOS);
                Platform.runLater(() -> orderTable.setItems(FXCollections.observableArrayList(orderDTOS)));
            } catch (Exception e) {
                e.printStackTrace();
            }

        }).start();
    }
    public void setDataIntoTableView() {
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));

        // Hiển thị customerName + ID
        inforCustomer.setCellValueFactory(cellData -> {
            OrderDTO order = cellData.getValue();
            String customerInfo = String.format("%s (ID: %d)",
                    order.getCustomerName(), order.getCustomerId());
            return new SimpleStringProperty(customerInfo);
        });

        // Hiển thị tên xe
        inforCar.setCellValueFactory(cellData -> {
            CarDTO car = cellData.getValue().getCar();
            return new SimpleStringProperty(car != null ? car.getName() : "");
        });

        // Hiển thị và định dạng số tiền
        totalAmount.setCellValueFactory(new PropertyValueFactory<>("totalAmount"));
        totalAmount.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Long amount, boolean empty) {
                super.updateItem(amount, empty);
                setText(empty || amount == null ? "" : FormatUtils.formatPrice(amount));
            }
        });

        createAt.setCellValueFactory(new PropertyValueFactory<>("orderDate"));
        status.setCellValueFactory(new PropertyValueFactory<>("status"));
    }

    public void addInvoice(ActionEvent actionEvent) {

    }
}
