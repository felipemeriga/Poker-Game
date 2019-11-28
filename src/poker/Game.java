package poker;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Game {

    private Deck deck;
    private List<Player> players;
    private Player dealer;
    private Scanner input;

    public Game() {
        this.input = new Scanner(System.in);
        this.players = new ArrayList<>();
        this.deck = new Deck();
    }

    public void play() {
        this.configureGameStep();
        this.startPlayersTurns();
        this.computeResults();
    }

    public void playAgain() {
        this.deck.fillDeck();
        this.deck.shuffleDeck();
        for(Player player: this.players) {
            player.setNumberOfCardsChange(3);
            PokerHand hand = new PokerHand(this.deck.getHand());
            player.setPokerHand(hand);
        }
        PokerHand dealerHand = new PokerHand(this.deck.getHand());
        this.dealer.setPokerHand(dealerHand);

        this.dealer.getPokerHand().rankHand();
        this.startPlayersTurns();
        this.computeResults();

    }

    private void configureGameStep() {
        System.out.println("---- Welcome to the Poker Game ----");
        System.out.println("\n\n");
        System.out.println("Insert the number of players: (Max 5 players)");
        int numberOfPlayers = input.nextInt();
        System.out.println("\n\n");
        if(numberOfPlayers< 1 || numberOfPlayers > 5) {
            System.out.println("You inserted a wrong number of players, start a new game");
            return;
        }

        this.deck.fillDeck();
        this.deck.shuffleDeck();
        for(int i = 0; i < numberOfPlayers; i++) {
            int playerNumber = i + 1;
            System.out.println("Insert the Name of the Player: " + playerNumber +":");
            String playerName = input.next();
            PokerHand hand = new PokerHand(this.deck.getHand());
            Player player = new Player(playerName, playerNumber);
            player.setPokerHand(hand);
            this.players.add(player);
            System.out.println("\n\n");
        }

        // Creating the Dealer Hand
        PokerHand dealerHand = new PokerHand(this.deck.getHand());
        this.dealer = new Player("Dealer", 0);
        this.dealer.setPokerHand(dealerHand);
        this.dealer.getPokerHand().rankHand();
    }

    private void startPlayersTurns() {
        for(Player player: players) {
           this.playerTurn(player);
        }
    }

    private void playerTurn(Player player) {
        System.out.println("It's " + player.getName() + " time! What you want to do? \n");
        System.out.println("1 - Check your hand\n2 - Change a card\n3 - Evaluate your hand");
        int playerChoice = input.nextInt();
        if(playerChoice != 1 && playerChoice != 2 && playerChoice != 3) {
            System.out.println("\n\n");
            this.clearConsole();
            System.out.println("Select a correct option!!!");
            this.playerTurn(player);
        }

        switch (playerChoice){
            case 1:
                // CHECK HAND
                System.out.println("You have in your hand the cards: ");
                System.out.println("\n\n");
                System.out.println(this.checkHand(player));
                System.out.println("\n\n");
                this.playerTurn(player);
                break;
            case 2:
                // CHANGE CARD
                if(player.getNumberOfCardsChange() > 0) {
                    System.out.println("Please select a card number for changing");
                    System.out.println("\n\n");
                    System.out.println(this.checkHand(player));
                    int cardNumber = this.input.nextInt();
                    this.changeCard(cardNumber, player);
                    this.playerTurn(player);

                } else {
                    System.out.println("You cannot change cards at all!");
                    System.out.println("\n\n");
                    this.playerTurn(player);
                }
                break;
            case 3:
                // RANK HAND
                System.out.println("\n\n");
                player.getPokerHand().rankHand();
        }

        this.clearConsole();
    }

    public final void clearConsole()
    {
        for(int clear = 0; clear < 1000; clear++)
        {
            System.out.println("\b") ;
        }
    }

    public String checkHand(Player player) {
        String hand = "";
        for(int i = 1; i <= 5 ; i++) {
            hand = hand + "Card " +  i  + ": " + player.getPokerHand().getPlayerHand().get(i-1).getRank().name() +
                    " of " + player.getPokerHand().getPlayerHand().get(i-1).getTraducedSuit() + "\n";
        }
        return hand;
    }

    public void changeCard(int cardNumber, Player player) {
        Card selectedCard = player.getPokerHand().getPlayerHand().get(cardNumber-1);
        player.getPokerHand().getPlayerHand().remove(cardNumber-1);
        player.getPokerHand().addCard(this.deck.getCard());
        player.setNumberOfCardsChange(player.getNumberOfCardsChange()-1);
    }

    private void computeResults() {
        for(Player player : players) {
            Result result = player.getPokerHand().getCombinationLogic().compareWith(player.getPokerHand().getPlayerHand(), this.dealer.getPokerHand());
            if(result == Result.WIN) {
                System.out.println(player.getName() + " has won!");
                System.out.println("\n\n");
            } else if(result == Result.LOSS) {
                System.out.println(player.getName() + " has lost!");
                System.out.println("\n\n");
            } else {
                System.out.println(player.getName() + " has tied with dealer!");
                System.out.println("\n\n");
            }
        }
        System.out.println("\n\n");
        System.out.println("Do you wanna to play again? y/n");
        String decision = this.input.next();
        if(!decision.equals("y") && !decision.equals("n")){
            System.out.println("Wrong decision! Quiting game");
            return;
        } else {
            if(decision.equals("y")) {
                this.clearConsole();
                this.playAgain();
            } else {
                return;
            }
        }

    }

    public Deck getDeck() {
        return deck;
    }

    public void setDeck(Deck deck) {
        this.deck = deck;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }
}
