import java.util.ArrayList;

/**
 * Created by Andrew Merritt and Ryan Terrell on 4/19/2017.
 */
public class Player {

    //Data fields for the number of each type of pisti that a player has gotten and also two arraylists of cards, one for the player's hand and one for the pile of won cards
    private int numPistis, numJPistis;
    private ArrayList<Card> hand;
    private ArrayList<Card> wonCards;

    //initializes the values of the data fields
    public Player()
    {
        this.numPistis = 0;
        this.numJPistis = 0;
        this.hand = new ArrayList<Card>();
        this.wonCards = new ArrayList<Card>();
    }

    public void fillHand(Card c)
    {
        hand.add(c);
    }

    public ArrayList<Card> getHand()
    {
        return this.hand;
    }

    /**
     * Essentially the most important method in the player class. It's what allows the GUI to connect to the actual data.
     * Its purpose is to play a card. To do this, it first takes a Card as a parameter. The method starts by creating a Card
     * variable called searched and it sets it to null. It then searches through the player's hand and calls the isEqual method to
     * determines if any of the cards in the hand are THE EXACT SAME CARD as the parameter card. If so, variable searched is set to the value
     * of the parameter, and then that card is removed from the player's hand. The method finishes by returning searched.
     * @param c
     * @return
     */
    public Card playCard(Card c)
    {
        Card searched = null;
        for(int i = 0; i < hand.size(); i++)
        {
            if (hand.get(i).isEqual(c))
            {
                searched = c;
                hand.remove(i);
            }
        }

        //This is here mostly for testing purposes
        if(searched == null)
        {
            System.out.println("Null card");
        }
        return searched;
    }

    //also for testing purposes
    public void printHand()
    {
        for(int i = 0; i < hand.size(); i++)
        {
            System.out.println(hand.get(i));
        }
    }

    //increments the number of pistis
    public void addPisti()
    {
        this.numPistis += 1;
    }

    //increments the number of j pistis
    public void addJPisti()
    {
        this.numJPistis += 1;
    }

    //returns the number of pistis
    public int getNumPistis()
    {
        return this.numPistis;
    }

    //returns the number of pistis
    public int getNumJPistis()
    {
        return this.numJPistis;
    }

    //Yet another method for testing purposes
    public void printWonCards()
    {
        System.out.println("Player Won Cards: ");
        for(int i = 0; i < wonCards.size(); i++)
        {
            System.out.println(wonCards.get(i));
        }
    }

    //Calculates the amount of points that the player currently has.
    public int calculatePoints()
    {
        //starts off by setting points to 0
        int points = 0;
        //then increments points by a certain number as certain conditions are met.
        points += 10 * numPistis;
        points += 20 * numJPistis;

        //adds the amount of points that each card is worth
        for(Card c : wonCards)
        {
            points += c.getPoints();
        }

        //adds 3 points if the player has won the majority of cards
        if(hasMajorityOfCards())
        {
            points += 3;
        }

        return points;
    }

    //If the user has more than half of the total cards from the deck, returns true
    public boolean hasMajorityOfCards()
    {
        if(wonCards.size() > 26)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    //clears the player's hand
    public void clearHand()
    {
        hand.clear();
    }

    //returns the list of won cards
    public ArrayList<Card> getWonCards()
    {
        return this.wonCards;
    }

    //Takes an arraylist of cards and adds all of them to wonCards
    public void addToWonCards(ArrayList<Card> pile)
    {
        wonCards.addAll(pile);
    }

}
