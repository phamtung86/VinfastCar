package com.vinfast.ui.table;

import com.vinfast.dto.CarDTO;
import com.vinfast.dto.LibraryDTO;
import com.vinfast.util.FormatUtils;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.io.InputStream;
import java.util.List;

public class CarTableConfigurer {
    private final TableView<CarDTO> carTable;
    private final TableColumn<CarDTO, Integer> idCol;
    private final TableColumn<CarDTO, String> nameCol;
    private final TableColumn<CarDTO, List<LibraryDTO>> imageCol;
    private final TableColumn<CarDTO, Double> odoCol;
    private final TableColumn<CarDTO, Integer> yearCol;
    private final TableColumn<CarDTO, String> gearCol;
    private final TableColumn<CarDTO, String> originalCol;
    private final TableColumn<CarDTO, Long> priceCol;
    private final TableColumn<CarDTO, String> statusCol;
    private final TableColumn<CarDTO, String> engineCol;

    public CarTableConfigurer(
            TableView<CarDTO> carTable,
            TableColumn<CarDTO, Integer> idCol,
            TableColumn<CarDTO, String> nameCol,
            TableColumn<CarDTO, List<LibraryDTO>> imageCol,
            TableColumn<CarDTO, Double> odoCol,
            TableColumn<CarDTO, Integer> yearCol,
            TableColumn<CarDTO, String> gearCol,
            TableColumn<CarDTO, String> originalCol,
            TableColumn<CarDTO, Long> priceCol,
            TableColumn<CarDTO, String> statusCol,
            TableColumn<CarDTO, String> engineCol
    ) {
        this.carTable = carTable;
        this.idCol = idCol;
        this.nameCol = nameCol;
        this.imageCol = imageCol;
        this.odoCol = odoCol;
        this.yearCol = yearCol;
        this.gearCol = gearCol;
        this.originalCol = originalCol;
        this.priceCol = priceCol;
        this.statusCol = statusCol;
        this.engineCol = engineCol;

        carTable.setRowFactory(tv -> {
            TableRow<CarDTO> row = new TableRow<>();
            Tooltip tooltip = new Tooltip();

            row.hoverProperty().addListener((obs, wasHovered, isNowHovered) -> {
                if (isNowHovered && !row.isEmpty()) {
                    CarDTO car = row.getItem();
                    String info = String.format(
                            "Tên: %s\nODO: %.0f km\nNăm: %d\nHộp số: %s\nXuất xứ: %s\nGiá: %s\nĐộng cơ: %s\nTrạng thái: %s\nKiểu dáng: %s\nMàu ngoài: %s\nMàu trong: %s\nChỗ ngồi: %d\nCửa: %d\nDẫn động: %s",
                            car.getName(),
                            car.getOdo(),
                            car.getYear(),
                            car.getGear(),
                            car.getOriginal(),
                            FormatUtils.formatPrice(car.getPrice()),
                            car.getEngine(),
                            car.getStatus(),
                            car.getStyle(),
                            car.getColorOut(),
                            car.getColorIn(),
                            car.getSlotSeats(),
                            car.getSlotDoor(),
                            car.getDriveTrain()
                    );
                    tooltip.setText(info);
                    row.setTooltip(tooltip);
                }
            });

            return row;
        });

    }

    public void configureSize() {
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

    public void configureStyle() {
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
                if (empty || images == null || images.isEmpty()) {
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
                        InputStream stream = getClass().getResourceAsStream("/com/vinfast/fe/icon/available.png");
                        if (stream != null) {
                            ImageView successIcon = new ImageView(new Image(stream));
                            successIcon.setFitWidth(16);
                            successIcon.setFitHeight(16);
                            setGraphic(successIcon);
                        } else {
                            System.err.println("Failed to load icon at /com/vinfast/fe/icon/available.png");
                            Circle successIcon = new Circle(8, Color.GREEN);
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
        try {
            ImageView imageView = new ImageView(new Image(imagePath));
            imageView.setFitWidth(70);
            imageView.setFitHeight(50);
            return imageView;
        } catch (Exception e) {
            System.err.println("Failed to load image: " + imagePath);
            return new ImageView();
        }
    }
}