package y58meng.bezier_curve;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

public class curve_group extends Group {
    board_canvas b;

    // store the curves
    curve start_curve = null;

    // curves setup
    Color color;
    double width;
    int style;

    // for first set up
    double init_x = 0;
    double init_y = 0;
    curve bf = null;
    curve af = null;

    // default control length
    double offset_x = 0;
    double offset_y = 5;

    // for temp use
    boolean temp = false;
    Rectangle temp_start;
    Rectangle temp_ctrl1;
    Rectangle temp_ctrl2;
    Line temp_l1;
    Line temp_l2;

    curve_group (board_canvas _b) {
        b = _b;
        b.getChildren().add(this);
        b.curves.add(this);
        color = b.color;
        width = b.width;
        style = b.style;
    }

    public void draw_new_curve (double posX, double posY) {
        // we click the first point
        init_x = posX;
        init_y = posY;

        // draw segment / control point
        temp_start = new Rectangle(10, 10);
        temp_start.setFill(Color.WHITE);
        temp_start.setStroke(Color.BLACK);
        temp_start.setX(init_x - 5);
        temp_start.setY(init_y - 5);
        this.getChildren().add(temp_start);

        temp_l1 = new Line(init_x, init_y, init_x + offset_x, init_y + offset_y);
        temp_l1.setStroke(Color.BLACK);
        temp_l1.setStrokeWidth(1);
        this.getChildren().add(temp_l1);

        temp_l2 = new Line(init_x, init_y, init_x + offset_x, init_y - offset_y);
        temp_l2.setStroke(Color.BLACK);
        temp_l2.setStrokeWidth(1);
        this.getChildren().add(temp_l2);

        temp_ctrl1 = new Rectangle(10, 10);
        temp_ctrl1.setFill(Color.WHITE);
        temp_ctrl1.setStroke(Color.BLACK);
        temp_ctrl1.setX(init_x + offset_x - 5);
        temp_ctrl1.setY(init_y + offset_y - 5);
        this.getChildren().add(temp_ctrl1);

        temp_ctrl2 = new Rectangle(10, 10);
        temp_ctrl2.setFill(Color.WHITE);
        temp_ctrl2.setStroke(Color.BLACK);
        temp_ctrl2.setX(init_x - offset_x - 5);
        temp_ctrl2.setY(init_y - offset_y - 5);
        this.getChildren().add(temp_ctrl2);

        temp_start.toFront();

        temp = true;
    }

    public void draw_segment_point (double posX, double posY) {
        if (temp) {
            this.getChildren().remove(temp_start);
            this.getChildren().remove(temp_l2);
            this.getChildren().remove(temp_ctrl2);
            temp = false;
        }

        bf = af;
        af = new curve(init_x, init_y, false,
                temp_ctrl1.getX() + 5, temp_ctrl1.getY() + 5,
                posX - offset_x, posY - offset_y,
                posX, posY, false,
                color, width, style,this);
        if (bf != null) {
            af.before = bf;
            bf.after = af;
        }
        init_x = posX;
        init_y = posY;
        temp_l1.setStartX(init_x);
        temp_l1.setEndX(init_x + offset_x);
        temp_l1.setStartY(init_y);
        temp_l1.setEndY(init_y + offset_y);
        temp_ctrl1.setX(init_x + offset_x - 5);
        temp_ctrl1.setY(init_y + offset_y - 5);
        if (start_curve == null) {
            start_curve = af;
        }
        set_modified();
    }

    public void modify_ctrl_temp (double posX, double posY) {
        temp_l1.setEndX(init_x + (posX - init_x));
        temp_l1.setEndY(init_y + (posY - init_y));
        temp_ctrl1.setX(temp_l1.getEndX() - 5);
        temp_ctrl1.setY(temp_l1.getEndY() - 5);
        temp_l2.setEndX(init_x - (posX - init_x));
        temp_l2.setEndY(init_y - (posY - init_y));
        temp_ctrl2.setX(temp_l2.getEndX() - 5);
        temp_ctrl2.setY(temp_l2.getEndY() - 5);
    }

    public void modify_ctrl (double posX, double posY) {
        if (af == null) return;
        af.update_ctrl2(posX, posY);
        modify_ctrl_temp(posX, posY);
        temp_l1.setEndX(init_x - (posX - init_x));
        temp_l1.setEndY(init_y - (posY - init_y));
        temp_ctrl1.setX(temp_l1.getEndX() - 5);
        temp_ctrl1.setY(temp_l1.getEndY() - 5);
    }

    public void set_modified () {
        b.modified = true;
    }

    public void set_select () {
        if (b.selected != null) {
            b.drop_select();
        }
        b.selected = this;

        // update other widgets
        b.tools.update_properties(color, width);
        b.color = color;
        b.width = width;
        b.style = style;
    }

    public boolean is_selected () {
        return b.selected == this;
    }

    public void hide_detail () {
        if (start_curve == null) return;
        start_curve.hide_detail();
    }

    public void delete () {
        if (start_curve == null) return;
        start_curve.delete();
        b.getChildren().remove(this);
        b.curves.remove(this);
        set_modified();
    }

    public void change_color (Color c) {
        color = c;
        if (start_curve == null) return;
        start_curve.change_color(c);
        set_modified();
    }

    public void change_width (double w) {
        width = w;
        if (start_curve == null) return;
        start_curve.change_width(w);
        set_modified();
    }

    public void change_style (int s) {
        style = s;
        if (start_curve == null) return;
        start_curve.change_style(s);
        set_modified();
    }

    public int get_mode () {
        return b.mode;
    }

    public void clean_rest_temp () {
        this.getChildren().remove(temp_l1);
        this.getChildren().remove(temp_ctrl1);
    }
}
