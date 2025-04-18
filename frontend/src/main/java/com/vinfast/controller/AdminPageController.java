package com.vinfast.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AdminPageController implements Initializable {
    @FXML
    private HBox contentBox;
    @FXML
    private VBox mainContainer;
    private Node savedContentBox; // Lưu contentBox gốc để khôi phục khi cần
    private void loadPage(String fxmlFile) {
        try {
            if (savedContentBox == null) {
                savedContentBox = contentBox; // Cập nhật contentBox mỗi lần thay đổi
            }
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            HBox newContent = loader.load();
            newContent.setMaxHeight(Double.MAX_VALUE);
            newContent.setMaxWidth(Double.MAX_VALUE);
            VBox.setVgrow(newContent, Priority.ALWAYS);

            int index = mainContainer.getChildren().indexOf(contentBox);
            if (index != -1) {
                mainContainer.getChildren().set(index, newContent);
                contentBox = newContent; // Cập nhật contentBox hiện tại
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private ChoiceBox<String> logChoice;
    ObservableList<String> list = FXCollections.observableArrayList("LogOut");
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        logChoice.setItems(list);
    }
    @FXML
    private void handleChoiceBoxSelection(ActionEvent e){
        String selectedBox = logChoice.getValue();
        if("LogOut".equals(selectedBox)){
            loadLoginPage();
        }
    }
    private void loadLoginPage() {
        try {
            // Load trang Login
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/vinfast/fe/Login.fxml"));
            Parent root = fxmlLoader.load();

            // Tạo Stage mới
            Stage loginStage = new Stage();
            loginStage.setScene(new Scene(root));
            loginStage.setTitle("Login - VinFast");
            loginStage.initStyle(StageStyle.UNDECORATED); // Đặt không có viền cửa sổ

            // Lấy Stage hiện tại và đóng nó
            Stage currentStage = (Stage) logChoice.getScene().getWindow();
            currentStage.close();

            // Hiển thị cửa sổ login mới
            loginStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Lỗi", "Không thể tải lại trang đăng nhập!");
        }
    }


    private void showAlert(String title, String message) {
        System.out.println(title + ": " + message);
    }
    public void moveToDashBoard(MouseEvent mouseEvent) {
        if (savedContentBox != null) {
            int index = mainContainer.getChildren().indexOf(contentBox);
            if (index != -1) {
                mainContainer.getChildren().set(index, savedContentBox); // Phục hồi contentBox gốc
                contentBox = (HBox) savedContentBox; // Cập nhật lại tham chiếu
            }
        }
    }

    public void moveToManageCar(MouseEvent mouseEvent) {
        loadPage("/com/vinfast/fe/ManageCar.fxml");
    }

    public void moveToManageClient(MouseEvent mouseEvent) {
        loadPage("/com/vinfast/fe/ManageClient.fxml");

    }

    public void moveToWarehouse(MouseEvent mouseEvent) {
        loadPage("/com/vinfast/fe/Warehouse.fxml");

    }

    public void moveToSupport(MouseEvent mouseEvent) {
        loadPage("/com/vinfast/fe/Support.fxml");

    }

    public void moveToReport(MouseEvent mouseEvent) {
        loadPage("/com/vinfast/fe/Report.fxml");

    }
}