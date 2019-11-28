package poker;

import java.util.*;

public class PokerHand {

    private String stringPlayerHand;
    private List<Card> playerHand;
    private CombinationLogic combinationLogic;

    public PokerHand(List<Card> cards) {
        playerHand = new ArrayList<>();
        this.playerHand = cards;
        this.combinationLogic = new CombinationLogic();
    }

    public void addCard(Card card) {
        if(this.playerHand.size() <= 5) {
            this.playerHand.add(card);
        }
    }

    public void rankHand() {
        this.combinationLogic.rankHand(this.playerHand);
    }

    public String getStringPlayerHand() {
        return stringPlayerHand;
    }

    public void setStringPlayerHand(String stringPlayerHand) {
        this.stringPlayerHand = stringPlayerHand;
    }

    public List<Card> getPlayerHand() {
        return playerHand;
    }

    public void setPlayerHand(List<Card> playerHand) {
        this.playerHand = playerHand;
    }

    public CombinationLogic getCombinationLogic() {
        return combinationLogic;
    }

    public void setCombinationLogic(CombinationLogic combinationLogic) {
        this.combinationLogic = combinationLogic;
    }
}





