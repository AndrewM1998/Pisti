import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Andrew Merritt and Ryan Terrell on 4/19/2017.
 */
public class Deck {

    //The deck is simply a bunch of cards, so its only data field is an arraylist of cards
    private ArrayList<Card> deck = new ArrayList<Card>();

    public Deck()
    {
        //Creates the deck by looping from number 1 to 52 and creating a new card object from each value and adding them to the deck
        for(int i = 1; i <= 52; i++)
        {
            deck.add(new Card(i));
        }

        Collections.shuffle(deck); //shuffles the deck
    }

    //If the deck has more than 0 cards in it, it removes a card from the top of the deck and then returns it.
    public Card drawCard()
    {
        Card c;
        if(deck.size() > 0)
        {
            c = deck.get(deck.size() - 1);
            deck.remove(c);
            return c;
        }
        else
        {
            return null;
        }
    }

    //returns the deck arraylist
    public ArrayList<Card> getDeck()
    {
        return this.deck;
    }

    //shuffles the deck
    public void shuffleDeck()
    {
        Collections.shuffle(deck);
    }
}
