package y58meng.bezier_curve;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;

public class board_canvas extends Group {
    // store the curve groups
    ArrayList<curve_group> curves;

    // if the canvas has been modified
    Boolean modified = false;

    // background canvas
    Rectangle bg;

    // currently selected curve
    curve_group selected;

    // tools
    Tools_bar tools;

    // mode
    int mode = 0;
    int click_time = 0;

    // properties
    Color color = Color.BLACK;
    double width = 2.0;
    int style = 1;

    board_canvas (double w, double h) {
        super();

        // set up background
        bg = new Rectangle(w, h);
        bg.setFill(Color.rgb(224,224,224));
        this.getChildren().add(bg);

        curves = new ArrayList<>();

        bg.setOnMousePressed(mouseEvent -> {
            if (mode == 1) {
                // drawing mode
                click_time += 1;

                if (click_time == 1) {
                    // we click the first point
                    selected = new curve_group(this);
                    selected.draw_new_curve(mouseEvent.getX(), mouseEvent.getY());
                }
                else {
                    selected.draw_segment_point(mouseEvent.getX(), mouseEvent.getY());
                }
            }
            else {
                // doing nothing
                if (selected != null) {
                    selected.hide_detail();
                    selected = null;
                }
            }
        });

        bg.setOnMouseDragged(mouseEvent -> {
            if (mode == 1) {
                // update the control point (end) of selected curve, plus modify the temp point
                if (click_time == 1) {
                    selected.modify_ctrl_temp(mouseEvent.getX(), mouseEvent.getY());
                }
                else {
                    selected.modify_ctrl(mouseEvent.getX(), mouseEvent.getY());
                }
            }
        });
    }

    public void set_tools_bar (Tools_bar _tools) {
        tools = _tools;
    }

    public void change_mode (int new_mode) {
        if (mode == 1) {
            draw_end();
        }
        if (new_mode != 4) {
            drop_select();
        }

        mode = new_mode;
    }

    public void drop_select () {
        if (selected != null) {
            selected.hide_detail();
            selected = null;
        }
    }

    public void draw_end () {
        if (click_time == 1) {
            // only clicked once, start curve is null
            getChildren().remove(selected);
            curves.remove(selected);
            selected = null;
        }
        else {
            if (selected != null) {
                selected.clean_rest_temp();
            }
        }
        click_time = 0;
    }
}
