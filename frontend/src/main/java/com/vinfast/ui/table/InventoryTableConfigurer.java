package com.vinfast.ui.table;

import com.vinfast.dto.InventoryDTO;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class InventoryTableConfigurer {
    private final TableView<InventoryDTO> inventoryTable;
    private final TableColumn<InventoryDTO, Integer> idCol;
    private final TableColumn<InventoryDTO, String> nameCol;
    private final TableColumn<InventoryDTO, Integer> capacityCol;
    private final TableColumn<InventoryDTO, String> locationCol;
    private final TableColumn<InventoryDTO, Integer> carCountCol;
    private final TableColumn<InventoryDTO, Float> statusCol;


    public InventoryTableConfigurer(
            TableView<InventoryDTO> inventoryTable,
            TableColumn<InventoryDTO, Integer> idCol,
            TableColumn<InventoryDTO, String> nameCol,
            TableColumn<InventoryDTO, String> locationCol,
            TableColumn<InventoryDTO, Integer> capacityCol,
            TableColumn<InventoryDTO, Integer> carCountCol,
            TableColumn<InventoryDTO, Float> statusCol
    ) {
        this.inventoryTable = inventoryTable;
        this.idCol = idCol;
        this.nameCol = nameCol;
        this.capacityCol = capacityCol;
        this.locationCol = locationCol;
        this.carCountCol = carCountCol;
        this.statusCol = statusCol;
    }

    public void configureSize() {
        inventoryTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        idCol.prefWidthProperty().bind(inventoryTable.widthProperty().multiply(0.05));
        nameCol.prefWidthProperty().bind(inventoryTable.widthProperty().multiply(0.20));
        locationCol.prefWidthProperty().bind(inventoryTable.widthProperty().multiply(0.25));
        capacityCol.prefWidthProperty().bind(inventoryTable.widthProperty().multiply(0.15));
        carCountCol.prefWidthProperty().bind(inventoryTable.widthProperty().multiply(0.15));
        statusCol.prefWidthProperty().bind(inventoryTable.widthProperty().multiply(0.15));
    }

    public void configureStyle() {
        idCol.setStyle("-fx-alignment: CENTER;");
        nameCol.setStyle("-fx-alignment: CENTER;");
        locationCol.setStyle("-fx-alignment: CENTER;");
        capacityCol.setStyle("-fx-alignment: CENTER;");
        carCountCol.setStyle("-fx-alignment: CENTER;");
        statusCol.setStyle("-fx-alignment: CENTER;");
    }

    public void setDataIntoTableView() {
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        locationCol.setCellValueFactory(new PropertyValueFactory<>("location"));
        capacityCol.setCellValueFactory(new PropertyValueFactory<>("capacity"));
        carCountCol.setCellValueFactory(new PropertyValueFactory<>("carCount"));
        statusCol.setCellValueFactory(new PropertyValueFactory<>("perFull"));
    }
}
