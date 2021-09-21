package y58meng.bezier_curve;

import javafx.scene.paint.Color;
import javafx.scene.shape.CubicCurve;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

import static java.lang.Math.*;

public class curve extends CubicCurve {
    curve_group g;
    curve before;
    curve after;
    Line l1;
    Line l2;
    Rectangle ctrl1;
    Rectangle ctrl2;
    Rectangle start;
    Rectangle end;
    int style;
    double start_x_dist;
    double start_y_dist;
    double start_length;
    double end_x_dist;
    double end_y_dist;
    double end_length;
    boolean deleted = false;
    boolean detail_shown = true;

    boolean start_sharp;
    boolean end_sharp;

    curve (double startX, double startY, boolean s_sharp, double ctrl1X, double ctrl1Y, double ctrl2X, double ctrl2Y, double endX, double endY, boolean e_sharp, Color color, double width, int style, curve_group _g) {
        g = _g;
        this.setStartX(startX);
        this.setEndX(endX);
        this.setStartY(startY);
        this.setEndY(endY);
        this.setStroke(color);
        this.setFill(Color.TRANSPARENT);
        this.setStrokeWidth(width);
        this.change_style(style);
        start_sharp = s_sharp;
        end_sharp = e_sharp;

        g.getChildren().add(this);
        this.toBack();

        // set up control points
        this.setControlX1(ctrl1X);
        this.setControlY1(ctrl1Y);
        this.setControlX2(ctrl2X);
        this.setControlY2(ctrl2Y);
        start_x_dist = getControlX1() - getStartX();
        start_y_dist = getControlY1() - getStartY();
        update_start_length();
        end_x_dist = getControlX2() - getEndX();
        end_y_dist = getControlY2() - getEndY();
        update_end_length();

        // set up lines connecting control points
        l1 = new Line(this.getStartX(), this.getStartY(), this.getControlX1(), this.getControlY1());
        l1.setStroke(Color.BLACK);
        l1.setStrokeWidth(1);
        g.getChildren().add(l1);

        l2 = new Line(this.getEndX(), this.getEndY(), this.getControlX2(), this.getControlY2());
        l2.setStroke(Color.BLACK);
        l2.setStrokeWidth(1);
        g.getChildren().add(l2);

        // use small rectangles to indicate control points
        ctrl1 = new Rectangle(10, 10);
        ctrl1.setFill(Color.WHITE);
        ctrl1.setStroke(Color.BLACK);
        ctrl1.setX(getControlX1() - 5);
        ctrl1.setY(getControlY1() - 5);
        g.getChildren().add(ctrl1);

        ctrl2 = new Rectangle(10, 10);
        ctrl2.setFill(Color.WHITE);
        ctrl2.setStroke(Color.BLACK);
        ctrl2.setX(getControlX2() - 5);
        ctrl2.setY(getControlY2() - 5);
        g.getChildren().add(ctrl2);

        // segment points
        start = new Rectangle(10, 10);
        start.setFill(Color.WHITE);
        start.setStroke(Color.BLACK);
        start.setX(getStartX() - 5);
        start.setY(getStartY() - 5);
        g.getChildren().add(start);

        // segment points
        end = new Rectangle(10, 10);
        end.setFill(Color.WHITE);
        end.setStroke(Color.BLACK);
        end.setX(getEndX() - 5);
        end.setY(getEndY() - 5);
        g.getChildren().add(end);

        // if sharp modify some settings
        if (s_sharp) {
            setControlX1(getStartX());
            setControlY1(getStartY());
            l1.setStroke(Color.TRANSPARENT);
            ctrl1.setFill(Color.TRANSPARENT);
            ctrl1.setStroke(Color.TRANSPARENT);
            start.setFill(Color.YELLOW);
        }

        if (e_sharp) {
            setControlX2(getEndX());
            setControlY2(getEndY());
            l2.setStroke(Color.TRANSPARENT);
            ctrl2.setFill(Color.TRANSPARENT);
            ctrl2.setStroke(Color.TRANSPARENT);
            end.setFill(Color.YELLOW);
        }

        ctrl1.toFront();
        ctrl2.toFront();
        start.toFront();
        end.toFront();

        // set control point event
        ctrl1.setOnMouseDragged(mouseEvent -> {
            if (!start_sharp && g.is_selected() && g.get_mode() == 3) {
                update_ctrl1(mouseEvent.getX(), mouseEvent.getY());
                g.set_modified();
            }
        });

        ctrl2.setOnMouseDragged(mouseEvent -> {
            if (!end_sharp && g.is_selected() && g.get_mode() == 3) {
                update_ctrl2(mouseEvent.getX(), mouseEvent.getY());
                g.set_modified();
            }
        });

        start.setOnMouseDragged(mouseEvent -> {
            if (g.is_selected() && g.get_mode() == 3) {
                double posX = handle_outside_X(mouseEvent.getX());
                double posY = handle_outside_Y(mouseEvent.getY());
                start.setX(posX - 5);
                start.setY(posY - 5);
                l1.setStartX(posX);
                l1.setStartY(posY);
                this.setStartX(posX);
                this.setStartY(posY);
                double controlX1 = getStartX() + start_x_dist;
                double controlY1 = getStartY() + start_y_dist;
                if (!start_sharp) {
                    setControlX1(controlX1);
                    setControlY1(controlY1);
                }
                ctrl1.setX(controlX1 - 5);
                ctrl1.setY(controlY1 - 5);
                l1.setEndX(controlX1);
                l1.setEndY(controlY1);

                if (this.before != null) {
                    before.end.setX(posX - 5);
                    before.end.setY(posY - 5);
                    before.l2.setStartX(posX);
                    before.l2.setStartY(posY);
                    before.setEndX(posX);
                    before.setEndY(posY);
                    double controlX2 = before.getEndX() + before.end_x_dist;
                    double controlY2 = before.getEndY() + before.end_y_dist;
                    if (!start_sharp) {
                        before.setControlX2(controlX2);
                        before.setControlY2(controlY2);
                    }
                    before.ctrl2.setX(controlX2 - 5);
                    before.ctrl2.setY(controlY2 - 5);
                    before.l2.setEndX(controlX2);
                    before.l2.setEndY(controlY2);
                }
                g.set_modified();
                mouseEvent.consume();
            }
        });

        end.setOnMouseDragged(mouseEvent -> {
            if (g.is_selected() && g.get_mode() == 3) {
                double posX = handle_outside_X(mouseEvent.getX());
                double posY = handle_outside_Y(mouseEvent.getY());
                end.setX(posX - 5);
                end.setY(posY - 5);
                l2.setStartX(posX);
                l2.setStartY(posY);
                this.setEndX(posX);
                this.setEndY(posY);
                double controlX2 = getEndX() + end_x_dist;
                double controlY2 = getEndY() + end_y_dist;
                if (!end_sharp) {
                    setControlX2(controlX2);
                    setControlY2(controlY2);
                }
                ctrl2.setX(controlX2 - 5);
                ctrl2.setY(controlY2 - 5);
                l2.setEndX(controlX2);
                l2.setEndY(controlY2);

                if (this.after != null) {
                    after.start.setX(posX - 5);
                    after.start.setY(posY - 5);
                    after.l1.setStartX(posX);
                    after.l1.setStartY(posY);
                    after.setStartX(posX);
                    after.setStartX(posY);
                    double controlX1 = after.getStartX() + after.start_x_dist;
                    double controlY1 = after.getStartY() + after.start_y_dist;
                    if (!start_sharp) {
                        after.setControlX1(controlX1);
                        after.setControlY1(controlY1);
                    }
                    after.ctrl1.setX(controlX1 - 5);
                    after.ctrl1.setY(controlY1 - 5);
                    after.l1.setEndX(controlX1);
                    after.l1.setEndY(controlY1);
                }
                g.set_modified();
                mouseEvent.consume();
            }
        });

        // set erase / select event
        this.setOnMouseClicked(mouseEvent -> {
            if (g.get_mode() == 2) {
                // erase mode
                this.delete();
                g.delete();
                g.set_modified();
            }
            else if (g.get_mode() == 3) {
                // select mode
                g.set_select();
                if (!this.detail_shown) {
                    this.show_detail();
                }
                g.set_modified();
            }
            mouseEvent.consume();
        });

        ctrl1.setOnMouseClicked(mouseEvent -> {
            if (g.get_mode() == 2) {
                // erase mode
                this.delete();
                g.delete();
                g.set_modified();
            }
            else if (g.get_mode() == 3) {
                // select mode
                g.set_select();
                if (!this.detail_shown) {
                    this.show_detail();
                }
                g.set_modified();
            }
        });

        ctrl2.setOnMouseClicked(mouseEvent -> {
            if (g.get_mode() == 2) {
                // erase mode
                this.delete();
                g.delete();
                g.set_modified();
            }
            else if (g.get_mode() == 3) {
                // select mode
                g.set_select();
                if (!this.detail_shown) {
                    this.show_detail();
                }
                g.set_modified();
            }
        });

        // sharp / smooth conversion
        start.setOnMouseClicked(mouseEvent -> {
            if (g.get_mode() == 2) {
                // erase mode
                this.delete();
                g.delete();
                g.set_modified();
            }
            else if (g.get_mode() == 3) {
                // select mode
                g.set_select();
                if (!this.detail_shown) {
                    this.show_detail();
                }
                g.set_modified();
            }
            else if (g.get_mode() == 4 && g.is_selected()) {
                if (start_sharp) {
                    // convert to smooth
                    start_sharp = false;
                    setControlX1(getStartX() + start_x_dist);
                    setControlY1(getStartY() + start_y_dist);
                    l1.setStroke(Color.BLACK);
                    ctrl1.setFill(Color.WHITE);
                    ctrl1.setStroke(Color.BLACK);
                    start.setFill(Color.WHITE);
                    if (before != null) {
                        before.end_sharp = false;
                        before.setControlX2(before.getEndX() + before.end_x_dist);
                        before.setControlY2(before.getEndY() + before.end_y_dist);
                        before.l2.setStroke(Color.BLACK);
                        before.ctrl2.setFill(Color.WHITE);
                        before.ctrl2.setStroke(Color.BLACK);
                        before.end.setFill(Color.WHITE);
                    }
                }
                else {
                    // convert to sharp
                    start_sharp = true;
                    setControlX1(getStartX());
                    setControlY1(getStartY());
                    l1.setStroke(Color.TRANSPARENT);
                    ctrl1.setFill(Color.TRANSPARENT);
                    ctrl1.setStroke(Color.TRANSPARENT);
                    start.setFill(Color.YELLOW);
                    if (before != null) {
                        before.end_sharp = true;
                        before.setControlX2(before.getEndX());
                        before.setControlY2(before.getEndY());
                        before.l2.setStroke(Color.TRANSPARENT);
                        before.ctrl2.setFill(Color.TRANSPARENT);
                        before.ctrl2.setStroke(Color.TRANSPARENT);
                        before.end.setFill(Color.YELLOW);
                    }
                }
                g.set_modified();
            }
        });

        end.setOnMouseClicked(mouseEvent -> {
            if (g.get_mode() == 2) {
                // erase mode
                this.delete();
                g.delete();
                g.set_modified();
            }
            else if (g.get_mode() == 3) {
                // select mode
                g.set_select();
                if (!this.detail_shown) {
                    this.show_detail();
                }
                g.set_modified();
            }
            else if (g.get_mode() == 4 && g.is_selected()) {
                if (end_sharp) {
                    // convert to smooth
                    end_sharp = false;
                    setControlX2(getEndX() + end_x_dist);
                    setControlY2(getEndY() + end_y_dist);
                    l2.setStroke(Color.BLACK);
                    ctrl2.setFill(Color.WHITE);
                    ctrl2.setStroke(Color.BLACK);
                    end.setFill(Color.WHITE);
                    if (after != null) {
                        after.start_sharp = false;
                        after.setControlX1(after.getStartX() + after.start_x_dist);
                        after.setControlY1(after.getStartY() + after.start_y_dist);
                        after.l1.setStroke(Color.BLACK);
                        after.ctrl1.setFill(Color.WHITE);
                        after.ctrl1.setStroke(Color.BLACK);
                        after.start.setFill(Color.WHITE);
                    }
                }
                else {
                    // convert to sharp
                    end_sharp = true;
                    setControlX2(getEndX());
                    setControlY2(getEndY());
                    l2.setStroke(Color.TRANSPARENT);
                    ctrl2.setFill(Color.TRANSPARENT);
                    ctrl2.setStroke(Color.TRANSPARENT);
                    end.setFill(Color.YELLOW);
                    if (after != null) {
                        after.start_sharp = true;
                        after.setControlX1(getStartX());
                        after.setControlY1(getStartY());
                        after.l1.setStroke(Color.TRANSPARENT);
                        after.ctrl1.setFill(Color.TRANSPARENT);
                        after.ctrl1.setStroke(Color.TRANSPARENT);
                        after.start.setFill(Color.YELLOW);
                    }
                }
                g.set_modified();
            }
        });
    }

