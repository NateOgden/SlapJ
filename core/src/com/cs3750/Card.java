package com.cs3750;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Card {
	private enum Suit { HEARTS, SPADES, DIAMONDS, CLUBS }
	private Suit suit;
	
	private Sprite sprite;
	public static final int WIDTH = 110;

	public Card(int suit, Sprite s){
		this.suit = Suit.values()[suit];
		
		this.sprite = s;
		this.sprite.setBounds(0, 0, 110, 160);  //change the initial position and size of the cards
	}
	
	public void draw(SpriteBatch batch){
		sprite.draw(batch);
	}
	
	public void SetPosition(int xPos, int yPos){
		sprite.setPosition(xPos, yPos);
	}
	
	public int getWidth(){
		return (int)sprite.getWidth();
	}
	
	public String getSuit(){
		return suit.toString();
	}
}