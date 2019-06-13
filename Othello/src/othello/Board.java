package othello;

import static othello.GameManager.*;
import static othello.Utils.*;

import java.util.StringJoiner;

public class Board {

	// 盤面
	private Piece[][] board;
	// 列と行のマス数
	private int squareCount;

	/**
	 * 渡された引数の正方形の盤面を作成します。
	 */
	Board(int squareCount) {
		this.squareCount = squareCount;

		this.board = new Piece[squareCount][squareCount];
		for (Piece[] row : board) {
			for (int index = 0; index < row.length; index++) {
				row[index] = Piece.EMPTY;
			}
		}
	}

	/**
	 * 盤面の初期化処理
	 */
	void init() {
		// 予め中央に配置しておく。
		int centerSquare = (squareCount / 2) - 1;
		board[centerSquare][centerSquare] = CPU_PIECE;
		board[centerSquare + 1][centerSquare + 1] = CPU_PIECE;
		board[centerSquare + 1][centerSquare] = PLAYER_PIECE;
		board[centerSquare][centerSquare + 1] = PLAYER_PIECE;
	}

	/**
	 * 盤面の情報を表示（出力）する
	 */
	void displayInfo() {
		// 位置調整の兼ね合いでハイフンを2つ入れておく。
		String border = "  --";
		for (int i = 0; i < squareCount; i++) {
			border += "-----";
		}
		border += "---";

		String verticalLine = "｜";

		print(border);
		{
			StringJoiner joiner = new StringJoiner(verticalLine, verticalLine, verticalLine);
			joiner.add("　");
			for (int i = 0; i < squareCount; i++) {
				joiner.add(convertCircleNumber(i));
			}
			print(joiner.toString());
		}
		{
			int count = 0;
			for (Piece[] pieces : board) {
				StringJoiner joiner = new StringJoiner(verticalLine, verticalLine, verticalLine);
				joiner.add(convertCircleNumber(count++));
				for (Piece piece : pieces) {
					joiner.add(piece.getDisp());
				}
				print(joiner.toString());
			}
		}
		print(border);
	}

	/**
	 * 盤面で指定したマス目が何も配置されていないか
	 *
	 * @param column 列のindex値
	 * @param row 行のindex値
	 * @return 何も配置されていないならtrue
	 */
	boolean squareIsEmpty(int column, int row) {
		return this.board[row][column].isEmpty();
	}

	/**
	 * 選択可能なマスかどうか
	 * @param column 列のindex値
	 * @param row 行のindex値
	 * @param piece 駒
	 * @return 選択できるマスならtrue
	 */
	boolean isSelectable(int column, int row, Piece piece) {
		// 相手の駒
		Piece opponentsPiece = piece.is(PLAYER_PIECE) ? CPU_PIECE : PLAYER_PIECE;

		// 八方いずれかに相手の駒があること。
		if (checkLeft(column, row, opponentsPiece)
				|| checkRight(column, row, opponentsPiece)
				|| checkCenter(column, row, opponentsPiece)) {
			// 裏返す対象が１つ以上あるか。
			if (canTurnOver(column, row, opponentsPiece)) {
				return true;
			}
		}
		return false;
	}

	private boolean checkLeft(int column, int row, Piece opponentsPiece) {
		boolean isSelectable = false;
		int leftColumn = column - 1;
		// 左端の場合はチェック不要
		if (leftColumn < 0) {
			return isSelectable;
		}
		// 左列いずれかに相手の駒があるか
		if (row > 0 && board[row - 1][leftColumn].is(opponentsPiece)) {
			isSelectable = true;
		}
		if (board[row][leftColumn].is(opponentsPiece)) {
			isSelectable = true;
		}
		if (row + 1 < squareCount && board[row + 1][leftColumn].is(opponentsPiece)) {
			isSelectable = true;
		}
		return isSelectable;
	}

	private boolean checkRight(int column, int row, Piece opponentsPiece) {
		boolean isSelectable = false;
		int rightColumn = column + 1;
		// 右端の場合はチェック不要
		if (rightColumn > board.length - 1) {
			return isSelectable;
		}
		// 右列いずれかに相手の駒があるか
		if (row > 0 && board[row - 1][rightColumn].is(opponentsPiece)) {
			isSelectable = true;
		}
		if (board[row][rightColumn].is(opponentsPiece)) {
			isSelectable = true;
		}
		if (row + 1 < squareCount && board[row + 1][rightColumn].is(opponentsPiece)) {
			isSelectable = true;
		}
		return isSelectable;
	}

