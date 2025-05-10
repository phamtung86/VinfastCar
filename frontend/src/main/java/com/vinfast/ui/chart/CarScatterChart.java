package com.vinfast.ui.chart;

import com.vinfast.dto.CarDTO;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.VBox;

import java.util.List;

public class CarScatterChart extends VBox {
    private final ScatterChart<Number, Number> scatterChart;
    private final List<CarDTO> cars;
    private final String chartTitle;
    private final String yAxisLabel;

    public CarScatterChart(List<CarDTO> cars, String chartTitle, String yAxisLabel) {
        this.cars = cars;
        this.chartTitle = chartTitle;
        this.yAxisLabel = yAxisLabel;

        setSpacing(10);

        Label titleLabel = new Label(chartTitle);
        titleLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        NumberAxis xAxis = new NumberAxis();
        xAxis.setLabel("Năm sản xuất");
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel(yAxisLabel);

        scatterChart = new ScatterChart<>(xAxis, yAxis);
        scatterChart.setTitle(chartTitle);
        scatterChart.setPrefHeight(400);

        getChildren().addAll(titleLabel, scatterChart);
        updateChart();
    }

    private void updateChart() {
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        series.setName("Xe");

        for (CarDTO car : cars) {
            XYChart.Data<Number, Number> data = new XYChart.Data<>(car.getYear(), car.getPrice() / 1_000_000.0);
            data.setNode(new javafx.scene.Node() {
                protected void updateBounds() {
                }
            });
            Tooltip.install(data.getNode(), new Tooltip(car.getName() + ": " + car.getYear() + ", " + (car.getPrice() / 1_000_000.0) + " triệu VND"));
            series.getData().add(data);
        }

        scatterChart.getData().setAll(series);
    }

    public void setCars(List<CarDTO> newCars) {
        this.cars.clear();
        this.cars.addAll(newCars);
        updateChart();
    }
}