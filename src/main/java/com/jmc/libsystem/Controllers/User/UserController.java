package com.jmc.libsystem.Controllers.User;

import com.jmc.libsystem.Models.Model;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class UserController implements Initializable {
    public BorderPane user_parent;


    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        user_parent.setCenter(Model.getInstance().getViewFactory().getDashboardView());
        user_parent.setLeft(Model.getInstance().getViewFactory().getUserMenu());
        handleChangeMenu();
    }

    private void handleChangeMenu() {
        Model.getInstance().getViewFactory().getUserSelectedMenuItem()
                .addListener((observableValue, oldVal, newVal) -> {
                    switch (newVal) {
                        case LOGOUT -> {
                            Model.getInstance().getViewFactory().showLoginWindow();
                            Stage stage = (Stage) user_parent.getScene().getWindow();
                            Model.getInstance().getViewFactory().closeStage(stage);
                        }
                        case BOOKLOANPREVIEW ->
                                user_parent.setCenter(Model.getInstance().getViewFactory().getBookLoanPreview());
                        case BOOKPREVIEW ->
                                user_parent.setCenter(Model.getInstance().getViewFactory().getBookPreview());
                        case SEARCH -> user_parent.setCenter(Model.getInstance().getViewFactory().getSearchView());
                        case PROFILE -> user_parent.setCenter(Model.getInstance().getViewFactory().getProfileView());
                        case PROPOSE -> user_parent.setCenter(Model.getInstance().getViewFactory().getProposeView());
                        case MYBOOK -> user_parent.setCenter(Model.getInstance().getViewFactory().getMyBookView());
                        default -> user_parent.setCenter(Model.getInstance().getViewFactory().getDashboardView());

                    }
                });
    }
}
