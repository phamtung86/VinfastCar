module com.vinfast.fe {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;

    opens com.vinfast to javafx.graphics, javafx.fxml;
    exports com.vinfast;

    opens com.vinfast.fe to javafx.graphics, javafx.fxml;
    exports com.vinfast.fe;

    opens com.vinfast.controller to javafx.fxml;
    exports com.vinfast.controller;
}