    public void delete () {
        g.getChildren().remove(l1);
        g.getChildren().remove(l2);
        g.getChildren().remove(ctrl1);
        g.getChildren().remove(ctrl2);
        g.getChildren().remove(start);
        g.getChildren().remove(end);
        g.getChildren().remove(this);
        deleted = true;
        if (this.before != null && !this.before.deleted) {
            this.before.delete();
        }
        if (this.after != null && !this.after.deleted) {
            this.after.delete();
        }
    }

    public void hide_detail () {
        l1.setStroke(Color.TRANSPARENT);
        l2.setStroke(Color.TRANSPARENT);
        ctrl1.setFill(Color.TRANSPARENT);
        ctrl1.setStroke(Color.TRANSPARENT);
        ctrl2.setFill(Color.TRANSPARENT);
        ctrl2.setStroke(Color.TRANSPARENT);
        start.setFill(Color.TRANSPARENT);
        start.setStroke(Color.TRANSPARENT);
        end.setFill(Color.TRANSPARENT);
        end.setStroke(Color.TRANSPARENT);

        detail_shown = false;
        if (this.before != null && this.before.detail_shown) {
            this.before.hide_detail();
        }
        if (this.after != null && this.after.detail_shown) {
            this.after.hide_detail();
        }
    }

    public void show_detail () {
        if (!start_sharp) {
            l1.setStroke(Color.BLACK);
            ctrl1.setFill(Color.WHITE);
            ctrl1.setStroke(Color.BLACK);
            start.setFill(Color.WHITE);
            start.setFill(Color.WHITE);
        } else {
            start.setFill(Color.YELLOW);
        }
        if (!end_sharp) {
            l2.setStroke(Color.BLACK);
            ctrl2.setFill(Color.WHITE);
            ctrl2.setStroke(Color.BLACK);
            end.setFill(Color.WHITE);
        } else {
            end.setFill(Color.YELLOW);
        }
        start.setStroke(Color.BLACK);
        end.setStroke(Color.BLACK);
        detail_shown = true;
        if (this.before != null && !this.before.detail_shown) {
            this.before.show_detail();
        }
        if (this.after != null && !this.after.detail_shown) {
            this.after.show_detail();
        }
    }

