package com.vinfast.ui.car;

import com.vinfast.api.CarApi;
import com.vinfast.dto.CarDTO;
import com.vinfast.dto.LibraryDTO;
import com.vinfast.ui.alert.AlertNotice;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class CarActionHandler {
    private final TableView<CarDTO> carTable;
    private final Consumer<List<CarDTO>> updateTableCallback;
    private final List<String> driveTrainOptions = Arrays.asList("FWD", "RWD", "AWD");
    private final List<String> gearOptions = Arrays.asList("Số sàn", "Số tự động");
    private final List<String> styleOptions = Arrays.asList("Hatchback", "Sedan", "SUV", "Crossover (CUV)", "MPV", "Pickup");
    private final List<String> statusOptions = Arrays.asList("AVAILABLE", "RESERVED","SOLD", "DELIVERED");
    private AlertNotice alertNotice = new AlertNotice();
    private final CarApi carApi = new CarApi();

    public CarActionHandler(TableView<CarDTO> carTable, Consumer<List<CarDTO>> updateTableCallback) {
        this.carTable = carTable;
        this.updateTableCallback = updateTableCallback;
    }

    public void handleAddCar() {
        Dialog<CarDTO> dialog = new Dialog<>();
        dialog.setTitle("Thêm Xe Mới");
        dialog.setHeaderText("Nhập thông tin xe mới");

        TextField nameField = new TextField();
        nameField.setPromptText("Tên xe");

        TextField odoField = new TextField();
        odoField.setPromptText("Odo");

        TextField yearField = new TextField();
        yearField.setPromptText("Năm");

        ComboBox<String> gearField = new ComboBox<>(FXCollections.observableArrayList(gearOptions));
        gearField.setPromptText("Hộp số");

        TextField originalField = new TextField();
        originalField.setPromptText("Xuất xứ");

        TextField priceField = new TextField();
        priceField.setPromptText("Giá");

        TextField engineField = new TextField();
        engineField.setPromptText("Động cơ");

        TextField statusField = new TextField();
        statusField.setPromptText("Trạng thái");

        ComboBox<String> styleField = new ComboBox<>(FXCollections.observableArrayList(styleOptions));
        styleField.setPromptText("Kiểu dáng");

        TextField colorOutField = new TextField();
        colorOutField.setPromptText("Màu ngoại thất");

        TextField colorInField = new TextField();
        colorInField.setPromptText("Màu nội thất");

        TextField slotSeatsField = new TextField();
        slotSeatsField.setPromptText("Số ghế ngồi");

        TextField slotDoorField = new TextField();
        slotDoorField.setPromptText("Số cửa");

        ComboBox<String> driveTrainField = new ComboBox<>(FXCollections.observableArrayList(driveTrainOptions));
        driveTrainField.setPromptText("Dẫn động");

        List<LibraryDTO> imageLibraries = new ArrayList<>();
        List<File> selectedFiles = new ArrayList<>();
        ListView<String> imageListView = new ListView<>();
        imageListView.setPrefHeight(120);

        Button chooseImageButton = new Button("Chọn ảnh");
        chooseImageButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Chọn ảnh");
            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("Hình ảnh", "*.jpg", "*.jpeg", "*.png")
            );
            List<File> files = fileChooser.showOpenMultipleDialog(null);
            if (files != null) {
                selectedFiles.addAll(files);
                for (File file : files) {
                    LibraryDTO library = new LibraryDTO();
                    library.setTitle(file.getName());
                    library.setUrlLink(file.getAbsolutePath()); // tạm lưu đường dẫn cục bộ
                    imageLibraries.add(library);
                    imageListView.getItems().add(file.getName());
                }
            }
        });

        VBox vbox = new VBox(
                10,
                nameField, odoField, yearField, gearField,
                originalField, priceField, engineField, styleField, colorOutField, colorInField, driveTrainField, slotSeatsField, slotDoorField, statusField,
                new Label("Ảnh xe:"), chooseImageButton, imageListView
        );
        vbox.setPadding(new Insets(10));

        dialog.getDialogPane().setContent(vbox);
        dialog.setWidth(200);
        ButtonType addButtonType = new ButtonType("Thêm", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        grid.add(new Label("Tên xe:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Odo:"), 0, 1);
        grid.add(odoField, 1, 1);
        grid.add(new Label("Năm:"), 0, 2);
        grid.add(yearField, 1, 2);
        grid.add(new Label("Hộp số:"), 0, 3);
        grid.add(gearField, 1, 3);
        grid.add(new Label("Xuất xứ:"), 0, 4);
        grid.add(originalField, 1, 4);
        grid.add(new Label("Giá:"), 0, 5);
        grid.add(priceField, 1, 5);
        grid.add(new Label("Động cơ:"), 0, 6);
        grid.add(engineField, 1, 6);
        grid.add(new Label("Kiểu dáng:"), 0, 7);
        grid.add(styleField, 1, 7);
        grid.add(new Label("Màu nội thất:"), 0, 8);
        grid.add(colorInField, 1, 8);
        grid.add(new Label("Màu ngoại thất:"), 0, 9);
        grid.add(colorOutField, 1, 9);
        grid.add(new Label("Số ghế:"), 0, 10);
        grid.add(slotSeatsField, 1, 10);
        grid.add(new Label("Số cửa:"), 0, 11);
        grid.add(slotDoorField, 1, 11);
        grid.add(new Label("Dẫn động:"), 0, 12);
        grid.add(driveTrainField, 1, 12);
        grid.add(new Label("Ảnh:"), 0, 13);
        grid.add(chooseImageButton, 1, 13);
        grid.add(imageListView, 1, 14);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                try {
                    CarDTO newCar = new CarDTO();
                    newCar.setId(0);
                    newCar.setName(nameField.getText());
                    newCar.setOdo(Double.parseDouble(odoField.getText()));
                    newCar.setYear(Integer.parseInt(yearField.getText()));
                    newCar.setGear(gearField.getValue());
                    newCar.setOriginal(originalField.getText());
                    newCar.setPrice(Long.parseLong(priceField.getText()));
                    newCar.setEngine(engineField.getText());
                    newCar.setLibraries(imageLibraries);
                    newCar.setStyle(styleField.getValue());
                    newCar.setColorOut(colorOutField.getText());
                    newCar.setColorIn(colorInField.getText());
                    newCar.setSlotSeats(Integer.parseInt(slotSeatsField.getText()));
                    newCar.setSlotDoor(Integer.parseInt(slotDoorField.getText()));
                    newCar.setDriveTrain(driveTrainField.getValue());
                    return newCar;
                } catch (NumberFormatException e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Lỗi nhập liệu");
                    alert.setHeaderText("Vui lòng nhập đúng định dạng số trong các trường Odo, Năm, Giá.");
                    alert.showAndWait();
                    System.out.println(e);
                }
            }
            return null;
        });

        Optional<CarDTO> result = dialog.showAndWait();
        result.ifPresent(car -> {
            // Gọi hàm thêm xe truyền luôn danh sách ảnh thực tế
            new Thread(() -> {
                try {
                    carApi.addCar(car, selectedFiles);
                    Platform.runLater(() -> {
                        reloadData();
                        alertNotice.showAlert(Alert.AlertType.INFORMATION, "Thành công", "Thêm xe thành công", null);
                    });
                } catch (Exception ex) {
                    Platform.runLater(() -> {
                        alertNotice.showAlert(Alert.AlertType.ERROR, "Lỗi", "Có lỗi xảy ra khi thêm xe", null);
                    });
                }
            }).start();
        });
        reloadData();
    }

    public void handleEditCar() {
        CarDTO selectedCar = carTable.getSelectionModel().getSelectedItem();
        if (selectedCar != null) {
            Dialog<CarDTO> dialog = new Dialog<>();
            dialog.setTitle("Sửa Thông Tin Xe");
            dialog.setHeaderText("Chỉnh sửa thông tin xe");

            ButtonType saveButtonType = new ButtonType("Lưu", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

            // Tạo các trường nhập
            TextField idField = new TextField(String.valueOf(selectedCar.getId()));
            TextField nameField = new TextField(selectedCar.getName());
            TextField odoField = new TextField(String.valueOf(selectedCar.getOdo()));
            TextField yearField = new TextField(String.valueOf(selectedCar.getYear()));
            TextField gearField = new TextField(selectedCar.getGear());
            TextField originalField = new TextField(selectedCar.getOriginal());
            TextField priceField = new TextField(String.valueOf(selectedCar.getPrice()));
            TextField engineField = new TextField(selectedCar.getEngine());
            TextField statusField = new TextField(selectedCar.getStatus());
            ComboBox<String> styleField = new ComboBox<>(FXCollections.observableArrayList(styleOptions));
            styleField.setValue(selectedCar.getStyle());
            ComboBox<String> carStatusField = new ComboBox<>(FXCollections.observableArrayList(statusOptions));
            carStatusField.setValue(selectedCar.getCarStatus());
            TextField colorOutField = new TextField(selectedCar.getColorOut());
            TextField colorInField = new TextField(selectedCar.getColorIn());
            TextField slotSeatsField = new TextField(String.valueOf(selectedCar.getSlotSeats()));
            TextField slotDoorField = new TextField(String.valueOf(selectedCar.getSlotDoor()));
            ComboBox<String> driveTrainField = new ComboBox<>(FXCollections.observableArrayList(driveTrainOptions));
            driveTrainField.setValue(selectedCar.getDriveTrain());

            // Dùng GridPane để bố trí layout
            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new Insets(20, 150, 10, 10));

            // Thêm các trường vào grid
            grid.add(new Label("Tên xe:"), 0, 0);
            grid.add(nameField, 1, 0);
            grid.add(new Label("Số km đã đi:"), 0, 1);
            grid.add(odoField, 1, 1);
            grid.add(new Label("Năm SX:"), 0, 2);
            grid.add(yearField, 1, 2);
            grid.add(new Label("Hộp số:"), 0, 3);
            grid.add(gearField, 1, 3);
            grid.add(new Label("Xuất xứ:"), 0, 4);
            grid.add(originalField, 1, 4);
            grid.add(new Label("Giá:"), 0, 5);
            grid.add(priceField, 1, 5);
            grid.add(new Label("Động cơ:"), 0, 6);
            grid.add(engineField, 1, 6);
            grid.add(new Label("Trạng thái:"), 0, 7);
            grid.add(statusField, 1, 7);
            grid.add(new Label("Kiểu dáng:"), 0, 8);
            grid.add(styleField, 1, 8);
            grid.add(new Label("Màu ngoại thất:"), 0, 9);
            grid.add(colorOutField, 1, 9);
            grid.add(new Label("Màu nội thất:"), 0, 10);
            grid.add(colorInField, 1, 10);
            grid.add(new Label("Số chỗ:"), 0, 11);
            grid.add(slotSeatsField, 1, 11);
            grid.add(new Label("Số cửa:"), 0, 12);
            grid.add(slotDoorField, 1, 12);
            grid.add(new Label("Dẫn động:"), 0, 13);
            grid.add(driveTrainField, 1, 13);
            grid.add(new Label("Trạng thái"), 0, 14);
            grid.add(carStatusField, 1, 14);

            dialog.getDialogPane().setContent(grid);

            // Xử lý khi người dùng bấm "Lưu"
            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == saveButtonType) {
                    selectedCar.setId(Integer.parseInt(idField.getText()));
                    selectedCar.setName(nameField.getText());
                    selectedCar.setOdo((int) Double.parseDouble(slotSeatsField.getText()));
                    selectedCar.setYear(Integer.parseInt(yearField.getText()));
                    selectedCar.setGear(gearField.getText());
                    selectedCar.setOriginal(originalField.getText());
                    selectedCar.setPrice(Long.parseLong(priceField.getText()));
                    selectedCar.setEngine(engineField.getText());
                    selectedCar.setStatus(statusField.getText());
                    selectedCar.setStyle(styleField.getValue());
                    selectedCar.setColorOut(colorOutField.getText());
                    selectedCar.setColorIn(colorInField.getText());
                    selectedCar.setSlotSeats(Integer.parseInt(slotSeatsField.getText()));
                    selectedCar.setSlotDoor(Integer.parseInt(slotDoorField.getText()));
                    selectedCar.setDriveTrain(driveTrainField.getValue());
                    selectedCar.setCarStatus(carStatusField.getValue());
                    return selectedCar;
                }
                return null;
            });

            Optional<CarDTO> result = dialog.showAndWait();
            result.ifPresent(car -> {
                int statusResponse = carApi.editCar(car);
                if (statusResponse == 200) {
                    carTable.refresh();
                    alertNotice.showAlert(Alert.AlertType.INFORMATION, "Thành công", "Cập nhật thành công", null);
                } else {
                    alertNotice.showAlert(Alert.AlertType.ERROR, "Thất bại", "Cập nhật thất bại", null);
                }
            });
        }
    }

    public void handleDeleteCar() {
        CarDTO selectedCar = carTable.getSelectionModel().getSelectedItem();
        if (selectedCar != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Xác Nhận Xóa");
            alert.setHeaderText("Bạn có chắc chắn muốn xóa xe này?");
            alert.setContentText("Xe: " + selectedCar.getName());
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                int statusCode = carApi.deleteCar(selectedCar.getId());
                if(statusCode == 200){
                    reloadData();
                    alertNotice.showAlert(Alert.AlertType.INFORMATION, "Thành công", "Xóa thành công",null);
                }
                reloadData();
            }
        } else {
            alertNotice.showAlert(Alert.AlertType.INFORMATION, "Chưa chọn xe", "Vui lòng chọn một xe để xóa", null);
        }
    }

    public void handleSearchCar(String searchTerm) {
        new Thread(() -> {
            try {
                List<CarDTO> cars = carApi.searchCars(searchTerm);
                Platform.runLater(() -> updateTableCallback.accept(cars));
            } catch (Exception e) {
                e.printStackTrace();
                Platform.runLater(() -> alertNotice.showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể tìm kiếm xe", null));
            }
        }).start();
    }

    private void reloadData() {
        updateTableCallback.accept(null); // Gọi callback để reload dữ liệu
    }

    public void handleViewDetailCar(){
        CarDTO selectedCar = carTable.getSelectionModel().getSelectedItem();
        try {
            CarDTO carDTO = carApi.findCarById(selectedCar.getId());
            CarDetailView carDetailView = new CarDetailView();
            carDetailView.show(carDTO);
        } catch (Exception e){
            e.printStackTrace();
        }

    }
}