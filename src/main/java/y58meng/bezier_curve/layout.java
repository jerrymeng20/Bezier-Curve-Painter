package y58meng.bezier_curve;

import javafx.scene.control.Button;

public class layout {
    final static int BUTTON_MIN_WIDTH = 50;
    final static int BUTTON_PREF_WIDTH = 50;
    final static int BUTTON_MAX_WIDTH = 100;

    final static int BUTTON_MIN_HEIGHT = 50;
    final static int BUTTON_PREF_HEIGHT = 50;
    final static int BUTTON_MAX_HEIGHT = 100;

    public static class StandardButton extends Button {
        StandardButton(String name) {
            super(name); // call super class to construct a button with "name"

            this.setMinWidth(BUTTON_MIN_WIDTH);
            this.setPrefWidth(BUTTON_PREF_WIDTH);
            this.setMaxWidth(BUTTON_MAX_WIDTH);

            this.setMinHeight(BUTTON_MIN_HEIGHT);
            this.setPrefHeight(BUTTON_PREF_HEIGHT);
            this.setMaxHeight(BUTTON_MAX_HEIGHT);

            setVisible(true);
        }
    }
}