    // ** max x **
    // get the max x of all curve segments before current curve
    private double get_before_max_x () {
        if (before != null) {
            return max(this.getStartX(), before.get_before_max_x());
        }
        else {
            return this.getStartX();
        }
    }

    // get the max x of all curve segments after current curve
    private double get_after_max_x () {
        if (after != null) {
            return max(this.getEndX(), after.get_after_max_x());
        }
        else {
            return this.getEndX();
        }
    }

    // get max x of current curve
    public double get_max_x () {
        return max(this.get_before_max_x(), this.get_after_max_x());
    }

    // ** min x **
    // get the min x of all curve segments before current curve
    private double get_before_min_x () {
        if (before != null) {
            return min(this.getStartX(), before.get_before_min_x());
        }
        else {
            return this.getStartX();
        }
    }

    // get the min x of all curve segments after current curve
    private double get_after_min_x () {
        if (after != null) {
            return min(this.getEndX(), after.get_after_min_x());
        }
        else {
            return this.getEndX();
        }
    }

    // get min x of current curve
    public double get_min_x () {
        return min(this.get_before_min_x(), this.get_after_min_x());
    }

    // ** max y **
    // get the max y of all curve segments before current curve
    private double get_before_max_y () {
        if (before != null) {
            return max(this.getStartY(), before.get_before_max_y());
        }
        else {
            return this.getStartY();
        }
    }

