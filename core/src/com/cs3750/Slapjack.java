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
	
	private ArrayList<Player> players;
	
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
	private TextButton startGameButton;
	private TextButton resetGameButton;
	
	//Don't delete!
	@Override
	public void create () {	
		
		//environment variables
		numPlayers = 4;
		players = new ArrayList<Player>();
		
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
		
		//create buttons
		//TODO second argument needs dynamic width for the width of the button, right now it is hard coded at 110
		playCardButton = getButton("Play Card", (Gdx.graphics.getWidth()-buttonTexture.getWidth())/2, 75, "playCardButton", textButtonStyle);
		startGameButton = getButton("Start", (Gdx.graphics.getWidth()-buttonTexture.getWidth())/2, 75, "startGameButton", textButtonStyle);
		resetGameButton = getButton("Reset", (Gdx.graphics.getWidth()-buttonTexture.getWidth())/2, 75, "resetGameButton", textButtonStyle);
		
		//add buttons to map
		buttonMap.put("playCardButton", new Runnable(){
			public void run() {
				//method called when the button is clicked
				//the line below was part of a merge conflict
				//deck.add(player1.playCard());
				//gamePhase = GamePhases.DEAL;
				//playGame();
			}
		});
		buttonMap.put("startGameButton", new Runnable(){
			public void run() {
				//method called when the button is clicked
				//the line below was part of a merge conflict
				//deck.add(player1.playCard());
				gamePhase = GamePhases.DEAL;
				playGame();
			}
		});
		buttonMap.put("resetGameButton", new Runnable(){
			public void run() {
				//method called when the button is clicked
			}
		});
		
		//add actors to stage
		startStage.addActor(startGameButton);
		stage.addActor(playCardButton);
		endStage.addActor(resetGameButton);
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
		
		if(gamePhase == GamePhases.TITLE_SCREEN){
			Gdx.input.setInputProcessor(startStage); 
			startStage.draw();
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
			Gdx.input.setInputProcessor(stage); 
			stage.draw();
		}
		
		if(gamePhase == GamePhases.WINNER){
			Gdx.input.setInputProcessor(endStage); 
			endStage.draw();
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
		for(int i = 0; i < numPlayers; i++){
			players.add(new Player());
		}
			
		
		for(Player player : players){
			player.addToHand(deck.deal(numPlayers));;
		}
	
		//for testing 
		for(Player player : players){
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
}

