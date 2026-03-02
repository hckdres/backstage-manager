module com.example.ax0006 {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.ax0006 to javafx.fxml;
    exports com.example.ax0006;
}