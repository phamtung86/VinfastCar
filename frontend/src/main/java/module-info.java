module com.vinfast.fe {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires java.net.http;
    requires com.fasterxml.jackson.databind;
    requires java.desktop;
    requires kernel;
    requires layout;

    opens com.vinfast to javafx.graphics, javafx.fxml;
    exports com.vinfast;

    opens com.vinfast.fe to javafx.graphics, javafx.fxml;
    exports com.vinfast.dto;

    opens com.vinfast.controller to javafx.fxml;
    exports com.vinfast.controller;

    opens com.vinfast.model to javafx.fxml;
    exports com.vinfast.model;
}