	private boolean checkCenter(int column, int row, Piece opponentsPiece) {

		boolean isSelectable = false;
		// 最上段ではない場合、上段のチェック
		if ((row - 1) >= 0 && board[row - 1][column].is(opponentsPiece)) {
			isSelectable = true;
		}
		// 最下段ではない場合、下段のチェック
		if (row < board.length - 1 && board[row + 1][column].is(opponentsPiece)) {
			isSelectable = true;
		}
		return isSelectable;
	}

	/**
	 * １つ以上相手の駒を裏返すことができるかどうか
	 *
	 * @param column  列番号
	 * @param row 行番号
	 * @param opponentsPiece 相手の駒オブジェクト
	 * @return 裏返せる箇所なら、true
	 */
	boolean canTurnOver(int column, int row, Piece opponentsPiece) {
		boolean canTurnOver = false;
		if (
		// 上方向のチェック
		upperDirectionCanBeTurnedOver(column, row, opponentsPiece)
				// 下方向のチェック
				|| lowerDirectionCanBeTurnedOver(column, row, opponentsPiece)
				// 左方向のチェック
				|| leftDirectionCanBeTurnedOver(column, row, opponentsPiece)
				// 右方向のチェック
				|| rightDirectionCanBeTurnedOver(column, row, opponentsPiece)
				// 左上斜め方向のチェック
				|| upperLeftDirectionCanBeTurnedOver(column, row, opponentsPiece)
				// 右上斜め方向のチェック
				|| upperRightDirectionCanBeTurnedOver(column, row, opponentsPiece)
				// 左下斜め方向のチェック
				|| lowerLeftDirectionCanBeTurnedOver(column, row, opponentsPiece)
				// 右下斜め方向のチェック
				|| lowerRightDirectionCanBeTurnedOver(column, row, opponentsPiece)) {
			canTurnOver = true;
		}

		return canTurnOver;
	}

	boolean upperDirectionCanBeTurnedOver(int column, int row, Piece opponentsPiece) {
		// ひっくり返す事ができるかどうか
		boolean canTurnOver = false;
		// 相手の駒を含む
		boolean turnOverFlg = false;
		// 初回ループSkipフラグ
		boolean isSkipFirst = true;

		for (int index = row; index >= 0; index--) {
			// 初回は自分自身の場所を指すので、SKIPする必要がある
			if (isSkipFirst) {
				isSkipFirst = false;
				continue;
			}
			// 相手の駒が存在すれば、フラグを立てる。（立てるだけ）
			if (board[index][column].is(opponentsPiece)) {
				turnOverFlg = true;
				continue;
			} else {
				// いくつかの相手の駒の先に、自分の駒がある場合はひっくり返す事ができるが、
				// すぐ隣に自分の駒がある場合はひっくり返す事ができないので、ループを終了する。
				if (!board[index][column].isEmpty() && turnOverFlg) {
					canTurnOver = true;
				}
				break;
			}
		}
		return canTurnOver;
	}

	boolean lowerDirectionCanBeTurnedOver(int column, int row, Piece opponentsPiece) {
		// ひっくり返す事ができるかどうか
		boolean canTurnOver = false;
		// 相手の駒を含む
		boolean turnOverFlg = false;
		// 初回ループSkipフラグ
		boolean isSkipFirst = true;

		for (int index = row; index < squareCount; index++) {
			// 初回は自分自身の場所を指すので、SKIPする必要がある
			if (isSkipFirst) {
				isSkipFirst = false;
				continue;
			}
			// 相手の駒が存在すれば、フラグを立てる。（立てるだけ）
			if (board[index][column].is(opponentsPiece)) {
				turnOverFlg = true;
				continue;
			} else {
				// いくつかの相手の駒の先に、自分の駒がある場合はひっくり返す事ができるが、
				// すぐ隣に自分の駒がある場合はひっくり返す事ができないので、ループを終了する。
				if (!board[index][column].isEmpty() && turnOverFlg) {
					canTurnOver = true;
				}
				break;
			}
		}
		return canTurnOver;
	}

	boolean leftDirectionCanBeTurnedOver(int column, int row, Piece opponentsPiece) {
		// ひっくり返す事ができるかどうか
		boolean canTurnOver = false;
		// 相手の駒を含む
		boolean turnOverFlg = false;
		// 初回ループSkipフラグ
		boolean isSkipFirst = true;

		for (int index = column; index > 0; index--) {
			// 初回は自分自身の場所を指すので、SKIPする必要がある
			if (isSkipFirst) {
				isSkipFirst = false;
				continue;
			}
			// 相手の駒が存在すれば、フラグを立てる。（立てるだけ）
			if (board[row][index].is(opponentsPiece)) {
				turnOverFlg = true;
				continue;
			} else {
				// いくつかの相手の駒の先に、自分の駒がある場合はひっくり返す事ができるが、
				// すぐ隣に自分の駒がある場合はひっくり返す事ができないので、ループを終了する。
				if (!board[row][index].isEmpty() && turnOverFlg) {
					canTurnOver = true;
				}
				break;
			}
		}
		return canTurnOver;
	}

