package poker;

public class Player {
    private String name;
    private int numberOfCardsChange;
    private PokerHand pokerHand;
    private int playerNumber;

    public Player(String name, int playerNumber) {
        this.name = name;
        this.playerNumber = playerNumber;
        this.numberOfCardsChange = 3;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumberOfCardsChange() {
        return numberOfCardsChange;
    }

    public void setNumberOfCardsChange(int numberOfCardsChange) {
        this.numberOfCardsChange = numberOfCardsChange;
    }

    public PokerHand getPokerHand() {
        return pokerHand;
    }

    public void setPokerHand(PokerHand pokerHand) {
        this.pokerHand = pokerHand;
    }

}