    // get the max y of all curve segments after current curve
    private double get_after_max_y () {
        if (after != null) {
            return max(this.getEndY(), after.get_after_max_y());
        }
        else {
            return this.getEndY();
        }
    }

    // get max y of current curve
    public double get_max_y () {
        return max(this.get_before_max_y(), this.get_after_max_y());
    }

    // ** min y **
    // get the min y of all curve segments before current curve
    private double get_before_min_y () {
        if (before != null) {
            return min(this.getStartY(), before.get_before_min_y());
        }
        else {
            return this.getStartY();
        }
    }

    // get the min y of all curve segments after current curve
    private double get_after_min_y () {
        if (after != null) {
            return min(this.getEndY(), after.get_after_min_y());
        }
        else {
            return this.getEndY();
        }
    }

    // get min y of current curve
    public double get_min_y () {
        return min(this.get_before_min_y(), this.get_after_min_y());
    }

    public void change_color (Color c) {
        this.setStroke(c);
        if (after != null) {
            after.change_color(c);
        }
    }

    public void change_width (double w) {
        this.setStrokeWidth(w);
        if (after != null) {
            after.change_width(w);
        }
    }

    public void change_style (int style) {
        switch (style) {
            case 1:
                this.setStyle("-fx-stroke-dash-array: 0 1;");
                break;
            case 2:
                this.setStyle("-fx-stroke-dash-array: 20 10;");
                break;
            case 3:
                this.setStyle("-fx-stroke-dash-array: 5;");
                break;
            case 4:
                this.setStyle("-fx-stroke-dash-array: 25 15 5 15;");
                break;
            default:
                break;
        }
        this.style = style;
        if (after != null) {
            after.change_style(style);
        }
    }

