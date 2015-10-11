package com.cs3750;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Card {
	public enum Suit { HEARTS, SPADES, DIAMONDS, CLUBS }
	
	public enum Rank {ACE, DEUCE, THREE, FOUR, FIVE, SIX, SEVEN, 
		EIGHT, NINE, TEN, JACK, QUEEN, KING 
	}
	
	private Suit suit;
	private Rank rank;
	private Sprite sprite;
	public static final int WIDTH = 110;

	public Card(int suit, int rank, Sprite s) {
		this.sprite = s;
		this.suit = Suit.values()[suit];
		this.rank = Rank.values()[rank];
		this.sprite.setBounds(0, 0, 110, 160);  //change the initial position and size of the cards
	}
	
	public void draw(SpriteBatch batch) {
		sprite.setPosition((Gdx.graphics.getWidth() - sprite.getWidth()) / 2, (Gdx.graphics.getHeight() - sprite.getHeight()) / 2);
		sprite.draw(batch);
	}
	
	public void SetPosition(float xPos, float yPos) {
		sprite.setPosition(xPos, yPos);
	}
	
	public float getX() {
		return sprite.getX();
	}
	
	public float getY() {
		return sprite.getY();
	}
	
	public String getSuit() {
		return suit.toString();
	}
	
	public String getRank() {
		return rank.toString();
	}
	
	public String toString(){
		return rank + " of " + suit;
	}
}