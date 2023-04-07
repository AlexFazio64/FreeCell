package yorha.freecell;

import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.VBox;

import java.util.Objects;

public class HomeCell extends VBox {
	final String suit;
	
	public HomeCell(String suit) {
		super();
		
		this.suit = suit;
		
		setOnDragOver(event -> {
			Dragboard db = event.getDragboard();
			if ( event.getGestureSource() != this && event.getDragboard().hasString() ) {
				if ( db.getString().lastIndexOf(",") == -1 ) {
					if ( Objects.equals(( (Card) event.getGestureSource() ).getSuit(), suit) || GAME.godmode ) {
						event.acceptTransferModes(TransferMode.MOVE);
					}
				}
			}
			event.consume();
		});
		setOnDragDropped(event -> {
			Dragboard db = event.getDragboard();
			boolean success = false;
			if ( db.hasString() && ( this.getChildren().size() == 0 || GAME.godmode ) ) {
				String[] card = db.getString().split(" ");
				card[0] = card[0].replace("[", "");
				card[1] = card[1].replace("]", "");
				this.getChildren().removeIf(a -> true);
				this.getChildren().add(new Card(card[0], card[1], null));
				success = true;
			}
			event.setDropCompleted(success);
			event.consume();
		});
	}
}