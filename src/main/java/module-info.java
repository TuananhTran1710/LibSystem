module com.jmc.libsystem {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires de.jensd.fx.glyphs.fontawesome;

    opens com.jmc.libsystem to javafx.fxml;
    exports com.jmc.libsystem;
    exports com.jmc.libsystem.Controllers;

}