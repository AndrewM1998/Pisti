import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextBoundsType;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.util.ArrayList;

import static javafx.geometry.Pos.TOP_RIGHT;

/**
 * Created by Andrew Merritt and Ryan Terrell on 4/19/2017.
 */
public class DisplayGame extends Application {

    private ArrayList<Card> centerPile = new ArrayList<Card>();
    private Deck deck;
    private Human p;
    private Bot b;
    private StackPane root = new StackPane(), playerWonPile = new StackPane(), botWonPile = new StackPane(), deckPile = new StackPane();
    private Pane cardTable = new Pane();
    private HBox playerHand = new HBox(), playerHandBackground = new HBox(), center = new HBox(), botHand = new HBox();
    private FlowPane viewWonPilePane = new FlowPane();
    private boolean humanTurn = true;
    private Text botTurn = new Text(800, 120, "Bot's Turn"),
            playerTurn = new Text(800, 600, "Player Turn"),
            botNumCardsWon = new Text("0"), playerNumCardsWon = new Text("0"),
            botPoints = new Text("Bot Points: 0"), playerPoints = new Text(),
            deckSize = new Text(), playerRoundWinText = new Text(500, 510, "Player Wins Round!"),
            botRoundWinText = new Text(500, 210, "Bot Wins Round!"), gameWinner = new Text(), playerEndStats = new Text(), botEndStats = new Text(),
            name = new Text("Enter Name: "),
            title = new Text();
    private Timeline botTurnAnimation, playerTurnAnimation, playerRoundWin, botRoundWin;
    private int previousWinner = 0; //integer 0, 1, or 2 represents the last winner of a round. 0 means no winner yet, 1 means the human player was the last winner, and 2 means he bot was the last winner
    private int lastPlayed = 0; //0 = no player has played yet, 1 = human was the last player, 2 = bot was the last player
    private TextField tfPlayerName = new TextField();
    private String playerName;
    private Button restartGame = new Button("Play Again"), beginGameButton = new Button("Begin Game");

