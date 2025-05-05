package com.vinfast.controller;

import com.vinfast.api.CarApi;
import com.vinfast.dto.CarDTO;
import com.vinfast.dto.LibraryDTO;
import com.vinfast.util.FormatUtils;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class ManageCarController implements Initializable {

    @FXML
    private TableView<CarDTO> carTable;

    @FXML
    private TableColumn<CarDTO, Integer> idCol;

    @FXML
    private TableColumn<CarDTO, String> nameCol;

    @FXML
    private TableColumn<CarDTO, List<LibraryDTO>> imageCol;

    @FXML
    private TableColumn<CarDTO, Double> odoCol;

    @FXML
    private TableColumn<CarDTO, Integer> yearCol;

    @FXML
    private TableColumn<CarDTO, String> gearCol;
    @FXML
    private TableColumn<CarDTO, String> originalCol;

    @FXML
    private TableColumn<CarDTO, Long> priceCol;

    @FXML
    private TableColumn<CarDTO, String> statusCol;

    @FXML
    private TableColumn<CarDTO, String> engineCol;

    @FXML
    private HBox boxFeature;

    @FXML
    private Pane boxFeatureSearch;

    @FXML
    private TextField searchField;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setSizeColumnTable();
        setStyleColumnTable();
        setDataIntoTableView();
        loadDataFromApi();
    }

    private void loadDataFromApi() {
        new Thread(() -> {
            try {
                List<CarDTO> cars = CarApi.getAllCars();
                Platform.runLater(() -> carTable.setItems(FXCollections.observableArrayList(cars)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void setDataIntoTableView() {
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        imageCol.setCellValueFactory(new PropertyValueFactory<>("libraries"));
        odoCol.setCellValueFactory(new PropertyValueFactory<>("odo"));
        yearCol.setCellValueFactory(new PropertyValueFactory<>("year"));
        gearCol.setCellValueFactory(new PropertyValueFactory<>("gear"));
        originalCol.setCellValueFactory(new PropertyValueFactory<>("original"));
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        statusCol.setCellValueFactory(new PropertyValueFactory<>("carStatus"));
        engineCol.setCellValueFactory(new PropertyValueFactory<>("engine"));

        priceCol.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Long price, boolean empty) {
                super.updateItem(price, empty);
                setText(empty || price == null ? "" : FormatUtils.formatPrice(price));
            }
        });

        imageCol.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(List<LibraryDTO> images, boolean empty) {
                super.updateItem(images, empty);
                if (empty || images == null) {
                    setGraphic(null);
                } else {
                    setGraphic(createCarImageView(images));
                }
            }
        });

        statusCol.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String carStatus, boolean empty) {
                super.updateItem(carStatus, empty);
                if (empty || carStatus == null) {
                    setGraphic(null);
                    setText(null);
                } else {
                    if ("AVAILABLE".equals(carStatus)) {
                        // Load image from resources
                        InputStream stream = getClass().getResourceAsStream("/com/vinfast/fe/icon/available.png");
                        if (stream != null) {
                            ImageView successIcon = new ImageView(new Image(stream));
                            successIcon.setFitWidth(16); // Adjust size as needed
                            successIcon.setFitHeight(16);
                            setGraphic(successIcon);
                        } else {
                            // Fallback to green circle if image is missing
                            System.err.println("Failed to load icon at /com/vinfast/fe/icon/available.png");
                            Circle successIcon = new Circle(8, Color.GREEN); // 8px radius, green color
                            setGraphic(successIcon);
                        }
                    } else {
                        setGraphic(null);
                        setText(carStatus);
                    }
                }
            }
        });
    }

    private ImageView createCarImageView(List<LibraryDTO> images) {
        if (images == null || images.isEmpty()) return new ImageView();
        String imagePath = images.get(0).getUrlLink();

        ImageView imageView = new ImageView(new Image(imagePath));
        imageView.setFitWidth(70);
        imageView.setFitHeight(50);
        return imageView;
    }

    public void setSizeColumnTable() {
        carTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        idCol.prefWidthProperty().bind(carTable.widthProperty().multiply(0.05));
        nameCol.prefWidthProperty().bind(carTable.widthProperty().multiply(0.26));
        imageCol.prefWidthProperty().bind(carTable.widthProperty().multiply(0.07));
        odoCol.prefWidthProperty().bind(carTable.widthProperty().multiply(0.08));
        yearCol.prefWidthProperty().bind(carTable.widthProperty().multiply(0.07));
        gearCol.prefWidthProperty().bind(carTable.widthProperty().multiply(0.1));
        originalCol.prefWidthProperty().bind(carTable.widthProperty().multiply(0.1));
        priceCol.prefWidthProperty().bind(carTable.widthProperty().multiply(0.1));
        engineCol.prefWidthProperty().bind(carTable.widthProperty().multiply(0.15));
        statusCol.prefWidthProperty().bind(carTable.widthProperty().multiply(0.07));
    }

    public void setStyleColumnTable() {
        idCol.setStyle("-fx-alignment: CENTER;");
        nameCol.setStyle("-fx-alignment: CENTER;");
        odoCol.setStyle("-fx-alignment: CENTER;");
        yearCol.setStyle("-fx-alignment: CENTER;");
        gearCol.setStyle("-fx-alignment: CENTER;");
        originalCol.setStyle("-fx-alignment: CENTER;");
        priceCol.setStyle("-fx-alignment: CENTER;");
        statusCol.setStyle("-fx-alignment: CENTER;");
        engineCol.setStyle("-fx-alignment: CENTER;");
        imageCol.setStyle("-fx-alignment: CENTER;");
    }

    @FXML
    private void handleAddCar() {
        Dialog<CarDTO> dialog = new Dialog<>();
        dialog.setTitle("Thêm Xe Mới");
        dialog.setHeaderText("Nhập thông tin xe mới");

        // Tạo các trường nhập liệu
        TextField nameField = new TextField();
        nameField.setPromptText("Tên xe");

        TextField odoField = new TextField();
        odoField.setPromptText("Odo");

        TextField yearField = new TextField();
        yearField.setPromptText("Năm");

        TextField gearField = new TextField();
        gearField.setPromptText("Hộp số");

        TextField originalField = new TextField();
        originalField.setPromptText("Xuất xứ");

        TextField priceField = new TextField();
        priceField.setPromptText("Giá");

        TextField engineField = new TextField();
        engineField.setPromptText("Động cơ");

        // Image Picker
        List<LibraryDTO> imageLibraries = new ArrayList<>();
        ListView<String> imageListView = new ListView<>();
        imageListView.setPrefHeight(120);

        Button chooseImageButton = new Button("Chọn ảnh");
        chooseImageButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Chọn ảnh");
            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("Hình ảnh", "*.jpg", "*.jpeg", "*.png")
            );
            List<File> selectedFiles = fileChooser.showOpenMultipleDialog(null);
            if (selectedFiles != null) {
                for (File file : selectedFiles) {
                    LibraryDTO library = new LibraryDTO();
                    library.setTitle(file.getName());
                    library.setUrlLink(file.getAbsolutePath()); // Có thể thay bằng đường dẫn thực tế từ upload
                    imageLibraries.add(library);
                    imageListView.getItems().add(file.getName());
                }
            }
        });

        VBox vbox = new VBox(
                10,
                nameField, odoField, yearField, gearField,
                originalField, priceField, engineField,
                new Label("Ảnh xe:"), chooseImageButton, imageListView
        );
        vbox.setPadding(new Insets(10));

        dialog.getDialogPane().setContent(vbox);

        ButtonType addButtonType = new ButtonType("Thêm", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                try {
                    CarDTO newCar = new CarDTO();
                    newCar.setName(nameField.getText());
                    newCar.setOdo(Double.parseDouble(odoField.getText()));
                    newCar.setYear(Integer.parseInt(yearField.getText()));
                    newCar.setGear(gearField.getText());
                    newCar.setOriginal(originalField.getText());
                    newCar.setPrice(Long.parseLong(priceField.getText()));
                    newCar.setEngine(engineField.getText());
                    newCar.setLibraries(imageLibraries);

                    return newCar;
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }
            return null;
        });

        Optional<CarDTO> result = dialog.showAndWait();
        result.ifPresent(car -> {
            CarApi.addCar(car);
            loadDataFromApi(); // Refresh danh sách
        });
    }


    @FXML
    private void handleEditCar() {
        CarDTO selectedCar = carTable.getSelectionModel().getSelectedItem();
        if (selectedCar != null) {
            // Mở hộp thoại để sửa thông tin xe
            Dialog<CarDTO> dialog = new Dialog<>();
            dialog.setTitle("Sửa Thông Tin Xe");
            dialog.setHeaderText("Chỉnh sửa thông tin xe");

            // Tạo các trường nhập liệu
            TextField nameField = new TextField(selectedCar.getName());
            TextField odoField = new TextField(String.valueOf(selectedCar.getOdo()));
            TextField yearField = new TextField(String.valueOf(selectedCar.getYear()));
            TextField gearField = new TextField(selectedCar.getGear());
            TextField originalField = new TextField(selectedCar.getOriginal());
            TextField priceField = new TextField(String.valueOf(selectedCar.getPrice()));
            TextField engineField = new TextField(selectedCar.getEngine());

            VBox vbox = new VBox(nameField, odoField, yearField, gearField, originalField, priceField, engineField);
            dialog.getDialogPane().setContent(vbox);

            ButtonType editButtonType = new ButtonType("Sửa", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(editButtonType, ButtonType.CANCEL);

            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == editButtonType) {
                    selectedCar.setName(nameField.getText());
                    selectedCar.setOdo(Double.parseDouble(odoField.getText()));
                    selectedCar.setYear(Integer.parseInt(yearField.getText()));
                    selectedCar.setGear(gearField.getText());
                    selectedCar.setOriginal(originalField.getText());
                    selectedCar.setPrice(Long.parseLong(priceField.getText()));
                    selectedCar.setEngine(engineField.getText());
                    return selectedCar;
                }
                return null;
            });

