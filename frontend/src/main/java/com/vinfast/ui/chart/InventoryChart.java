package com.vinfast.ui.chart;

import com.vinfast.api.InventoryApi;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.layout.HBox;

public class InventoryChart {

    @FXML
    private HBox inventoryChart;
    private InventoryApi inventoryApi;

    public void initialize() {

        // Dữ liệu kho
        String[] warehouses = {"Kho VN39", "Kho HN16", "Kho HN15", "Kho HN12", "Kho VN30"};
        int[] carCounts = {11, 9, 9, 9, 9};
        int[] capacities = {38, 36, 41, 42, 43};

        // Tính tỉ lệ sử dụng
        double[] usageRates = new double[warehouses.length];
        for (int i = 0; i < warehouses.length; i++) {
            usageRates[i] = (double) carCounts[i] / capacities[i] * 100.0;
        }

        // Trục X và Y
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxisLeft = new NumberAxis();
        NumberAxis yAxisRight = new NumberAxis(0, 100, 10); // phần trăm

        xAxis.setLabel("Tên kho");
        yAxisLeft.setLabel("Số xe");
        yAxisRight.setLabel("Tỉ lệ sử dụng (%)");

        // Bar chart
        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxisLeft);
        barChart.setLegendVisible(false);
        barChart.setBarGap(5);
        barChart.setCategoryGap(20);
        barChart.setStyle("-fx-bar-fill: skyblue;");

        XYChart.Series<String, Number> carSeries = new XYChart.Series<>();
        for (int i = 0; i < warehouses.length; i++) {
            carSeries.getData().add(new XYChart.Data<>(warehouses[i], carCounts[i]));
        }
        barChart.getData().add(carSeries);

        // Line chart (dùng chung trục X)
        LineChart<String, Number> lineChart = new LineChart<>(xAxis, yAxisRight);
        lineChart.setLegendVisible(false);
        lineChart.setCreateSymbols(true);
        lineChart.setOpacity(0.9);
        lineChart.setAlternativeRowFillVisible(false);
        lineChart.setHorizontalGridLinesVisible(false);
        lineChart.setVerticalGridLinesVisible(false);
        lineChart.setAnimated(false);

        XYChart.Series<String, Number> usageSeries = new XYChart.Series<>();
        for (int i = 0; i < warehouses.length; i++) {
            usageSeries.getData().add(new XYChart.Data<>(warehouses[i], usageRates[i]));
        }
        lineChart.getData().add(usageSeries);

        // Gộp cả hai biểu đồ vào cùng layout
        HBox combinedChart = new HBox();
        combinedChart.getChildren().addAll(barChart, lineChart);
        combinedChart.setSpacing(-barChart.getCategoryGap()); // chống lệch trục X

        // Gắn vào giao diện
        inventoryChart.getChildren().add(combinedChart);
    }
}
