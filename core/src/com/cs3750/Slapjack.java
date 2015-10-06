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
	private Deck deck;
	
	private Stage startStage;
	private Stage stage;
	private Stage endStage;
	
	private Skin skin;
	private BitmapFont bitmapfont;
	private TextButtonStyle textButtonStyle;
	
	private HashMap<String, Runnable> buttonMap = new HashMap<String, Runnable>();
	private TextButton playCardButton;
	
	//Don't delete create or render
	@Override
	public void create () {
		batch = new SpriteBatch();
		background = new Texture("rustic_background.jpg");
		cardSpriteSheet = new Texture(Gdx.files.internal("sprite_deck.png"));
		deck = new Deck(cardSpriteSheet);
		
		startStage = new Stage();
		stage = new Stage();
		endStage = new Stage();
		
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
		playCardButton = getButton("Play Card", (Gdx.graphics.getWidth())/2, 75, "playCardButton", textButtonStyle);
		
		//add buttons to map
		buttonMap.put("playCardButton", new Runnable(){
			public void run() {
				//method called when the button is clicked
			}
		});
		
		//add actors to stage
		stage.addActor(playCardButton);
	}
	
	//Don't delete create or render
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