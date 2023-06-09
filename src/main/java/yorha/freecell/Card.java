package yorha.freecell;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.ArrayList;
import java.util.List;

public class Card extends Label {
	private final String value;
	private final String suit;
	public Column stack;
	
	public Card(String value, String suit, Column stack) {
		super();
		
		this.value = value;
		this.suit = suit;
		this.stack = stack;
		
		switch (value) {
			case "1" -> value = "A";
			case "11" -> value = "J";
			case "12" -> value = "Q";
			case "13" -> value = "K";
		}
		
		setText(value + " " + suit);
		setFont(Font.font(20));
		setAlignment(Pos.CENTER);
		setMinWidth(80);
		setPadding(new javafx.geometry.Insets(5));
		setTextFill(isRed() ? Color.RED : Color.BLACK);
		setStyle("-fx-background-color: white; -fx-border-color: black; " + "-fx-border-width: 1px; -fx-border-radius: 5px; -fx-background-radius: 5px;");
		
		setOnDragDetected(event -> {
			if ( this.getParent() instanceof HomeCell ) {
				return;
			}
			Dragboard db = startDragAndDrop(TransferMode.MOVE);
			ClipboardContent content = new ClipboardContent();
			List<Node> children;
			if ( stack != null ) {
				children = stack.getChildren().subList(stack.getChildren().indexOf(this), stack.getChildren().size());
			} else {
				children = new ArrayList<>();
				children.add(this);
			}
			content.putString(children.toString());
			db.setContent(content);
			event.consume();
		});
		
		setOnDragDone(event -> {
			if ( this.getParent() instanceof HomeCell ) {
				return;
			}
			if ( event.getTransferMode() == TransferMode.MOVE ) {
				Parent parent = getParent();
				if ( parent instanceof VBox ) {
					if ( stack != null ) {
						int last = stack.getChildren().indexOf(this);
						stack.getChildren().remove(last, stack.getChildren().size());
					} else {
						( (VBox) parent ).getChildren().remove(this);
					}
				}
			}
			event.consume();
		});
		
		setOnMouseClicked(event -> {
			if ( event.getClickCount() == 2 && GAME.godmode ) {
				Parent parent = getParent();
				if ( parent instanceof VBox ) {
					if ( stack != null ) {
						( (VBox) parent ).getChildren().remove(this);
					}
				}
			}
		});
		
	}
	
	public int getValue() {
		return switch (value) {
			case "A" -> 1;
			case "J" -> 11;
			case "Q" -> 12;
			case "K" -> 13;
			default -> Integer.parseInt(value);
		};
	}
	
	public String getSuit() {
		return suit;
	}
	
	@Override
	public String toString() {
		return switch (value) {
			case "1" -> "A";
			case "11" -> "J";
			case "12" -> "Q";
			case "13" -> "K";
			default -> value;
		} + " " + suit;
	}
	
	public boolean isRed() {
		return suit.equals("❤") || suit.equals("♦");
	}
	
	public boolean isBlack() {
		return suit.equals("♠") || suit.equals("♣");
	}
	
	@Override
	public boolean equals(Object obj) {
		return obj instanceof Card && obj.toString().equals(this.toString());
	}
}
