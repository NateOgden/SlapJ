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
	
	private SpriteBatch batch;
	private Texture background;	
	private Texture cardSpriteSheet;
	
	//for cardback animations
	private Texture cardBackTexture;
	private Sprite cardBack;
	private Sprite cardBackUL;
	private Sprite cardBackLL;
	private Sprite cardBackUR;
	private Sprite cardBackLR;
	private float timer = 0f;
	private int MAX_HEIGHT;
	private int MIN_HEIGHT; 
	private int MAX_WIDTH; 
	private int MIN_WIDTH;
	private int numPlayers;
	
	
	
	private Stage startStage;
	private Stage stage;
	private Stage endStage;
	
	private Skin skin;
	private BitmapFont bitmapfont;
	private TextButtonStyle textButtonStyle;
	
	private HashMap<String, Runnable> buttonMap = new HashMap<String, Runnable>();
	private TextButton playCardButton;
	
	//Don't delete!
	@Override
	public void create () {	
		
		//environment variables
		numPlayers = 4;
		
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
		cardBack = new Sprite(cardBackTexture);
		cardBack.setPosition((Gdx.graphics.getWidth()-cardBack.getWidth())/2, (Gdx.graphics.getHeight()-cardBack.getHeight())/2);
		cardBackUL = new Sprite(cardBackTexture);
		cardBackUL.setPosition((Gdx.graphics.getWidth()-cardBack.getWidth())/2, (Gdx.graphics.getHeight()-cardBack.getHeight())/2);
		cardBackLL = new Sprite(cardBackTexture);
		cardBackLL.setPosition((Gdx.graphics.getWidth()-cardBack.getWidth())/2, (Gdx.graphics.getHeight()-cardBack.getHeight())/2);
		cardBackUR = new Sprite(cardBackTexture);
		cardBackUR.setPosition((Gdx.graphics.getWidth()-cardBack.getWidth())/2, (Gdx.graphics.getHeight()-cardBack.getHeight())/2);
		cardBackLR = new Sprite(cardBackTexture);
		cardBackLR.setPosition((Gdx.graphics.getWidth()-cardBack.getWidth())/2, (Gdx.graphics.getHeight()-cardBack.getHeight())/2);
		
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
		playCardButton = getButton("Play Game", (Gdx.graphics.getWidth()-110)/2, 75, "playCardButton", textButtonStyle);
		
		//add buttons to map
		buttonMap.put("playCardButton", new Runnable(){
			public void run() {
				//method called when the button is clicked
				//the line below was part of a merge conflict
				//deck.add(player1.playCard());
				gamePhase = GamePhases.DEAL;
				playGame();
			}
		});
		
		//add actors to stage
		stage.addActor(playCardButton);
	}

	//Don't delete! Used for drawing on the screen
	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		batch.begin();
		batch.draw(background, 0, 0);
		//cardBack.draw(batch);
		cardBackUL.draw(batch);
		cardBackLL.draw(batch);
		cardBackUR.draw(batch);
		cardBackLR.draw(batch);
		batch.end();
		Gdx.input.setInputProcessor(stage); 
		stage.draw();
		
		if(gamePhase == GamePhases.TITLE_SCREEN){
			
		} 
		
		if(gamePhase == GamePhases.DEAL && timer >= 4){
			//stopDealAnimation();
			timer = 0f;
			gamePhase = GamePhases.GAME_PLAY;
		}else if(gamePhase == GamePhases.DEAL){
			dealAnimation(numPlayers);
			timer += Gdx.graphics.getDeltaTime();
		}
		
		if(gamePhase == GamePhases.GAME_PLAY){
			
		}
		
		if(gamePhase == GamePhases.WINNER){
			
		}
	}
	
	private void stopDealAnimation() {
		//again, testing with 4 players, needs to be refactored for different amount of players		
		//target positions
		Vector2 targetUpperLeft = new Vector2(MIN_WIDTH,MAX_HEIGHT);
		Vector2 targetLowerLeft = new Vector2(MIN_WIDTH,MIN_HEIGHT);
		Vector2 targetUpperRight = new Vector2(MAX_WIDTH,MAX_HEIGHT);
		Vector2 targetLowerRight = new Vector2(MAX_WIDTH,MIN_HEIGHT);
		
		cardBackUL.setPosition(targetUpperLeft.x, targetUpperLeft.y);
		cardBackLL.setPosition(targetLowerLeft.x, targetLowerLeft.y);
		cardBackUR.setPosition(targetUpperRight.x, targetUpperRight.y);
		cardBackLR.setPosition(targetLowerRight.x, targetLowerRight.y);
		
	}

	private void dealAnimation(int numPlayers) {
		//again, testing with 4 players, needs to be refactored for different amount of players
		//target positions
		Vector2 targetUpperLeft = new Vector2(MIN_WIDTH,MAX_HEIGHT);
		Vector2 targetLowerLeft = new Vector2(MIN_WIDTH,MIN_HEIGHT);
		Vector2 targetUpperRight = new Vector2(MAX_WIDTH,MAX_HEIGHT);
		Vector2 targetLowerRight = new Vector2(MAX_WIDTH,MIN_HEIGHT);
		
		//movement position
		Vector2 movementUpperLeft = new Vector2();
		Vector2 movementLowerLeft = new Vector2();
		Vector2 movementUpperRight = new Vector2();
		Vector2 movementLowerRight = new Vector2();
		
		//velocity
		float xMovement = (65 * Gdx.graphics.getDeltaTime());
		float yMovement = (37 * Gdx.graphics.getDeltaTime());
		
		//For cardBack Upper Left
		if(cardBackUL.getX() > targetUpperLeft.x ){
			movementUpperLeft.x = cardBackUL.getX() - xMovement;
		}
		if(cardBackUL.getY() < targetUpperLeft.y) {
			movementUpperLeft.y = cardBackUL.getY() + yMovement;
		}
		cardBackUL.setPosition(movementUpperLeft.x, movementUpperLeft.y);
		

		//For cardBack Lower Left
		if(cardBackLL.getX() > targetLowerLeft.x){
			movementLowerLeft.x = cardBackLL.getX() - xMovement;
		}
		if(cardBackLL.getY() > targetLowerLeft.y){
			movementLowerLeft.y = cardBackLL.getY() - yMovement;
		}
		cardBackLL.setPosition(movementLowerLeft.x, movementLowerLeft.y);
		
		//For cardBack Upper Right
		if(cardBackUR.getX() < targetUpperRight.x){
			movementUpperRight.x = cardBackUR.getX() + xMovement;
		}
		if(cardBackUR.getY() < targetUpperRight.y){
			movementUpperRight.y = cardBackUR.getY() + yMovement;
		}
		cardBackUR.setPosition(movementUpperRight.x, movementUpperRight.y);
		
		//For cardBack Lower Right
		if(cardBackLR.getX() < targetLowerRight.x) {
			movementLowerRight.x = cardBackLR.getX() + xMovement;
		}
		if(cardBackLR.getY() > targetLowerRight.y){
			movementLowerRight.y = cardBackLR.getY() - yMovement;
		}
		cardBackLR.setPosition(movementLowerRight.x, movementLowerRight.y);
	}

	//Don't delete! Used to clean up at the end
	@Override
	public void dispose() {
		batch.dispose();
	}
	
	public void playGame() {
		startGame();
	}
	
	//Starts the game
	public void startGame() {
		
		// create the deck
		Deck deck = new Deck(cardSpriteSheet);
		deck.shuffle();
		
		//deal deck out to players
		//for now there will be four players
		//can make it more dynamic, more or less players, if we want
		//but for the sake of testing now, I will create four players
		Player player1 = new Player();
		Player player2 = new Player();
		Player player3 = new Player();
		Player player4 = new Player();
		
		//each player gets a hand of cards
		int numPlayers = 4;
		player1.addToHand(deck.deal(numPlayers));
		player2.addToHand(deck.deal(numPlayers));
		player3.addToHand(deck.deal(numPlayers));
		player4.addToHand(deck.deal(numPlayers));	
	
		/*
		testWhatDoesPlayerHave(player1);
		testWhatDoesPlayerHave(player2);
		testWhatDoesPlayerHave(player3);
		testWhatDoesPlayerHave(player4);
		*/
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
}

