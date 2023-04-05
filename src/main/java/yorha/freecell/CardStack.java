package yorha.freecell;

import javafx.geometry.Insets;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.VBox;

import java.util.ArrayList;

public class CardStack extends VBox {
	public CardStack() {
		super();
		setPadding(new Insets(5, 0, 0, 0));
		
		setOnDragOver(event -> {
			if ( event.getGestureSource() != this && event.getDragboard().hasString() && ( (Card) event.getGestureSource() ).stack != this ) {
				if ( GAME.checkMove(event.getDragboard().getString(), getLastOfStack()) ) {
					event.acceptTransferModes(TransferMode.MOVE);
				} else {
					System.out.println("no");
				}
			}
			event.consume();
		});
		setOnDragDropped(event -> {
			Dragboard db = event.getDragboard();
			boolean success = false;
			if ( db.hasString() ) {
				ArrayList<Card> cards = GAME.strToList(db.getString());
				for (Card c: cards) {
					this.getChildren().add(new Card(c.getValue() + "", c.getSuit(), this));
					System.out.println("added 1");
				}
				System.out.println(cards.size());
				success = true;
			}
			event.setDropCompleted(success);
			event.consume();
		});
	}
	
	public Card getLastOfStack() {
		return (Card) this.getChildren().get(getChildren().size() - 1);
	}
}
