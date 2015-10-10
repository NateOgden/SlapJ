package com.cs3750;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar.ProgressBarStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Slider.SliderStyle;

public class Slapjack extends ApplicationAdapter {
	public enum GamePhases{ TITLE_SCREEN, DEAL, GAME_PLAY, WINNER }
	private GamePhases gamePhase = GamePhases.TITLE_SCREEN;
	public enum GamePlayTurn{HUMAN, COMPUTER}
	private GamePlayTurn whoseTurn = GamePlayTurn.HUMAN;
	private Player lastToPlay; 
	
	//for dynamic amount of players 
	private ArrayList<Player> players;
	private Deck cardStack;

	private SpriteBatch batch;
	private Texture background;	
	private Texture titlescreen;
	private BitmapFont titleFont;
	private Texture cardSpriteSheet;
	private Texture cardLanderTexture;
	
	//for card back animations
	private Sprite[] cardBackSprites;
	private Texture cardBackTexture;
	private float timer = 0f;
	private int MAX_HEIGHT;
	private int MIN_HEIGHT; 
	private int MAX_WIDTH; 
	private int MIN_WIDTH;
	private int numPlayers;
	private static boolean jackPlayed = false;
	//private List<Card> cardsPlayed;
	
	private Stage startStage;
	private Stage stage;
	private Stage endStage;
	
	private Skin skin;
	private BitmapFont bitmapfont;
	private TextButtonStyle textButtonStyle;
	private SliderStyle sliderStyle;
	
	private HashMap<String, Runnable> buttonMap = new HashMap<String, Runnable>();
	private TextButton playGameButton;
	private TextButton playCardButton;
	private TextButton resetGameButton;
	private TextButton testCardStack;
	private Slider numOfPlayerSlider;
	private Slider difficultySlider;
	private Slider resetSlider;
	
