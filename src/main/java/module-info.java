module com.jmc.libsystem {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.jmc.libsystem to javafx.fxml;
    exports com.jmc.libsystem;
}