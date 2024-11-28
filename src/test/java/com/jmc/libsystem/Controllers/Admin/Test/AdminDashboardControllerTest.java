package com.jmc.libsystem.Controllers.Admin.Test;

import com.jmc.libsystem.Controllers.Admin.AdminDashboardController;
import com.jmc.libsystem.QueryDatabase.QueryBookData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AdminDashboardControllerTest {

    private AdminDashboardController controller;

    @BeforeEach
    void setUp() {
        controller = new AdminDashboardController();
    }

    @Test
    void testGetBooks() {
        // Tạo mock ResultSet
        ResultSet mockResultSet = mock(ResultSet.class);

        // Mock static method QueryBookData.getBookStatistic() sử dụng MockedStatic
        try (MockedStatic<QueryBookData> mockedQueryBookData = mockStatic(QueryBookData.class)) {
            // Giả lập kết quả của ResultSet
            when(mockResultSet.next()).thenReturn(true).thenReturn(false);
            when(mockResultSet.getString("title")).thenReturn("Test Title");
            when(mockResultSet.getInt("total_books")).thenReturn(10);
            when(mockResultSet.getString("authors")).thenReturn("Test Author");
            when(mockResultSet.getString("status")).thenReturn("Available");

            // Giả lập phương thức tĩnh QueryBookData.getBookStatistic()
            mockedQueryBookData.when(QueryBookData::getBookStatistic).thenReturn(mockResultSet);

            // Gọi hàm getBooks()
            var books = AdminDashboardController.getBooks();

            // Kiểm tra kết quả
            assertFalse(books.isEmpty(), "The books list should not be empty");
            assertEquals("Test Title", books.get(0).get("title"), "Book title does not match");
            assertEquals(10, books.get(0).get("quantity"), "Book quantity does not match");
            assertEquals("Test Author", books.get(0).get("authors"), "Book authors do not match");
            assertEquals("Available", books.get(0).get("status"), "Book status does not match");
        } catch (SQLException e) {
            fail("Should not throw SQLException");
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
