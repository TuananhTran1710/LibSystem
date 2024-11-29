package com.jmc.libsystem.Controllers.User.Test;

import com.jmc.libsystem.Controllers.User.DashboardController;
import com.jmc.libsystem.Information.Book;
import com.jmc.libsystem.Information.User;
import com.jmc.libsystem.QueryDatabase.QueryBookLoans;
import com.jmc.libsystem.HandleResultSet.SearchBookDatabase;
import com.jmc.libsystem.SuggestionBox.SuggestionBook;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.ArrayList;

public class DashboardControllerTest {

    private SearchBookDatabase searchBookDatabase;
    private QueryBookLoans queryBookLoans;
    private User currentUser;
    private DashboardController dashboardController;

    @BeforeEach
    public void setUp() {
        // Mocking inline (không sử dụng @Mock hay MockitoAnnotations)
        searchBookDatabase = mock(SearchBookDatabase.class);
        queryBookLoans = mock(QueryBookLoans.class);
        currentUser = new User("user123", "John Doe", "john.doe@example.com", "password123", "active");

        // Khởi tạo DashboardController mà không cần inject mocks
        dashboardController = new DashboardController();
    }

    @Test
    public void testSearchBooks() {
        // Tham số giới hạn số lượng sách
        int limit = 20;

        // Mã từ khóa tìm kiếm
        String searchTerm = "Java";

        // Giả lập kết quả trả về từ phương thức getBookFromResultSet
        List<Book> booksFound = new ArrayList<>();
        booksFound.add(new Book("1", "JavaFX for Beginners", "John Doe", new byte[] {}));
        booksFound.add(new Book("2", "Advanced Java Programming", "Jane Smith", new byte[] {}));

        // Mô phỏng hành vi gọi phương thức getBookFromResultSet
        when(searchBookDatabase.getBookFromResultSet(searchTerm, "TITLE")).thenReturn(booksFound);

        // Gọi phương thức searchBooks
        dashboardController.searchBooks(limit);

        // Kiểm tra xem phương thức getBookFromResultSet đã được gọi đúng
        verify(searchBookDatabase, times(1)).getBookFromResultSet(searchTerm, "TITLE");
    }

    @Test
    public void testResetReading() {
        // Giả lập các sách đang đọc
        List<Book> booksReading = new ArrayList<>();
        booksReading.add(new Book("1", "The Catcher in the Rye", "J.D. Salinger", new byte[] {}));
        booksReading.add(new Book("2", "Brave New World", "Aldous Huxley", new byte[] {}));

        // Mô phỏng phương thức lấy danh sách các sách đang đọc
        when(queryBookLoans.getLoansForUser(currentUser.getId())).thenReturn(booksReading);

        // Kiểm tra resetReading
        dashboardController.resetReading();

        assertNotNull(dashboardController.bookReading);
        assertEquals(2, dashboardController.bookReading.size());
    }

    @Test
    public void testResetPopular() {
        // Giả lập sách phổ biến
        List<Book> booksPopular = new ArrayList<>();
        booksPopular.add(new Book("1", "The Great Gatsby", "F. Scott Fitzgerald", new byte[] {}));
        booksPopular.add(new Book("2", "1984", "George Orwell", new byte[] {}));

        // Mô phỏng phương thức gợi ý sách
        when(SuggestionBook.getListRecommend()).thenReturn(booksPopular);

        // Kiểm tra resetPopular
        dashboardController.resetPopular();

        assertNotNull(dashboardController.bookPopular);
        assertEquals(2, dashboardController.bookPopular.size());
    }

    @Test
    public void testResetAll() {
        // Kiểm tra resetAll sẽ gọi tất cả các phương thức reset
        dashboardController.resetAll();

        verify(dashboardController, times(1)).reset();
        verify(dashboardController, times(1)).resetReading();
        verify(dashboardController, times(1)).resetPopular();
    }
}
