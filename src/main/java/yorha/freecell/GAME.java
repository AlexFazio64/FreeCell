package yorha.freecell;

import java.util.ArrayList;

public class GAME {
	public static boolean checkMove(String card, Card last) {
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
}
