package com.cs3750;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Deck {
	List<Card> cards = new ArrayList<Card>();
	ArrayList<Card> playerHand = new ArrayList<Card>();
	
	public Deck(Texture cardSpriteSheet){
		TextureRegion[][] cardTextureRegion = TextureRegion.split(cardSpriteSheet, cardSpriteSheet.getWidth()/13, cardSpriteSheet.getHeight()/4);
		
		for (int i = 0; i < 4; i++){
			for (int j = 0; j < 13; j++){
				Sprite sprite = new Sprite(cardTextureRegion[i][j], 0, 0, cardSpriteSheet.getWidth()/13, cardSpriteSheet.getHeight()/4);
				Card card = new Card(i, sprite);
				cards.add(card);
			}
		}
	}

	public void shuffle(){
		long seed = System.nanoTime();
		Collections.shuffle(cards, new Random(seed));
	}
	
	public Card getTopCard(){
		return cards.remove(0);
	}
	
	public void addAll(Collection <? extends Card> collection){
		cards.addAll(collection);
	}	
		
	public ArrayList<Card> dealCardsToPlayers(int numOfPlayers){
		
		for(int i = 1; i < numOfPlayers + 1; i++){

		}
		return playerHand;
	} 
}