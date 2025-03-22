package com.vinfast.controller;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class LoginController {
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label notificationLabel;
    @FXML
    private Button loginButton;
    @FXML
    private Button exitButton;

    @FXML
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if ("admin".equals(username) && "1234".equals(password)) {
            notificationLabel.setText("Đăng nhập thành công!");
            loadDashboard();
        } else {
            notificationLabel.setText("Sai tài khoản hoặc mật khẩu!");
        }
    }
    private void loadDashboard() {
        try {
            // Tải giao diện Dashboard từ FXML
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/vinfast/fe/AdminPage.fxml"));
            Scene scene = new Scene(fxmlLoader.load());

            // Lấy Stage hiện tại từ nút đăng nhập
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.initStyle(StageStyle.DECORATED);
            stage.setTitle("Admin - VinFast");
            Image icon = new Image(getClass().getResourceAsStream("/com/vinfast/fe/image/icon.png"));
            stage.getIcons().add(icon);
            stage.show();
            stage.centerOnScreen();
            Stage loginStage =(Stage) loginButton.getScene().getWindow();
            loginStage.close();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Lỗi", "Không thể tải giao diện Dashboard!");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    @FXML
    private void exitLogin(){
        Stage stage = (Stage) exitButton.getScene().getWindow();
        stage.close();

    }
}