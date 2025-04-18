package com.vinfast.controller;

import com.vinfast.api.CarApi;
import com.vinfast.dto.CarDTO;
import com.vinfast.dto.LibraryDTO;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ManageCarController implements Initializable {

    @FXML
    private TableView<CarDTO> carTable;

    @FXML
    private TableColumn<CarDTO, Integer> idCol;

    @FXML
    private TableColumn<CarDTO, String> nameCol;

    @FXML
    private TableColumn<CarDTO, List<LibraryDTO>> imageCol; // ✅ Đúng kiểu dữ liệu

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
    private HBox contentBox;

    public void getContentBox() {
        HBox.setHgrow(contentBox, Priority.ALWAYS);
    }

    private void loadDataFromApi() {
        new Thread(() -> {
            try {
                List<CarDTO> cars = CarApi.getCarsFromApi();
                Platform.runLater(() -> carTable.setItems(FXCollections.observableArrayList(cars)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        imageCol.setCellValueFactory(new PropertyValueFactory<>("libraries")); // ✅ Phải trỏ đúng property

        odoCol.setCellValueFactory(new PropertyValueFactory<>("odo"));
        yearCol.setCellValueFactory(new PropertyValueFactory<>("year"));
        gearCol.setCellValueFactory(new PropertyValueFactory<>("gear"));
        originalCol.setCellValueFactory(new PropertyValueFactory<>("original"));
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        statusCol.setCellValueFactory(new PropertyValueFactory<>("carStatus"));
        engineCol.setCellValueFactory(new PropertyValueFactory<>("engine"));

        // ✅ Tùy chỉnh hiển thị nhiều ảnh
        imageCol.setCellFactory(col -> new TableCell<CarDTO, List<LibraryDTO>>() {
            private final HBox imageContainer = new HBox(5);

            @Override
            protected void updateItem(List<LibraryDTO> libraries, boolean empty) {
                super.updateItem(libraries, empty);
                if (empty || libraries == null || libraries.isEmpty()) {
                    setGraphic(null);
                } else {
                    imageContainer.getChildren().clear();
                    int maxImages = 3; // Hiển thị tối đa 3 ảnh, bạn có thể chỉnh lại

                    for (int i = 0; i < Math.min(libraries.size(), maxImages); i++) {
                        String url = libraries.get(i).getUrlLink();
                        ImageView img = new ImageView(new Image(url, 100, 0, true, true));
                        img.setFitHeight(80);
                        imageContainer.getChildren().add(img);
                    }

                    setGraphic(imageContainer);
                }
            }
        });

        loadDataFromApi();
    }
}
