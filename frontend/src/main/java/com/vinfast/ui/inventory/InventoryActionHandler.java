package com.vinfast.ui.inventory;

import com.vinfast.api.InventoryApi;
import com.vinfast.dto.InventoryDTO;
import com.vinfast.ui.alert.AlertNotice;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class InventoryActionHandler {
    private final TableView<InventoryDTO> inventoryTable;
    private final Consumer<List<InventoryDTO>> updateTableCallback;
    private AlertNotice alertNotice = new AlertNotice();
    private final InventoryApi inventoryApi = new InventoryApi();

    public InventoryActionHandler(TableView<InventoryDTO> inventoryTable, Consumer<List<InventoryDTO>> updateTableCallback) {
        this.inventoryTable = inventoryTable;
        this.updateTableCallback = updateTableCallback;
    }

    public void handleAdd() {
        Dialog<InventoryDTO> dialog = new Dialog<>();
        dialog.setTitle("Thêm Kho Mới");
        dialog.setHeaderText("Nhập thông tin kho mới");

        TextField nameField = new TextField();
        nameField.setPromptText("Tên kho");

        TextField locationField = new TextField();
        locationField.setPromptText("Địa chỉ");

        TextField capacityField = new TextField();
        capacityField.setPromptText("Sức chứa");
    /*
        TextField phoneNumberField = new TextField();
        nameField.setPromptText("Số điện thoại");
    */

        VBox vbox = new VBox(
            10,
            nameField, locationField,capacityField
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

        grid.add(new Label("Tên kho:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Địa chỉ:"), 0, 1);
        grid.add(locationField, 1, 1);
        grid.add(new Label("Sức chứa:"), 0, 2);
        grid.add(capacityField, 1, 2);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                try {
                    InventoryDTO newInventory = new InventoryDTO();
                    newInventory.setId((long)0);
                    newInventory.setName(nameField.getText());
                    newInventory.setLocation(locationField.getText());
                    newInventory.setCapacity((Integer.parseInt(capacityField.getText())));
                    return newInventory;
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

        Optional<InventoryDTO> result = dialog.showAndWait();
        result.ifPresent(inventory -> {

            new Thread(() -> {
                try {
                    int statusCode = inventoryApi.addInventory(inventory);
                    Platform.runLater(() -> {
                        if (statusCode != 200) {
                            alertNotice.showAlert(Alert.AlertType.ERROR, "Lỗi", "Có lỗi xảy ra khi thêm kho", null);
                           return;
                        }
                        reloadData();
                        alertNotice.showAlert(Alert.AlertType.INFORMATION, "Thành công", "Thêm kho thành công", null);
                    });
                } catch (Exception ex) {
                    Platform.runLater(() -> {
                        alertNotice.showAlert(Alert.AlertType.ERROR, "Lỗi", "Có lỗi xảy ra khi thêm kho", null);
                    });
                }
            }).start();
        });
        reloadData();
    }

    public void handleEditInventory() {
        InventoryDTO selectedInventory = inventoryTable.getSelectionModel().getSelectedItem();
        if (selectedInventory != null) {
            Dialog<InventoryDTO> dialog = new Dialog<>();
            dialog.setTitle("Sửa Thông Tin Kho");
            dialog.setHeaderText("Chỉnh sửa thông tin kho");

            ButtonType saveButtonType = new ButtonType("Lưu", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

            // Tạo các trường nhập
            TextField idField = new TextField(String.valueOf(selectedInventory.getId()));
            TextField nameField = new TextField(selectedInventory.getName());
            TextField locationField = new TextField(String.valueOf(selectedInventory.getLocation()));
            TextField capacityField = new TextField(String.valueOf(selectedInventory.getCapacity()));

            // Dùng GridPane để bố trí layout
            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new Insets(20, 150, 10, 10));

            // Thêm các trường vào grid
            grid.add(new Label("Tên kho:"), 0, 0);
            grid.add(nameField, 1, 0);
            grid.add(new Label("Địa chỉ:"), 0, 2);
            grid.add(locationField, 1, 2);
            grid.add(new Label("Sức chứa:"), 0, 3);
            grid.add(capacityField, 1, 3);

            dialog.getDialogPane().setContent(grid);

            // Xử lý khi người dùng bấm "Lưu"
            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == saveButtonType) {
                    selectedInventory.setId(Long.parseLong(idField.getText()));
                    selectedInventory.setName(nameField.getText());
                    selectedInventory.setLocation(locationField.getText());
                    selectedInventory.setCapacity(Integer.parseInt(capacityField.getText()));

                    return selectedInventory;
                }
                return null;
            });

            Optional<InventoryDTO> result = dialog.showAndWait();
            result.ifPresent(inventory -> {
                int statusCode = inventoryApi.updateInventory(inventory);
                if (statusCode != 200) {
                    alertNotice.showAlert(Alert.AlertType.ERROR, "Thất bại", "Cập nhật thất bại", null);
                    return;
                }
                alertNotice.showAlert(Alert.AlertType.INFORMATION, "Thành công", "Cập nhật thành công", null);
                inventoryTable.refresh();
            });
        }
    }

    public void handleDeleteInventory() {
        InventoryDTO selectedInventory = inventoryTable.getSelectionModel().getSelectedItem();
        if (selectedInventory == null) {
            alertNotice.showAlert(Alert.AlertType.INFORMATION, "Chưa chọn kho", "Vui lòng chọn một kho để xóa", null);
            return;
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Xác Nhận Xóa");
        alert.setHeaderText("Bạn có chắc chắn muốn xóa kho này? Việc làm này sẽ KHÔNG xóa các xe trong kho này khỏi cơ sở dữ liệu");
        alert.setContentText("Kho: " + selectedInventory.getName());
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            int statusCode = inventoryApi.deleteAInventory(selectedInventory.getId());
            if (statusCode != 200) {
                alertNotice.showAlert(Alert.AlertType.ERROR, "Thất bại", "Xóa thất bại", null);
                return;
            }
            alertNotice.showAlert(Alert.AlertType.INFORMATION, "Thành công", "Xóa thành công", null);
            reloadData();
        }
    }

    private void reloadData() {
        updateTableCallback.accept(null); // Gọi callback để reload dữ liệu
    }

}
