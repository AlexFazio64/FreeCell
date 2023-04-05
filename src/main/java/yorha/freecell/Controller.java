package yorha.freecell;

import javafx.geometry.Pos;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

import java.util.ArrayList;

public class Controller {
	public BorderPane container;
	public HBox main;
	public HBox drafts;
	public HBox piles;
	
	ArrayList<CardStack> stacks = new ArrayList<>();
	final String[] suits = new String[]{"❤", "♦", "♣", "♠"};
	final String[] ranks = new String[]{"A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"};
	
	public void initialize() {
		// create drafts
		for (int i = 0; i < 4; i++) {
			DraftPile stack = new DraftPile();
			stack.setStyle("-fx-border-color: black;");
			stack.setAlignment(Pos.TOP_CENTER);
			stack.setPrefWidth(200);
			drafts.getChildren().add(stack);
		}
		
		// create piles
		for (int i = 0; i < 4; i++) {
			SuitPile pile = new SuitPile(suits[i]);
			pile.setStyle("-fx-border-color: black;");
			pile.setAlignment(Pos.TOP_CENTER);
			pile.setPrefWidth(200);
			piles.getChildren().add(pile);
		}
		
		// create stacks
		for (int i = 0; i < 8; i++) {
			CardStack stack = new CardStack();
			stack.setStyle("-fx-border-color: black;");
			stack.setAlignment(Pos.TOP_CENTER);
			stack.setPrefWidth(200);
			main.getChildren().add(stack);
			stacks.add(stack);
		}
		
		// create cards
		ArrayList<Card> cards = new ArrayList<>();
		for (String suit: suits) {
			for (String rank: ranks) {
				cards.add(new Card(rank, suit, null));
			}
		}
		
		// assign cards to stacks
		for (int i = 0; i < 52; i++) {
			int index = (int) ( Math.random() * cards.size() );
			Card card = cards.get(index);
			stacks.get(i % 8).getChildren().add(new Card(card.getValue() + "", card.getSuit(), stacks.get(i % 8)));
			cards.remove(index);
		}
	}
}