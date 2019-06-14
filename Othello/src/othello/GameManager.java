package othello;

import static othello.ConsolePrinter.*;
import static othello.Utils.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

public class GameManager {

	// プレイヤー群
	final Player[] players;
	// 使用する駒
	static Piece PLAYER_PIECE = null;
	static Piece CPU_PIECE = null;

	// 入力要求用インスタンス
	BufferedReader reader;
	// 縦×横のマス数
	private int squareCount;
	// 自動進行モードの有効/無効
	private static boolean enableAutoMode;

	GameManager(boolean playerIsFirst) {
		// 先手/後手の設定
		if (playerIsFirst) {
			PLAYER_PIECE = Piece.BLACK;
			CPU_PIECE = Piece.WHITE;
		} else {
			PLAYER_PIECE = Piece.WHITE;
			CPU_PIECE = Piece.BLACK;
		}
		// プレイヤー情報の取得
		players = getPlayers();
		// 入力要求用インスタンスの取得
		reader = new BufferedReader(new InputStreamReader(System.in));
	}

	/**
	 * 盤面オブジェクトの作成処理
	 * @param squareCount 縦と横のマス数：4以上の偶数（奇数の場合は-1されます）
	 * @return 盤面オブジェクト
	 */
	Board createBoard(int squareCount) {
		if (squareCount < 4) {
			throw new IllegalArgumentException("マス目は４以上を指定してください。");
		}
		if (squareCount % 2 == 1) {
			printAlert(String.format("マスは偶数である必要があるため、%sマスで盤面を作成します。", --squareCount));
		}
		this.squareCount = squareCount;
		return new Board(this.squareCount);
	}

	/**
	 * 縦横のマス数の入力を要求します。
	 * @return 入力されたマス目
	 */
	int requestEntryForSquareCount() {
		int squareCount = 0;
		while (true) {
			String input = requestEntry(reader, "縦横いくつのオセロゲームをプレイしますか？（4から50の間)"
					+ "\n※未入力の場合はデフォルトで開始されます。");
			if (input.isEmpty()) {
				// オセロは８×８が標準ルール
				int defaultCount = 8;
				printInfo(String.format("省略されましたので、%dマスで開始します。", defaultCount));
				return defaultCount;
			}
			if (!checkValid(input)) {
				// 有効な値が入力されていないので、繰り返す
				printAlert("有効な数値を入力してください。");
				continue;
			}
			squareCount = Integer.parseInt(input);
			if (squareCount > 50 || squareCount < 4) {
				printAlert("4以上且つ50以下の数値を入力してください。");
				continue;
			}
			break;
		}
		return squareCount;
	}

	/**
	 * 自動進行モードを有効化を問う処理
	 * @return 有効化する場合はtrue
	 */
	boolean requestEntryForAutoMode() {
		// 全角と半角双方を許容する。
		List<String> agree = Arrays.asList(new String[] { "y", "ｙ" });
		String input = requestEntry(reader, "自動進行モードを有効にする場合はyを入力してください。");
		return agree.contains(input);
	}

	/**
	 * ゲーム開始時のメッセージ出力処理
	 */
	void printStartMessage() {
		String number = convertCircleNumber(squareCount);
		print("*****************************************************************");
		StringBuffer startMessage = new StringBuffer()
				.append(number + "×" + number + "のオセロゲームを開始します。")
				.append("あなたは" + PLAYER_PIECE.getState() + "番です。");
		print(startMessage.toString());
		print("*****************************************************************");
		sleep(2000);
	}

