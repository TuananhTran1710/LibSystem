package com.jmc.libsystem.Controllers.Admin.Test;

import com.jmc.libsystem.Controllers.Admin.ManageBookController;
import com.jmc.libsystem.Controllers.Admin.ManageUserController;
import com.jmc.libsystem.QueryDatabase.QueryAccountData;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.util.WaitForAsyncUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ManageUserControllerTest extends ApplicationTest {

    private ManageUserController controller;

    @BeforeEach
    void setUp() {
        controller = new ManageUserController();
    }

    @Test
    void testgetData() throws SQLException {
        // Mock ResultSet
        ResultSet mockResultSet = mock(ResultSet.class);
        when(mockResultSet.next()).thenReturn(true).thenReturn(false);
        when(mockResultSet.getString("user_id")).thenReturn("Mocked Id");
        when(mockResultSet.getString("fullName")).thenReturn("Mocked Name");
        when(mockResultSet.getString("email")).thenReturn("Mocked email");
        when(mockResultSet.getString("password")).thenReturn("Mocked password");
        when(mockResultSet.getString("state")).thenReturn("Active");

        controller.getInstance().data = FXCollections.observableArrayList();
        controller.getInstance().getData(mockResultSet);
        ObservableList<Map<String, Object>> data = controller.getInstance().data;

        WaitForAsyncUtils.waitForFxEvents();

        assertNotNull(data, "Data should not be null");
        assertFalse(data.isEmpty(), "Data should not be empty");
        assertEquals("Mocked Id", data.get(0).get("id"));
        assertEquals("Mocked Name", data.get(0).get("name"));
        assertEquals("Mocked email", data.get(0).get("email"));
        assertEquals("Mocked password", data.get(0).get("password"));
        assertEquals("Active", data.get(0).get("state"));
    }

    @Test
    void testSearchAction() throws SQLException {
        // Mock ResultSet
        ResultSet mockResultSet = mock(ResultSet.class);
        when(mockResultSet.next()).thenReturn(true).thenReturn(false);
        when(mockResultSet.getString("user_id")).thenReturn("2");
        when(mockResultSet.getString("fullName")).thenReturn("Search User");
        when(mockResultSet.getString("email")).thenReturn("search@example.com");
        when(mockResultSet.getString("password")).thenReturn("password456");
        when(mockResultSet.getString("state")).thenReturn("inactive");

        // Mock QueryAccountData.getAccountForSearch
        try (MockedStatic<QueryAccountData> mockedQuery = mockStatic(QueryAccountData.class)) {
            mockedQuery.when(() -> QueryAccountData.getAccountForSearch("Search User", "fullName"))
                    .thenReturn(mockResultSet);

            // Thiết lập tiêu chí tìm kiếm
            controller.criteriaBox = new ChoiceBox<>();
            controller.search_tf = new TextField();
            controller.criteriaBox.setValue("fullName");
            controller.search_tf.setText("Search User");
            controller.data = FXCollections.observableArrayList();
            controller.getInstance().getData(QueryAccountData.getAccountForSearch("Search User", "fullName"));
            //controller.getInstance().searchAction();

            // Chờ các sự kiện JavaFX hoàn thành
            WaitForAsyncUtils.waitForFxEvents();
            //System.out.println(controller.data);

            // Kiểm tra dữ liệu trong ObservableList
            ObservableList<Map<String, Object>> data = controller.data;
            assertNotNull(data);
            assertFalse(data.isEmpty(), "Search result should not be empty");
            assertEquals("2", data.get(0).get("id"));
            assertEquals("Search User", data.get(0).get("name"));
        }
    }

}