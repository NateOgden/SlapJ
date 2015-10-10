package com.cs3750;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Deck {

	private ArrayList<Card> deck = new ArrayList<Card>();
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

	public void deal(ArrayList<Player> players) {
		while (deck.size() != 0){
			for(Player player : players){
				if(deck.size() != 0){
					player.addToHand(getTopCard());
				} else {
					break;
				}
			}
		}
	}


	public Card getTopCard(){
		return deck.remove(0);
	}

	public void addAll(Collection <? extends Card> collection){
		deck.addAll(collection);
	}	
}