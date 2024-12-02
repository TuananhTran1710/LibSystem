package com.jmc.libsystem.Controllers.User.Test;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.jmc.libsystem.Controllers.User.MyBookController;
import javafx.scene.control.Label;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

class MyBookControllerTest {
    private MyBookController controller;

    @Mock
    private Label numberBorrowMock;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        controller = new MyBookController();
        controller.NumberBorrow = numberBorrowMock;
    }

    @Test
    public void testRefreshData() {
        // Giả định: Phương thức refreshData sẽ cập nhật lại số sách mượn.
        int expectedBorrowedBooks = 5;

        // Giả sử refreshData() sẽ cập nhật label với số lượng sách mượn
        controller.refreshData();  // Đây là phương thức cần được kiểm tra

        // Kiểm tra: Xác minh xem Label đã được cập nhật đúng chưa
        verify(numberBorrowMock).setText("5");
    }

    @Test
    public void testModifyBookLimit() {
        // Giả lập thay đổi giới hạn sách mượn
        MyBookController.limitBookBorrowing = 50;
        MyBookController.limitBookFavorite = 50;

        // Kiểm tra sự thay đổi sau khi thay đổi giá trị
        assertEquals(50, MyBookController.limitBookBorrowing);
        assertEquals(50, MyBookController.limitBookFavorite);
    }
}
