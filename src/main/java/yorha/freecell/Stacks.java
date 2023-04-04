package yorha.freecell;

import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.VBox;

import java.util.ArrayList;

public class Stacks extends VBox {
	ArrayList<Card> cards;
	
	public Stacks() {
		super();
		cards = new ArrayList<>();
		
		setOnDragOver(event -> {
			if ( event.getGestureSource() != this && event.getDragboard().hasString() ) {
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
				String[] card = db.getString().split(" ");
				this.getChildren().add(new Card(card[0], card[1]));
				success = true;
			}
			event.setDropCompleted(success);
			event.consume();
		});
	}
}
