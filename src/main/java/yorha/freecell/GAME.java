package yorha.freecell;

import javafx.collections.ObservableList;
import javafx.scene.Node;

import java.util.ArrayList;
import java.util.Objects;

public class GAME {
	public static boolean godmode;
	public static ArrayList<String> lines = new ArrayList<>();
	
	public static boolean checkMove(String card, Card last) {
		if ( last == null || godmode ) {
			return true;
		}
		
		ArrayList<Card> cards = strToList(card);
		Card curr = cards.get(0);
		return curr.getValue() == ( last.getValue() - 1 ) && ( ( curr.isRed() && last.isBlack() ) || ( curr.isBlack() && last.isRed() ) );
	}
	
	public static ArrayList<Card> strToList(String str) {
		ArrayList<Card> cards = new ArrayList<>();
		str = str.substring(1, str.length() - 1);
		for (String s: str.split(", ")) {
			String[] card = s.split(" ");
			cards.add(new Card(card[0], card[1], null));
		}
		return cards;
	}
	
	public static boolean checkHome(String cards, HomeCell cell) {
		ArrayList<Card> cardList = strToList(cards);
		if ( cardList.size() > 1 ) {
			return false;
		}
		Card card = cardList.get(0);
		
		ObservableList<Node> children = cell.getChildren();
		if ( children.size() == 0 ) {
			return card.getValue() == 1 && Objects.equals(card.getSuit(), cell.suit);
		}
		
		Card last = (Card) children.get(children.size() - 1);
		return card.getValue() == ( last.getValue() + 1 ) && Objects.equals(card.getSuit(), cell.suit);
	}
}
