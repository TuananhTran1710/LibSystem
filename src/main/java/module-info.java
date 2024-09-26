module com.jmc.libsystem {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;

    opens com.jmc.libsystem to javafx.fxml;
    exports com.jmc.libsystem;
}