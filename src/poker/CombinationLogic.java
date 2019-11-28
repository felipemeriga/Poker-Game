package poker;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CombinationLogic {
    private Combination playerResult;
    private Map<String, String> straightMap;
    private boolean isFlush = false;
    private boolean isStraight = false;
    private boolean isFullHouse = false;
    private boolean isFourOfAKind = false;
    private boolean isThreeOfAKind = false;
    private boolean isTwoPair = false;
    private boolean isOnePair = false;
    private int firstPairRank = 0;
    private int secondPairRank = 0;
    private int threeOfAKindRank = 0;
    private int fourOfAKindRank = 0;

    public CombinationLogic() {
    }

    private void createStraightMap() {
        this.straightMap = new HashMap<String, String>();
        this.straightMap.put("A", "2");
        this.straightMap.put("2", "3");
        this.straightMap.put("3", "4");
        this.straightMap.put("4", "5");
        this.straightMap.put("5", "6");
        this.straightMap.put("6", "7");
        this.straightMap.put("7", "8");
        this.straightMap.put("8", "9");
        this.straightMap.put("9", "T");
        this.straightMap.put("T", "J");
        this.straightMap.put("J", "Q");
        this.straightMap.put("Q", "K");
        this.straightMap.put("K", "A");
    }

    private void checkStraight(List<Card> playerHand) {
        createStraightMap();
        Boolean straightFlag = false;
        int index = 0;

        for (int z = 0; z < playerHand.size(); z++) {
            index = z + 1;
            while (index < 5) {
                if (straightMap.get(playerHand.get(z).stringRank).equals(playerHand.get(index).stringRank)) {
                    straightFlag = true;
                    break;
                } else {
                    straightFlag = false;
                    break;
                }
            }
            if (!straightFlag) {
                break;
            }
        }
        if (straightFlag) {
            this.isStraight = true;
        }
    }

    private void checkFlush(List<Card> playerHand) {
        Boolean flushFlag = false;
        int clubsCount = 0;
        int diamondsCount = 0;
        int heartsCount = 0;
        int spadesCount = 0;
        for (int z = 0; z < playerHand.size(); z++) {
            switch (playerHand.get(z).getSuit()) {
                case "S":
                    spadesCount++;
                    break;
                case "H":
                    heartsCount++;
                    break;
                case "C":
                    clubsCount++;
                    break;
                case "D":
                    diamondsCount++;
                    break;
            }
        }
        if (clubsCount >= 4 || heartsCount >= 4 || diamondsCount >= 4 || spadesCount >= 4) {
            this.isFlush = true;
        }
    }

    private void checkNormalCombinations(List<Card> playerHand) {
        int matchesRank = 0;
        int foundCombinationRank = 0;
        int matchesSuit = 0;
        int index;

        for (int i = 0; i < playerHand.size(); i++) {
            index = i + 1;

            while (index < 5) {
                if (this.isThreeOfAKind) {
                    if (playerHand.get(i - 1).rank.equals(playerHand.get(i).rank)) {
                        break;
                    }
                }
                if (playerHand.get(i).rank.equals(playerHand.get(index).rank)) {
                    matchesRank++;
                    foundCombinationRank = playerHand.get(i).rank.value;
                }
                index++;
            }
            //FOUR-OF-A-KIND
            if (matchesRank == 3) {
                this.isFourOfAKind = true;
                this.fourOfAKindRank = foundCombinationRank;
                break;
            } else if (matchesRank == 2) {
                this.isThreeOfAKind = true;
                this.threeOfAKindRank = foundCombinationRank;
            } else if (matchesRank == 1) {
                if (this.isOnePair == true) {
                    this.isOnePair = false;
                    this.isTwoPair = true;
                    this.secondPairRank = foundCombinationRank;
                } else {
                    this.isOnePair = true;
                    this.firstPairRank = foundCombinationRank;
                }
            }
            foundCombinationRank = 0;
            matchesRank = 0;
            matchesSuit = 0;
        }
    }

    public void rankHand(List<Card> playerHand) {
        this.checkStraight(playerHand);
        checkFlush(playerHand);
        checkNormalCombinations(playerHand);
        rankHandResult();
    }

    private void rankHandResult() {

        if (this.isStraight && this.isFlush) {
            this.playerResult = Combination.StraightFlush;
        } else if (this.isFourOfAKind) {
            this.playerResult = Combination.FourOfAKind;
        } else if (this.isThreeOfAKind && this.isOnePair) {
            this.playerResult = Combination.FullHouse;
        } else if (this.isFlush) {
            this.playerResult = Combination.Flush;
        } else if (this.isStraight) {
            this.playerResult = Combination.Straight;
        } else if (this.isThreeOfAKind) {
            this.playerResult = Combination.ThreeOfAKind;
        } else if (this.isTwoPair) {
            this.playerResult = Combination.TwoPair;
        } else if (this.isOnePair) {
            this.playerResult = Combination.OnePair;
        } else {
            this.playerResult = Combination.Highest;
        }
    }

    public Result compareWith(List<Card> playerHand, PokerHand opponentHand) {
        Result playerResult;
        Result opponentResult;

        if (this.playerResult.value > opponentHand.getCombinationLogic().playerResult.value) {
            playerResult = Result.WIN;
            opponentResult = Result.LOSS;
        } else if (this.playerResult.value < opponentHand.getCombinationLogic().playerResult.value) {
            playerResult = Result.LOSS;
            opponentResult = Result.WIN;
        } else {
            playerResult = drawHandle(playerHand, opponentHand);
        }

        if (playerResult.equals(Result.LOSS)) {
            return Result.LOSS;
        } else if (playerResult.equals(Result.WIN)) {
            return Result.WIN;
        }

        return Result.TIE;
    }

    private Result drawHandle(List<Card> playerHand, PokerHand opponentHand) {
        Result result = Result.TIE;
        int a = 2;

        if (this.playerResult.equals(Combination.StraightFlush)) { //Straight Flush Draw
            if (playerHand.get(4).rank.value > opponentHand.getPlayerHand().get(4).rank.value) {
                result = Result.WIN;
            } else if (playerHand.get(4).rank.value < opponentHand.getPlayerHand().get(4).rank.value) {
                result = Result.LOSS;
            } else {
                result = Result.TIE;
            }
        } else if (this.playerResult.equals(Combination.FourOfAKind)) { //Four Of a Kind Draw
            if (this.fourOfAKindRank > opponentHand.getCombinationLogic().fourOfAKindRank) {
                result = Result.WIN;
            } else if (this.fourOfAKindRank < opponentHand.getCombinationLogic().fourOfAKindRank) {
                result = Result.LOSS;
            } else {
                for(int k = 0; k < playerHand.size(); k++){
                    if(playerHand.get(k).rank.value == this.fourOfAKindRank){
                        playerHand.remove(k);
                        k--;
                    }
                }
                for(int l = 0; l < opponentHand.getPlayerHand().size(); l++){
                    if(opponentHand.getPlayerHand().get(l).rank.value == opponentHand.getCombinationLogic().fourOfAKindRank  ){
                        opponentHand.getPlayerHand().remove(l);
                        l--;
                    }
                }
                if(playerHand.get(0).rank.value > opponentHand.getPlayerHand().get(0).rank.value){
                    result = Result.WIN;
                } else if(playerHand.get(0).rank.value < opponentHand.getPlayerHand().get(0).rank.value){
                    result = Result.LOSS;
                } else{
                    result = Result.TIE;
                }
            }
        } else if (this.playerResult.equals(Combination.FullHouse)) { //FullHouse Draw
            if (this.threeOfAKindRank > opponentHand.getCombinationLogic().threeOfAKindRank) {
                result = Result.WIN;
            } else if (this.threeOfAKindRank < opponentHand.getCombinationLogic().threeOfAKindRank) {
                result = Result.LOSS;
            } else {
                for(int k = 0; k < playerHand.size(); k++){
                    if(playerHand.get(k).rank.value == this.threeOfAKindRank){
                        playerHand.remove(k);
                        k--;
                    }
                }
                for(int l = 0; l < opponentHand.getPlayerHand().size(); l++){
                    if(opponentHand.getPlayerHand().get(l).rank.value == opponentHand.getCombinationLogic().threeOfAKindRank  ){
                        opponentHand.getPlayerHand().remove(l);
                        l--;
                    }
                }
                if(playerHand.get(1).rank.value + playerHand.get(0).rank.value > opponentHand.getPlayerHand().get(1).rank.value + opponentHand.getPlayerHand().get(0).rank.value){
                    result = Result.WIN;
                } else if(playerHand.get(1).rank.value + playerHand.get(0).rank.value <  opponentHand.getPlayerHand().get(1).rank.value + opponentHand.getPlayerHand().get(0).rank.value){
                    result = Result.LOSS;
                } else{
                    result = Result.TIE;
                }
            }
        } else if (this.playerResult.equals(Combination.Flush)) { //Flush Draw
            if (playerHand.get(4).rank.value > opponentHand.getPlayerHand().get(4).rank.value) {
                result = Result.WIN;
            } else if (playerHand.get(4).rank.value < opponentHand.getPlayerHand().get(4).rank.value) {
                result = Result.LOSS;
            } else {
                if (playerHand.get(4).rank.value + playerHand.get(3).rank.value + playerHand.get(2).rank.value + playerHand.get(1).rank.value + playerHand.get(0).rank.value > opponentHand.getPlayerHand().get(4).rank.value + opponentHand.getPlayerHand().get(3).rank.value +opponentHand.getPlayerHand().get(2).rank.value + opponentHand.getPlayerHand().get(1).rank.value + opponentHand.getPlayerHand().get(0).rank.value) {
                    result = Result.WIN;
                } else if (playerHand.get(4).rank.value + playerHand.get(3).rank.value + playerHand.get(2).rank.value + playerHand.get(1).rank.value + playerHand.get(0).rank.value < opponentHand.getPlayerHand().get(4).rank.value + opponentHand.getPlayerHand().get(3).rank.value  +  opponentHand.getPlayerHand().get(2).rank.value + opponentHand.getPlayerHand().get(1).rank.value + opponentHand.getPlayerHand().get(0).rank.value) {
                    result = Result.LOSS;
                } else {
                    result = Result.TIE;
                }
            }
        } else if (this.playerResult.equals(Combination.Straight)) { //Straight Draw
            if (playerHand.get(4).rank.value > opponentHand.getPlayerHand().get(4).rank.value) {
                result = Result.WIN;
            } else if (playerHand.get(4).rank.value < opponentHand.getPlayerHand().get(4).rank.value) {
                result = Result.LOSS;
            } else {
                result = Result.TIE;
            }
        } else if (this.playerResult.equals(Combination.ThreeOfAKind)) { //Three Of a Kind Draw
            if (this.threeOfAKindRank > opponentHand.getCombinationLogic().threeOfAKindRank) {
                result = Result.WIN;
            } else if (this.threeOfAKindRank < opponentHand.getCombinationLogic().threeOfAKindRank) {
                result = Result.LOSS;
            } else {
                for(int k = 0; k < playerHand.size(); k++){
                    if(playerHand.get(k).rank.value == this.threeOfAKindRank){
                        playerHand.remove(k);
                        k--;
                    }
                }
                for(int l = 0; l < opponentHand.getPlayerHand().size(); l++){
                    if(opponentHand.getPlayerHand().get(l).rank.value == opponentHand.getCombinationLogic().threeOfAKindRank  ){
                        opponentHand.getPlayerHand().remove(l);
                        l--;
                    }
                }
                if(playerHand.get(1).rank.value + playerHand.get(0).rank.value > opponentHand.getPlayerHand().get(1).rank.value + opponentHand.getPlayerHand().get(0).rank.value){
                    result = Result.WIN;
                } else if(playerHand.get(1).rank.value + playerHand.get(0).rank.value <  opponentHand.getPlayerHand().get(1).rank.value + opponentHand.getPlayerHand().get(0).rank.value){
                    result = Result.LOSS;
                } else{
                    result = Result.TIE;
                }
            }
        } else if (this.playerResult.equals(Combination.TwoPair)) { //Two Pair Draw
            if (this.secondPairRank > opponentHand.getCombinationLogic().secondPairRank) {
                result = Result.WIN;
            } else if (this.secondPairRank < opponentHand.getCombinationLogic().secondPairRank) {
                result = Result.LOSS;
            } else {
                if (this.firstPairRank > opponentHand.getCombinationLogic().firstPairRank) {
                    result = Result.WIN;
                } else if (this.firstPairRank < opponentHand.getCombinationLogic().firstPairRank) {
                    result = Result.LOSS;
                } else {
                    for(int k = 0; k < playerHand.size(); k++){
                        if(playerHand.get(k).rank.value == this.firstPairRank || playerHand.get(k).rank.value == this.secondPairRank ){
                            playerHand.remove(k);
                            k--;
                        }
                    }
                    for(int l = 0; l < opponentHand.getPlayerHand().size(); l++){
                        if(opponentHand.getPlayerHand().get(l).rank.value == opponentHand.getCombinationLogic().firstPairRank || opponentHand.getPlayerHand().get(l).rank.value == opponentHand.getCombinationLogic().secondPairRank ){
                            opponentHand.getPlayerHand().remove(l);
                            l--;
                        }
                    }
                    if(playerHand.get(0).rank.value > opponentHand.getPlayerHand().get(0).rank.value){
                        result = Result.WIN;
                    } else if(playerHand.get(0).rank.value < opponentHand.getPlayerHand().get(0).rank.value){
                        result = Result.LOSS;
                    } else{
                        result = Result.TIE;
                    }
                }
            }
        } else if (this.playerResult.equals(Combination.OnePair)) { //One Pair Draw
            if (this.firstPairRank > opponentHand.getCombinationLogic().firstPairRank) {
                result = Result.WIN;
            } else if (this.firstPairRank < opponentHand.getCombinationLogic().firstPairRank) {
                result = Result.LOSS;
            } else {
                for (int z = 0; z < playerHand.size(); z++) {
                    if (playerHand.get(z).rank.value == this.firstPairRank) {
                        playerHand.remove(z);

                        z--;
                    }
                }
                for (int y = 0; y < opponentHand.getPlayerHand().size(); y++) {
                    if (opponentHand.getPlayerHand().get(y).rank.value == this.firstPairRank) {
                        opponentHand.getPlayerHand().remove(y);
                        y--;
                    }
                }
                if (playerHand.get(2).rank.value + playerHand.get(1).rank.value + playerHand.get(0).rank.value > opponentHand.getPlayerHand().get(2).rank.value + opponentHand.getPlayerHand().get(1).rank.value + opponentHand.getPlayerHand().get(0).rank.value) {
                    result = Result.WIN;
                } else if (playerHand.get(2).rank.value + playerHand.get(1).rank.value + playerHand.get(0).rank.value < opponentHand.getPlayerHand().get(2).rank.value + opponentHand.getPlayerHand().get(1).rank.value + opponentHand.getPlayerHand().get(0).rank.value) {
                    result = Result.LOSS;
                } else {
                    result = Result.TIE;
                }
            }
        } else if (this.playerResult.equals(Combination.Highest)) { //Highest Draw
            if (playerHand.get(4).rank.value > opponentHand.getPlayerHand().get(4).rank.value) {
                result = Result.WIN;
            } else if (playerHand.get(4).rank.value < opponentHand.getPlayerHand().get(4).rank.value) {
                result = Result.LOSS;
            } else if (playerHand.get(3).rank.value > opponentHand.getPlayerHand().get(3).rank.value) {
                result = Result.WIN;
            } else if (playerHand.get(3).rank.value < opponentHand.getPlayerHand().get(3).rank.value) {
                result = Result.LOSS;
            }  else if (playerHand.get(2).rank.value > opponentHand.getPlayerHand().get(2).rank.value) {
                result = Result.WIN;
            } else if (playerHand.get(2).rank.value < opponentHand.getPlayerHand().get(2).rank.value) {
                result = Result.LOSS;
            } else if (playerHand.get(1).rank.value > opponentHand.getPlayerHand().get(1).rank.value) {
                result = Result.WIN;
            } else if (playerHand.get(1).rank.value < opponentHand.getPlayerHand().get(1).rank.value) {
                result = Result.LOSS;
            } else if (playerHand.get(0).rank.value > opponentHand.getPlayerHand().get(0).rank.value) {
                result = Result.WIN;
            } else if (playerHand.get(0).rank.value < opponentHand.getPlayerHand().get(0).rank.value) {
                result = Result.LOSS;
            }
            else {
                if (playerHand.get(4).rank.value + playerHand.get(3).rank.value + playerHand.get(2).rank.value + playerHand.get(1).rank.value + playerHand.get(0).rank.value > opponentHand.getPlayerHand().get(4).rank.value + opponentHand.getPlayerHand().get(3).rank.value +opponentHand.getPlayerHand().get(2).rank.value + opponentHand.getPlayerHand().get(1).rank.value + opponentHand.getPlayerHand().get(0).rank.value) {
                    result = Result.WIN;
                } else if (playerHand.get(4).rank.value + playerHand.get(3).rank.value + playerHand.get(2).rank.value + playerHand.get(1).rank.value + playerHand.get(0).rank.value < opponentHand.getPlayerHand().get(4).rank.value + opponentHand.getPlayerHand().get(3).rank.value  +  opponentHand.getPlayerHand().get(2).rank.value + opponentHand.getPlayerHand().get(1).rank.value + opponentHand.getPlayerHand().get(0).rank.value) {
                    result = Result.LOSS;
                } else {
                    result = Result.TIE;
                }
            }
        }


        return result;
    }

    public Map<String, String> getStraightMap() {
        return straightMap;
    }

    public void setStraightMap(Map<String, String> straightMap) {
        this.straightMap = straightMap;
    }

    public boolean isFlush() {
        return isFlush;
    }

    public void setFlush(boolean flush) {
        isFlush = flush;
    }

    public boolean isStraight() {
        return isStraight;
    }

    public void setStraight(boolean straight) {
        isStraight = straight;
    }

    public boolean isFullHouse() {
        return isFullHouse;
    }

    public void setFullHouse(boolean fullHouse) {
        isFullHouse = fullHouse;
    }

    public boolean isFourOfAKind() {
        return isFourOfAKind;
    }

    public void setFourOfAKind(boolean fourOfAKind) {
        isFourOfAKind = fourOfAKind;
    }

    public boolean isThreeOfAKind() {
        return isThreeOfAKind;
    }

    public void setThreeOfAKind(boolean threeOfAKind) {
        isThreeOfAKind = threeOfAKind;
    }

    public boolean isTwoPair() {
        return isTwoPair;
    }

    public void setTwoPair(boolean twoPair) {
        isTwoPair = twoPair;
    }

    public boolean isOnePair() {
        return isOnePair;
    }

    public void setOnePair(boolean onePair) {
        isOnePair = onePair;
    }

    public int getFirstPairRank() {
        return firstPairRank;
    }

    public void setFirstPairRank(int firstPairRank) {
        this.firstPairRank = firstPairRank;
    }

    public int getSecondPairRank() {
        return secondPairRank;
    }

    public void setSecondPairRank(int secondPairRank) {
        this.secondPairRank = secondPairRank;
    }

    public int getThreeOfAKindRank() {
        return threeOfAKindRank;
    }

    public void setThreeOfAKindRank(int threeOfAKindRank) {
        this.threeOfAKindRank = threeOfAKindRank;
    }

    public int getFourOfAKindRank() {
        return fourOfAKindRank;
    }

    public void setFourOfAKindRank(int fourOfAKindRank) {
        this.fourOfAKindRank = fourOfAKindRank;
    }

    public Combination getPlayerResult() {
        return playerResult;
    }

    public void setPlayerResult(Combination playerResult) {
        this.playerResult = playerResult;
    }

}
