package yorha.freecell;

import javafx.geometry.Pos;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

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
		for (String line: GAME.lines) {
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
						stack.getChildren().add(stack.getChildren().indexOf(other) + 1, card); // lol mancava il +1...
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
		for (String line: GAME.lines) {
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