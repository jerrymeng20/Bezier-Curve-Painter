package y58meng.bezier_curve;

import javafx.scene.control.Separator;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class Tools_bar extends VBox {
    Properties_bar properties_tools;
    Paint_bar paint_tools;

    Tools_bar (board_canvas b) {
        super();
        properties_tools = new Properties_bar(b);
        paint_tools = new Paint_bar(b);

        this.setMinWidth(250);
        this.setWidth(250);
        this.setMaxWidth(250);

        Separator sep = new Separator();
        sep.setMaxWidth(paint_tools.getWidth() - 20);

        this.getChildren().add(paint_tools);
        this.getChildren().add(sep);
        this.getChildren().add(properties_tools);
        this.setSpacing(10);
        this.setTranslateX(10);
    }

    public void update_properties (Color c, double width) {
        properties_tools.update_color(c);
        properties_tools.update_width(width);
    }
}
