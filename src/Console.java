import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class BlackJack {
    private class Card {
        String value;
        String type;

        Card(String value, String type) {
            this.value = value;
            this.type = type;
        }

        public String toString() {
            return value + "-" + type;
        }

        public int getValue() {
            if ("AJQK".contains(value)) {
                if (value.equals("A")) {
                    return 11;
                }
                return 10;
            }
            return Integer.parseInt(value);
        }

        public boolean isAce() {
            return value.equals("A");
        }
    }

    ArrayList<Card> deck;
    Random random = new Random();

    Card hiddenCard;
    ArrayList<Card> dealerHand;
    int dealerSum;
    int dealerAceCount;

    ArrayList<Card> playerHand;
    int playerSum;
    int playerAceCount;

    Scanner scanner = new Scanner(System.in);

    BlackJack() {
        startGame();
        playGame();
    }

    public void startGame() {
        buildDeck();
        shuffleDeck();

        // Dealer
        dealerHand = new ArrayList<>();
        dealerSum = 0;
        dealerAceCount = 0;

        hiddenCard = deck.remove(deck.size() - 1);
        dealerSum += hiddenCard.getValue();
        dealerAceCount += hiddenCard.isAce() ? 1 : 0;

        Card card = deck.remove(deck.size() - 1);
        dealerSum += card.getValue();
        dealerAceCount += card.isAce() ? 1 : 0;
        dealerHand.add(card);

        // Player
        playerHand = new ArrayList<>();
        playerSum = 0;
        playerAceCount = 0;

        for (int i = 0; i < 2; i++) {
            card = deck.remove(deck.size() - 1);
            playerSum += card.getValue();
            playerAceCount += card.isAce() ? 1 : 0;
            playerHand.add(card);
        }
    }

    public void playGame() {
        System.out.println("Dealer shows: " + dealerHand.get(0));
        System.out.println("Your hand: " + playerHand);
        System.out.println("Your total: " + reducePlayerAce());

        boolean playing = true;
        while (playing) {
            System.out.print("Do you want to [hit] or [stay]? ");
            String move = scanner.nextLine().trim().toLowerCase();

            if (move.equals("hit")) {
                Card card = deck.remove(deck.size() - 1);
                playerHand.add(card);
                playerSum += card.getValue();
                playerAceCount += card.isAce() ? 1 : 0;
                System.out.println("You drew: " + card);
                System.out.println("Your hand: " + playerHand);
                System.out.println("Your total: " + reducePlayerAce());

                if (reducePlayerAce() > 21) {
                    System.out.println("You busted! Dealer wins.");
                    return;
                }
            } else if (move.equals("stay")) {
                playing = false;
            } else {
                System.out.println("Invalid input. Please type 'hit' or 'stay'.");
            }
        }

        // Dealer's turn
        System.out.println("Dealer's hidden card was: " + hiddenCard);
        dealerHand.add(0, hiddenCard);
        System.out.println("Dealer's hand: " + dealerHand);
        System.out.println("Dealer's total: " + reduceDealerAce());

        while (reduceDealerAce() < 17) {
            Card card = deck.remove(deck.size() - 1);
            dealerHand.add(card);
            dealerSum += card.getValue();
            dealerAceCount += card.isAce() ? 1 : 0;
            System.out.println("Dealer draws: " + card);
            System.out.println("Dealer's hand: " + dealerHand);
            System.out.println("Dealer's total: " + reduceDealerAce());
        }

        // Determine winner
        int finalPlayer = reducePlayerAce();
        int finalDealer = reduceDealerAce();

        System.out.println("Final Player Total: " + finalPlayer);
        System.out.println("Final Dealer Total: " + finalDealer);

        if (finalPlayer > 21) {
            System.out.println("You busted. Dealer wins.");
        } else if (finalDealer > 21) {
            System.out.println("Dealer busted. You win!");
        } else if (finalPlayer > finalDealer) {
            System.out.println("You win!");
        } else if (finalPlayer < finalDealer) {
            System.out.println("Dealer wins.");
        } else {
            System.out.println("It's a tie!");
        }
    }

    public void buildDeck() {
        deck = new ArrayList<>();
        String[] values = {"A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"};
        String[] types = {"C", "D", "H", "S"};

        for (String type : types) {
            for (String value : values) {
                deck.add(new Card(value, type));
            }
        }
    }

    public void shuffleDeck() {
        for (int i = 0; i < deck.size(); i++) {
            int j = random.nextInt(deck.size());
            Card temp = deck.get(i);
            deck.set(i, deck.get(j));
            deck.set(j, temp);
        }
    }

    public int reducePlayerAce() {
        while (playerSum > 21 && playerAceCount > 0) {
            playerSum -= 10;
            playerAceCount--;
        }
        return playerSum;
    }

    public int reduceDealerAce() {
        while (dealerSum > 21 && dealerAceCount > 0) {
            dealerSum -= 10;
            dealerAceCount--;
        }
        return dealerSum;
    }

    public static void main(String[] args) {
        new BlackJack();
    }
}
