module com.jmc.libsystem {
    requires javafx.fxml;
    requires de.jensd.fx.glyphs.fontawesome;
    requires org.apache.logging.log4j.core;
    requires com.google.gson;
    requires java.net.http;
    requires com.fasterxml.jackson.databind;
    requires org.apache.httpcomponents.client5.httpclient5;
    requires org.apache.httpcomponents.core5.httpcore5;
    requires java.sql;
    requires org.controlsfx.controls;
    requires java.desktop;
    requires jdk.compiler;
    requires mysql.connector.j;

    exports com.jmc.libsystem;
    exports com.jmc.libsystem.Controllers;
    exports com.jmc.libsystem.Controllers.Admin;
    exports com.jmc.libsystem.Controllers.User;
    exports com.jmc.libsystem.Controllers.Book;
    exports com.jmc.libsystem.Models;
    exports com.jmc.libsystem.Views;
    exports com.jmc.libsystem.Information;

    opens com.jmc.libsystem.Information to com.google.gson, javafx.fxml;
    exports com.jmc.libsystem.QueryDatabase;
    exports com.jmc.libsystem.HandleResultSet;
    exports com.jmc.libsystem.HandleJsonString;
    opens com.jmc.libsystem.Controllers to com.google.gson, javafx.fxml;

}