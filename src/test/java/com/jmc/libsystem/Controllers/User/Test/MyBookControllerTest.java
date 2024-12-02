package com.jmc.libsystem.Controllers.User.Test;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.jmc.libsystem.Controllers.User.MyBookController;
import com.jmc.libsystem.HandleResultSet.SearchBookDatabase;
import com.jmc.libsystem.Information.Book;
import com.jmc.libsystem.Information.User;
import com.jmc.libsystem.Models.Model;
import com.jmc.libsystem.QueryDatabase.QueryBookLoans;
import com.jmc.libsystem.QueryDatabase.QueryFavoriteBook;
import javafx.scene.control.Label;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.testfx.framework.junit5.ApplicationTest;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

class MyBookControllerTest extends ApplicationTest {
    private MyBookController controller;

    @Mock
    private Label numberBorrowMock;

    @BeforeEach
    public void setUp() {
        User user = new User("12", "hehe", "heke", "dad", "availbe");
        Model.getInstance().setMyUser(user);
        controller = new MyBookController();
        controller.NumberBorrow = numberBorrowMock;
    }

    @Test
    public void testGetBorrowBook() throws Exception {
        // Giả lập kết quả ResultSet
        ResultSet mockResultSet = mock(ResultSet.class);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getInt("total_borrows")).thenReturn(5);

        // Mock phương thức tĩnh QueryBookLoans.getTotalLoaned
        try (MockedStatic<QueryBookLoans> mockedStatic = Mockito.mockStatic(QueryBookLoans.class)) {
            mockedStatic.when(() -> QueryBookLoans.getTotalLoaned("user123")).thenReturn(mockResultSet);

            // Gọi phương thức cần kiểm tra
            int totalBorrows = MyBookController.getBorrowBook("user123");

            // Kiểm tra kết quả
            assertEquals(5, totalBorrows, "The total borrowed books should be 5");

            // Kiểm tra phương thức tĩnh được gọi đúng tham số
            mockedStatic.verify(() -> QueryBookLoans.getTotalLoaned("user123"), times(1));
        }
    }

}
