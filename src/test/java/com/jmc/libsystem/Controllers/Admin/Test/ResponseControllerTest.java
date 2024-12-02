package com.jmc.libsystem.Controllers.Admin.Test;

import com.jmc.libsystem.Controllers.Admin.ResponseController;
import com.jmc.libsystem.QueryDatabase.QueryBookRecommend;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
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

class ResponseControllerTest extends ApplicationTest {

    private ResponseController controller;

    @BeforeEach
    void setUp() {
        controller = new ResponseController();
        controller.getInstance().dataBook = javafx.collections.FXCollections.observableArrayList();
        controller.getInstance().total_proposal = new Label();
    }

    @Test
    void testGetDataList() throws SQLException {
        // Mock ResultSet
        ResultSet mockResultSet = mock(ResultSet.class);

        // Giả lập dữ liệu ResultSet
        when(mockResultSet.next()).thenReturn(true).thenReturn(false);
        when(mockResultSet.getString("google_book_id")).thenReturn("1");
        when(mockResultSet.getString("title")).thenReturn("Mocked Title");
        when(mockResultSet.getString("authors")).thenReturn("Mocked Author");
        when(mockResultSet.getString("state")).thenReturn("In queue");

        // Gọi hàm getDataList
        Platform.runLater(() -> controller.getInstance().getDataList(mockResultSet));

        // Chờ các sự kiện JavaFX hoàn thành
        org.testfx.util.WaitForAsyncUtils.waitForFxEvents();

        // Kiểm tra dữ liệu trong ObservableList
        ObservableList<Map<String, String>> data = controller.dataBook;
        assertNotNull(data);
        assertFalse(data.isEmpty(), "Data should not be empty");
        assertEquals("1", data.get(0).get("id"));
        assertEquals("Mocked Title", data.get(0).get("title"));
        assertEquals("Mocked Author", data.get(0).get("authors"));
        assertEquals("In queue", data.get(0).get("state"));
    }

    @Test
    void testUpdateNumber() throws SQLException {
        // Mock ResultSet
        ResultSet mockResultSet = mock(ResultSet.class);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getString("count")).thenReturn("10");

        // Mock QueryBookrcm
        try (MockedStatic<QueryBookRecommend> mockedQuery = mockStatic(QueryBookRecommend.class)) {
            mockedQuery.when(QueryBookRecommend::getCountAllPropose).thenReturn(mockResultSet);
            mockedQuery.when(() -> QueryBookRecommend.getNumberofState("In queue")).thenReturn(mockResultSet);
            mockedQuery.when(() -> QueryBookRecommend.getNumberofState("Accept")).thenReturn(mockResultSet);
            mockedQuery.when(() -> QueryBookRecommend.getNumberofState("Reject")).thenReturn(mockResultSet);

            // Gọi hàm updateNumber
            Platform.runLater(() -> {
                try {
                    String num = controller.getInstance().getNumber(mockResultSet);
                    controller.total_proposal.setText(num);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });

            // Đợi các sự kiện JavaFX hoàn thành
            WaitForAsyncUtils.waitForFxEvents();

            // Kiểm tra các Label
            assertEquals("10", controller.total_proposal.getText());
        }
    }

}
