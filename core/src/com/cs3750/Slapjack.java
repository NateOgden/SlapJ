package com.cs3750;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
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
	private SpriteBatch batch;
	private Texture background;	
	private Texture cardSpriteSheet;
	private Stage stage;
	private Deck deck;
	
	
	//Don't delete!
	@Override
	public void create () {	
		batch = new SpriteBatch();
		background = new Texture(Gdx.files.internal("rustic_background.jpg"));
		cardSpriteSheet = new Texture(Gdx.files.internal("sprite_deck.png"));
		deck = new Deck(cardSpriteSheet);
		stage = new Stage();
	}
	//Don't delete! Used for drawing on the screen
	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		batch.begin();
		batch.draw(background, 0, 0);
		batch.end();
		Gdx.input.setInputProcessor(stage); 
		stage.draw();
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
		
		
		
	}
}
