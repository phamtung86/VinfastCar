module com.vinfast.fe {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.vinfast.fe to javafx.fxml;
    exports com.vinfast.fe;
    opens com.vinfast.controller to javafx.fxml;
    exports com.vinfast.controller to javafx.fxml;

}