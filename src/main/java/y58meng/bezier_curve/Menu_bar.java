package y58meng.bezier_curve;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;

public class Menu_bar extends MenuBar {
    board_canvas b;

    Menu_bar (board_canvas _b) {
        super();
        b = _b;

        // file menu
        Menu File_Menu = new Menu("File");
        MenuItem file1 = new MenuItem("New");
        MenuItem file2 = new MenuItem("Save");
        MenuItem file3 = new MenuItem("Load");
        MenuItem file4 = new MenuItem("Quit");
        File_Menu.getItems().add(file1);
        File_Menu.getItems().add(file2);
        File_Menu.getItems().add(file3);
        File_Menu.getItems().add(file4);

        file1.setOnAction(mouseEvent -> {
            if (b.modified && b.curves.size() > 0) {
                new confirm_window(b, this, 1);
            }
            else {
                int loops = b.curves.size();
                for (int i = 0; i < loops; i++) {
                    b.curves.get(0).delete();
                }
            }
        });

        file2.setOnAction(mouseEvent -> {
            save_to_file();
            b.modified = false;
        });

        file3.setOnAction(mouseEvent -> {
            if (b.modified) {
                new confirm_window(b, this, 2);
            }
            else {
                read_curves(b);
            }
        });

        file4.setOnAction(mouseEvent -> {
            if (b.modified) {
                new confirm_window(b, this, 3);
            }
            else {
                System.exit(0);
            }
        });

        Menu Edit_Menu = new Menu("Help");
        MenuItem help1 = new MenuItem("About");
        Edit_Menu.getItems().add(help1);

        help1.setOnAction(mouseEvent -> show_about_window());

        this.getMenus().add(File_Menu);
        this.getMenus().add(Edit_Menu);

        this.setHeight(25);
    }

    public void save_to_file () {
        // save as file
        FileChooser fileChooser = new FileChooser();

        // set extension filter for bc files
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("BCurve files (.bc)", "*.bc");
        fileChooser.getExtensionFilters().add(filter);

        // show save file dialog
        final Stage primary_stage = new Stage();
        File file = fileChooser.showSaveDialog(primary_stage);

        if (file != null) {
            save_curves_to_file(file);
        }
    }

    private void save_curves_to_file (File f) {
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

    public void read_curves (board_canvas b) {
        // open the file
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Open bCurve Files");

        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("BCurve files (.bc)", "*.bc");
        chooser.getExtensionFilters().add(filter);

        final Stage primary_stage = new Stage();
        File file = chooser.showOpenDialog(primary_stage);

        if (file != null) {
            // first clean up current curves
            int loops = b.curves.size();
            for (int i = 0; i < loops; i++) {
                b.curves.get(0).delete();
            }
            b.modified = false;

            read_curve_file(file, b);
        }
    }

    private void read_curve_file (File file, board_canvas b) {
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(file));
            String line = reader.readLine();
            int curve_count = Integer.parseInt(line);

            int curr = -1;
            int read_count = 0;
            curve_group cg = null;
            curve bf = null;
            curve af = null;

            while (curr < curve_count) {
                line = reader.readLine();
                if (line == null) return;
                if (line.charAt(0) != ':') {
                    // read curve segment
                    String[] splited = line.split("\\s+");
                    double startX = Double.parseDouble(splited[1]);
                    double startY = Double.parseDouble(splited[2]);
                    boolean start_sharp = splited[3].equals("true");

                    double ctrl1X = Double.parseDouble(splited[4]);
                    double ctrl1Y = Double.parseDouble(splited[5]);

                    double ctrl2X = Double.parseDouble(splited[6]);
                    double ctrl2Y = Double.parseDouble(splited[7]);

                    double endX = Double.parseDouble(splited[8]);
                    double endY = Double.parseDouble(splited[9]);
                    boolean end_sharp = splited[10].equals("true");

                    Color c = Color.web(splited[11]);
                    double w = Double.parseDouble(splited[12]);
                    int style = Integer.parseInt(splited[13]);

                    if (read_count == 0) {
                        // fresh new curve
                        cg = new curve_group(b);
                        cg.color = c;
                        cg.width = w;
                        cg.style = style;
                        curve cv = new curve(startX, startY, start_sharp,
                                ctrl1X, ctrl1Y, ctrl2X, ctrl2Y,
                                endX, endY, end_sharp,
                                c, w, style, cg);
                        cg.start_curve = cv;
                        af = cv;
                    }
                    else {
                        // following segments
                        curve cv = new curve(startX, startY, start_sharp,
                                ctrl1X, ctrl1Y, ctrl2X, ctrl2Y,
                                endX, endY, end_sharp,
                                c, w, style, cg);
                        bf = af;
                        af = cv;
                        bf.after = af;
                        af.before = bf;
                    }
                    read_count++;
                } else {
                    // finish read current curve
                    if (cg != null) {
                        cg.hide_detail();
                        curr++;
                        read_count = 0;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void show_about_window () {
        Stage about_window = new Stage();

        VBox content = new VBox();
        content.setSpacing(20);

        // title
        VBox title = new VBox();
        title.setSpacing(5);
        title.setAlignment(Pos.CENTER);

        Text title_name = new Text("Bezier Curve Painter Ver. 1.0.1");
        title_name.setStyle("-fx-font: 25 arial;");

        Text copyRight = new Text("(c) 2021 Yanchen Meng (y58meng). Last revised: March 19, 2021.");

        title.getChildren().add(title_name);
        title.getChildren().add(copyRight);

        content.getChildren().add(title);

        // Description
        VBox Description = new VBox();
        Description.setSpacing(5);
        Description.setTranslateX(10);

        Text desc_title = new Text("Description:");
        desc_title.setStyle("-fx-font: 15 arial; -fx-font-weight: bold;");

        Text desc = new Text("The main area of painter is a canvas where users can draw Bezier curves.\n" +
                "Users can select a pen tool from the list on the side and then use their mouse to draw that a Bezier curve on the canvas.\n" +
                "Users can also choose a selection tool to edit the shape of a curve by changing its segment points (i.e., points on the curve) and control points.\n" +
                "Additionally, users can specify the colour, line thickness, and style of a selected curve and these properties of a new curve to draw.");

        Text more = new Text("For layout and features please read attached readme file.");

        Description.getChildren().add(desc_title);
        Description.getChildren().add(desc);
        Description.getChildren().add(more);

        content.getChildren().add(Description);

        // tutorial image
        VBox tutorial = new VBox();
        tutorial.setSpacing(5);
        tutorial.setTranslateX(10);

        Text tut_title = new Text("Image Tutorial:");
        tut_title.setStyle("-fx-font: 15 arial; -fx-font-weight: bold;");
        tutorial.getChildren().add(tut_title);

        Image tut = new Image("tutorial.png");
        ImageView tut_view = new ImageView(tut);
        tut_view.setFitWidth(750);
        tut_view.setPreserveRatio(true);
        tutorial.getChildren().add(tut_view);

        content.getChildren().add(tutorial);

        Scene s = new Scene(content, 800, 700);
        about_window.setTitle("About");
        about_window.setScene(s);
        about_window.show();
    }
}
