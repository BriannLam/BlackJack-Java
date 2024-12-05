//NOTE: all of the imports are basically pre-made classes that you give a new name to in order to use them as objects in you own code(They are also known as Toolkits)
//awt & swing are both ways to make a window but awt is older and more reliable whil swing is newer and has more functions 

import java.awt.BorderLayout; //This allows for the other functions to be moved around and in a LAYOUT
import java.awt.Color; //This decides the color of what the background will be 
import java.awt.Graphics; //Allows for things to be drawn on the window (i.e the cards)
import java.awt.Image; //This is how you get images in java, for each image you wnat you have to start with this then the path to the image 

//random classes that can make coding smoother such as pre-made arrays and randomizers
import java.util.ArrayList; //creates an array based on name given to which can use .add or other modifers to check what is in array
import java.util.Random; //when something passes into random they will then get a random number 

//These are smaller functions and actions that can be taken on the window created by awt
import javax.swing.ImageIcon; //normally images dont do nothing but with this, it allows the image to be clickable and be used as a button
import javax.swing.JButton; //This creates a standard java button with like no styling
import javax.swing.JFrame; //This creates the window that the game is created in
import javax.swing.JPanel; //bascially like BorderLayout
import javax.swing.border.Border;

public class BrianJack {

    private class Card{
        String value; 
        String type; 

        Card(String value, String type){ //This creates the card by giving a type and value
            this.value = value;
            this.type = type;
        }

        public String toString(){ //This turns it into a string so that it can be referred by getImagepath
            return value + "-" + type;
        }

        public int getValue(){
            if("AJQK".contains(value)){ // A J Q K
                if(value == "A"){
                    return 11; //if its an Ace it will have a value of 11
                }
                return 10; // otherwise it would be a face card so a 10
            }
            return Integer.parseInt(value); //2-10
        }
        
        
        public boolean isAce(){
            return value == "A";
        }

        public String getImagepath(){
            return "./cards/" + toString() + ".png";
        }
    }

    ArrayList<Card> deck; //making a list named deck and that list is made up of cards
    Random random = new Random(); //shuffling the deck 


    //dealer 
    Card hiddenCard; 
    ArrayList<Card> dealerHand;
    int dealerSum; 
    int dealerAceCount;

    //player 
    ArrayList<Card> playerHand;
    int playerSum;
    int playerAceCount;
    
    //window 
    int boardWidth = 600;
    int boardHeight = boardWidth;

    int cardWidth = 110;
    int cardHeight = 154; //changable ration should be 1:1.4

    JFrame frame = new JFrame("Black Jack");
    //This creates the gamePanel and all of the things rendered in it
    JPanel gamePanel = new JPanel() {
        //When you start the game and load in these are the things that are loaded in first, instead of a blank screen
        @Override
        public void paintComponent(Graphics g){
            super.paintComponent(g);

            try {
                //Creates the image of the card facedown
                Image hiddenCardImg = new ImageIcon(getClass().getResource("./cards/BACK.png")).getImage();
                g.drawImage(hiddenCardImg, 20, 20, cardWidth, cardHeight, null);

                //This gives a value to the card that is face down and adding it to the dealersHand
                for(int i = 0; i <dealerHand.size(); i++){
                    Card card = dealerHand.get(i);
                    Image cardImg = new ImageIcon(getClass().getResource(card.getImagepath())).getImage();
                    g.drawImage(cardImg, cardWidth + 25 + (cardWidth + 5)*i, 20, cardWidth, cardHeight, null);
                }
                //This helps you track any bugs that might occur in the code
            } catch (Exception e) {
                e.printStackTrace();
            }
            
        }
    };
    JPanel buttonPanel = new JPanel();
    JButton hitButton = new JButton("Hit");
    JButton stayButton = new JButton("Stay");



    BrianJack() {
        startGame();

        frame.setVisible(true);
        frame.setSize(boardWidth, boardHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        gamePanel.setLayout(new BorderLayout());
        gamePanel.setBackground(new Color(53, 101, 77));
        frame.add(gamePanel);

        hitButton.setFocusable(false);
        buttonPanel.add(hitButton);
        stayButton.setFocusable(false);
        buttonPanel.add(stayButton);
        frame.add(buttonPanel, BorderLayout.SOUTH);

    }

    public void startGame(){
        //deck 
        buildDeck();
        shuffleDeck();
        
        
        //dealer
        dealerHand = new ArrayList<Card>();
        dealerSum = 0;
        dealerAceCount = 0;

        hiddenCard = deck.remove(deck.size()-1); //remove card at last index 
        dealerSum += hiddenCard.getValue();
        dealerAceCount += hiddenCard.isAce() ? 1 : 0;

        Card card = deck.remove(deck.size()-1);
        dealerSum += card.getValue();
        dealerAceCount += card.isAce() ? 1: 0;
        dealerHand.add(card);

        System.out.println("DEALER:");
        System.out.println(hiddenCard);
        System.out.println(dealerHand);
        System.out.println(dealerSum);
        System.out.println(dealerAceCount);

        //player 
        playerHand = new ArrayList<Card>();
        playerSum = 0;
        playerAceCount = 0;

        for(int i = 0; i<2; i++){
            card = deck.remove(deck.size()-1);
            playerSum += card.getValue();
            playerAceCount += card.isAce() ? 1 : 0;
            playerHand.add(card);
        }


        System.out.println("PLAYER:");
        System.out.println(playerHand);
        System.out.println(playerSum);
        System.out.println(playerAceCount);

    }


    public void buildDeck(){
        deck = new ArrayList<Card>();
        String[] value = {"A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K" };
        String[] types = {"C", "D", "H", "S"};

        for( int i = 0; i <types.length; i++){
            for(int j = 0; j < value.length; j++){
                Card card = new Card(value[j], types[i]);
                deck.add(card);
            }
        }
        System.out.println("BUILD DECK");
        System.out.println(deck);
    }

    public void shuffleDeck(){
        for(int i = 0; i < deck.size(); i++){
            int j = random.nextInt(deck.size()); //it is getting a random INT (bc of the next.Int) and having the upper limit be deck.size()
            Card currCard = deck.get(i);
            Card randomCard = deck.get(j);
            deck.set(i, randomCard);
            deck.set(j, currCard);

        }
        System.out.println("AFTER SHUFFLE");
        System.out.println(deck);
    }
}
