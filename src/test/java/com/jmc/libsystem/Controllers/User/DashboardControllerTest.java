package com.jmc.libsystem.Controllers.User;

import com.jmc.libsystem.HandleResultSet.SearchBookDatabase;
import com.jmc.libsystem.Information.Book;
import com.jmc.libsystem.Views.SearchCriteria;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class DashboardControllerTest {

    private DashboardController controller;

    @BeforeEach
    void setUp() {
        controller = new DashboardController();

        // Mock các thành phần UI
        controller.search_tf = mock(TextField.class);
        controller.resultList_hb = mock(HBox.class);
        controller.criteriaBox = mock(ChoiceBox.class);
        controller.num_show_search = mock(ChoiceBox.class);

        // Đặt giá trị mặc định cho các component mock
        when(controller.num_show_search.getValue()).thenReturn(20);
    }

    // Test cho phương thức tìm kiếm sách
    @Test
    void testSearchBooks_success() {
        // Giả lập từ khóa tìm kiếm
        when(controller.search_tf.getText()).thenReturn("Java");

        // Giả lập kết quả trả về từ SearchBookDatabase
        List<Book> mockBooks = List.of(new Book("Java Programming", "Author1", "Programming".getBytes()));
        mockStatic(SearchBookDatabase.class);
        when(SearchBookDatabase.getBookFromResultSet("Java", "TITLE")).thenReturn(mockBooks);

        // Gọi phương thức
        controller.searchBooks(20);

        // Kiểm tra danh sách sách tìm kiếm
        assertNotNull(DashboardController.bookSearch);
        assertEquals(1, DashboardController.bookSearch.size());
        assertEquals("Java Programming", DashboardController.bookSearch.get(0).getTitle());

        // Kiểm tra UI được cập nhật
        verify(controller.resultList_hb, times(1)).requestLayout();
    }

    // Test cho trường hợp không nhập từ khóa
    @Test
    void testSearchBooks_emptyKeyword() {
        // Giả lập từ khóa rỗng
        when(controller.search_tf.getText()).thenReturn("");

        // Gọi phương thức
        controller.searchBooks(20);

        // Kiểm tra danh sách sách trống
        assertTrue(DashboardController.bookSearch.isEmpty());
    }

    // Test reset giao diện
    @Test
    void testReset() {
        // Gọi phương thức reset
        controller.reset();

        // Kiểm tra các thành phần được reset
        verify(controller.search_tf, times(1)).clear();
        verify(controller.criteriaBox, times(1)).setValue(SearchCriteria.TITLE);
        verify(controller.resultList_hb, times(1)).getChildren().clear();
    }

    // Test thay đổi số lượng sách hiển thị
    @Test
    void testModifyShowBookSearch() {
        // Gán giá trị cho num_show_search
        when(controller.num_show_search.getValue()).thenReturn(50);

        // Gọi phương thức
        controller.modifyShowBookSearch();

        // Kiểm tra giới hạn sách hiển thị
        assertEquals(50, DashboardController.limitBookSearch);
    }
}
