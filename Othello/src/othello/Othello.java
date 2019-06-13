package othello;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;

/**
 * このプログラムを実行すると、コンソールでオセロゲームが出来ます。
 * @author R.Kageyama
 */
public class Othello {

	public static void main(String[] args) {

		// 先手/後手 をランダムで決める。
		boolean isPlayerFirst = new Random().nextBoolean();

		// Gameマネージャーの生成
		GameManager gm = new GameManager(isPlayerFirst);

		// オセロゲームの縦横のマス数の入力要求と取得
		int boardSquareCount = gm.requestEntryForSquareCount();

		// 自動進行モードの有効/無効
		boolean enableAutoMode = gm.requestEntryForAutoMode();

		// 盤面オブジェクトの取得
		Board board = gm.createBoard(boardSquareCount);

		// オセロゲーム開始
		gm.printStartMessage();

		// 盤面の初期化
		board.init();

		// 盤面の表示
		board.displayInfo();

		// オセロゲームの開始
		gm.startGame(board, enableAutoMode);

		// オセロゲームの終了
		gm.endGame(board);
	}

}
