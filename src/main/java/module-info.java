module com.example.javaescape2 {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.javaescape2 to javafx.fxml;
    exports com.example.javaescape2;
}