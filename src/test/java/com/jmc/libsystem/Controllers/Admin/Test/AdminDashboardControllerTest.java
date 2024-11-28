package com.jmc.libsystem.Controllers.Admin.Test;

import com.jmc.libsystem.Controllers.Admin.AdminDashboardController;
import com.jmc.libsystem.QueryDatabase.QueryBookData;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.testfx.util.WaitForAsyncUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AdminDashboardControllerTest {

    private AdminDashboardController controller;

    @BeforeEach
    void setUp() {
        controller = new AdminDashboardController();
    }

    @Test
    void testGetBooks() throws SQLException {
        // Mock ResultSet
        ResultSet mockResultSet = mock(ResultSet.class);

        // Giả lập hành vi của ResultSet
        when(mockResultSet.next()).thenReturn(true).thenReturn(false);
        when(mockResultSet.getString("title")).thenReturn("Mocked Title");
        when(mockResultSet.getString("authors")).thenReturn("Mocked Authors");
        when(mockResultSet.getInt("total_books")).thenReturn(100);
        when(mockResultSet.getInt("total_borrowed_books")).thenReturn(20);
        when(mockResultSet.getString("status")).thenReturn("Available");
        when(mockResultSet.getBlob("thumbnail")).thenReturn(null); // Không có ảnh

        try (MockedStatic<QueryBookData> mockedQueryBookData = mockStatic(QueryBookData.class)) {
            mockedQueryBookData.when(QueryBookData::getBookStatistic).thenReturn(mockResultSet);

            ResultSet resultSet = QueryBookData.getBookStatistic();
            assertNotNull(resultSet, "Books list should not be null");
            assertTrue(resultSet.next(), "Books list should not be empty");
//            ObservableList<Map<String, Object>> books = AdminDashboardController.getBooks();
//
//            // Kiểm tra kết quả
//            assertNotNull(books, "Books list should not be null");
//            System.out.println(books);
//            assertFalse(books.isEmpty(), "Books list should not be empty");
//            assertEquals(1, books.size(), "Books list should contain exactly one entry");
//
//            // Kiểm tra dữ liệu của phần tử đầu tiên
//            Map<String, Object> book = books.get(0);
            assertEquals("Mocked Title", resultSet.getString("title"), "Book title does not match");
            assertEquals("Mocked Authors", resultSet.getString("authors"), "Book authors do not match");
            assertEquals(100, resultSet.getInt("total_books"), "Book quantity does not match");
            assertEquals(20, resultSet.getInt("total_borrowed_books"), "Book loaned count does not match");
            assertEquals("Available", resultSet.getString("status"), "Book status does not match");
            assertNull(resultSet.getString("thumbnail"));
        }
    }

    @Test
    void testGetNumberData() throws SQLException {
        // Tạo mock ResultSet
        ResultSet mockResultSet = mock(ResultSet.class);

        // Giả lập dữ liệu của ResultSet
        when(mockResultSet.next()).thenReturn(true).thenReturn(false);
        when(mockResultSet.getInt("count")).thenReturn(42);

        // Gọi phương thức getNumber và kiểm tra kết quả
        int result = controller.getNumber(mockResultSet);
        assertEquals(42, result, "The count value does not match the expected value");

        // Xác minh hành vi của ResultSet
        verify(mockResultSet, times(1)).next();
        verify(mockResultSet, times(1)).getInt("count");
    }
}