	//Don't delete!
	@Override
	public void create () {		
		
		//environment variables
		numPlayers = 3;
		players = new ArrayList<Player>();
		lastToPlay = null;
		batch = new SpriteBatch();
		
		//background
		background = new Texture(Gdx.files.internal("rustic_background.jpg"));
		cardLanderTexture = new Texture(Gdx.files.internal("cardStackLander.png"));
		titlescreen = new Texture(Gdx.files.internal("titlescreen.jpg"));
		titleFont = new BitmapFont(Gdx.files.internal("titlefont.fnt"));
		
		//cardSpriteSheet
		cardSpriteSheet = new Texture(Gdx.files.internal("sprite_deck.png"));
		cardStack = new Deck(cardSpriteSheet);
		
		//Stage
		startStage = new Stage();
		stage = new Stage();
		endStage = new Stage();
		
		//cardBack 
		cardBackTexture = new Texture(Gdx.files.internal("cardback.png"));
		
		cardBackSprites = new Sprite[numPlayers];
		//sets all the cards initial position to the center of the screen
		for(int i = 0; i < numPlayers; i++){	
			cardBackSprites[i] = new Sprite(cardBackTexture);
			cardBackSprites[i].setPosition((Gdx.graphics.getWidth()-cardBackSprites[i].getWidth())/2, (Gdx.graphics.getHeight()-cardBackSprites[i].getHeight())/2);
		}
		
			
		MAX_HEIGHT = Gdx.graphics.getHeight()-215;
		MIN_HEIGHT = 75;
		MAX_WIDTH = Gdx.graphics.getWidth()-300;
		MIN_WIDTH = 175;
		
		//button style
		Texture buttonTexture = new Texture(Gdx.files.internal("button.png"));
		Texture buttonDownTexture = new Texture(Gdx.files.internal("buttonDown.png"));
		bitmapfont = new BitmapFont();
		skin = new Skin();
		skin.add("default", bitmapfont);
		skin.add("buttonUp", buttonTexture);
		skin.add("buttonDown", buttonDownTexture);
		
		textButtonStyle = new TextButtonStyle();
		textButtonStyle.up = skin.newDrawable("buttonUp");
		textButtonStyle.down = skin.newDrawable("buttonDown");
		textButtonStyle.font = skin.getFont("default");
		
		//TODO, DO WE NEED THIS? NOT SURE HOW ITS IMPLEMENTED ON THE GUI
		sliderStyle = new SliderStyle();

		//create buttons
		playGameButton = getButton("Play Game", (Gdx.graphics.getWidth()-buttonTexture.getWidth())/2, 75, "playGameButton", textButtonStyle);
		playCardButton = getButton("Play Card", (Gdx.graphics.getWidth()-buttonTexture.getWidth())/2, 75, "playCardButton", textButtonStyle);
		resetGameButton = getButton("Reset", (Gdx.graphics.getWidth()-buttonTexture.getWidth())/2, 75, "resetGameButton", textButtonStyle);
		testCardStack = getButton("Test Card Stack", 200, 75, "testCardStack", textButtonStyle);
		
		//create sliders
		//TODO we need to add the slider for number of players, difficulty, and reset
		//Do we need to create a skin for the slider?
		//numOfPlayerSlider = getSlider("Number of Players", (Gdx.graphics.getWidth()-buttonTexture.getWidth())/2, 75, "numOfPlayerSlider", sliderStyle);
		//difficultySlider = getSlider("Level of Difficulty", (Gdx.graphics.getWidth()-buttonTexture.getWidth())/2, 75, "difficultySlider", sliderStyle);
		//resetSlider = getSlider("Reset", (Gdx.graphics.getWidth()-buttonTexture.getWidth())/2, 75, "resetSlider", sliderStyle);

		//add buttons to map
		buttonMap.put("playGameButton", new Runnable(){
			public void run() {
				//method called when the button is clicked
				//the line below was part of a merge conflict
				//deck.add(player1.playCard());
				timer = 0f;
				gamePhase = GamePhases.DEAL;
				playGame();
			}
		});
		buttonMap.put("playCardButton", new Runnable(){
			public void run() {
				//method called when the button is clicked
				
				//players.get(0).playCard();
				if(players.get(0).handSize() != 0){
					cardBackSprites[0].setTexture(cardBackTexture);
					cardStack.add(players.get(0).playCard());
					lastToPlay = players.get(0);
					// now the computer's turn
					playCardButton.setVisible(false);
					whoseTurn = GamePlayTurn.COMPUTER;
				}
				else{
					cardBackSprites[0].setTexture(cardLanderTexture);
					//the game has been lost
					gamePhase = GamePhases.WINNER;
				}
			}
		});
		buttonMap.put("resetGameButton", new Runnable(){
			public void run() {
				//method called when the button is clicked
				resetGame();
			}
		});
		buttonMap.put("testCardStack", new Runnable(){
			public void run() {
				testWhatCardsAreOnTheBoard();
			}
		});
		
		//add actors to stage
		startStage.addActor(playGameButton);
		stage.addActor(playCardButton);
		stage.addActor(testCardStack);
		endStage.addActor(resetGameButton);
		
		//TODO COMPLETE
		//add sliders to map
		/*buttonMap.put("numOfPlayerSlider", new Runnable() {
			public void run(){
				
			}
		});
		buttonMap.put("difficultySlider", new Runnable() {
			public void run(){
				
			}
		});
		buttonMap.put("resetSlider", new Runnable() {
			public void run(){
				
			}
		});*/
		
		//add sliders to stage		
	}

	//Don't delete! Used for drawing on the screen
	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		batch.begin();
		if (gamePhase == GamePhases.TITLE_SCREEN) {
			//can change to alternative background if desired
			batch.draw(titlescreen, -100, -100);
			titleFont.draw(batch, "SLAPJACK", 100, 350);
		}
		else if (gamePhase == GamePhases.DEAL) {
			batch.draw(background, 0, 0);
			batch.draw(cardLanderTexture,(Gdx.graphics.getWidth()-cardLanderTexture.getWidth())/2, (Gdx.graphics.getHeight()-cardLanderTexture.getHeight())/2);
			for (Sprite s : cardBackSprites){
				s.draw(batch);
			}
		} 
		else if (gamePhase == GamePhases.WINNER) {
			//can change to alternative background if desired
			batch.draw(background, 0, 0);
		}
		else {
			batch.draw(background, 0, 0);
			batch.draw(cardLanderTexture,(Gdx.graphics.getWidth()-cardLanderTexture.getWidth())/2, (Gdx.graphics.getHeight()-cardLanderTexture.getHeight())/2);
			for (Sprite s : cardBackSprites){
				s.draw(batch);
			}
			
			if(!cardStack.isEmpty()){
				cardStack.get(cardStack.size()-1).draw(batch);
			}
		}
		batch.end();
		
		
		if(gamePhase == GamePhases.TITLE_SCREEN){
			Gdx.input.setInputProcessor(startStage); 
			startStage.draw();
		}
		
