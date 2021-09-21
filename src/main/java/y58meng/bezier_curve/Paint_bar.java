package y58meng.bezier_curve;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

public class Paint_bar extends GridPane {
    Paint_bar (board_canvas b) {
        super();

        Text t = new Text("Tools:");
        t.setStyle("-fx-font: 15 arial;");

        Button pen = new layout.StandardButton("");
        Image pen_image = new Image("pen.png");
        ImageView pen_view = new ImageView(pen_image);
        pen_view.setFitHeight(30);
        pen_view.setPreserveRatio(true);
        pen.setGraphic(pen_view);

        pen.setOnMouseClicked(mouseEvent -> b.change_mode(1));

        Button selection = new layout.StandardButton("");
        Image select_image = new Image("select.png");
        ImageView select_view = new ImageView(select_image);
        select_view.setFitHeight(30);
        select_view.setPreserveRatio(true);
        selection.setGraphic(select_view);

        selection.setOnMouseClicked(mouseEvent -> b.change_mode(3));

        Button point = new layout.StandardButton("");
        Image point_image = new Image("point.png");
        ImageView point_view = new ImageView(point_image);
        point_view.setFitHeight(30);
        point_view.setPreserveRatio(true);
        point.setGraphic(point_view);

        point.setOnMouseClicked(mouseEvent -> b.change_mode(4));

        Button erase = new layout.StandardButton("");
        Image erase_image = new Image("erase.png");
        ImageView erase_view = new ImageView(erase_image);
        erase_view.setFitHeight(30);
        erase_view.setPreserveRatio(true);
        erase.setGraphic(erase_view);

        erase.setOnMouseClicked(mouseEvent -> b.change_mode(2));

        this.add(t, 0, 0);
        this.add(pen, 0, 1);
        this.add(selection, 1, 1);
        this.add(point, 0, 2);
        this.add(erase, 1, 2);

        this.setPadding(new Insets(10.0));
        this.setHgap(10);
        this.setVgap(10);

        this.setWidth(250);
    }
}
