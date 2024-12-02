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

public class ProfileControllerTest {

    private ProfileController profileController;

    private TextField user_id_fld;
    private TextField fullname_fld;
    private TextField email_fld;
    private TextField password_fld;

    @BeforeEach
    void setUp() {
        // Khởi tạo controller
        profileController = new ProfileController();

        // Mock các TextField thay vì sử dụng chúng trực tiếp
        user_id_fld = mock(TextField.class);
        fullname_fld = mock(TextField.class);
        email_fld = mock(TextField.class);
        password_fld = mock(TextField.class);

        // Giả lập hành vi của các phương thức setText và setEditable để tránh NullPointerException
        doNothing().when(user_id_fld).setText(anyString());
        doNothing().when(fullname_fld).setText(anyString());
        doNothing().when(email_fld).setText(anyString());
        doNothing().when(password_fld).setText(anyString());
        doNothing().when(user_id_fld).setEditable(false);
        doNothing().when(fullname_fld).setEditable(false);
        doNothing().when(email_fld).setEditable(false);
        doNothing().when(password_fld).setEditable(false);

        // Gán các TextField mock vào controller
        profileController.user_id_fld = user_id_fld;
        profileController.fullname_fld = fullname_fld;
        profileController.email_fld = email_fld;
        profileController.password_fld = password_fld;
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

            // Gọi phương thức loadUserProfile() trực tiếp mà không cần gọi showProfile() hay refreshProfile()
            profileController.loadUserProfile();

            // Kiểm tra xem các giá trị trả về từ User có đúng không
            assertEquals("12345", userMock.getId());
            assertEquals("John Doe", userMock.getFullName());
            assertEquals("john.doe@example.com", userMock.getEmail());
            assertEquals("password123", userMock.getPassword());

            // Kiểm tra các trường TextField có nhận đúng giá trị không
            verify(user_id_fld).setText("12345");
            verify(fullname_fld).setText("John Doe");
            verify(email_fld).setText("john.doe@example.com");
            verify(password_fld).setText("password123");

            // Kiểm tra các trường không chỉnh sửa
            verify(user_id_fld).setEditable(false);
            verify(fullname_fld).setEditable(false);
            verify(email_fld).setEditable(false);
            verify(password_fld).setEditable(false);
        }
    }
}