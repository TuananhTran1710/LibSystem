package com.jmc.libsystem.SuggestionBox;

import com.jmc.libsystem.Views.SearchCriteria;
import javafx.scene.control.TextField;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;

import java.util.HashSet;
import java.util.Set;

public class SuggestionBookAPI {
    public static AutoCompletionBinding<String> autoCompletionBinding;
    public static Set<String> titleSuggest = new HashSet<>();
    public static Set<String> categorySuggest = new HashSet<>();
    public static Set<String> authorSuggest = new HashSet<>();

    public static void autoCompletionLearnWord(TextField search_tf, String newWord, SearchCriteria type) {
        Set<String> typeSuggest;
        if (type == SearchCriteria.TITLE) {
            typeSuggest = titleSuggest;
        } else if (type == SearchCriteria.CATEGORY) {
            typeSuggest = categorySuggest;
        } else {
            typeSuggest = authorSuggest;
        }
        if (!typeSuggest.contains(newWord)) {
            if (autoCompletionBinding != null) {
                autoCompletionBinding.dispose();
            }
            typeSuggest.add(newWord);
            autoCompletionBinding = TextFields.bindAutoCompletion(search_tf, typeSuggest);
            SuggestionBook.autoCompletionBinding.setPrefWidth(search_tf.getWidth() - 160);
        }
    }
}
