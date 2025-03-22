package com.vinfast.controller;

import javafx.fxml.FXML;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

public class ManageCarController {
    @FXML
    private HBox contentBox;
    public void getContentBox() {
        HBox.setHgrow(contentBox, Priority.ALWAYS);
    }
}