    private void update_start_length () {
        start_length = sqrt(start_x_dist * start_x_dist + start_y_dist * start_y_dist);
    }

    private void update_end_length() {
        end_length = sqrt(end_x_dist * end_x_dist + end_y_dist * end_y_dist);
    }

    public void update_ctrl1 (double posX, double posY) {
        posX = handle_outside_X(posX);
        posY = handle_outside_Y(posY);
        ctrl1.setX(posX - 5);
        ctrl1.setY(posY - 5);
        l1.setEndX(posX);
        l1.setEndY(posY);
        this.setControlX1(posX);
        this.setControlY1(posY);
        start_x_dist = getControlX1() - this.getStartX();
        start_y_dist = getControlY1() - this.getStartY();
        update_start_length();
        if (this.before != null && start_length >= 1) {
            before.ctrl2.setX(getStartX() - (start_x_dist * before.end_length / start_length) - 5);
            before.ctrl2.setY(getStartY() - (start_y_dist * before.end_length / start_length) - 5);
            before.l2.setEndX(before.ctrl2.getX() + 5);
            before.l2.setEndY(before.ctrl2.getY() + 5);
            before.setControlX2(before.ctrl2.getX() + 5);
            before.setControlY2(before.ctrl2.getY() + 5);
            before.end_x_dist = before.getControlX2() - before.getEndX();
            before.end_y_dist = before.getControlY2() - before.getEndY();
        }
    }

    public void update_ctrl2 (double posX, double posY) {
        posX = handle_outside_X(posX);
        posY = handle_outside_Y(posY);
        ctrl2.setX(posX - 5);
        ctrl2.setY(posY - 5);
        l2.setEndX(posX);
        l2.setEndY(posY);
        this.setControlX2(posX);
        this.setControlY2(posY);
        end_x_dist = getControlX2() - this.getEndX();
        end_y_dist = getControlY2() - this.getEndY();
        update_end_length();
        if (this.after != null && end_length >= 1) {
            after.ctrl1.setX(getEndX() - (end_x_dist * after.start_length / end_length) - 5);
            after.ctrl1.setY(getEndY() - (end_y_dist * after.start_length / end_length) - 5);
            after.l1.setEndX(after.ctrl1.getX() + 5);
            after.l1.setEndY(after.ctrl1.getY() + 5);
            after.setControlX1(after.ctrl1.getX() + 5);
            after.setControlY1(after.ctrl1.getY() + 5);
            after.start_x_dist = after.getControlX1() - after.getStartX();
            after.start_y_dist = after.getControlY1() - after.getStartY();
        }
    }

    private double handle_outside_X (double posX) {
        if (posX < 0) return 0;
        if (posX > 1920) return 1920;
        return posX;
    }

    private double handle_outside_Y (double posY) {
        if (posY < 0) return 0;
        if (posY > 1080) return 1080;
        return posY;
    }
}
