package com.jmc.libsystem.Controllers.User;

import com.jmc.libsystem.Views.SearchCriteria;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.ResourceBundle;

public class ProposeController implements Initializable {

    public TextField search_tf;
    public ChoiceBox<SearchCriteria> criteriaBox;
    public Button search_btn;
    public ChoiceBox num_show_search;
    public ScrollPane scrollPane_search;
    public HBox resultSearchList_hb;
    public ScrollPane scrollPane_suggest;
    public HBox resultSuggestList_hb;

    private static ProposeController instance;

    public static ProposeController getInstance() {
        return instance;
    }

    public ProposeController() {
        instance = this;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
