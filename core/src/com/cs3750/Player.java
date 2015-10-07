package com.cs3750;

import java.util.ArrayList;

public class Player {
	private static final int NUMBER_OF_STACKS = 1;
	private boolean canSlapIn;
	private ArrayList<Card> hand; 
	
	public Player(){
		hand = new ArrayList<Card>();
		canSlapIn = true;
	}
	
	public void addToHand(Card card) {
		hand.add(card);
	}
	
	public void addToHand(ArrayList<Card> stack) {
		for (Card card : stack) {
			addToHand(card);
		}
	}
	
	public void revealHand() {
		for (Card card : hand) {
			String rank = card.getRank().toString();
			String suit = card.getSuit().toString();
			System.out.println(rank + " of " + suit);
		}
	}
		
	private void slap(Card card) {
		if(canSlapIn /*&& card is Jack*/)
		{
			//player.slap();
		}
	}
	

	
	private void giveUpCard(Card card){
		
	}
	
	private void playCard(Card card){
		
	}
	
	private void winGame() {
		
	}
}
