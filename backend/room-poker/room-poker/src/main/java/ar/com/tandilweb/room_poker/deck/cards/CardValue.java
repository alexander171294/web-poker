package ar.com.tandilweb.room_poker.deck.cards;

public enum CardValue {

	SMALL_ACE(1),
	TWO(2),
	THREE(3),
	FOUR(4),
	FIVE(5),
	SIX(6),
	SEVEN(7),
	EIGHT(8),
	NINE(9),
	TEEN(10),
	JACK(11),
	QUEEN(12),
	KING(13),
	ACE(14);
	
	private int value;
	
	CardValue(int value) {
		this.value = value;
	}
	
	public int getNumericValue() {
		return this.value;
	}
	
//	public int getNumericValueBigACE() {
//		return this.value == 0 ? 14 : this.value; 
//	}
	
}
