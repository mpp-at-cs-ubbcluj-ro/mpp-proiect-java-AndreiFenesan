module com.example.labgui {
    requires javafx.controls;
    requires javafx.fxml;
    requires labGui.backend.main;


    opens com.example.labgui to javafx.fxml;
    opens com.example.labgui.controllers to javafx.fxml;
    exports com.example.labgui;
}