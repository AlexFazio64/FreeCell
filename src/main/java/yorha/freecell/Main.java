package yorha.freecell;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Main extends Application {
	@Override
	public void start(Stage stage) throws IOException {
		
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Select File");
		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text " + "Files", "*.pddl", "*.txt"));
		fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")));
		File selectedFile = fileChooser.showOpenDialog(null);
		
		try {
			FileReader fileReader = new FileReader(selectedFile);
			BufferedReader br = new BufferedReader(fileReader);
			String line;
			while (( line = br.readLine() ) != null) {
				GAME.lines.add(line);
			}
			br.close();
		} catch (Exception ex) {
			System.out.println("Error reading file");
		}
		
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