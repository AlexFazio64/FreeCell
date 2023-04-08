package yorha.freecell;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.stage.FileChooser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Objects;

public class GAME {
	public static boolean godmode;
	public static ArrayList<String> problem = new ArrayList<>();
	public static ArrayList<String> plan = new ArrayList<>();
	
	public static boolean checkColumn(String card, Card last) {
		if ( last == null || godmode ) {
			return true;
		}
		
		ArrayList<Card> cards = strToList(card);
		Card curr = cards.get(0);
		return curr.getValue() == ( last.getValue() - 1 ) && ( ( curr.isRed() && last.isBlack() ) || ( curr.isBlack() && last.isRed() ) );
	}
	
	public static ArrayList<Card> strToList(String str) {
		ArrayList<Card> cards = new ArrayList<>();
		str = str.substring(1, str.length() - 1);
		for (String s: str.split(", ")) {
			String[] card = s.split(" ");
			cards.add(new Card(card[0], card[1], null));
		}
		return cards;
	}
	
	public static boolean checkHome(String cards, HomeCell cell) {
		ArrayList<Card> cardList = strToList(cards);
		if ( cardList.size() > 1 ) {
			return false;
		}
		Card card = cardList.get(0);
		
		ObservableList<Node> children = cell.getChildren();
		if ( children.size() == 0 ) {
			return card.getValue() == 1 && Objects.equals(card.getSuit(), cell.suit);
		}
		
		Card last = (Card) children.get(children.size() - 1);
		return card.getValue() == ( last.getValue() + 1 ) && Objects.equals(card.getSuit(), cell.suit);
	}
	
	public static void loadProblem() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Select Problem File");
		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text " + "Files", "*.pddl", "*.txt"));
		fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")));
		File selectedFile = fileChooser.showOpenDialog(null);
		
		try {
			FileReader fileReader = new FileReader(selectedFile);
			BufferedReader br = new BufferedReader(fileReader);
			String line;
			while (( line = br.readLine() ) != null) {
				GAME.problem.add(line);
			}
			br.close();
		} catch (Exception ex) {
			System.out.println("Error reading file");
		}
	}
	
	public static void loadPlan() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Select Plan File");
		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text " + "Files", "*.txt"));
		fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")));
		File selectedFile = fileChooser.showOpenDialog(null);
		
		try {
			FileReader fileReader = new FileReader(selectedFile);
			BufferedReader br = new BufferedReader(fileReader);
			String line;
			while (( line = br.readLine() ) != null) {
				GAME.plan.add(line);
			}
			br.close();
		} catch (Exception ex) {
			System.out.println("Error reading file");
		}
	}
}
