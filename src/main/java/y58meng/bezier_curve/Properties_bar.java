package y58meng.bezier_curve;

import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Separator;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;



public class Properties_bar extends VBox {
    ColorPicker cp;
    Slider slide;
    HBox style;

    Properties_bar (board_canvas b) {
        this.setSpacing(5);
        this.setWidth(250);

        // color pickers
        cp = new ColorPicker();
        cp.setValue(Color.BLACK);
        cp.setPrefHeight(50);
        cp.setPrefWidth(100);

        cp.setOnAction(ActionEvent -> {
            Color c = cp.getValue();
            b.color = c;
            if (b.selected != null) {
                b.selected.change_color(c);
            }
        });

        // line size controller
        HBox size = new HBox();

        slide = new Slider(1, 5, 1);
        slide.setShowTickLabels(true);
        slide.setShowTickMarks(true);
        slide.setMajorTickUnit(1);
        slide.setBlockIncrement(0.5);

        slide.setValue(b.width);
        slide.setPrefWidth(200);
        slide.setPrefHeight(50);

        slide.setOnMouseDragged(mouseEvent -> {
            double w = slide.getValue();
            b.width = w;
            if (b.selected != null) {
                b.selected.change_width(w);
            }
        });

        size.getChildren().add(slide);

        size.setSpacing(5);


        // line style controller
        style = new HBox();

        Button style1 = new layout.StandardButton("");
        Image style1_img = new Image("style1.png");
        ImageView style1_view = new ImageView(style1_img);
        style1_view.setFitHeight(40);
        style1_view.setPreserveRatio(true);
        style1.setGraphic(style1_view);

        style1.setOnMouseClicked(mouseEvent -> {
            b.style = 1;
            if (b.selected != null) {
                b.selected.change_style(1);
            }
        });

        Button style2 = new layout.StandardButton("");
        Image style2_img = new Image("style2.png");
        ImageView style2_view = new ImageView(style2_img);
        style2_view.setFitHeight(40);
        style2_view.setPreserveRatio(true);
        style2.setGraphic(style2_view);

        style2.setOnMouseClicked(mouseEvent -> {
            b.style = 2;
            if (b.selected != null) {
                b.selected.change_style(2);
            }
        });

        Button style3 = new layout.StandardButton("");
        Image style3_img = new Image("style3.png");
        ImageView style3_view = new ImageView(style3_img);
        style3_view.setFitWidth(40);
        style3_view.setPreserveRatio(true);
        style3.setGraphic(style3_view);

        style3.setOnMouseClicked(mouseEvent -> {
            b.style = 3;
            if (b.selected != null) {
                b.selected.change_style(3);
            }
        });

        Button style4 = new layout.StandardButton("");
        Image style4_img = new Image("style4.png");
        ImageView style4_view = new ImageView(style4_img);
        style4_view.setFitWidth(40);
        style4_view.setPreserveRatio(true);
        style4.setGraphic(style4_view);

        style4.setOnMouseClicked(mouseEvent -> {
            b.style = 4;
            if (b.selected != null) {
                b.selected.change_style(4);
            }
        });

        style.getChildren().add(style1);
        style.getChildren().add(style2);
        style.getChildren().add(style3);
        style.getChildren().add(style4);

        style.setSpacing(5);


        // add to VBOX
        Text color_text = new Text("Color:");
        color_text.setStyle("-fx-font: 15 arial;");
        this.getChildren().add(color_text);
        this.getChildren().add(cp);

        Separator sep1 = new Separator();
        sep1.setMaxWidth(this.getWidth() - 20);
        this.getChildren().add(sep1);

        Text size_text = new Text("Line Size:");
        size_text.setStyle("-fx-font: 15 arial;");
        this.getChildren().add(size_text);
        this.getChildren().add(size);

        Separator sep2 = new Separator();
        sep2.setMaxWidth(this.getWidth() - 20);
        this.getChildren().add(sep2);

        Text style_text = new Text("Line Style:");
        style_text.setStyle("-fx-font: 15 arial;");
        this.getChildren().add(style_text);
        this.getChildren().add(style);
    }

    void update_color (Color c) {
        cp.setValue(c);
    }

    void update_width (double width) {
        slide.setValue(width);
    }
}
