package yorha.freecell;

import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class Card extends Label {
	private final String value;
	private final String suit;
	
	public Card(String value, String suit) {
		super();
		setFont(Font.font(20));
		
		// set font color
		setTextFill(isBlack() ? Color.BLACK : Color.RED);
		
		this.value = value;
		this.suit = suit;
		
		
		switch (value) {
			case "1" -> setText("A");
			case "11" -> setText("J");
			case "12" -> setText("Q");
			case "13" -> setText("K");
		}
		
		switch (suit) {
			case "S" -> setText(value + "♠");
			case "H" -> setText(value + "♥");
			case "C" -> setText(value + "♣");
			case "D" -> setText(value + "♦");
		}
		
		drag_drop_setup();
	}
	
	private void drag_drop_setup() {
		setOnDragDetected(event -> {
			Dragboard db = startDragAndDrop(TransferMode.MOVE);
			ClipboardContent content = new ClipboardContent();
			content.putString(toString());
			db.setContent(content);
			event.consume();
		});
		setOnDragDone(event -> {
			if ( event.getTransferMode() == TransferMode.MOVE ) {
				Parent parent = getParent();
				if ( parent instanceof VBox ) {
					( (VBox) parent ).getChildren().remove(this);
				}
			}
			event.consume();
		});
	}
	
	public int getValue() {
		return Integer.parseInt(value);
	}
	
	public String getSuit() {
		return suit;
	}
	
	@Override
	public String toString() {
		return value + " " + suit;
	}
	
	public boolean equals(Card other) {
		return this.getValue() == other.getValue() && suit.equals(other.getSuit());
	}
	
	public boolean isRed() {
		return suit.equals("Hearts") || suit.equals("Diamonds");
	}
	
	public boolean isBlack() {
		return suit.equals("Spades") || suit.equals("Clubs");
	}
}
