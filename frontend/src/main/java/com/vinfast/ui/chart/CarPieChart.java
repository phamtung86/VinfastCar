package com.vinfast.ui.chart;

import com.vinfast.dto.CarDTO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.VBox;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CarPieChart extends VBox {
    private final PieChart pieChart;
    private final List<CarDTO> cars;
    private final String chartTitle;

    public CarPieChart(List<CarDTO> cars, String chartTitle) {
        this.cars = cars;
        this.chartTitle = chartTitle;

        setPadding(new Insets(10));
        setSpacing(10);

        Label titleLabel = new Label(chartTitle);
        titleLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        pieChart = new PieChart();
        pieChart.setTitle(chartTitle);
        pieChart.setPrefHeight(400);
        pieChart.setLabelsVisible(true);
        pieChart.setLegendVisible(true);

        getChildren().addAll(titleLabel, pieChart);
        updateChart();
    }

    private void updateChart() {
        Map<String, Long> statusCount = cars.stream()
                .collect(Collectors.groupingBy(CarDTO::getCarStatus, Collectors.counting()));

        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        statusCount.forEach((status, count) -> {
            String name = status + " (" + count + ")";
            pieChartData.add(new PieChart.Data(name, count));
        });

        pieChart.setData(pieChartData);

        String[] colors = {"#00cc00", "#ff3333", "#3399ff", "#ffcc00"};
        int i = 0;
        for (PieChart.Data data : pieChart.getData()) {
            data.getNode().setStyle("-fx-pie-color: " + colors[i % colors.length] + ";");
            Tooltip tooltip = new Tooltip(data.getName() + ": " + (int) data.getPieValue() + " xe");
            Tooltip.install(data.getNode(), tooltip);
            i++;
        }
    }

    public void setCars(List<CarDTO> newCars) {
        this.cars.clear();
        this.cars.addAll(newCars);
        updateChart();
    }
}