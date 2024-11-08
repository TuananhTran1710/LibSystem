package com.jmc.libsystem.Controllers.User;

import com.jmc.libsystem.Views.SearchCriteria;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.ResourceBundle;

public class SearchController implements Initializable {

    public TextField search_tf;
    public ChoiceBox<SearchCriteria> criteriaBox;
    public Button search_btn;
    public HBox resultList_hb;
    public HBox popular_hbox;
    public ChoiceBox<Integer> num_show_search;
    public ChoiceBox<Integer> num_show_popular;
    public ImageView imageView;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Image image = new Image(SearchController.class.getResource("/Images/duyquatlam.jfif").toString(), true);
        imageView.setImage(image);
    }
}
