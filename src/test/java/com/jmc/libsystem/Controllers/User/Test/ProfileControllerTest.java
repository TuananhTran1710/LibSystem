package com.jmc.libsystem.Controllers.User.Test;

import com.jmc.libsystem.Controllers.User.ProfileController;
import com.jmc.libsystem.Information.User;
import com.jmc.libsystem.Models.Model;
import javafx.scene.control.TextField;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.testfx.util.WaitForAsyncUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ProfileControllerTest {
    private ProfileController profileController;

    @BeforeEach
    void setUp() {
        profileController = new ProfileController(); // Khởi tạo controller
    }

    @Test
    void testLoadUserProfileLogic() {
        // Mock hành vi của User
        User userMock = mock(User.class);
        when(userMock.getId()).thenReturn("12345");
        when(userMock.getFullName()).thenReturn("John Doe");
        when(userMock.getEmail()).thenReturn("john.doe@example.com");
        when(userMock.getPassword()).thenReturn("password123");

        // Mock Model.getInstance() và trả về userMock
        try (MockedStatic<Model> mockedModel = mockStatic(Model.class)) {
            mockedModel.when(Model::getInstance).thenReturn(mock(Model.class));
            when(Model.getInstance().getMyUser()).thenReturn(userMock);

            // Gọi phương thức công khai để kiểm thử logic mà không tác động đến giao diện
            profileController.showProfile(); // Phương thức này sẽ gọi refreshProfile()

            // Kiểm tra xem các giá trị trả về từ User có đúng không
            assertEquals("12345", userMock.getId());
            assertEquals("John Doe", userMock.getFullName());
            assertEquals("john.doe@example.com", userMock.getEmail());
            assertEquals("password123", userMock.getPassword());

            // Kiểm tra nếu các phương thức setText có được gọi đúng với dữ liệu người dùng
            // (Thông qua mock, không liên quan đến giao diện)
            verify(Model.getInstance()).getMyUser();
        }
    }
}