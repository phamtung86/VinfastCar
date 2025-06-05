package com.vinfast.controller;

import com.itextpdf.layout.properties.TextAlignment;
import com.vinfast.api.OrderApi;
import com.vinfast.dto.CarDTO;
import com.vinfast.dto.OrderDTO;
import com.vinfast.model.OrderPageResponse;
import com.vinfast.ui.car.CarActionHandler;
import com.vinfast.ui.order.OrderActionHandler;
import com.vinfast.util.FormatUtils;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;

import java.io.File;
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
    private TableColumn<OrderDTO, Long> totalAmount;
    @FXML
    private TableColumn<OrderDTO, Date> createAt;
    @FXML
    private TableColumn<OrderDTO, String> status;
    @FXML
    private TableColumn<OrderDTO, String> action;
    @FXML
    private Button btnPrev;

    @FXML
    private Label lblPage;

    @FXML
    private Button btnNext;

    private int currentPage = 1;
    private final int pageSize = 20;
    private long totalCars = 0;
    private boolean isLastPage = false;
    private boolean isFirstPage = true;

    private OrderActionHandler orderActionHandler;
    private OrderApi orderApi;
    @FXML
    private TextField searchBox;
    private boolean isSreaching = false;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        orderActionHandler = new OrderActionHandler(orderTable, this::updateTableData);
        orderApi = new OrderApi();
        setDataIntoTableView();
        loadDataFormApi();
        searchBox.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null || newValue.isEmpty()) {
                isSreaching = false;
                currentPage = 1;
                // Nếu searchBox rỗng thì load dữ liệu toàn bộ theo trang
                loadDataFormApi();
            } else {
                isSreaching = true;
                currentPage = 1;
                // Gọi API tìm kiếm theo tên khách hàng
                searchOrdersByCustomerName(newValue);
            }
        });
    }
    private void searchOrdersByCustomerName(String name) {
        new Thread(() -> {
            try {
                List<OrderDTO> orders = orderApi.searchOrdersByCustomerName(name);
                Platform.runLater(() -> {
                    if (orders == null || orders.isEmpty()) {
                        orderTable.getItems().clear();
                        showAlert("Thông báo", "Không tìm thấy đơn hàng nào với tên khách hàng: " + name);
                    } else {
                        orderTable.setItems(FXCollections.observableArrayList(orders));
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                Platform.runLater(() -> showAlert("Lỗi", "Lỗi khi gọi API tìm kiếm: " + e.getMessage()));
            }
        }).start();
    }

    private void loadDataFormApi() {
        new Thread(() -> {
            try {
                OrderPageResponse response = orderApi.getAllCarsByPages(currentPage, pageSize, "id,desc");
                totalCars = response.getTotalElements();
                isLastPage = response.isLast();
                isFirstPage = response.isFirst();
                Platform.runLater(() -> {
                    orderTable.setItems(FXCollections.observableArrayList(response.getContent()));
                    updatePaginationControls();
                });
            } catch (Exception e) {
                e.printStackTrace();
                Platform.runLater(() -> showAlert("Lỗi", "Không thể tải dữ liệu từ API."));
            }
        }).start();
    }
    private void updateTableData(List<OrderDTO> orders) {
        if (orders == null) {
            loadDataFormApi();
        } else {
            orderTable.setItems(FXCollections.observableArrayList(orders));
        }
    }
    private void updatePaginationControls() {
        lblPage.setText("Trang " + currentPage + "/" + ((totalCars + pageSize - 1) / pageSize));
        btnPrev.setDisable(isFirstPage);
        btnNext.setDisable(isLastPage);
    }

    @FXML
    private void goToPreviousPage() {
        if (!isFirstPage) {
            currentPage--;
            loadDataFormApi();
        }
    }

    @FXML
    private void goToNextPage() {
        if (!isLastPage) {
            currentPage++;
            loadDataFormApi();
        }
    }

    public void setDataIntoTableView() {
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));

        inforCustomer.setCellValueFactory(cellData -> {
            OrderDTO order = cellData.getValue();
            String customerInfo = String.format("%s (ID: %d)",
                    order.getCustomerName(), order.getCustomerId());
            return new SimpleStringProperty(customerInfo);
        });

        inforCar.setCellValueFactory(cellData -> {
            CarDTO car = cellData.getValue().getCar();
            return new SimpleStringProperty(car != null ? car.getName() : "");
        });

        totalAmount.setCellValueFactory(new PropertyValueFactory<>("totalAmount"));
        totalAmount.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Long amount, boolean empty) {
                super.updateItem(amount, empty);
                setText(empty || amount == null ? "" : FormatUtils.formatPrice(amount));
            }
        });

        createAt.setCellValueFactory(new PropertyValueFactory<>("orderDate"));
        status.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStatus()));
        status.setCellFactory(column -> new TableCell<>() {
            private final ComboBox<String> comboBox = new ComboBox<>();

            {
                comboBox.getItems().addAll("Pending", "Processing", "Completed");
                comboBox.setOnAction(event -> {
                    OrderDTO order = getTableView().getItems().get(getIndex());
                    String newStatus = comboBox.getValue();

                    // Nếu giống trạng thái cũ thì không làm gì
                    if (newStatus.equals(order.getStatus())) return;

                    // Xác nhận
                    Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
                    confirm.setTitle("Xác nhận thay đổi trạng thái");
                    confirm.setHeaderText("Bạn có chắc muốn đổi trạng thái đơn hàng?");
                    confirm.setContentText("Từ: " + order.getStatus() + " -> " + newStatus);

                    confirm.showAndWait().ifPresent(response -> {
                        if (response == ButtonType.OK) {
                            updateOrderStatus(order, newStatus);
                        } else {
                            comboBox.setValue(order.getStatus()); // Reset lại nếu không xác nhận
                        }
                    });
                });
            }

            @Override
            protected void updateItem(String status, boolean empty) {
                super.updateItem(status, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    comboBox.setValue(status);
                    setGraphic(comboBox);
                }
            }
        });

        // Cột hành động
        action.setCellFactory(col -> new TableCell<>() {
            private final Button cancelButton = new Button("Hủy");
            private final Button pdfButton = new Button("PDF");
//            private final Button editButton = new Button("Sửa");
            private final HBox hBox = new HBox(10, cancelButton, pdfButton);

            {
                cancelButton.setStyle("-fx-background-color: red; -fx-text-fill: white;");
                pdfButton.setStyle("-fx-background-color: green; -fx-text-fill: white;");
//                editButton.setStyle("-fx-background-color: blue; -fx-text-fill: white;");

                cancelButton.setOnAction(event -> {
                    OrderDTO order = getTableView().getItems().get(getIndex());
                    handleCancel(order);
                });

                pdfButton.setOnAction(event -> {
                    OrderDTO order = getTableView().getItems().get(getIndex());
                    handleExportPDF(order);
                });

                // Xử lý sửa
//                editButton.setOnAction(event -> {
//                    OrderDTO order = getTableView().getItems().get(getIndex());
//                    handleEdit(order);
//                });
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(hBox);
                }
            }
        });
    }

    private void handleCancel(OrderDTO order) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Xác nhận hủy đơn hàng");
        confirm.setHeaderText("Bạn có chắc muốn hủy đơn hàng?");
        confirm.setContentText("ID: " + order.getId());

        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                new Thread(() -> {
                    try {
                        String url = "http://localhost:8080/api/v1/orders/" + order.getId();
                        java.net.HttpURLConnection connection = (java.net.HttpURLConnection)
                                new java.net.URL(url).openConnection();

                        connection.setRequestMethod("DELETE");
                        connection.setRequestProperty("Content-Type", "application/json");
                        connection.setDoOutput(true);

                        int responseCode = connection.getResponseCode();

                        Platform.runLater(() -> {
                            if (responseCode == 200) {
                                Alert success = new Alert(Alert.AlertType.INFORMATION);
                                success.setTitle("Thành công");
                                success.setHeaderText("Đã hủy đơn hàng thành công");
                                success.setContentText("Mã đơn: " + order.getId());
                                success.showAndWait();
                                loadDataFormApi(); // reload lại bảng
                            } else {
                                Alert fail = new Alert(Alert.AlertType.ERROR);
                                fail.setTitle("Thất bại");
                                fail.setHeaderText("Không thể hủy đơn hàng");
                                fail.setContentText("Mã lỗi: " + responseCode);
                                fail.showAndWait();
                            }
                        });

                        connection.disconnect();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Platform.runLater(() -> {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Lỗi");
                            alert.setHeaderText("Xảy ra lỗi khi gửi yêu cầu hủy đơn hàng");
                            alert.setContentText(e.getMessage());
                            alert.showAndWait();
                        });
                    }
                }).start();
            }
        });
    }


    private void handleExportPDF(OrderDTO order) {
        try {
            String folderPath = System.getProperty("user.dir") + "/invoicePDF";
            File folder = new File(folderPath);
            if (!folder.exists()) {
                folder.mkdirs();
            }

            String fileName = "order_" + order.getId() + ".pdf";
            File pdfFile = new File(folderPath + "/" + fileName);

            // Dùng font Unicode để hỗ trợ tiếng Việt

            PdfWriter writer = new PdfWriter(pdfFile);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);
            document.add(new Paragraph("HÓA ĐƠN ĐẶT XE").setFontSize(20).setBold().setTextAlignment(TextAlignment.CENTER));
            document.add(new Paragraph(" "));
            document.add(new Paragraph("Mã đơn hàng: " + order.getId()));
            document.add(new Paragraph("Khách hàng: " + order.getCustomerName() + " (ID: " + order.getCustomerId() + ")"));
            document.add(new Paragraph("Ngày đặt: " + order.getOrderDate()));
            document.add(new Paragraph("Trạng thái đơn hàng: " + order.getStatus()));
            document.add(new Paragraph("Tổng tiền: " + FormatUtils.formatPrice(order.getTotalAmount())));
            document.add(new Paragraph(" "));

            if (order.getCar() != null) {
                CarDTO car = order.getCar();
                document.add(new Paragraph("THÔNG TIN CHI TIẾT XE").setFontSize(16).setBold());

                document.add(new Paragraph("Tên xe: " + car.getName()));
                document.add(new Paragraph("Năm sản xuất: " + car.getYear()));
                document.add(new Paragraph("Số km đã đi (ODO): " + car.getOdo() + " km"));
                document.add(new Paragraph("Xuất xứ: " + car.getOriginal()));
                document.add(new Paragraph("Phong cách: " + car.getStyle()));
                document.add(new Paragraph("Hộp số: " + car.getGear()));
                document.add(new Paragraph("Động cơ: " + car.getEngine()));
                document.add(new Paragraph("Màu ngoại thất: " + car.getColorOut()));
                document.add(new Paragraph("Màu nội thất: " + car.getColorIn()));
                document.add(new Paragraph("Số chỗ ngồi: " + car.getSlotSeats()));
                document.add(new Paragraph("Số cửa: " + car.getSlotDoor()));
                document.add(new Paragraph("Hệ dẫn động: " + car.getDriveTrain()));
                document.add(new Paragraph("Tình trạng xe: " + car.getStatus()));
                document.add(new Paragraph("Trạng thái trong kho: " + car.getCarStatus()));
                document.add(new Paragraph("Kho xe: " + car.getInventoryName() + " (ID: " + car.getInventoryId() + ")"));
                document.add(new Paragraph("Giá: " + FormatUtils.formatPrice(car.getPrice())));
            } else {
                document.add(new Paragraph("Không có thông tin xe."));
            }

            document.close();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Xuất PDF");
            alert.setHeaderText("Xuất hóa đơn thành công!");
            alert.setContentText("File đã lưu tại: " + pdfFile.getAbsolutePath());
            alert.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Lỗi");
            alert.setHeaderText("Không thể xuất PDF");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }



    // Xử lý sửa
