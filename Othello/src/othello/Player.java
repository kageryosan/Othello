package othello;

public class Player {
	private String name;
	private Piece piece;
	private boolean autoPlay;

	public Player(String name, Piece piece, boolean autoPlay) {
		this.name = name;
		this.piece = piece;
		this.autoPlay = autoPlay;
	}

	String getName() {
		return name;
	}

	void setName(String name) {
		this.name = name;
	}

	Piece getPiece() {
		return piece;
	}

	void setPiece(Piece piece) {
		this.piece = piece;
	}

	boolean getAutoPlay() {
		return autoPlay;
	}

	void setAutoPlay(boolean autoPlay) {
		this.autoPlay = autoPlay;
	}

}
