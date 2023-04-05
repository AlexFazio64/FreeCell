package yorha.freecell;

import javafx.geometry.Insets;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.VBox;

import java.util.ArrayList;

public class CardStack extends VBox {
	ArrayList<Card> cards;
	
	public CardStack() {
		super();
		cards = new ArrayList<>();
		
		setPadding(new Insets(5, 0, 0, 0));
		
		setOnDragOver(event -> {
			if ( event.getGestureSource() != this && event.getDragboard().hasString() && ( (Card) event.getGestureSource() ).stack != this ) {
				if ( GAME.checkMove(event.getDragboard().getString(), cards) ) {
					event.acceptTransferModes(TransferMode.MOVE);
				}
			}
			event.consume();
		});
		setOnDragDropped(event -> {
			Dragboard db = event.getDragboard();
			boolean success = false;
			if ( db.hasString() ) {
				String all = db.getString().replace("[", "");
				all = all.replace("]", "");
				for (String s: all.split(", ")) {
					String[] card = s.split(" ");
					this.getChildren().add(new Card(card[0], card[1], this));
				}
				success = true;
			}
			event.setDropCompleted(success);
			event.consume();
		});
	}
}