	boolean rightDirectionCanBeTurnedOver(int column, int row, Piece opponentsPiece) {
		// ひっくり返す事ができるかどうか
		boolean canTurnOver = false;
		// 相手の駒を含む
		boolean turnOverFlg = false;
		// 初回ループSkipフラグ
		boolean isSkipFirst = true;

		for (int index = column; index < squareCount; index++) {
			// 初回は自分自身の場所を指すので、SKIPする必要がある
			if (isSkipFirst) {
				isSkipFirst = false;
				continue;
			}
			// 相手の駒が存在すれば、フラグを立てる。（立てるだけ）
			if (board[row][index].is(opponentsPiece)) {
				turnOverFlg = true;
				continue;
			} else {
				// いくつかの相手の駒の先に、自分の駒がある場合はひっくり返す事ができるが、
				// すぐ隣に自分の駒がある場合はひっくり返す事ができないので、ループを終了する。
				if (!board[row][index].isEmpty() && turnOverFlg) {
					canTurnOver = true;
				}
				break;
			}
		}
		return canTurnOver;
	}

	boolean upperLeftDirectionCanBeTurnedOver(int column, int row, Piece opponentsPiece) {
		// ひっくり返す事ができるかどうか
		boolean canTurnOver = false;
		// 相手の駒を含む
		boolean turnOverFlg = false;
		// 初回ループSkipフラグ
		boolean isSkipFirst = true;

		for (int i = 0; column - i >= 0 && row - i >= 0; i++) {
			// 初回は自分自身の場所を指すので、SKIPする必要がある
			if (isSkipFirst) {
				isSkipFirst = false;
				continue;
			}
			// 相手の駒が存在すれば、フラグを立てる。（立てるだけ）
			if (board[row - i][column - i].is(opponentsPiece)) {
				turnOverFlg = true;
				continue;
			} else {
				// いくつかの相手の駒の先に、自分の駒がある場合はひっくり返す事ができるが、
				// すぐ隣に自分の駒がある場合はひっくり返す事ができないので、ループを終了する。
				if (!board[row - i][column - i].isEmpty() && turnOverFlg) {
					canTurnOver = true;
				}
				break;
			}
		}
		return canTurnOver;
	}

	boolean upperRightDirectionCanBeTurnedOver(int column, int row, Piece opponentsPiece) {
		// ひっくり返す事ができるかどうか
		boolean canTurnOver = false;
		// 相手の駒を含む
		boolean turnOverFlg = false;
		// 初回ループSkipフラグ
		boolean isSkipFirst = true;

		for (int i = 0; column + i < squareCount && row - i > 0; i++) {
			// 初回は自分自身の場所を指すので、SKIPする必要がある
			if (isSkipFirst) {
				isSkipFirst = false;
				continue;
			}
			// 相手の駒が存在すれば、フラグを立てる。（立てるだけ）
			if (board[row - i][column + i].is(opponentsPiece)) {
				turnOverFlg = true;
				continue;
			} else {
				// いくつかの相手の駒の先に、自分の駒がある場合はひっくり返す事ができるが、
				// すぐ隣に自分の駒がある場合はひっくり返す事ができないので、ループを終了する。
				if (!board[row - i][column + i].isEmpty() && turnOverFlg) {
					canTurnOver = true;
				}
				break;
			}
		}
		return canTurnOver;
	}

	boolean lowerLeftDirectionCanBeTurnedOver(int column, int row, Piece opponentsPiece) {
		// ひっくり返す事ができるかどうか
		boolean canTurnOver = false;
		// 相手の駒を含む
		boolean turnOverFlg = false;
		// 初回ループSkipフラグ
		boolean isSkipFirst = true;

		for (int i = 0; column - i >= 0 && row + i < squareCount; i++) {
			// 初回は自分自身の場所を指すので、SKIPする必要がある
			if (isSkipFirst) {
				isSkipFirst = false;
				continue;
			}
			// 相手の駒が存在すれば、フラグを立てる。（立てるだけ）
			if (board[row + i][column - i].is(opponentsPiece)) {
				turnOverFlg = true;
				continue;
			} else {
				// いくつかの相手の駒の先に、自分の駒がある場合はひっくり返す事ができるが、
				// すぐ隣に自分の駒がある場合はひっくり返す事ができないので、ループを終了する。
				if (!board[row + i][column - i].isEmpty() && turnOverFlg) {
					canTurnOver = true;
				}
				break;
			}
		}
		return canTurnOver;
	}

