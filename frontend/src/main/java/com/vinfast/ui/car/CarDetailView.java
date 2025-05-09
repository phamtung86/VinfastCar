package com.vinfast.ui.car;

import com.vinfast.dto.CarDTO;
import com.vinfast.dto.LibraryDTO;
import com.vinfast.util.FormatUtils;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class CarDetailView {
    public void show(CarDTO car) {
        Stage detailStage = new Stage();
        detailStage.setTitle("Chi tiết xe: " + car.getName());

        // VBox để chứa tất cả các thông tin chi tiết xe
        VBox mainLayout = new VBox(15);
        mainLayout.setPadding(new Insets(20));
        mainLayout.setStyle("-fx-background-color: #f0f0f0;");

        // Thêm tiêu đề
        Label titleLabel = new Label("Chi tiết xe: " + car.getName());
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #333;");
        mainLayout.getChildren().add(titleLabel);

        // Danh sách thông tin xe
        VBox infoLayout = new VBox(10);
        infoLayout.setStyle("-fx-background-color: #ffffff; -fx-padding: 15; -fx-border-radius: 10; -fx-border-color: #ccc;");

        // Các thông tin xe
        addInfoRow(infoLayout, "Tên xe:", car.getName());
        addInfoRow(infoLayout, "ODO (km):", String.valueOf(car.getOdo()));
        addInfoRow(infoLayout, "Năm sản xuất:", String.valueOf(car.getYear()));
        addInfoRow(infoLayout, "Hộp số:", car.getGear());
        addInfoRow(infoLayout, "Xuất xứ:", car.getOriginal());
        addInfoRow(infoLayout, "Giá:", FormatUtils.formatPrice(car.getPrice()));
        addInfoRow(infoLayout, "Động cơ:", car.getEngine());
        addInfoRow(infoLayout, "Trạng thái:", car.getStatus());
        addInfoRow(infoLayout, "Kiểu dáng:", car.getStyle());
        addInfoRow(infoLayout, "Màu ngoài:", car.getColorOut());
        addInfoRow(infoLayout, "Màu trong:", car.getColorIn());
        addInfoRow(infoLayout, "Số chỗ ngồi:", String.valueOf(car.getSlotSeats()));
        addInfoRow(infoLayout, "Số cửa:", String.valueOf(car.getSlotDoor()));
        addInfoRow(infoLayout, "Dẫn động:", car.getDriveTrain());

        mainLayout.getChildren().add(infoLayout);

        // Thêm phần ảnh xe
        if (car.getLibraries() != null && !car.getLibraries().isEmpty()) {
            VBox imageLayout = new VBox(15);
            imageLayout.setStyle("-fx-background-color: #ffffff; -fx-padding: 15; -fx-border-radius: 10; -fx-border-color: #ccc;");
            imageLayout.getChildren().add(new Label("Ảnh xe:"));

            // FlowPane để ảnh không bị chồng lên nhau
            FlowPane flowPane = new FlowPane();
            flowPane.setVgap(10);
            flowPane.setHgap(10);
            flowPane.setPadding(new Insets(10));

            for (LibraryDTO library : car.getLibraries()) {
                try {
                    Image image = new Image(library.getUrlLink());
                    ImageView imageView = new ImageView(image);
//                    imageView.setFitWidth(300);
                    imageView.setPreserveRatio(true);
                    imageView.setStyle("-fx-effect: dropshadow(gaussian, #bcbaba, 10, 0.5, 0, 0);");

                    // Thêm hiệu ứng hover khi người dùng rê chuột qua ảnh
                    imageView.setOnMouseEntered(e -> imageView.setStyle("-fx-effect: dropshadow(gaussian, #4692ec, 5, 0.5, 0, 0);"));
                    imageView.setOnMouseExited(e -> imageView.setStyle("-fx-effect: dropshadow(gaussian, #d3d3d3, 5, 0.5, 0, 0);"));

                    flowPane.getChildren().add(imageView);
                } catch (Exception ignored) {
                }
            }
            imageLayout.getChildren().add(flowPane);
            mainLayout.getChildren().add(imageLayout);
        }

        // Tạo một ScrollPane để người dùng có thể cuộn trang nếu cần
        ScrollPane scrollPane = new ScrollPane(mainLayout);
        scrollPane.setFitToWidth(true);

        // Tạo scene và hiển thị
        Scene scene = new Scene(scrollPane, 800, 800);
        detailStage.setScene(scene);
        detailStage.show();
    }

    private void addInfoRow(VBox layout, String label, String value) {
        HBox row = new HBox(10);
        row.setStyle("-fx-padding: 5; -fx-border-color: #ddd; -fx-border-width: 0 0 1 0;");
        row.getChildren().add(new Label(label));
        Label valueLabel = new Label(value);
        valueLabel.setStyle("-fx-text-fill: #333;");
        row.getChildren().add(valueLabel);
        layout.getChildren().add(row);
    }
}
