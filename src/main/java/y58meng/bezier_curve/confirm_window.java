package y58meng.bezier_curve;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

public class confirm_window extends Stage {

    confirm_window (board_canvas b, Menu_bar m, int type) {
        super();

        VBox sp = new VBox();
        Text warning = new Text("Changes have been made. Quiting now will cause you to lose all your progress. Continue?");
        sp.getChildren().add(warning);
        sp.setSpacing(10);
        sp.setPrefWidth(500);
        sp.setAlignment(Pos.CENTER);
        sp.setPadding(new Insets(10.0));

        HBox buttons = new HBox();
        buttons.setSpacing(50);
        buttons.setPrefWidth(500);
        buttons.setAlignment(Pos.CENTER);
        buttons.setPadding(new Insets(10.0));

        Button confirm = new Button();
        confirm.setText("Yes");
        confirm.setPrefWidth(100);

        confirm.setOnMouseClicked(mouserEvent -> {
            this.close();
            if (type == 1) {
                // new
                int loops = b.curves.size();
                for (int i = 0; i < loops; i++) {
                    b.curves.get(0).delete();
                }
                b.modified = false;
            }
            else if (type == 2) {
                // load
                m.read_curves(b);
            }
            else {
                // quit
                System.exit(0);
            }
        });

        Button not_confirm = new Button();
        not_confirm.setText("No");
        not_confirm.setPrefWidth(100);

        not_confirm.setOnMouseClicked(mouserEvent -> this.close());

        Button save = new Button();
        save.setText("Save");
        save.setPrefWidth(100);

        buttons.getChildren().add(save);
        buttons.getChildren().add(confirm);
        buttons.getChildren().add(not_confirm);

        save.setOnMouseClicked(mouserEvent -> {
            this.close();
            save_to_file(b);
        });

        sp.getChildren().add(buttons);

        Scene scene = new Scene (sp, 500, 100);
        this.setScene(scene);
        this.setTitle("Warning");
        this.show();

    }

    public void save_to_file (board_canvas b) {
        // save as file
        FileChooser fileChooser = new FileChooser();

        // set extension filter for bc files
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("BCurve files (.bc)", "*.bc");
        fileChooser.getExtensionFilters().add(filter);

        // show save file dialog
        final Stage primary_stage = new Stage();
        File file = fileChooser.showSaveDialog(primary_stage);

        if (file != null) {
            save_curves_to_file(file, b);
            b.modified = false;
        }
    }

    private void save_curves_to_file (File f, board_canvas b) {
        try {
            PrintWriter writer = new PrintWriter(f);

            // print curve contents
            int length = b.curves.size();
            writer.println(length);

            for (int i = 0; i < length; i++) {
                writer.println(":" + (i+1));
                curve_group cg = b.curves.get(i);
                curve c = cg.start_curve;
                while (c != null) {
                    // a curve file contains i blocks, where i = # of curve groups stored
                    // each curve group contains j lines, each line representing a curve segment in order
                    // each curve segment is formatted as below, separated by white space:
                    //   * (startX startY isSharp) (Ctrl1X Ctrl1Y) (Ctrl2X Ctrl2Y) (endX endY isSharp) color width style
                    writer.println("\t" + c.getStartX() + " " + c.getStartY() + " " + c.start_sharp + " " +c.getControlX1() + " " +c.getControlY1() + " " + c.getControlX2() + " " +c.getControlY2() + " " + c.getEndX() + " " + c.getEndY() + " " + c.end_sharp + " " + c.getStroke() + " " + c.getStrokeWidth() + " " + c.style);
                    c = c.after;
                }
            }
            writer.println(":");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
