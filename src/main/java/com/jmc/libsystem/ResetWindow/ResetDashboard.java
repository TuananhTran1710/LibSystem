package com.jmc.libsystem.ResetWindow;

import com.jmc.libsystem.Controllers.User.DashboardController;
import com.jmc.libsystem.HandleResultSet.SearchBookDatabase;
import com.jmc.libsystem.Information.Book;
import com.jmc.libsystem.Models.Model;
import com.jmc.libsystem.QueryDatabase.QueryBookLoans;
import com.jmc.libsystem.QueryDatabase.RecommendationSystem;
import com.jmc.libsystem.Views.SearchCriteria;
import com.jmc.libsystem.Views.ShowListBookFound;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

import java.sql.ResultSet;
import java.util.List;

public class ResetDashboard {

    public static void resetDashboard(TextField searchField, ChoiceBox<SearchCriteria> criteriaBox,
                                      Label nameLabel, ChoiceBox<Integer> limitSearchBox,
                                      HBox resultListContainer) {
        searchField.clear();
        criteriaBox.setValue(SearchCriteria.TITLE);
        nameLabel.setText("Hi " + Model.getInstance().getMyUser().getFullName() + "!");
        limitSearchBox.setValue(20);
        resultListContainer.getChildren().clear();
    }

    public static void updatePopularBox(ChoiceBox<Integer> limitPopularBox, HBox containResult) {
        List<Book> book = RecommendationSystem.getListRecommend();
        DashboardController.setListBookPopular(book);
        if (limitPopularBox.getValue() != 20)
            limitPopularBox.setValue(20);
        else ShowListBookFound.show(book, containResult, 20);
    }

    public static void updateReadingBox(ChoiceBox<Integer> limitReadingBox, HBox containResult) {
        ResultSet resultSet = QueryBookLoans.getListBorrow(Model.getInstance().getMyUser().getId());
        List<Book> book = SearchBookDatabase.getBookFromResultSet(resultSet);
        DashboardController.setListBookReading(book);

        if (limitReadingBox.getValue() != 20)
            limitReadingBox.setValue(20); // sau khi set thi lap tuc nhay vao ham modifyReading o Dashboard
        else ShowListBookFound.show(book, containResult, 20);
    }

}