	boolean lowerRightDirectionCanBeTurnedOver(int column, int row, Piece opponentsPiece) {
		// ひっくり返す事ができるかどうか
		boolean canTurnOver = false;
		// 相手の駒を含む
		boolean turnOverFlg = false;
		// 初回ループSkipフラグ
		boolean isSkipFirst = true;

		for (int i = 0; column + i < squareCount && row + i < squareCount; i++) {
			// 初回は自分自身の場所を指すので、SKIPする必要がある
			if (isSkipFirst) {
				isSkipFirst = false;
				continue;
			}
			// 相手の駒が存在すれば、フラグを立てる。（立てるだけ）
			if (board[row + i][column + i].is(opponentsPiece)) {
				turnOverFlg = true;
				continue;
			} else {
				// いくつかの相手の駒の先に、自分の駒がある場合はひっくり返す事ができるが、
				// すぐ隣に自分の駒がある場合はひっくり返す事ができないので、ループを終了する。
				if (!board[row + i][column + i].isEmpty() && turnOverFlg) {
					canTurnOver = true;
				}
				break;
			}
		}
		return canTurnOver;
	}

	void putPiece(int column, int row, Piece piece) {
		board[row][column] = piece;
	}

	void turnOver(int column, int row, Piece piece) {
		Piece opponent = piece.is(PLAYER_PIECE) ? CPU_PIECE : PLAYER_PIECE;
		// 上方向
		if (upperDirectionCanBeTurnedOver(column, row, opponent)) {
			for (int i = 1; i < squareCount; i++) {
				if (board[row - i][column].is(opponent)) {
					board[row - i][column] = piece;
				} else {
					break;
				}
			}
		}
		// 下方向
		if (lowerDirectionCanBeTurnedOver(column, row, opponent)) {
			for (int i = 1; i < squareCount; i++) {
				if (board[row + i][column].is(opponent)) {
					board[row + i][column] = piece;
				} else {
					break;
				}
			}
		}
		// 左方向
		if (leftDirectionCanBeTurnedOver(column, row, opponent)) {
			for (int i = 1; i < squareCount; i++) {
				if (board[row][column - i].is(opponent)) {
					board[row][column - i] = piece;
				} else {
					break;
				}
			}
		}
		// 右方向
		if (rightDirectionCanBeTurnedOver(column, row, opponent)) {
			for (int i = 1; i < squareCount; i++) {
				if (board[row][column + i].is(opponent)) {
					board[row][column + i] = piece;
				} else {
					break;
				}
			}
		}
		// 左上斜め方向
		if (upperLeftDirectionCanBeTurnedOver(column, row, opponent)) {
			for (int i = 1; i < squareCount; i++) {
				if (board[row - i][column - i].is(opponent)) {
					board[row - i][column - i] = piece;
				} else {
					break;
				}
			}
		}
		// 右上斜め方向
		if (upperRightDirectionCanBeTurnedOver(column, row, opponent)) {
			for (int i = 1; i < squareCount; i++) {
				if (board[row - i][column + i].is(opponent)) {
					board[row - i][column + i] = piece;
				} else {
					break;
				}
			}
		}
		// 左下斜め方向
		if (lowerLeftDirectionCanBeTurnedOver(column, row, opponent)) {
			for (int i = 1; i < squareCount; i++) {
				if (board[row + i][column - i].is(opponent)) {
					board[row + i][column - i] = piece;
				} else {
					break;
				}
			}
		}
		// 右下斜め方向
		if (lowerRightDirectionCanBeTurnedOver(column, row, opponent)) {
			for (int i = 1; i < squareCount; i++) {
				if (board[row + i][column + i].is(opponent)) {
					board[row + i][column + i] = piece;
				} else {
					break;
				}
			}
		}
	}

	/**
	 * パスの条件に該当するか
	 * @param player
	 * @return パスの条件に該当するか否か
	 */
	boolean needsSkip(Player player) {
		boolean isSkip = true;
		for (int row = 0; row < squareCount; row++) {
			for (int column = 0; column < squareCount; column++) {
				if (!squareIsEmpty(column, row)) {
					continue;
				}
				if (isSelectable(column, row, player.getPiece())) {
					isSkip = false;
					break;
				}
			}
		}
		return isSkip;
	}

	/**
	 * 終了判定
	 * @return 盤面に空がない場合はtrue
	 */
	boolean isFull() {
		for (Piece[] pieces : board) {
			for (Piece piece : pieces) {
				if (piece.isEmpty()) {
					return false;
				}
			}
		}
		return true;
	}

	Piece[][] getBoard() {
		return board;
	}

	void setBoard(Piece[][] board) {
		this.board = board;
	}

	int getSquareCount() {
		return squareCount;
	}

	void setSquareCount(int squareCount) {
		this.squareCount = squareCount;
	}

}
