/**
 * Created by Andrew Merritt and Ryan Terrell on 4/21/2017.
 */
public class Bot extends Player {

    public Bot()
    {
        super();
    }

    //This method is exclusive to the bot class. Since the computer has to choose a card on its own, the bot class needed a method that did just that.
    public Card chooseCard()
    {
        //picks a random integer between 0 and the size of the bot's hand - 1
        int cardChoice = (int)(Math.random() * this.getHand().size());
        return playCard(this.getHand().get(cardChoice)); //based on this int value, it calls the playCard method from the parent class.
    }

    @Override
    //This method was used for testing purposes.
    public void printWonCards()
    {
        System.out.println("Bot Won Cards: ");
        for(int i = 0; i < this.getWonCards().size(); i++)
        {
            System.out.println(this.getWonCards().get(i));
        }
    }
}
