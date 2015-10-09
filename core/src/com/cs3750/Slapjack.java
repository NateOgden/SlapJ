package com.cs3750;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class Slapjack extends ApplicationAdapter {
	public enum GamePhases{ TITLE_SCREEN, DEAL, GAME_PLAY, WINNER }
	private GamePhases gamePhase = GamePhases.TITLE_SCREEN;
	
	//for dynamic amount of players 
	private ArrayList<Player> players;
	private ArrayList<Card> cardStack;

	private SpriteBatch batch;
	private Texture background;	
	private Texture cardSpriteSheet;
	
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
	private List<Card> cardsPlayed;
	
	private Stage startStage;
	private Stage stage;
	private Stage endStage;
	
	private Skin skin;
	private BitmapFont bitmapfont;
	private TextButtonStyle textButtonStyle;
	
	private HashMap<String, Runnable> buttonMap = new HashMap<String, Runnable>();
	private TextButton playGameButton;
	
	//Don't delete!
	@Override
	public void create () {	
		
		//environment variables
		numPlayers = 7;
		players = new ArrayList<Player>();
		cardStack = new ArrayList<Card>();
		
		batch = new SpriteBatch();
		
		//background
		background = new Texture(Gdx.files.internal("rustic_background.jpg"));
		
		//cardSpriteSheet
		cardSpriteSheet = new Texture(Gdx.files.internal("sprite_deck.png"));

		//Stage
		startStage = new Stage();
		stage = new Stage();
		endStage = new Stage();
		
		//cardBack 
		cardBackTexture = new Texture(Gdx.files.internal("cardback.png"));
		
		cardBackSprites = new Sprite[numPlayers];
		for(int i = 0; i < numPlayers; i++){
			cardBackSprites[i] = new Sprite(cardBackTexture);
			cardBackSprites[i].setPosition((Gdx.graphics.getWidth()-cardBackSprites[i].getWidth())/2, (Gdx.graphics.getHeight()-cardBackSprites[i].getHeight())/2);
		}
			
		MAX_HEIGHT = Gdx.graphics.getHeight()-215;
		MIN_HEIGHT = 75;
		MAX_WIDTH = Gdx.graphics.getWidth()-300;
		MIN_WIDTH = 175;
		
		//button style
		bitmapfont = new BitmapFont();
		skin = new Skin();
		skin.add("default", bitmapfont);
		skin.add("buttonUp", new Texture(Gdx.files.internal("button.png")));
		skin.add("buttonDown", new Texture(Gdx.files.internal("buttonDown.png")));
		
		textButtonStyle = new TextButtonStyle();
		textButtonStyle.up = skin.newDrawable("buttonUp");
		textButtonStyle.down = skin.newDrawable("buttonDown");
		textButtonStyle.font = skin.getFont("default");
		
		//create buttons
		//TODO second argument needs dynamic width for the width of the button, right now it is hard coded at 110
		playGameButton = getButton("Play Game", (Gdx.graphics.getWidth()-110)/2, 75, "playGameButton", textButtonStyle);
		
		//add buttons to map
		buttonMap.put("playGameButton", new Runnable(){
			public void run() {
				//method called when the button is clicked
				//the line below was part of a merge conflict
				//deck.add(player1.playCard());
				gamePhase = GamePhases.DEAL;
				playGame();
			}
		});
		
		//add actors to stage
		stage.addActor(playGameButton);
	}

	//Don't delete! Used for drawing on the screen
	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		batch.begin();
		batch.draw(background, 0, 0);
		for(Sprite s: cardBackSprites){
			s.draw(batch);
		}
		batch.end();
		Gdx.input.setInputProcessor(stage); 
		stage.draw();
		
		if(gamePhase == GamePhases.TITLE_SCREEN){
			
		} 
		
		if(gamePhase == GamePhases.DEAL && timer >= 4.5){
			stopDealAnimation();
			timer = 0f;
			gamePhase = GamePhases.GAME_PLAY;
		}else if(gamePhase == GamePhases.DEAL){
			dealAnimation(numPlayers);
			timer += Gdx.graphics.getDeltaTime();
		}
		
		if(gamePhase == GamePhases.GAME_PLAY){
			if(cardsPlayed.size() > 0)
			{
				Card currCard = cardsPlayed.get(cardsPlayed.size() - 1);
				if(currCard.getRank() == "JACK"){
					jackPlayed = true;
				}
			}
		}
		
		if(gamePhase == GamePhases.WINNER){
			
		}
	}
	
	private void stopDealAnimation() {		
		//multiple player animations
		Vector2 targetPositions[] = new Vector2[numPlayers];
		int margin = ((1080 - (cardBackTexture.getWidth() * (numPlayers-1))) / numPlayers) / 2;
		
		for(int i = 1; i < numPlayers; i++){
			targetPositions[i] = new Vector2((i-1) * ((1080 + margin) / (numPlayers - 1)) + margin , MAX_HEIGHT);
			cardBackSprites[i].setPosition(targetPositions[i].x, targetPositions[i].y);
		}
	}

	private void dealAnimation(int numPlayers) {
		//multiple player animations
		//target positions and movement positions
		Vector2 targetPositions[] = new Vector2[numPlayers];
		Vector2 movementPositions[] = new Vector2[numPlayers];
		int margin = ((1080 - (cardBackTexture.getWidth() * (numPlayers-1))) / numPlayers) / 2;
		
		for(int i = 1; i < numPlayers; i++){
			targetPositions[i] = new Vector2((i-1) * ((1080 + margin) / (numPlayers - 1)) + margin , MAX_HEIGHT);
			movementPositions[i] = new Vector2();
		}
		
		//velocity
		float xMovement = (100 * Gdx.graphics.getDeltaTime());
		float yMovement = (37 * Gdx.graphics.getDeltaTime());
		
		//move the cards
		for(int i = 1; i < numPlayers; i++){
			if(cardBackSprites[i].getX() < targetPositions[i].x){
				movementPositions[i].x = cardBackSprites[i].getX() + xMovement;
			}
			else if(cardBackSprites[i].getX() > targetPositions[i].x){
				movementPositions[i].x = cardBackSprites[i].getX() - xMovement;
			}
			
			if(cardBackSprites[i].getY() < targetPositions[i].y){
				movementPositions[i].y = cardBackSprites[i].getY() + yMovement;
			}
			cardBackSprites[i].setPosition(movementPositions[i].x, movementPositions[i].y);
		}
	}

	//Don't delete! Used to clean up at the end
	@Override
	public void dispose() {
		batch.dispose();
	}
	
	//Starts the game
	public void playGame() {
		
		// create the deck
		Deck deck = new Deck(cardSpriteSheet);
		deck.shuffle();
				
		//deal deck out to players
		
		for(int i = 0; i < numPlayers; i++){
			players.add(new Player());
		}
		
		deck.deal(players);
		
		for (Player player : players){
			testWhatDoesPlayerHave(player);
		}
	}
	
	public void testWhatDoesPlayerHave(Player player) {
		System.out.println(player.toString() + " has the following cards:");
		player.revealHand();
	}
	
	//this method is used to create a button
	private TextButton getButton(String buttonText, int xPosition,
			int yPosition, final String id, TextButtonStyle textButtonStyle) {
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

	
	//Getter/Setter to see if a jack has been played. Helps manage the slap method.
	public static boolean isJackPlayed() {
		return jackPlayed;
	}
	
}



