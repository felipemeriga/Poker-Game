package poker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck {

    private List<Card> deck;
    private List<String> suits;
    private List<String> figures;

    private final int DECK_SIZE = 52;
    private final int HAND_SIZE = 5;

    public Deck() {
        this.suits = new ArrayList<>();
        this.deck = new ArrayList<>();
        this.figures = new ArrayList<>();
        this.fillSuitsList();
        this.fillFiguresList();
    }

    public void fillDeck() {
        this.deck.clear();
        for(int i = 2; i <= 9; i++) {
            for(int j = 0 ; j <= 3; j++) {
                String cardString = i + this.suits.get(j);
                Card actualCard = new Card(cardString);
                this.deck.add(actualCard);
            }
        }
        for(int i = 0; i <= 3; i++) {
            for(int j = 0 ; j <= 3; j++) {
                String cardString = this.figures.get(i) + this.suits.get(j);
                Card actualCard = new Card(cardString);
                this.deck.add(actualCard);
            }
        }
    }

    private void fillSuitsList() {
        this.suits.clear();
        this.suits.add("D");
        this.suits.add("C");
        this.suits.add("H");
        this.suits.add("S");
    }

    private void fillFiguresList() {
        this.figures.add("A");
        this.figures.add("J");
        this.figures.add("Q");
        this.figures.add("K");
    }

    public void shuffleDeck() {
        Collections.shuffle(this.deck);
    }

    public List<Card> getHand() {
        List<Card> hand = new ArrayList<>();
        for(int i = 0; i <= 4; i++) {
            hand.add(this.deck.get(i));
            this.deck.remove(i);
        }
        return hand;
    }

    public Card getCard() {
        Card card = this.deck.get(0);
        this.deck.remove(0);
        return card;
    }


}
