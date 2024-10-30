package com.jmc.libsystem.ResetWindow;

import com.jmc.libsystem.Models.Model;
import javafx.scene.control.Label;

public class ResetDashboard {
    public static void resetLabel(Label label) {
        label.setText("Hi " + Model.getInstance().getMyUser().getFullName() + "!");
    }
}
