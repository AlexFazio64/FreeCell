package yorha.freecell;

import javafx.geometry.Pos;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

import java.util.ArrayList;

public class Controller {
	public BorderPane container;
	public HBox top;
	public Pane draft1;
	public Pane draft2;
	public Pane draft3;
	public Pane draft4;
	public Pane aces1;
	public Pane aces2;
	public Pane aces3;
	public Pane aces4;
	public HBox main;
	
	ArrayList<Stacks> stacks = new ArrayList<>();
	ArrayList<Card> cards = new ArrayList<>();
	
	public void initialize() {
		// create cards
		for (int i = 0; i < 52; i++) {
			Card card = new Card(i % 13 + 1 + "", new String[]{"S", "H", "C", "D"}[i / 13]);
			cards.add(card);
		}
		
		// create stacks
		for (int i = 0; i < 8; i++) {
			Stacks stack = new Stacks();
			stack.setStyle("-fx-border-color: black;");
			stack.setAlignment(Pos.TOP_CENTER);
			stack.setPrefWidth(200);
			main.getChildren().add(stack);
			stacks.add(stack);
		}
		
		// shuffle cards in stacks
		cards.forEach(card -> {
			int index = (int) ( Math.random() * stacks.size() );
			stacks.get(index).getChildren().add(card);
		});
	}
}