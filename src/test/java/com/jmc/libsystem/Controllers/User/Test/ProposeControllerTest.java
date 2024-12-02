package com.jmc.libsystem.Controllers.User.Test;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;
import javafx.stage.Stage;
import com.jmc.libsystem.Controllers.User.ProposeController;
import com.jmc.libsystem.QueryDatabase.QueryBookRecommend;
import com.jmc.libsystem.Information.Book;

import java.util.Arrays;
import java.util.List;

public class ProposeControllerTest extends ApplicationTest {

    private ProposeController proposeController;

    @BeforeEach
    public void setUp() {
        // Khởi tạo ProposeController duy nhất
        proposeController = new ProposeController();
    }

    @Test
    public void testShowPreSuggest() {
        // Giả lập các phụ thuộc khi cần thiết
        QueryBookRecommend mockQueryBookRecommend = mock(QueryBookRecommend.class);
        List<Book> mockBooks = Arrays.asList(new Book("Book 1", "Cuong", new byte[]{}), new Book("Book 2", "Cuong", new byte[]{}));
        when(mockQueryBookRecommend.getPreSuggestBooks("1")).thenReturn(mockBooks);

        // Gọi phương thức showPreSuggest
        proposeController.showPreSuggest();

        // Kiểm tra xem danh sách sách đề xuất đã được cập nhật đúng chưa
        assertEquals(mockBooks, proposeController.bookPreSuggest);
    }

    @Test
    public void testResetPreSuggest() {
        // Kiểm tra việc làm mới danh sách đề xuất
        proposeController.resetPreSuggest();

        // Kiểm tra xem phương thức showPreSuggest đã được gọi
        verify(proposeController, times(1)).showPreSuggest();
        // Kiểm tra xem scrollPane_suggest đã được reset chưa
        assertEquals(0.0, proposeController.scrollPane_suggest.getHvalue(), 0.0);
    }
}