	/**
	 * ゲーム開始処理
	 * @param board 初期化済みの盤面オブジェクト
	 * @param autoMode 自動モードの有効/無効
	 */
	void startGame(Board board, boolean autoMode) {

		// 自動進行モードの初期化
		ConsolePrinter.autoMode = autoMode;
		enableAutoMode = autoMode;

		// 両方ともパスだった場合はゲームを終了するためのフラグ。
		boolean passFlg = false;

		// 先手は黒番である為、初期値で調整する。
		int zeroOrOne = PLAYER_PIECE.is(Piece.WHITE) ? 0 : 1;

		// ゲームが終了する -> break 制御とする。
		while (true) {
			int columnNumber;
			int rowNumber;

			// Loopごとに0 -> 1 -> 0 -> 1... のようになる。(XOR)
			zeroOrOne = zeroOrOne ^ 1;
			Player player = players[zeroOrOne];

			boolean needsSkip = board.needsSkip(player);
			if (passFlg && needsSkip) {
				// プレイヤー双方がパスだった場合はゲームを終了する。
				print("【ゲーム終了】置けるところがないので、ゲームを終了します。");
				break;
			} else if (passFlg = needsSkip) {
				// 置けるところがない場合はSKIP(elseが正常処理）
				print(String.format("【%s：パス】置けるところがないので、順番をスキップします。", player.getName()));
			} else {
				// 正常処理
				Piece piece = player.getPiece();
				while (true) {
					if (enableAutoMode || player.getAutoPlay()) {
						// 列と行の乱数取得
						columnNumber = getRandomNum(squareCount);
						rowNumber = getRandomNum(squareCount);
					} else {
						// 列と行の入力要求
						columnNumber = getInputColumnNumber(reader);
						rowNumber = getInputRowNumber(reader);
					}

					// 全角文字などを複数回入力した場合などの考慮
					if (columnNumber >= squareCount || rowNumber >= squareCount) {
						printAlert("入力数値が不正のため、もう一度列と行番号を指定してください。");
						continue;
					}

					// 指定箇所が空いているかチェック
					if (!board.squareIsEmpty(columnNumber, rowNumber)) {
						var notEmptyMessage = String.format("「%d列の%d行目」は%sではないので、配置できません。",
								columnNumber, rowNumber, Piece.EMPTY.getState());
						if (!player.getAutoPlay()) {
							printAlert(notEmptyMessage);
						}
						continue;
					}

					// 該当箇所が配置可かチェック
					if (!board.isSelectable(columnNumber, rowNumber, piece)) {
						var notSelectableMessage = String.format("「%d列の%d行目」は、配置できません。",
								columnNumber, rowNumber);
						if (!player.getAutoPlay()) {
							printAlert(notSelectableMessage);
						}
						continue;
					}
					break;
				}
				Piece tmpPiece = player.getPiece();
				// 配置する
				board.putPiece(columnNumber, rowNumber, tmpPiece);
				// ひっくり返す
				board.turnOver(columnNumber, rowNumber, tmpPiece);
				if (player.getAutoPlay()) {
					printInfo(String.format("[%s]が[%s列の%s行]に%sを配置しました。",
							player.getName(), columnNumber, rowNumber, tmpPiece.getDisp()));
				}
				// 盤面を表示する
				board.displayInfo();
			}
			// 間をあける
			sleep(1000);
			// 終了判定
			if (board.isFull()) {
				print("【ゲーム終了】ゲームを終了します。");
				break;
			}
		}
	}

	/**
	 * ゲーム終了処理
	 * @param board
	 */
	void endGame(Board board) {
		// 勝者を出力する
		printWinner(board);
	}

	/**
	 * 列番号を要求し、入力された数値を取得する。
	 * @param reader
	 * @return 入力された列番号
	 */
	int getInputColumnNumber(BufferedReader reader) {
		return getInputNumber(reader, String.format("配置したい列番号（0~%s）を入力してください。", squareCount));
	}

	/**
	 * 行番号を要求し、入力された数値を取得する。
	 * @param reader
	 * @return 入力された行番号
	 */
	int getInputRowNumber(BufferedReader reader) {
		return getInputNumber(reader, String.format("配置したい行番号（0~%s）を入力してください。", squareCount));
	}

	private int getInputNumber(BufferedReader reader, String msg) {
		int inputValue;
		while (true) {
			String input = requestEntry(reader, msg);
			if (!checkValid(input, squareCount - 1)) {
				// 有効な値が入力されていないので、繰り返す
				printAlert(String.format("有効な値（0～%s）を入力してください。", squareCount - 1));
				continue;
			}
			inputValue = Integer.parseInt(input);
			break;
		}
		return inputValue;
	}

	/**
	 * 有効値（50以下の数値）かどうか
	 * @param number 入力値
	 * @return 0以上、50以下の数値ならtrue
	 */
	private boolean checkValid(String number) {
		return checkVaidNumber(number, 4, 50);
	}

	/**
	 * 有効値かどうか
	 * @param number 数値
	 * @param end 0以上且つ、end以下の値なら有効な範囲終了値
	 * @return 範囲内ならtrue
	 */
	private boolean checkValid(String number, int end) {
		return checkVaidNumber(number, 0, end);
	}

	/**
	 * プレイヤー情報の取得
	 * @return [0] プレイヤー情報、[1] CPU情報
	 */
	private static Player[] getPlayers() {
		PlayerManager pm = new PlayerManager();
		return new Player[] { pm.getPlayer(), pm.getCpu() };
	}

	/**
	 * 渡された盤面から、駒数を取得し、勝者を判断し、結果と併せて出力する。
	 * @param board 盤面オブジェクト
	 */
	void printWinner(Board board) {

		int playerScore = 0;
		int cpuScore = 0;

		for (Piece[] pieces : board.getBoard()) {
			for (Piece piece : pieces) {
				if (piece.is(PLAYER_PIECE)) {
					playerScore++;
				} else if (piece.is(CPU_PIECE)) {
					cpuScore++;
				}
			}
		}
		print(String.format("[%s：%s][%s枚]", players[0].getName(), PLAYER_PIECE.getState(), playerScore));
		print(String.format("[%s：%s][%s枚]", players[1].getName(), CPU_PIECE.getState(), cpuScore));
		if (playerScore == cpuScore) {
			ConsolePrinter.changeTextColorToBlue(() -> print("引き分けです。"));
			return;
		}

		String winner = playerScore > cpuScore ? players[0].getName() : players[1].getName();
		ConsolePrinter.changeTextColorToRed(() -> print(String.format("[%s の勝利！]", winner)));
	}

	/**
	 * スレッド処理を一時的に休止する処理
	 */
	private static void sleep(int milliSec) {
		int stopMilliSec = milliSec;
		if (enableAutoMode) {
			// 自動進行モードが有効の場合は間の秒数を短くする。
			stopMilliSec = 500;
		}
		try {
			Thread.sleep(stopMilliSec);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
