package com.vinfast.ui.chart;

import com.vinfast.dto.CarDTO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.VBox;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CarBarChart extends VBox {
    private final BarChart<String, Number> barChart;
    private final List<CarDTO> cars;
    private final String chartTitle;

    public CarBarChart(List<CarDTO> cars, String chartTitle) {
        this.cars = cars;
        this.chartTitle = chartTitle;

        setPadding(new Insets(10));
        setSpacing(10);

        Label titleLabel = new Label(chartTitle);
        titleLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        // Define the axes
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Trạng thái");
        yAxis.setLabel("Số lượng");

        barChart = new BarChart<>(xAxis, yAxis);
        barChart.setTitle(chartTitle);
        barChart.setPrefHeight(400);
        barChart.setLegendVisible(false);

        getChildren().addAll(titleLabel, barChart);
        updateChart();
    }

    private void updateChart() {
        Map<String, Long> statusCount = cars.stream()
                .collect(Collectors.groupingBy(CarDTO::getCarStatus, Collectors.counting()));

        ObservableList<BarChart.Series<String, Number>> barChartData = FXCollections.observableArrayList();
        BarChart.Series<String, Number> series = new BarChart.Series<>();

        statusCount.forEach((status, count) -> {
            series.getData().add(new BarChart.Data<>(status, count));
        });

        barChart.getData().clear();
        barChart.getData().add(series);

        // Add tooltips to the bars
        for (BarChart.Data<String, Number> data : series.getData()) {
            Tooltip tooltip = new Tooltip(data.getYValue() + " xe");
            Tooltip.install(data.getNode(), tooltip);
        }
    }

    public void setCars(List<CarDTO> newCars) {
        this.cars.clear();
        this.cars.addAll(newCars);
        updateChart();
    }
}
