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
	
	ArrayList<Card> deck = new ArrayList<Card>();
	private static int DECK_SIZE = 52;
	
	private final Sprite[] cardSprites;
	
	public Deck(Texture cardTexture) {
		cardSprites = new Sprite[52];
		TextureRegion[][] tmp = TextureRegion.split(cardTexture, cardTexture.getWidth()/13, cardTexture.getHeight()/4);
		for (int i = 0; i < 4; i++){
			for (int j = 0; j < 13; j++){
				Sprite sprite = new Sprite(tmp[i][j], 1, 1, cardTexture.getWidth()/13-1, cardTexture.getHeight()/4-1);
				Card card = new Card(i, j, sprite);
				deck.add(card);	
			}
		}
		
	}

	public void shuffle(){
		long seed = System.nanoTime();
		Collections.shuffle(deck, new Random(seed));
	}
	
	public ArrayList<Card> deal(int numPlayers) {
		//for testing there are four players, giving each player 13 cards
		int dealOut = DECK_SIZE/numPlayers;
		ArrayList<Card> playerHand = new ArrayList<Card>();
		for(int i=0; i < dealOut; i++) {
			playerHand.add(deck.get(0));
			deck.remove(0);			//this bumps what was in [1] into [0], so it will always pull from the top of the deck
		}
		return playerHand;
	}
	
	public Card getTopCard(){
		return deck.remove(0);
	}
	
	public void addAll(Collection <? extends Card> collection){
		deck.addAll(collection);
	}	
}