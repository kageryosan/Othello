package othello;

import static othello.GameManager.*;

public class PlayerManager {

	Player player;
	Player cpu;

	PlayerManager() {
		player = new Player("プレイヤー", PLAYER_PIECE, false);
		cpu = new Player("コンピューター", CPU_PIECE, true);
	}

	void makePlayer(String name) {
		player = new Player(name, PLAYER_PIECE, false);
	}

	void makeCPU(String name) {
		cpu = new Player(name, CPU_PIECE, true);
	}

	Player getPlayer() {
		return player;
	}

	void setPlayer(Player player) {
		this.player = player;
	}

	Player getCpu() {
		return cpu;
	}

	void setCpu(Player cpu) {
		this.cpu = cpu;
	}
}
