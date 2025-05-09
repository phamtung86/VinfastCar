package com.vinfast.ui.alert;

import javafx.scene.control.Alert;

public class AlertNotice {

//    private static Object AlertType;
    private String title;
    private String message;


    public void showAlert(Alert.AlertType alertType, String title, String message, String headerText) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.setHeaderText(headerText);
        alert.showAndWait();
    }
}