		if(gamePhase == GamePhases.DEAL && timer >= 4.5f){
			stopDealAnimation();
			timer = 0f;
			gamePhase = GamePhases.GAME_PLAY;
		}else if(gamePhase == GamePhases.DEAL){
			dealAnimation(numPlayers);
			timer += Gdx.graphics.getDeltaTime();
		}
		
		if(gamePhase == GamePhases.GAME_PLAY){
			stage.addActor(playCardButton);
			Gdx.input.setInputProcessor(stage); 
			stage.draw();
		
			//always checking to see if a Jack has been played
			checkForJack();
			
			//always checking to see if a slap has happened
			checkForSlap();
			
			//if(whoseTurn == GamePlayTurn.HUMAN){
			//	// manually play their card by clicking on their deck
			//	if(checkForCardPlay()){
			//		cardStack.add(players.get(0).playCard());
			//		lastToPlay = players.get(0);
			//		// now the computer's turn
			//		whoseTurn = GamePlayTurn.COMPUTER;
			//	}
			//} else 
			if (whoseTurn == GamePlayTurn.COMPUTER){
				// computer players play their cards in turn with timer delay
				// timer delay is mostly for the human so they can slap
				for(int i = 1; i < players.size(); i++){
					if(players.get(i).handSize() != 0){
						cardBackSprites[0].setTexture(cardBackTexture);
						waitTimer();
						cardStack.add(players.get(i).playCard());
						lastToPlay = players.get(i);
					}
					else{
						cardBackSprites[i].setTexture(cardLanderTexture); //update the display to show that the player's hand is empty
					}
				}
				// now the human's turn
				playCardButton.setVisible(true);
				whoseTurn = GamePlayTurn.HUMAN;
			}	
		}
		
