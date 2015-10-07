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
	private Texture cardBackTexture;
	private Sprite cardBack;
	
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
		cardBack.draw(batch);
		batch.end();
		Gdx.input.setInputProcessor(stage); 
		stage.draw();
		
		if(gamePhase == GamePhases.TITLE_SCREEN){
			
			gamePhase = GamePhases.DEAL;
		} 
		else if(gamePhase == GamePhases.DEAL){
			
		}
		else if(gamePhase == GamePhases.GAME_PLAY){
			
		}
		else if(gamePhase == GamePhases.WINNER){
			
		}
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

