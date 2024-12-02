package com.jmc.libsystem.Controllers.User.UI;

import com.jmc.libsystem.Controllers.User.DashboardController;
import com.jmc.libsystem.Information.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.collections.ObservableList;
import javafx.scene.layout.HBox;

public class DashboardControllerUITest {

    @Mock
    private DashboardController dashboardController;

    @Mock
    private TextField search_tf;

    @Mock
    private Button search_btn;

    @Mock
    private HBox resultList_hb;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        dashboardController = new DashboardController();
    }

//    @Test
//    public void testSearchBooksUI() {
//        // Giả lập hành động tìm kiếm sách
//        String searchTerm = "Java";
//        ObservableList<Book> resultList = dashboardController.searchBooks(searchTerm);
//
//        // Kiểm tra rằng các sách có tên chứa "Java" được hiển thị
//        assertNotNull(resultList);
//        assertTrue(resultList.get(0).getTitle().contains("Java"));
//    }

    @Test
    public void testPopularBooksUI() {
        // Giả lập việc hiển thị sách phổ biến
        HBox popularBox = dashboardController.popular_hbox;
        assertNotNull(popularBox.getChildren());
    }

    @Test
    public void testReadingBooksUI() {
        // Kiểm tra việc hiển thị sách đang đọc
        HBox readingBox = dashboardController.reading_hbox;
        assertNotNull(readingBox.getChildren());
    }

    @Test
    public void testNoticeButton() {
        // Kiểm tra nút thông báo
        Button noticeButton = dashboardController.notice_btn;
        noticeButton.fire();
        verify(noticeButton, times(1)).fire();
    }
}
