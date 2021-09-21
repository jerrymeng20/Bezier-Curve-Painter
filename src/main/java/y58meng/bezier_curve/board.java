package y58meng.bezier_curve;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;


public class board extends Application {

    final int WIN_MAX_WIDTH = 1920;
    final int WIN_MAX_HEIGHT = 1080;
    final int WIN_MIN_WIDTH = 960;
    final int WIN_MIN_HEIGHT = 600;

    @Override
    public void start (Stage stage) {

        BorderPane root = new BorderPane();
        Scene s = new Scene(root, 1280, 760);

        stage.setMaxWidth(WIN_MAX_WIDTH);
        stage.setMaxHeight(WIN_MAX_HEIGHT);
        stage.setMinWidth(WIN_MIN_WIDTH);
        stage.setMinHeight(WIN_MIN_HEIGHT);

        stage.setTitle("Bezier Curve Painter (y58meng)");

        board_canvas board = new board_canvas(WIN_MAX_WIDTH - 250, WIN_MAX_HEIGHT - 25);
        Menu_bar menu = new Menu_bar(board);
        Tools_bar tools = new Tools_bar(board);
        board.set_tools_bar(tools);

        s.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ESCAPE) {
                board.change_mode(0);
            }
            else if (keyEvent.getCode() == KeyCode.DELETE) {
                if (board.mode == 1 && board.click_time == 1) {
                    board.draw_end();
                }

                if (board.selected != null) {
                    // delete current curve
                    board.selected.delete();
                    board.click_time = 0;
                    board.selected = null;
                }
            }
        });

        root.setTop(menu);
        root.setLeft(tools);
        root.setRight(board);

        stage.setScene(s);
        stage.show();
    }
}
