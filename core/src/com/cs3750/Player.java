package com.cs3750;

import java.util.ArrayList;
import java.util.List;

public class Player {
	private static final int NUMBER_OF_STACKS = 1;
	private boolean canSlapIn;
	private ArrayList<Card> hand; 
	
	public Player(){
		hand = new ArrayList<Card>();
		canSlapIn = true;
	}
	
	public int handSize(){
		return hand.size();
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
		
	public boolean slap(String rank) {	
		if(Slapjack.getJackPlayed() && rank != "JACK" && hand.isEmpty())
			canSlapIn = false;
		return canSlapIn;
	}
	
	private ArrayList<Card> giveUpCard(Card card){
		ArrayList<Card> temp = new ArrayList<Card>();
		if(card.getRank() != "JACK") {
			if(!hand.isEmpty()) {
				temp.add(hand.get(0));
				hand.remove(0);
			}
		}
		return temp;
	}
	

	public Card playCard(){
		Card temp = null;
		if(!hand.isEmpty()) {
			temp = hand.get(0);
			hand.remove(0);
		} 
		return temp;
	}
	
	/*
	private void winGame() {
	}*/
}
