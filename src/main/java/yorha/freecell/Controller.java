package yorha.freecell;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Controller {
	public BorderPane container;
	public HBox main;
	public HBox free;
	public HBox home;
	
	public void initialize() {
		final String[] suits = new String[]{"❤", "♦", "♣", "♠"};
		final String[] ranks = new String[]{"A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"};
		ArrayList<Column> columns = new ArrayList<>();
		
		freeCellsInit();
		homeCellsInit(suits);
		columnsInit(columns);
		
		parseBottomCols(columns);
		parseOnCols(columns);
		
		// if all columns are empty, print a message
		if ( columns.stream().allMatch(stack -> stack.getChildren().isEmpty()) ) {
			defaultConfig(suits, ranks, columns);
		}
		
		setupExecuteButton();
	}
	
	private void setupExecuteButton() {
		if ( GAME.plan.size() > 0 ) {
			Button nextMove = new Button("Execute");
			Label label = new Label(GAME.plan.get(0));
			HBox bottom = new HBox(nextMove, label);
			
			nextMove.setOnAction(event -> {
				if ( GAME.plan.size() > 0 ) {
					String s = GAME.plan.get(0);
					GAME.plan.remove(0);
					parsePlan(s);
					label.setText(GAME.plan.size() == 0 ? "No more moves" : GAME.plan.get(0));
				}
			});
			
			HBox.setHgrow(nextMove, Priority.NEVER);
			HBox.setHgrow(label, Priority.ALWAYS);
			BorderPane.setMargin(bottom, new Insets(10));
			
			label.setTextFill(Color.WHITE);
			label.setAlignment(Pos.CENTER_LEFT);
			label.setFont(new Font("Arial", 20));
			
			bottom.setAlignment(Pos.CENTER_LEFT);
			bottom.spacingProperty().setValue(10);
			bottom.setMinHeight(50);
			
			container.setBottom(bottom);
		}
	}
	
	private void parsePlan(String action) {
		System.out.println(action);
		
		Pattern pattern = Pattern.compile("\\((\\S+)");
		Matcher matcher = pattern.matcher(action);
		matcher.find();
		String command = matcher.group(1);
		
		action = action.toUpperCase();
		pattern = Pattern.compile("\\s+(\\S+)");
		matcher = pattern.matcher(action);
		
		Card target = extractToken(matcher);
		Card destination = extractToken(matcher);
		
		switch (command) {
			case "move-from-column-to-bottomcol", "move-from-bottomcol-to-column" -> {
				target = findCardInColumn(target);
				findColumnWithCard(destination).addCard(target);
			}
			case "move-from-column-to-column" -> {
				destination = extractToken(matcher);
				target = findCardInColumn(target);
				findColumnWithCard(destination).addCard(target);
			}
			
			case "move-from-column-to-home-top", "move-from-bottom-to-home" -> {
				target = findCardInColumn(target);
				findHomeCell(target).addCard(target);
			}
			case "move-from-column-to-free-cell" -> {
				target = findCardInColumn(target);
				findEmptyFreeCell().addCard(target);
			}
			case "move-from-free-cell-to-home" -> {
				target = findCardInFreeCell(target);
				findHomeCell(target).addCard(target);
				clearFreeCell(target);
			}
			case "move-from-free-cell-to-column" -> {
				target = findCardInFreeCell(target);
				findColumnWithCard(destination).addCard(target);
				clearFreeCell(target);
			}
		}
	}
	
	private Card extractToken(Matcher matcher) {
		matcher.find();
		String tok = matcher.group(1);
		tok = tok.replace(")", "");
		tok = tok.substring(1) + toUnicode(tok.substring(0, 1));
		return new Card(tok.substring(0, tok.length() - 1), tok.substring(tok.length() - 1), null);
	}
	
	private void clearFreeCell(Card card) {
		for (Node free: free.getChildren()) {
			FreeCell freeCell = (FreeCell) free;
			if ( freeCell.getChildren().contains(card) ) {
				freeCell.getChildren().clear();
			}
		}
	}
	
	private Column findColumnWithCard(Card destination) {
		for (Node columns: main.getChildren()) {
			Column column = (Column) columns;
			if ( column.getChildren().contains(destination) ) {
				return column;
			}
		}
		System.out.println("Column not found");
		return null;
	}
	
	private Card findCardInColumn(Card target) {
		for (Node columns: main.getChildren()) {
			Column column = (Column) columns;
			if ( column.getChildren().contains(target) ) {
				return (Card) column.getChildren().get(column.getChildren().indexOf(target));
			}
		}
		System.out.println("Card not found");
		return null;
	}
	
	private HomeCell findHomeCell(Card target) {
		for (Node home: home.getChildren()) {
			HomeCell homeCell = (HomeCell) home;
			if ( homeCell.suit.equals(target.getSuit()) ) {
				return homeCell;
			}
		}
		System.out.println("Home cell not found");
		return null;
	}
	
	private FreeCell findEmptyFreeCell() {
		for (Node free: free.getChildren()) {
			FreeCell freeCell = (FreeCell) free;
			if ( freeCell.getChildren().isEmpty() ) {
				return freeCell;
			}
		}
		System.out.println("Free cell not found");
		return null;
	}
	
	private Card findCardInFreeCell(Card target) {
		for (Node free: free.getChildren()) {
			FreeCell freeCell = (FreeCell) free;
			if ( freeCell.getChildren().contains(target) ) {
				return (Card) freeCell.getChildren().get(freeCell.getChildren().indexOf(target));
			}
		}
		System.out.println("Card not found");
		return null;
	}
	
	private void columnsInit(ArrayList<Column> columns) {
		// create columns
		for (int i = 0; i < 8; i++) {
			Column stack = new Column();
			stack.setStyle("-fx-border-color: black;");
			stack.setAlignment(Pos.TOP_CENTER);
			stack.setPrefWidth(200);
			main.getChildren().add(stack);
			columns.add(stack);
		}
	}
	
	private void homeCellsInit(String[] suits) {
		// create home cells
		for (int i = 0; i < 4; i++) {
			HomeCell pile = new HomeCell(suits[i]);
			pile.setStyle("-fx-border-color: black;");
			pile.setAlignment(Pos.TOP_CENTER);
			pile.setPrefWidth(200);
			home.getChildren().add(pile);
		}
	}
	
	private void freeCellsInit() {
		// create free cells
		for (int i = 0; i < 4; i++) {
			FreeCell stack = new FreeCell();
			stack.setStyle("-fx-border-color: black;");
			stack.setAlignment(Pos.TOP_CENTER);
			stack.setPrefWidth(200);
			free.getChildren().add(stack);
		}
	}
	
	private static void defaultConfig(String[] suits, String[] ranks, ArrayList<Column> columns) {
//		create cards
		ArrayList<Card> cards = new ArrayList<>();
		for (String suit: suits) {
			for (String rank: ranks) {
				cards.add(new Card(rank, suit, null));
			}
		}

//		 assign cards to columns
		for (int i = 0; i < 52; i++) {
			int index = (int) ( Math.random() * cards.size() );
			Card card = cards.get(index);
			columns.get(i % 8).getChildren().add(new Card(card.getValue() + "", card.getSuit(), columns.get(i % 8)));
			cards.remove(index);
		}
	}
	
	private void parseOnCols(ArrayList<Column> stacks) {
		ArrayList<String> lines = new ArrayList<>();
		for (String line: GAME.problem) {
			if ( line.contains("ON") ) {
				lines.add(line);
			}
		}
		
		while (lines.size() != 0) {
			String line = lines.get(0);
			Pattern pattern = Pattern.compile("ON ([DHCS])([A2-9JQK]|10) ([DHCS])([A2-9JQK]|10)");
			Matcher matcher = pattern.matcher(line);
			
			if ( matcher.find() ) {
				String suit = toUnicode(matcher.group(1));
				String rank = matcher.group(2);
				Card card = new Card(rank, suit, null);
				
				// find the other card on the line
				suit = toUnicode(matcher.group(3));
				rank = matcher.group(4);
				Card other = new Card(rank, suit, null);
				
				boolean added = false;
				for (Column stack: stacks) {
					if ( stack.getChildren().contains(other) ) {
						stack.getChildren().add(stack.getChildren().indexOf(other) + 1, new Card(card.getValue() + "", card.getSuit(), stack));
						lines.remove(0);
						added = true;
					}
				}
				
				if ( !added ) {
					lines.add(line);
					lines.remove(0);
				}
			}
		}
	}
	
	private static String toUnicode(String match) {
		return switch (match) {
			case "D" -> "♦";
			case "H" -> "❤";
			case "C" -> "♣";
			case "S" -> "♠";
			default -> match;
		};
	}
	
	private void parseBottomCols(ArrayList<Column> stacks) {
		for (String line: GAME.problem) {
			if ( line.contains("BOTTOMCOL") ) {
				Pattern pattern = Pattern.compile("BOTTOMCOL ([DHCS])([A2-9JQK]|10)\\)");
				Matcher matcher = pattern.matcher(line);
				if ( matcher.find() ) {
					String suit = toUnicode(matcher.group(1));
					String rank = matcher.group(2);
					Card card = new Card(rank, suit, null);
					
					for (Column stack: stacks) {
						if ( stack.getChildren().size() == 0 ) {
							stack.getChildren().add(new Card(card.getValue() + "", card.getSuit(), stack));
							break;
						}
					}
				}
			}
		}
	}
}