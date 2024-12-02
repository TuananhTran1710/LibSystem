package com.jmc.libsystem.Controllers.User.Test;

import com.jmc.libsystem.Controllers.User.ProfileController;
import com.jmc.libsystem.Information.User;
import com.jmc.libsystem.Models.Model;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ProfileControllerTest {
    private ProfileController profileController;

    @BeforeEach
    void setUp() {
        profileController = new ProfileController();
    }

    @Test
    void testLoadUserProfileLogic() {
        User userMock = mock(User.class);
        when(userMock.getId()).thenReturn("23020017");
        when(userMock.getFullName()).thenReturn("Nguyen Phu C");
        when(userMock.getEmail()).thenReturn("ececec");
        when(userMock.getPassword()).thenReturn("1234");

        try (MockedStatic<Model> mockedModel = mockStatic(Model.class)) {
            mockedModel.when(Model::getInstance).thenReturn(mock(Model.class));
            when(Model.getInstance().getMyUser()).thenReturn(userMock);

            User res = profileController.loadCurrentUser();

            assertEquals("23020017", res.getId());
            assertEquals("Nguyen Phu C", res.getFullName());
            assertEquals("ececec", res.getEmail());
            assertEquals("1234", res.getPassword());
        }
    }
}