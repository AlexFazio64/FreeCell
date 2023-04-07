package yorha.freecell;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
	
	@Override
	public void start(Stage stage) throws IOException {
		FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("board.fxml"));
		Scene scene = new Scene(fxmlLoader.load());
		stage.setTitle("FreeCell");
		stage.setScene(scene);
		stage.setMinWidth(900);
		stage.setMaxWidth(900);
		stage.setMinHeight(600);
		stage.show();
	}
	
	public static void main(String[] args) {
		for (String arg: args) {
			if ( arg.equals("godmode") ) {
				GAME.godmode = true;
				break;
			}
		}
		launch();
	}
}