    public void start(Stage primaryStage)
    {

        /**
         * This first section of code creates events for various animations such as a turn animation for each player that shows up while it is a certain player's turn.
         */
        // Bot turn animation
        EventHandler<ActionEvent> botTurnEvent = e -> {
            if (botTurn.getText().length() == 10) {
                botTurn.setText("Bot's Turn.");
            }
            else if (botTurn.getText().length() == 11) {
                botTurn.setText("Bot's Turn..");
            }
            else if (botTurn.getText().length() == 12) {
                botTurn.setText("Bot's Turn...");
            }
            else {
                botTurn.setText("Bot's Turn");
            }
        };

        // Player turn animation
        EventHandler<ActionEvent> playerTurnEvent = e -> {
            if (playerTurn.getText().length() - playerName.length() == 7) {
                playerTurn.setText(playerName + "'s Turn.");
            }
            else if (playerTurn.getText().length() - playerName.length() == 8) {
                playerTurn.setText(playerName + "'s Turn..");
            }
            else if (playerTurn.getText().length() - playerName.length() == 9) {
                playerTurn.setText(playerName + "'s Turn...");
            }
            else {
                playerTurn.setText(playerName + "'s Turn");
            }
        };

        EventHandler<ActionEvent> playerWins = e -> {

        };

        EventHandler<ActionEvent> botWins = e -> {

        };

        //initializes the timelines for the events we created earlier
        botTurnAnimation = new Timeline(
                new KeyFrame(Duration.seconds(.3), botTurnEvent));
        botTurnAnimation.setCycleCount((int)(Math.random() * 4) + 3);

        playerRoundWin = new Timeline(
                new KeyFrame(Duration.seconds(1.5), playerWins));
        playerRoundWin.setCycleCount(1);

        botRoundWin = new Timeline(
                new KeyFrame(Duration.seconds(1.5), botWins));
        botRoundWin.setCycleCount(1);

        playerTurnAnimation = new Timeline(
                new KeyFrame(Duration.seconds(.5), playerTurnEvent));
        playerTurnAnimation.setCycleCount(Timeline.INDEFINITE);


        /**
         * This section creates events for when various objects are clicked or hovered-over
         */

        //Starts Pisti game if the player enters a name that is between 1 and 10 characters
        beginGameButton.setOnMouseClicked(e -> {
            if(tfPlayerName.getText().length() > 10 || tfPlayerName.getText().length() < 1)
            {
                name.setText("Name must be between 1 and 10 characters in length!");
            }
            else
            {
                initializeGame();
            }
        });


        //Takes a card from the player's hand and puts it in the center
        playerHand.setOnMouseClicked(e -> {
            //nothing happens if the value of human turn is false. This prevents the user from playing a card while the bot is trying to play
            if (humanTurn)
            {
                //searches through every child node of playerHand
                for (Node n : playerHand.getChildren())
                {
                    //if the mouse is within the bounds of a certain card, it adds it to the center pile and removes it from the hand and then escapes the loop
                    if (e.getX() < n.getLayoutX() + 72 && e.getX() >= n.getLayoutX())
                    {
                        centerPile.add(p.playCard(p.getHand().get(playerHand.getChildren().indexOf(n))));
                        playerHand.getChildren().remove(n);
                        humanTurn = false; //makes sure user cannot play cards during the bot's turn
                        playerTurn.setFill(Color.TRANSPARENT); //hides the playerTurn Text
                        lastPlayed = 1; //sets the user as the last person who played a card
                        winCenterPile(); //calls this method to determine if the user won the pile
                        //adds the text on screen denoting that it is the bot's turn
                        if(!cardTable.getChildren().contains(botTurn))
                        {
                            cardTable.getChildren().add(botTurn);
                        }
                        botTurnAnimation.setCycleCount((int)(Math.random() * 4) + 3);
                        botTurnAnimation.play(); //plays the botTurn animation, which calls the botMove method once it is over. It creates a nice effect as if the computer were thinking before making a move
                        break;
                    }
                }

            }
        });

        // Shows the card the user will click
        playerHand.setOnMouseMoved(e -> { // *** does not hide the background to the cards if mouse is outside of the plane? ***
            if (humanTurn)
            {
                for (Node n : playerHand.getChildren())
                {
                    if (e.getX() < n.getLayoutX() + 72 && e.getX() >= n.getLayoutX())
                    {
                        playerHandBackground.getChildren().get(playerHand.getChildren().indexOf(n)).setOpacity(1);
                    }
                    else
                    {
                        playerHandBackground.getChildren().get(playerHand.getChildren().indexOf(n)).setOpacity(0);
                    }
                }
            }
        });

        //Removes the outlines from any cards in your hand if the mouse leaves the pane of playerHand
        playerHand.setOnMouseExited(e -> {
            if (humanTurn)
            {
                for (int i = 0; i < playerHandBackground.getChildren().size(); i++)
                {
                    playerHandBackground.getChildren().get(i).setOpacity(0);
                }
            }
        });

        /**
         * This section deals with the creation of a new window that contains all the cards the user has won
         */
        //creates a new window that displays all of the cards that the user has won so far when they click their discard pile
        Scene viewWonPile = new Scene(viewWonPilePane);
        Stage secondStage = new Stage();

        // creates StackPanes for buttons and a FlowPane for buttons
        StackPane xOverlap = new StackPane();
        StackPane mOverlap = new StackPane();
        FlowPane xBar = new FlowPane();

        // Adds Custom Close/Minimize Buttons
        Circle xButton = new Circle(15,Color.rgb(255,200,200,0.75));
        Circle mButton = new Circle(15,Color.rgb(200,255,200,0.75));
        Text close = new Text("Close");
        Text minimize = new Text("Min");

        // Puts the Buttons in the Right Corner
        xBar.setAlignment(TOP_RIGHT);

        // setts the border for the Circles
        xButton.setStroke(Color.rgb(255,150,150));
        mButton.setStroke(Color.rgb(150,255,150));

        // Sets Color and Bind type for the Text
        close.setBoundsType(TextBoundsType.VISUAL);
        close.setFill(Color.rgb(50,50,50));
        minimize.setBoundsType(TextBoundsType.VISUAL);
        minimize.setFill(Color.rgb(50,50,50));

        // adds all the text and circles
        xBar.getChildren().add(title);
        mOverlap.getChildren().addAll(mButton,minimize);
        xBar.getChildren().add(mOverlap);
        xOverlap.getChildren().addAll(xButton,close);
        xBar.getChildren().add(xOverlap);

        // Sets the Stage in the corner
        secondStage.setX((primaryStage.getX()));
        secondStage.setY((primaryStage.getY()));

        // sets the scene and makes the stage transparent showing no title bar or background
        secondStage.setScene(viewWonPile);
        secondStage.initStyle(StageStyle.TRANSPARENT);

        playerWonPile.setOnMouseClicked(e -> {
            viewWonPilePane.getChildren().clear();
            viewWonPilePane.getChildren().add(xBar);
            viewWonPilePane.getChildren().addAll(p.getWonCards());
            secondStage.setX(primaryStage.getX() + 7);
            secondStage.setY(primaryStage.getY() + 18.5);
            secondStage.hide(); //prevents the user from opening thousands of windows
            secondStage.show();
        });

        //creates new icons for the two stages
        primaryStage.getIcons().add(new Image("data/Logo.png"));
        secondStage.getIcons().add(new Image("card/b1fv.png"));

        //starts a new Pisti game when the restart button is clicked
        restartGame.setOnMouseClicked(e ->{
            viewWonPilePane.getChildren().clear();
            secondStage.hide();
            initializeGame();
        });

        // hides / "Closes" the window on click
        mOverlap.setOnMouseClicked(e -> {secondStage.hide();});
        xOverlap.setOnMouseClicked(e -> {secondStage.hide();});


        /**
         * This section creates items for and displays the starting screen of the game
         */
        VBox startMenuItems = new VBox(); //this pane will contain texr that prompts user to enter their name, a TextField for the name, and a start game button
        startMenuItems.setSpacing(7.5);
        tfPlayerName.setMaxWidth(150);
        name.setFill(Color.WHITE);
        name.setFont(Font.font ("Verdana", 15));

        startMenuItems.getChildren().addAll(name, tfPlayerName, beginGameButton); //adds everything to the start menu items

        //since the root is a StackPane, it is not easy to place items where you want them to go, so we created a pane to be added to the root for the start menu
        Pane start = new Pane();
        start.setPrefWidth(1280);
        start.setPrefHeight(720);
        startMenuItems.setLayoutX(50);
        startMenuItems.setLayoutY(300);
        start.getChildren().add(startMenuItems);

        root.getChildren().addAll(new ImageView("data/StartScreen.png"), start);

        //This commented block of code was a test for the ability to have a 3D-like view of the game. This never came to fruition, unfortunately.
        /**
        root.setRotationAxis(Rotate.X_AXIS);
        root.setRotate(-60);

        PerspectiveCamera camera = new PerspectiveCamera(false);
        scene.setCamera(camera);
        camera.setNearClip(0);
        camera.setTranslateZ(-30);
        camera.setTranslateY(-100);
        scene.setFill(Color.CYAN); */

        //creating the scene and stage
        Scene scene = new Scene(root, 1280, 720);
        primaryStage.setTitle("Pisti");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    /**
     * This method includes everything necessary to begin the Pisti game itself. We separated the code in this method and in the
     * initializeUIElements method from the start method so that we could easily start a new game if we wanted to.
     */
    public void initializeGame()
    {
        //initializes the deck and the two players
        deck = new Deck();
        p = new Human();
        b = new Bot();

        //resets the values of lastPlayed and previousWinner to 0 because at the start of a new game, nobody has played or won yet.
        lastPlayed = 0;
        humanTurn = true;
        previousWinner = 0;
        initializeUIElements(); //to avoid creating a gigantic method, we separated a large portion of UI creation and put it in its own method

        //draws cards from the deck and adds them to the center pile, the user's hand, and the bot's hand.
        for(int i = 0; i < 4; i++)
        {
            centerPile.add(deck.drawCard());
            p.fillHand(deck.drawCard());
            b.fillHand(deck.drawCard());
            botHand.getChildren().add(new ImageView("card/b1fv.png")); //adds 4 images of the backs of cards to represent the bot's hand
        }

        //if the top card on the center pile happens to be a jack, the hands of each player and the center pile are cleared and the deck is reinitialized until the top card is no longer a jack
        while(centerPile.get(centerPile.size() - 1).getRank() == 11)
        {
            centerPile.clear();
            p.clearHand();
            b.clearHand();
            botHand.getChildren().clear();
            deck = new Deck();

            for(int i = 0; i < 4; i++)
            {
                centerPile.add(deck.drawCard());
                p.fillHand(deck.drawCard());
                b.fillHand(deck.drawCard());
                botHand.getChildren().add(new ImageView("card/b1fv.png"));
            }
        }

        //adds 3 images of the backs of cards and then the face of the top card to represent how the start of a Pisti game would really look
        center.getChildren().addAll(new ImageView("card/b1fv.png"), new ImageView("card/b1fv.png"), new ImageView("card/b1fv.png"), centerPile.get(centerPile.size() - 1));

        //adds the backgrounds of the player's cards, which is just a white image that is slightly larger than the card, to a pane. This is what gives the white ring effect around the card that the user has their mouse on
        playerHandBackground.getChildren().addAll(new ImageView("data/CardBackground.png"),new ImageView("data/CardBackground.png"),
                new ImageView("data/CardBackground.png"),new ImageView("data/CardBackground.png"));

        //adds all the card images to the player's hand
        playerHand.getChildren().addAll(p.getHand());
        //shows the number of cards in the deck currently
        deckSize.setText("" + deck.getDeck().size());

        //ensures that the white background images for the cards cannot be seen initially
        for(Node n : playerHandBackground.getChildren())
            n.setOpacity(0);
    }

    public void initializeUIElements()
    {
        //clears out all of the panes to avoid errors upon a restart and also resets a few values. Then adds the various UI elements to the panes.
        root.getChildren().clear();
        cardTable.getChildren().clear();
        deckPile.getChildren().clear();
        playerWonPile.getChildren().clear();
        botWonPile.getChildren().clear();
        playerEndStats.setText("");
        botEndStats.setText("");
        gameWinner.setText("");
        cardTable.getChildren().add(playerHandBackground);
        cardTable.getChildren().addAll(playerHand, botHand, center, playerWonPile, botWonPile, botPoints, playerPoints, deckPile);
        root.getChildren().addAll(new ImageView("data/PlayScreen.png"), cardTable); //adds the background and the actually pane for the game to the root

        /**
         * This long section of code simply sets the location and appearance of various UI elements
         */
        playerName = tfPlayerName.getText();
        playerTurn.setText(playerName + "'s Turn");
        playerPoints.setText(playerName + " Points: " + p.calculatePoints());
        botPoints.setText("Bot Points: 0");

        deckSize.setText("" + deck.getDeck().size());
        deckSize.setTextAlignment(TextAlignment.CENTER);
        deckSize.setFill(Color.YELLOW);
        deckSize.setFont(Font.font ("Verdana", 20));

        deckPile.getChildren().addAll(new ImageView("card/b1fv.png"), deckSize);
        deckPile.setLayoutX(100);
        deckPile.setLayoutY(root.getHeight() / 2.3);

        playerTurn.setFill(Color.YELLOW);
        playerRoundWinText.setFill(Color.TRANSPARENT);
        botRoundWinText.setFill(Color.TRANSPARENT);

        playerTurnAnimation.play();

        center.setLayoutX(root.getWidth() / 2.45);
        center.setLayoutY(root.getHeight() / 2.3);
        center.setSpacing(-36);

        playerHand.setLayoutX(root.getWidth() / 2.8);
        playerHand.setLayoutY(root.getHeight() / 1.3);
        playerHand.setSpacing(5);

        playerHandBackground.setLayoutX((root.getWidth()-4) / 2.8);
        playerHandBackground.setLayoutY((root.getHeight()-2) / 1.3);
        playerHandBackground.setSpacing(0.5);

        botHand.setLayoutX(root.getWidth() / 2.8);
        botHand.setLayoutY(root.getHeight() / 10.0);
        botHand.setSpacing(5);

        botWonPile.setLayoutX(1000);
        botWonPile.setLayoutY(root.getHeight() / 10.0);

        playerWonPile.setLayoutX(1000);
        playerWonPile.setLayoutY(root.getHeight() / 1.3);

        botTurn.setFill(Color.YELLOW);
        botTurn.setFont(Font.font ("Verdana", 20));
        botTurnAnimation.setOnFinished(e -> botMove());

        playerTurn.setFont(Font.font ("Verdana", 20));
        cardTable.getChildren().add(playerTurn);

        botNumCardsWon.setTextAlignment(TextAlignment.CENTER);
        botNumCardsWon.setFill(Color.YELLOW);
        botNumCardsWon.setFont(Font.font ("Verdana", 20));

        playerNumCardsWon.setTextAlignment(TextAlignment.CENTER);
        playerNumCardsWon.setFill(Color.YELLOW);
        playerNumCardsWon.setFont(Font.font ("Verdana", 20));

        playerPoints.setLayoutX(1000);
        playerPoints.setLayoutY((root.getHeight() / 1.3) - 10);
        playerPoints.setFill(Color.YELLOW);
        playerPoints.setFont(Font.font ("Verdana", 20));

        botPoints.setLayoutX(1000);
        botPoints.setLayoutY((root.getHeight() / 10) - 10);
        botPoints.setFill(Color.YELLOW);
        botPoints.setFont(Font.font ("Verdana", 20));


        playerRoundWinText.setFont(Font.font ("Verdana", 25));
        botRoundWinText.setFont(Font.font ("Verdana", 25));

        cardTable.getChildren().addAll(playerRoundWinText, botRoundWinText);

        playerRoundWin.setOnFinished(e -> makeWinTextTransparent());
        botRoundWin.setOnFinished(e -> makeWinTextTransparent());

        gameWinner.setFont(Font.font ("Verdana", 30));
        gameWinner.setFill(Color.WHITE);
        gameWinner.setLayoutX(500);
        gameWinner.setLayoutY(360);

        playerEndStats.setFont(Font.font ("Verdana", 25));
        playerEndStats.setFill(Color.WHITE);
        playerEndStats.setLayoutX(400);
        playerEndStats.setLayoutY(500);

        playerRoundWinText.setText(playerName + " Wins Round!");

        botEndStats.setFont(Font.font ("Verdana", 25));
        botEndStats.setFill(Color.WHITE);
        botEndStats.setLayoutX(400);
        botEndStats.setLayoutY(150);

        title.setText(" " + playerName + "'s Won Cards \t\t\t");

        cardTable.getChildren().addAll(gameWinner, playerEndStats, botEndStats);
    }

    //This method checks if both players have 0 cards, and if that is the case, their hands are refilled
    public void dealCards()
    {
        if(p.getHand().size() == 0 && b.getHand().size() == 0 && deck.getDeck().size() >= 8)
        {
            for(int i = 0; i < 4; i++)
            {
                p.fillHand(deck.drawCard());
                b.fillHand(deck.drawCard());
                botHand.getChildren().add(new ImageView("card/b1fv.png"));
            }
            playerHand.getChildren().addAll(p.getHand());
            deckSize.setText("" + deck.getDeck().size());
        }
    }

    //Updates various aspects of the game screen
    public void updateScreen()
    {
        //if the center pile has at least 4 cards, as cards are added, the card on the bottom disappears. This prevents the creation of a giant center pile visual
        if(centerPile.size() >= 4 && center.getChildren().size() == 4)
        {
            center.getChildren().remove(0);
            center.getChildren().add(centerPile.get(centerPile.size() - 1));
        }
        //otherwise the center pile updates as cards are added
        else
        {
            center.getChildren().clear();
            center.getChildren().addAll(centerPile);
        }
        dealCards();

        for(Node n : playerHandBackground.getChildren())
            n.setOpacity(0);

        //checks if the game is over
        if(gameIsOver())
        {
            //Sets the text of gameWinner to show whoever won the game
            gameWinner.setText(whoWins());
            playerTurn.setFill(Color.TRANSPARENT);
            makeWinTextTransparent(); //makes any text determining who won a round transparent
            //Sets the text of each player's ending stats depending on who has the majority of cards
            if(p.hasMajorityOfCards())
            {
                playerEndStats.setText(playerName + " stats:\nNumber of Pistis: " + p.getNumPistis() + "\nNumber of J Pistis: " + p.getNumJPistis() + "\nWon the majority of cards: Yes");
                botEndStats.setText("Bot Stats:\nNumber of Pistis: " + b.getNumPistis() + "\nNumber of J Pistis: " + b.getNumJPistis() + "\nWon the majority of cards: No");
            }
            else
            {
                playerEndStats.setText(playerName + " stats:\nNumber of Pistis: " + p.getNumPistis() + "\nNumber of J Pistis: " + p.getNumJPistis() + "\nWon the majority of cards: No");
                botEndStats.setText("Bot Stats:\nNumber of Pistis: " + b.getNumPistis() + "\nNumber of J Pistis: " + b.getNumJPistis() + "\nWon the majority of cards: Yes");
            }

            //sets the position of, and adds the restart button to the pane
            restartGame.setLayoutX(50);
            restartGame.setLayoutY(50);
            if(!cardTable.getChildren().contains(restartGame))
            {
                cardTable.getChildren().add(restartGame);
            }
        }
    }

    //Determines if anyone won the center pile
    public void winCenterPile()
    {
        //creates an integer value at the start that represents the size of the center pile in order to simplify the lengthy line of if else statements
        int pileSize = centerPile.size();

        /**
         * This block of if else statements first determines which player was the last one who played a card,
         * then it checks if the pile has only two cards(in which case a pisti may occur), or if there are more than two cards.
         * If there are only two cards, it checks if the top card is a jack and if it matches the bottom card(J Pisti), if not, then it checks
         * if the top and bottom card match at all(Pisti), if not, it finally checks if the top card is simply a jack, in which case the previous player
         * wins the center pile but does not earn pisti points.
         */
        if(lastPlayed == 1)
        {
            if (pileSize == 2)
            {
                if (centerPile.get(pileSize - 1).getRank() == 11 && centerPile.get(pileSize - 1).sameRank(centerPile.get(pileSize - 2)))
                {
                    p.addJPisti(); //adds a J Pisti to the player
                    playerWinsRound(); //calls the method indicating that the user won the pile
                }
                else if (centerPile.get(pileSize - 1).sameRank(centerPile.get(pileSize - 2)))
                {
                    p.addPisti();
                    playerWinsRound();
                }
                else if(centerPile.get(pileSize - 1).getRank() == 11)
                {
                    playerWinsRound();
                }
            }
            else if (pileSize > 2)
            {
                if ((centerPile.get(pileSize - 1).getRank() == 11) || centerPile.get(pileSize - 1).sameRank(centerPile.get(pileSize - 2)))
                {
                    playerWinsRound();
                }
            }
        }
        else if(lastPlayed == 2)
        {
            if (pileSize == 2)
            {
                if (centerPile.get(pileSize - 1).getRank() == 11 && centerPile.get(pileSize - 1).sameRank(centerPile.get(pileSize - 2)))
                {
                    b.addJPisti();
                    botWinsRound(); //calls the method indicating that the bot won the pile
                }
                else if (centerPile.get(pileSize - 1).sameRank(centerPile.get(pileSize - 2)))
                {
                    b.addPisti();
                    botWinsRound();
                }
                else if(centerPile.get(pileSize - 1).getRank() == 11)
                {
                    botWinsRound();
                }
            }
            else if (pileSize > 2)
            {
                if ((centerPile.get(pileSize - 1).getRank() == 11) || centerPile.get(pileSize - 1).sameRank(centerPile.get(pileSize - 2)))
                {
                    botWinsRound();
                }
            }
        }


        //If nobody won the pile and nobody has any cards, the center pile is given to the previous winner
        if(centerPile.size() > 0)
        {
            if (b.getHand().size() == 0 && p.getHand().size() == 0 && previousWinner == 1)
            {
                playerWinsRound();
            }
            else if (b.getHand().size() == 0 && p.getHand().size() == 0 && previousWinner == 2)
            {
                botWinsRound();
            }
        }

        updateScreen();
    }

    //This method is called when the user wins a round
    public void playerWinsRound()
    {
        p.addToWonCards(centerPile); //adds the cards in the center pile to the user's pile of cards that they won
        previousWinner = 1; //sets the user as the most recent winner
        centerPile.clear(); //empties the center pile
        playerWonPile.getChildren().clear();
        playerNumCardsWon.setText("" + p.getWonCards().size()); //shows how many cards the player has won
        playerWonPile.getChildren().addAll(new ImageView("card/b1fh.png"), playerNumCardsWon);
        playerPoints.setText(playerName + " Points: " + p.calculatePoints()); //determines how many points the player has after every round they win
        playerRoundWinText.setFill(Color.WHITE); //colors the text white that says the user won the round
        playerRoundWin.play(); //plays a 2 second animation that does nothing other than makes the playerRoundWinText disappear at the end of the 2 seconds
        updateScreen();

    }

    //This method is called when the bot wins a round
    public void botWinsRound()
    {
        b.addToWonCards(centerPile); //adds the cards in the center to the bot's pile of won cards
        previousWinner = 2; //sets the bot as the most recent winner
        centerPile.clear();
        botWonPile.getChildren().clear();
        botNumCardsWon.setText("" + b.getWonCards().size());
        botWonPile.getChildren().addAll(new ImageView("card/b1fh.png"), botNumCardsWon);
        botPoints.setText("Bot Points: " + b.calculatePoints());
        botRoundWinText.setFill(Color.WHITE);
        botRoundWin.play();
        updateScreen();

    }

    //This method is called when it is the bot's turn
    public void botMove()
    {
        //if it is not the user's turn, it adds a randomly chosen card from the bot's hand to the center
        if(!humanTurn)
        {
            centerPile.add(b.chooseCard());
            if(botHand.getChildren().size() > 0)
            {
                botHand.getChildren().remove(0);
            }
        }
        lastPlayed = 2; //sets the bot as the most recent player
        cardTable.getChildren().remove(botTurn); //removes the text that says it is the bot's turn from the table
        playerTurn.setFill(Color.YELLOW);
        winCenterPile();
        humanTurn = true; //sets this value to true after the bot plays a card, which finally gives the user control again in order to pick another card
    }

    //just makes the text saying that either player won a round transparent
    public void makeWinTextTransparent()
    {
        playerRoundWinText.setFill(Color.TRANSPARENT);
        botRoundWinText.setFill(Color.TRANSPARENT);
    }

    //Determines if the game is over by checking if the deck, center pile, and all hands are empty. Returns true if that is the case, false otherwise.
    public boolean gameIsOver()
    {
        if(deck.getDeck().size() == 0 && b.getHand().size() == 0 && p.getHand().size() == 0 && centerPile.size() == 0)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    // calculates who wins based on who has the most points. Returns a string denoting who won.
    public String whoWins()
    {
        if(p.calculatePoints() > b.calculatePoints())
        {
            return playerName + " Wins!";
        }
        else if(p.calculatePoints() < b.calculatePoints())
        {
            return "Bot Wins!";
        }
        else
        {
            return "It's a Draw!";
        }
    }


    public static void main(String[] args)
    {
        launch(args);
    }
}
