package com.jmc.libsystem.Models;

import com.jmc.libsystem.Views.ViewFactory;

public class Model {
    private static Model model; // phai la static de dung chung khi goi o cac file
    private final ViewFactory viewFactory;

    Model() {
        this.viewFactory = new ViewFactory();
    }

    public static synchronized Model getInstance() {
        if (model == null) {
            model = new Model();
        }
        return model;
    }

    public ViewFactory getViewFactory() {
        return viewFactory;
    }
}