//    private void handleEdit(OrderDTO order) {
//        orderActionHandler.handleEditOrder();
//        // Bạn có thể tạo một cửa sổ mới (Dialog) để chỉnh sửa thông tin đơn hàng
//
//    }

    public void addInvoice(ActionEvent actionEvent) throws IOException, InterruptedException {
        orderActionHandler.handleAddOrder();
        // Chức năng thêm hóa đơn nếu cần
    }
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    private void updateOrderStatus(OrderDTO order, String newStatus) {
        new Thread(() -> {
            try {
                String url = "http://localhost:8080/api/v1/orders/" + order.getId() + "/status";
                java.net.HttpURLConnection connection = (java.net.HttpURLConnection)
                        new java.net.URL(url).openConnection();

                connection.setRequestMethod("PUT");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setDoOutput(true);

                String jsonBody = String.format("\"%s\"", newStatus);
                try (java.io.OutputStream os = connection.getOutputStream()) {
                    byte[] input = jsonBody.getBytes("utf-8");
                    os.write(input, 0, input.length);
                }

                int responseCode = connection.getResponseCode();
                Platform.runLater(() -> {
                    if (responseCode == 200) {
                        showAlert("Thành công", "Cập nhật trạng thái thành công.");
                        loadDataFormApi();
                    } else {
                        showAlert("Lỗi", "Không thể cập nhật trạng thái. Mã lỗi: " + responseCode);
                    }
                });

                connection.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
                Platform.runLater(() -> showAlert("Lỗi", "Lỗi khi gửi yêu cầu cập nhật trạng thái:\n" + e.getMessage()));
            }
        }).start();
    }

}
