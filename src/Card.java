import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

/**
 * Created by Andrew Merritt and Ryan Terrell on 4/19/2017.
 */
//We made the card class extend imageview so that the cards and their images were the same object
public class Card extends ImageView {

    //data fields for the value of a card(a number, 1-52, that denotes its imageview equivalent), suit, rank, and how many points it's worth
    private int value, suit, rank, points;
    private final int SPADES = 0, HEARTS = 1, DIAMONDS = 2, CLUBS = 3;

    public Card(int value)
    {
        //based on value, it calls the superclass constructor for ImageView, which is a URL determining which card image it is related to
        super("card/" + value + ".png");

        this.value = value;


        //determines the card's rank from its value
        rank = ((value - 1) % 13) + 1;

        //sets the suit of the card based on its value
        if((value - 1) / 13 == 0)
        {
            suit = SPADES;
        }
        else if((value - 1) / 13 == 1)
        {
            suit = HEARTS;
        }
        else if((value - 1) / 13 == 2)
        {
            suit = DIAMONDS;
        }
        else if((value - 1) / 13 == 3)
        {
            suit = CLUBS;
        }

        //sets the point value of the card
        setPoints();
    }

    //returns a card's value
    public int getValue()
    {
        return this.value;
    }

    //returns a card's rank
    public int getRank()
    {
        return this.rank;
    }

    //sets the points value of a card
    private void setPoints()
    {
        //sets the points based on the rules of Pisti
        if(rank == 2 && suit == CLUBS)
        {
            this.points = 2;
        }
        else if(rank == 10 && suit == DIAMONDS)
        {
            this.points = 3;
        }
        else if(rank == 1 || (rank >= 10 && rank <= 13))
        {
            this.points = 1;
        }
        else
        {
            this.points = 0;
        }
    }

    //returns the points value
    public int getPoints()
    {
        return this.points;
    }

    //This method determines if two cards are actually the same card, suit and rank.
    public boolean isEqual(Card c)
    {
        if(c.getValue() == this.value)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    //This method just determines if two cards have the same rank
    public boolean sameRank(Card c)
    {
        if(c.getRank() == this.rank)
        {
            return true;
        }
        else
        {
            return false;
        }
    }


    //simple toString method, mostly for testing purposes
    public String toString()
    {
        if(this.suit == SPADES)
        {
            return "" + this.rank + " of Spades";
        }
        else if(this.suit == HEARTS)
        {
            return "" + this.rank + " of Hearts";
        }
        else if(this.suit == DIAMONDS)
        {
            return "" + this.rank + " of Diamonds";
        }
        else
        {
            return "" + this.rank + " of Clubs";
        }

    }
}
