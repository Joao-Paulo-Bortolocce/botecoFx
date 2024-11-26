module com.example.botecofx {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;
    requires java.logging;
    requires org.json;
    requires javafx.swing;
    requires jasperreports;
    requires javafx.web;
    requires ant;


    opens com.example.botecofx to javafx.fxml;
    opens com.example.botecofx.db.entidades to javafx.fxml;
    exports com.example.botecofx.db.entidades;
    exports com.example.botecofx;
}