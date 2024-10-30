package com.jmc.libsystem.ResetWindow;

import com.jmc.libsystem.Models.Model;
import com.jmc.libsystem.Views.SearchCriteria;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

public class ResetDashboard {

    public static void resetDashboard(TextField searchField, ChoiceBox<SearchCriteria> criteriaBox,
                                      Label nameLabel, ChoiceBox<Integer> limitBox,
                                      HBox resultListContainer) {
        searchField.clear();
        criteriaBox.setValue(SearchCriteria.TITLE);
        nameLabel.setText("Hi " + Model.getInstance().getMyUser().getFullName() + "!");
        limitBox.setValue(20);
        resultListContainer.getChildren().clear();
    }

}
