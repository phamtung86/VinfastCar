package com.vinfast.controller;

import com.vinfast.api.CustomerApi;
import com.vinfast.dto.CarDTO;
import com.vinfast.dto.CustomerDTO;
import com.vinfast.dto.OrderDTO;
import com.vinfast.util.FormatUtils;
import javafx.application.Platform;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class ManageClientController implements Initializable {
    //    @FXML private TableColumn<CustomerDTO, Integer> colSerial;
    @FXML private TableView<CustomerDTO> customerTable;
    @FXML private TableColumn<CustomerDTO, Long> colId;
    @FXML private TableColumn<CustomerDTO, String> colName;
    @FXML private TableColumn<CustomerDTO, String> colPhone;
    @FXML private TableColumn<CustomerDTO, String> colEmail;
    @FXML private TableColumn<CustomerDTO, String> colAddress;
    @FXML private TableColumn<CustomerDTO, String> colCreatedAt;
    @FXML private TableColumn<CustomerDTO, String> colOrderCount;

    @FXML
    private Pane boxFeatureSearch;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setTableView();
        setColumn();
        getData();
        loadAPI();
//        fakeData();
    }

    private void loadAPI(){
        new Thread(() -> {
            try {
                List<CustomerDTO> customers = CustomerApi.getAllCustomers();
                Platform.runLater(() -> customerTable.setItems(FXCollections.observableArrayList(customers)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void setTableView(){
//        colSerial.setCellValueFactory(cell -> {
//            int index = customerTable.getItems().indexOf(cell.getValue()) + 1;
//            return new SimpleIntegerProperty(index).asObject();
//        });

        colId.setCellValueFactory(cell -> new SimpleLongProperty(cell.getValue().getId()).asObject());
        colName.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getName()));
        colPhone.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getPhone()));
        colEmail.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getEmail()));
        colAddress.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getAddress()));

        colCreatedAt.setCellValueFactory(cell ->
                new SimpleStringProperty(FormatUtils.formatDateVietnamese(cell.getValue().getCreatedAt()))
        );

        colOrderCount.setCellValueFactory(cell -> new SimpleStringProperty(FormatUtils.formatCurrency(cell.getValue().getTotalAmount())));

        customerTable.setRowFactory(tv -> {
            TableRow<CustomerDTO> row = new TableRow<>();

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
                    CustomerDTO selectedCustomer = row.getItem();
                    showCustomerDetail(selectedCustomer);
                }
            });

            return row;
        });
    }

    public void setColumn(){
        customerTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        for (TableColumn<?, ?> column : customerTable.getColumns()) {
            column.setMaxWidth(1f * Integer.MAX_VALUE);
        }

//        colSerial.setStyle("-fx-alignment: CENTER;");
        colId.setStyle("-fx-alignment: CENTER;");
        colName.setStyle("-fx-alignment: CENTER;");
        colPhone.setStyle("-fx-alignment: CENTER;");
        colEmail.setStyle("-fx-alignment: CENTER;");
        colAddress.setStyle("-fx-alignment: CENTER;");
        colCreatedAt.setStyle("-fx-alignment: CENTER;");
        colOrderCount.setStyle("-fx-alignment: CENTER;");
    }

    public void getData(){
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));

        colCreatedAt.setCellValueFactory(cell -> {
            Date createdAt = cell.getValue().getCreatedAt();
            String formattedDate = FormatUtils.formatDateVietnamese(createdAt);
            return new SimpleStringProperty(formattedDate);
        });

        colOrderCount.setCellValueFactory(cell -> {
            BigDecimal totalAmount = cell.getValue().getTotalAmount();
            String formattedAmount = FormatUtils.formatCurrency(totalAmount);
            return new SimpleStringProperty(formattedAmount);
        });
    }

    public void fakeData(){
        CustomerDTO demoCustomer = new CustomerDTO();
        demoCustomer.setId(1L);
        demoCustomer.setName("Nguyễn Văn A");
        demoCustomer.setPhone("0909123456");
        demoCustomer.setEmail("vana@example.com");
        demoCustomer.setAddress("Hà Nội");
        demoCustomer.setCreatedAt(new java.util.Date());

        // Tạo dữ liệu xe
        CarDTO car = new CarDTO();
        car.setId(1);
        car.setName("VinFast VF e34");
        car.setPrice(690_000_000L);

        // Tạo đơn hàng và gán xe
        List<OrderDTO> orders = new ArrayList<>();
        OrderDTO order1 = new OrderDTO();
        order1.setTotalAmount(new BigDecimal("1000000")); // 1 triệu VND
        order1.setCar(car); // Gán xe vào đơn hàng
        order1.setOrderDate(new Date()); // Thêm ngày mua
        orders.add(order1);

        demoCustomer.setOrders(orders);
        customerTable.getItems().add(demoCustomer);
    }

    @FXML
    private void handleSearchCustomer() {
        String searchTerm = ((TextField) boxFeatureSearch.getChildren().get(0)).getText();
        new Thread(() -> {
            try {
                List<CustomerDTO> customers = CustomerApi.searchCustomers(searchTerm);
                Platform.runLater(() -> customerTable.setItems(FXCollections.observableArrayList(customers)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    @FXML
    private void handleRefreshTable() {
        customerTable.getItems().clear();

        loadAPI();
    }

    private void showCustomerDetail(CustomerDTO customer) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/vinfast/fe/CustomerDetail.fxml"));
            Parent root = loader.load();

            CustomerDetailController controller = loader.getController();
            controller.setCustomerDetail(customer);

            Stage stage = new Stage();
            stage.setTitle("Thông Tin Khách Hàng");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}