		if(gamePhase == GamePhases.WINNER){
			Gdx.input.setInputProcessor(endStage); 
			endStage.draw();
		}
	}

	//Don't delete! Used to clean up at the end
	@Override
	public void dispose() {
		batch.dispose();
	}
	
	 /*******************************
	 * Visual Component Methods
	 ********************************/
	//this method is used to create a button
	private TextButton getButton(String buttonText, int xPosition, int yPosition, final String id, TextButtonStyle textButtonStyle) {
		TextButton button = new TextButton(buttonText, textButtonStyle);
		button.setPosition(xPosition, yPosition);
		//this uses the buttonMap to get the correct method to run once a button is clicked
		button.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				buttonMap.get(id).run();
			}
		});
		return button;
	}
	
	//this method is used to create a slider
	//	private Slider getSlider(String sliderText, int xPosition, 
	//			int yPosition, final String id, SliderStyle sliderStyle) {
	//		Slider sliders = new Slider(xPosition, yPosition, 1, false, sliderStyle);
	//		sliders.setPosition(xPosition, yPosition);
	//		sliders.addListener(new ClickListener(){
	//			public void clicked(InputEvent event, float x, float y) {
	//				buttonMap.get(id).run();
	//			}
	//		});
	//		return sliders;
	//	}
	
	 /*******************************
	 * Animation Methods
	 ********************************/
	
	private void stopDealAnimation() {		
		//multiple player animations
		Vector2 targetPositions[] = new Vector2[numPlayers];
		int margin = 1080;
		if(numPlayers == 2){
			margin = ((1080 - cardBackTexture.getWidth()) / 2);
		}
		else if(numPlayers == 3){
			margin = (360 - (cardBackTexture.getWidth() / 2));
		}else {
			margin = ((1080 - (cardBackTexture.getWidth() * (numPlayers-1))) / numPlayers) / 2;
		}
		
		for(int i = 1; i < numPlayers; i++){
			if(numPlayers == 3) {
				targetPositions[i] = new Vector2((i-1) * ((1080 + margin) / (numPlayers - 1)) + margin / 2 , MAX_HEIGHT);
			}
			else {
				targetPositions[i] = new Vector2((i-1) * ((1080 + margin) / (numPlayers - 1)) + margin , MAX_HEIGHT);
			}
			cardBackSprites[i].setPosition(targetPositions[i].x, targetPositions[i].y);
		}		
	}

	private void dealAnimation(int numPlayers) {
		//multiple player animations
		//target positions and movement positions
		Vector2 targetPositions[] = new Vector2[numPlayers];
		Vector2 movementPositions[] = new Vector2[numPlayers];
		int margin = 1080;
		if(numPlayers == 2) {
			margin = ((1080 - cardBackTexture.getWidth()) / 2);
		}
		else if(numPlayers == 3) {
			margin = (360 - (cardBackTexture.getWidth() / 2));
		}
		else {
			margin = ((1080 - (cardBackTexture.getWidth() * (numPlayers-1))) / numPlayers) / 2;
		}
		
		
		//target and movement positions for the human player's card
		targetPositions[0] = new Vector2((Gdx.graphics.getWidth()-cardBackSprites[0].getWidth())/2, (Gdx.graphics.getHeight()-cardBackSprites[0].getHeight())/2);
		movementPositions[0] = new Vector2();
		
		for(int i = 1; i < numPlayers; i++){
			if(numPlayers == 3) {
				targetPositions[i] = new Vector2((i-1) * ((1080 + margin) / (numPlayers - 1)) + margin / 2 , MAX_HEIGHT);
			}
			else {
				targetPositions[i] = new Vector2((i-1) * ((1080 + margin) / (numPlayers - 1)) + margin , MAX_HEIGHT);
			}
			movementPositions[i] = new Vector2();
		}
		
		//velocity
		float xMovement = (100 * Gdx.graphics.getDeltaTime());
		float yMovement = (37 * Gdx.graphics.getDeltaTime());
		float yMovementHuman = (40 * Gdx.graphics.getDeltaTime());
		
		//move the cards
		for(int i = 1; i < numPlayers; i++){
			if(cardBackSprites[i].getX() < targetPositions[i].x){
				movementPositions[i].x = cardBackSprites[i].getX() + xMovement;				
			}
			else if(cardBackSprites[i].getX() > targetPositions[i].x){
				movementPositions[i].x = cardBackSprites[i].getX() - xMovement;
			}
			
			if(Math.abs(cardBackSprites[i].getX() - targetPositions[i].x) < 1 )
			{
				movementPositions[i].x = targetPositions[i].x;
			}
			
			if(cardBackSprites[i].getY() < targetPositions[i].y){
				movementPositions[i].y = cardBackSprites[i].getY() + yMovement;
			}
			cardBackSprites[i].setPosition(movementPositions[i].x, movementPositions[i].y);
		}
		
		// special for the human player's card		
		if(cardBackSprites[0].getY() <= targetPositions[0].y){
			movementPositions[0].y = cardBackSprites[0].getY() - yMovementHuman;

		} else {
			yMovementHuman = 0;
		}
		
		cardBackSprites[0].setPosition((Gdx.graphics.getWidth()-cardBackSprites[0].getWidth())/2, movementPositions[0].y);
	}

	 /*******************************
	 * Game Play Methods
	 ********************************/
	
	//Starts the game
	public void playGame() {
		
		// create the deck
		Deck deck = new Deck(cardSpriteSheet);
		deck.shuffle();
				
		//create the players
		for(int i = 0; i < numPlayers; i++){
			players.add(new Player());
		}
		
		//deal deck out to players
		deck.deal(players);
		
		//test and see what cards each player has - not required for actual game play
		for (Player player : players){
			testWhatDoesPlayerHave(player);
		}
	}

	// run in the render method to see if the most recently played card is a jack 
	public void checkForJack() {
		if(cardStack.size() > 0){
			if(cardStack.get(cardStack.size() - 1).getRank() == "JACK"){
				jackPlayed = true;
			}
		}
	}
	
	//getter and setter for jackPlayed. Helps manage the slap method
	public void setJackPlayed(){
		jackPlayed = true;
		System.out.println("A jack is in the cardStack");
	}
	public static boolean getJackPlayed(){
		return jackPlayed;
	}
	
	// run in the render method to see if a player has slapped 
	private void checkForSlap() {
		// if the mouse click happened over the cardStack pile in the center of the play window
		if(Gdx.input.isTouched()){
			int x1 = Gdx.input.getX();
			int y1 = Gdx.input.getY();
			//Target of the discard pile
			int xMin = (Gdx.graphics.getWidth()-cardBackTexture.getWidth())/2;
			int xMax = (Gdx.graphics.getWidth()+cardBackTexture.getWidth())/2;
			int yMin = (Gdx.graphics.getHeight()-cardBackTexture.getHeight())/2;
			int yMax = (Gdx.graphics.getHeight()+cardBackTexture.getHeight())/2;
			System.out.println("X: "+ x1 + " Y: "+y1);
			
			//within the boundaries
			if(x1 > xMin && x1 < xMax && y1 > yMin && y1 < yMax && cardStack.size() != 0){
				    //get the player who slapped and call their slap method to determine validity
					String topCard = cardStack.get(cardStack.size()-1).getRank();
					if(players.get(0).slap(topCard)){
						//player gets the stack of cards with a correct slap
						System.out.println("Slapped");
						players.get(0).addToHand(cardStack);
						cardStack.clear();
						isWinner();
					} else {
						//gives a card to the last person to play if slap was incorrect
						lastToPlay.addToHand(players.get(0).giveUpCard());
					}
			}
		}
	}
	
	// run in the render method to see if a player has played their card
	private Boolean checkForCardPlay() {
		// if the mouse click happened over the cardStack pile in the center of the play window
		if(Gdx.input.isTouched()){
			int x1 = Gdx.input.getX();
			int y1 = Gdx.input.getY();
			int xMin = (Gdx.graphics.getWidth()-cardBackTexture.getWidth())/2;
			int xMax = (Gdx.graphics.getWidth()+cardBackTexture.getWidth())/2;
			int yMin = ((Gdx.graphics.getHeight()/2)-cardBackTexture.getHeight()+50);
			int yMax = ((Gdx.graphics.getHeight()/2)+cardBackTexture.getHeight()+50);
			
			//within the boundaries
			if(x1 > xMin && x1 < xMax && y1 > yMin && y1 < yMax){
					System.out.println("Player played card");
					return true;
			}
		}
		return false;
	}
	private void isWinner(){
		for (Player player : players) {
			if(player.handSize()==52){
				System.out.println("Someone won");
				gamePhase = GamePhases.WINNER;
			}
//			if(player.handSize()==0){
//				System.out.println(player+"Someone lost");
//			gamePhase = GamePhases.WINNER;
//			}
		}
		
}

		
	private void waitTimer(){
		Random r = new Random();
		float timerPeriod = (float)r.nextInt(5);
		if(timerPeriod <= 2f){
			timerPeriod = 3f;
		}
		timer = 0f;
		while(timer < timerPeriod){
			if( timer > timerPeriod){
				return;
			} else {
				timer += Gdx.graphics.getDeltaTime();
			}
		}
	}
	
	private void resetGame(){
		players = new ArrayList<Player>();
		cardStack = new Deck(cardSpriteSheet);
		
		cardBackSprites = new Sprite[numPlayers];
		//sets all the cards initial position to the center of the screen
		for(int i = 0; i < numPlayers; i++){	
			cardBackSprites[i] = new Sprite(cardBackTexture);
			cardBackSprites[i].setPosition((Gdx.graphics.getWidth()-cardBackSprites[i].getWidth())/2, (Gdx.graphics.getHeight()-cardBackSprites[i].getHeight())/2);
		}
		
		gamePhase = GamePhases.TITLE_SCREEN;
	}
	
	 /*******************************
	 * Testing Methods
	 ********************************/
	
	public void testWhatDoesPlayerHave(Player player) {
		System.out.println(player.toString() + " has the following cards:");
		player.revealHand();
	}
	
	public void testWhatCardsAreOnTheBoard() {
		System.out.println("________________________________________");
		System.out.println("The following cards are on the board:");
		for(Card card : cardStack){
			System.out.println(card.toString());
		}
		System.out.println("________________________________________");
	}
	
}