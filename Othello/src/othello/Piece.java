package othello;

public enum Piece {

	BLACK("黒", "●"), WHITE("白", "○"), EMPTY("空", "　");

	private String state;
	private String disp;

	private Piece(String state, String disp) {
		this.state = state;
		this.disp = disp;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getDisp() {
		return disp;
	}

	public void setDisp(String disp) {
		this.disp = disp;
	}

	public boolean isEmpty() {
		return this.name().equals(EMPTY.name());
	}

	public boolean is(Piece piece) {
		return this.equals(piece);
	}
}
