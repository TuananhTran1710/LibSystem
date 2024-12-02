package com.jmc.libsystem.Controllers.User.Test;

import com.jmc.libsystem.Controllers.User.DashboardController;
import com.jmc.libsystem.Information.Book;
import com.jmc.libsystem.Information.User;
import com.jmc.libsystem.Models.Model;
import com.jmc.libsystem.QueryDatabase.QueryBookData;
import com.jmc.libsystem.QueryDatabase.QueryBookLoans;
import com.jmc.libsystem.HandleResultSet.SearchBookDatabase;
import com.jmc.libsystem.SuggestionBox.SuggestionBook;
import com.jmc.libsystem.Views.SearchCriteria;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.util.WaitForAsyncUtils;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;

public class DashboardControllerTest extends ApplicationTest {

    private SearchBookDatabase searchBookDatabase;
    private QueryBookLoans queryBookLoans;
    private User currentUser;
    private DashboardController dashboardController;

    @BeforeEach
    public void setUp() {
        User user = new User("12", "hehe", "heke", "dad", "availbe");
        Model.getInstance().setMyUser(user);
        // Mocking inline (không sử dụng @Mock hay MockitoAnnotations)

        queryBookLoans = mock(QueryBookLoans.class);
        currentUser = new User("user123", "John Doe", "john.doe@example.com", "password123", "active");

        // Khởi tạo DashboardController mà không cần inject mocks
        dashboardController = new DashboardController();
        dashboardController.search_tf = new TextField();
    }

    @Test
    public void testSearchBooks() throws SQLException {

        try (MockedStatic<SearchBookDatabase> mockedStatic = Mockito.mockStatic(SearchBookDatabase.class)) {
            // Giả lập kết quả trả về từ phương thức getBookFromResultSet
            List<Book> booksFound = new ArrayList<>();
            booksFound.add(new Book("1", "JavaFX for Beginners", "John Doe", new byte[] {}));
            booksFound.add(new Book("2", "Advanced Java Programming", "Jane Smith", new byte[] {}));
            mockedStatic.when(() -> SearchBookDatabase.getBookFromResultSet(anyString(), anyString())).thenReturn(booksFound);

            dashboardController = new DashboardController(); // Khởi tạo controller mà không cần sửa mã

            // Gọi phương thức searchBooks
            dashboardController.search_tf = new TextField();
            dashboardController.typeSearch = SearchCriteria.TITLE;
            dashboardController.resultList_hb = new HBox();
            dashboardController.search_tf.setText("Java");
            dashboardController.searchBooks(20);

            // Kiểm tra phương thức static được gọi
            mockedStatic.verify(() -> SearchBookDatabase.getBookFromResultSet("Java", "TITLE"), times(1));
        }
    }

    @Test
    public void testResetPopular() {
        List<Book> booksPopular = new ArrayList<>();
        booksPopular.add(new Book("1", "The Great Gatsby", "F. Scott Fitzgerald", new byte[] {}));
        booksPopular.add(new Book("2", "1984", "George Orwell", new byte[] {}));

        try (MockedStatic<SuggestionBook> mockedStatic = Mockito.mockStatic(SuggestionBook.class)) {
            mockedStatic.when(SuggestionBook::getListRecommend).thenReturn(booksPopular);

            // Gọi phương thức resetPopular()
            dashboardController.scrollPane_popular = new ScrollPane();
            dashboardController.num_show_popular = new ChoiceBox<>();
            dashboardController.num_show_popular.setValue("All");
            dashboardController.resetPopular();

            WaitForAsyncUtils.waitForFxEvents();

            assertNotNull(dashboardController.bookPopular, "bookPopular should not be null");
            assertEquals(2, dashboardController.bookPopular.size(), "bookPopular size should match the mocked list size");
            assertEquals("The Great Gatsby", dashboardController.bookPopular.get(0).getTitle(), "First book title should match");
            assertEquals("1984", dashboardController.bookPopular.get(1).getTitle(), "Second book title should match");

            mockedStatic.verify(SuggestionBook::getListRecommend, times(1));
        }
    }
}
