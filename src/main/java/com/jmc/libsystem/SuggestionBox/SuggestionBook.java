package com.jmc.libsystem.SuggestionBox;

import com.jmc.libsystem.Controllers.User.DashboardController;
import com.jmc.libsystem.QueryDatabase.QueryBookData;
import com.jmc.libsystem.Views.SearchCriteria;
import javafx.scene.control.TextField;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class SuggestionBook {
    public static AutoCompletionBinding<String> autoCompletionBinding;
    public static Set<String> titleSuggest = new HashSet<>();
    public static Set<String> categorySuggest = new HashSet<>();
    public static Set<String> authorSuggest = new HashSet<>();

    public static void initData() throws SQLException {
        ResultSet resultSet = QueryBookData.getDataForSuggest();
        while (resultSet.next()) {
            //title
            String title = resultSet.getString("title");
            titleSuggest.add(title);
            //author
            String[] authors = resultSet.getString("authors").split(", ");
            authorSuggest.addAll(Arrays.asList(authors));
            //
            String[] cats = resultSet.getString("category").split(", ");
            categorySuggest.addAll(Arrays.asList(cats));
        }
    }


    public static void autoCompletionLearnWord(TextField search_tf, String newWord) {
        if (autoCompletionBinding != null) {
            autoCompletionBinding.dispose();
        }
        Set<String> typeSuggest = new HashSet<>();
        if (DashboardController.typeSearch == SearchCriteria.TITLE) {
            typeSuggest = titleSuggest;
        } else if (DashboardController.typeSearch == SearchCriteria.CATEGORY) {
            typeSuggest = categorySuggest;
        } else {
            typeSuggest = authorSuggest;
        }
        if (!typeSuggest.contains(newWord)) {
            typeSuggest.add(newWord);
            autoCompletionBinding = TextFields.bindAutoCompletion(search_tf, typeSuggest);
            SuggestionBook.autoCompletionBinding.setPrefWidth(search_tf.getWidth() - 160);
        }
    }
}
