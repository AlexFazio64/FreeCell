package yorha.freecell;

import javafx.collections.ListChangeListener;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.VBox;

import java.util.ArrayList;

public class Column extends VBox {
	public Column() {
		super();
		setPadding(new Insets(5, 0, 0, 0));
		
		setOnDragOver(event -> {
			if ( event.getGestureSource() != this && event.getDragboard().hasString() && ( (Card) event.getGestureSource() ).stack != this ) {
				if ( GAME.checkColumn(event.getDragboard().getString(), getLastOfStack()) || GAME.godmode ) {
					event.acceptTransferModes(TransferMode.MOVE);
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
				}
				success = true;
			}
			event.setDropCompleted(success);
			event.consume();
		});
		
		getChildren().addListener((ListChangeListener<? super Node>) styler -> {
			// set default style for all cards
			for (Node n: getChildren()) {
				Card c = (Card) n;
				c.setStyle("-fx-background-color: white; -fx-border-color: black; -fx-border-width: 1px; -fx-border-radius: 5px; -fx-background-radius: 5px;");
			}
			
			// set style for last card
			if ( getChildren().size() > 0 ) {
				Card c = (Card) getChildren().get(getChildren().size() - 1);
				c.setStyle("-fx-background-color: lightyellow; -fx-border-color: gold; -fx-border-width: 2px; -fx-border-radius: 5px; -fx-background-radius: 5px;");
			}
		});
	}
	
	public Card getLastOfStack() {
		return ( getChildren().size() == 0 ) ? null : (Card) this.getChildren().get(getChildren().size() - 1);
	}
}
