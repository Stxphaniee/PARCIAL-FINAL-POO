module org.example.parcial_final {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires com.microsoft.sqlserver.jdbc;


    opens org.example.parcial_final to javafx.fxml;
    exports org.example.parcial_final;
}