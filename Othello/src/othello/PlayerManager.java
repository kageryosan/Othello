package othello;

import static othello.GameManager.*;

public class PlayerManager {

	Player player;
	Player cpu;

	PlayerManager() {
		player = makePlayer("プレイヤー");
		cpu = makeCPU("コンピューター");
	}

	Player makePlayer(String name) {
		player = new Player(name, PLAYER_PIECE, false);
		return player;
	}

	Player makeCPU(String name) {
		Player cpu = new Player(name, CPU_PIECE, true);
		return cpu;
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