//            Optional<CarDTO> result = dialog.showAndWait();
//            result.ifPresent(car -> {
//                // Gọi API để sửa xe
//                CarApi.editCar(car);
//                loadDataFromApi(); // Cập nhật bảng
//            });
        } else {
//            showAlert("Chưa chọn xe", "Vui lòng chọn một xe để sửa.");
        }
    }

    @FXML
    private void handleDeleteCar() {
        CarDTO selectedCar = carTable.getSelectionModel().getSelectedItem();
        if (selectedCar != null) {
            // Hiển thị hộp thoại xác nhận trước khi xóa
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Xác Nhận Xóa");
            alert.setHeaderText("Bạn có chắc chắn muốn xóa xe này?");
            alert.setContentText("Xe: " + selectedCar.getName());

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                // Gọi API để xóa xe
//                CarApi.deleteCar(selectedCar.getId());
                loadDataFromApi(); // Cập nhật bảng
            }
        } else {
            showAlert("Chưa chọn xe", "Vui lòng chọn một xe để xóa.");
        }
    }

    @FXML
    private void handleSearchCar() {
        String searchTerm = ((TextField) boxFeatureSearch.getChildren().get(0)).getText();
        new Thread(() -> {
            try {
                List<CarDTO> cars = CarApi.searchCars(searchTerm);
                Platform.runLater(() -> carTable.setItems(FXCollections.observableArrayList(cars)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}