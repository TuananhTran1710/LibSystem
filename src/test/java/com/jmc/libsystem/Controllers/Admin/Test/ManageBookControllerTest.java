package com.jmc.libsystem.Controllers.Admin.Test;

import com.jmc.libsystem.Controllers.Admin.ManageBookController;
import com.jmc.libsystem.QueryDatabase.QueryBookData;
import com.jmc.libsystem.Views.SearchCriteria;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.util.WaitForAsyncUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ManageBookControllerTest extends ApplicationTest {

    private ManageBookController controller;

    @BeforeEach
    void setUp() {
        controller = new ManageBookController();
    }

    @Test
    void testgetData() throws SQLException {
        // Mock ResultSet
        ResultSet mockResultSet = mock(ResultSet.class);
        when(mockResultSet.next()).thenReturn(true).thenReturn(false);
        when(mockResultSet.getString("title")).thenReturn("Mocked Title");
        when(mockResultSet.getString("authors")).thenReturn("Mocked Authors");
        when(mockResultSet.getString("category")).thenReturn("Mocked Category");
        when(mockResultSet.getString("state")).thenReturn("Publishing");
        when(mockResultSet.getString("google_book_id")).thenReturn("123");
        when(mockResultSet.getBlob("thumbnail")).thenReturn(null); // Không có ảnh

        // Mock QueryBookData.getAllBook()
        controller.getInstance().data = FXCollections.observableArrayList();
        controller.getInstance().getData(mockResultSet);
        ObservableList<Map<String, Object>> data = controller.getInstance().data;
        WaitForAsyncUtils.waitForFxEvents();
        assertNotNull(data, "Data should not be null");
        assertFalse(data.isEmpty(), "Data should not be empty");
        assertEquals("Mocked Title", data.get(0).get("title"));
        assertEquals("Mocked Authors", data.get(0).get("authors"));
        assertEquals("Mocked Category", data.get(0).get("category"));
        assertEquals("Publishing", data.get(0).get("state"));
    }
}
