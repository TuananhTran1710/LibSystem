module com.jmc.libsystem {
    requires transitive javafx.controls;
    requires javafx.fxml;
    requires de.jensd.fx.glyphs.fontawesome;
    requires java.sql;
    requires transitive javafx.graphics;
    requires java.desktop;
    requires javafx.base;
    requires org.apache.logging.log4j.core;

    opens com.jmc.libsystem to javafx.fxml;

    exports com.jmc.libsystem;
    exports com.jmc.libsystem.Controllers;
    exports com.jmc.libsystem.Controllers.Admin;
    exports com.jmc.libsystem.Controllers.User;

    exports com.jmc.libsystem.Models;
    exports com.jmc.libsystem.Views;

}