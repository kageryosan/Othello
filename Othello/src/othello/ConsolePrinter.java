package othello;

import static othello.ConsolePrinter.*;
import static othello.Utils.*;

import java.io.BufferedReader;
import java.io.IOException;

final public class ConsolePrinter {

	// フラグでログの出力有無を判断
	static boolean autoMode = false;

	private ConsolePrinter() {
	};

	/**
	 * msgを出力した上で、入力を要求し、入力値を取得する処理
	 * @param reader
	 * @param msg 要求メッセージ
	 * @return 入力された値
	 */
	static String requestEntry(BufferedReader reader, String msg) {
		printGuidance(msg);
		String inputValue = "";
		try {
			inputValue = reader.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return inputValue;
	}

	/**
	 * 自動進行モードでない時に、接頭辞に「【警告】」を付けた上で、引数の文字列を出力する。
	 * @param msg
	 */
	static void printAlert(String msg) {
		if (!autoMode) {
			changeTextColorToRed(() -> print("【警告】" + msg));
		}
	}

	/**
	 * 自動進行モードでない時に、接頭辞に「【要求】」を付けた上で、引数の文字列を出力する。
	 * @param msg
	 */
	static void printGuidance(String msg) {
		if (!autoMode) {
			changeTextColorToBlue(() -> print("【要求】" + msg));
		}
	}

	/**
	 * 接頭辞に「【情報】」を付けた上で、引数の文字列を出力する。
	 * @param msg
	 */
	static void printInfo(String msg) {
		changeTextColorToBlue(() -> print("【情報】" + msg));
	}

	/**
	 * コンソールに出力する文字色を赤色に変えた上で、引数の処理を実行し、黒色に文字色を戻す。
	 * @param func 文字色を変更した上で実行したい処理
	 */
	static void changeTextColorToRed(IExFunction func) {
		System.out.print((char) 27 + "[31m");
		func.execute();
		System.out.print((char) 27 + "[00m");
	}

	/**
	 * コンソールに出力する文字色を青色に変えた上で、引数の処理を実行し、黒色に文字色を戻す。
	 * @param func 文字色を変更した上で実行したい処理
	 */
	static void changeTextColorToBlue(IExFunction func) {
		System.out.print((char) 27 + "[34m");
		func.execute();
		System.out.print((char) 27 + "[00m");
	}

	@FunctionalInterface
	interface IExFunction {
		// 引数なし、戻り値なしの関数型
		void execute();
	}

}
