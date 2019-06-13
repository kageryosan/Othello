package othello;

final public class Utils {

	private Utils() {
	}

	/**
	 * 数値を丸数値に変換します。
	 *
	 * @param number
	 * @return example : 1 -> ①、 15 -> ⑮
	 */
	public static String convertCircleNumber(int number) {
		String circleNum;
		switch (number) {
		case 0:
			circleNum = "⓪";
			break;
		case 1:
			circleNum = "①";
			break;
		case 2:
			circleNum = "②";
			break;
		case 3:
			circleNum = "③";
			break;
		case 4:
			circleNum = "④";
			break;
		case 5:
			circleNum = "⑤";
			break;
		case 6:
			circleNum = "⑥";
			break;
		case 7:
			circleNum = "⑦";
			break;
		case 8:
			circleNum = "⑧";
			break;
		case 9:
			circleNum = "⑨";
			break;
		case 10:
			circleNum = "⑩";
			break;
		case 11:
			circleNum = "⑪";
			break;
		case 12:
			circleNum = "⑫";
			break;
		case 13:
			circleNum = "⑬";
			break;
		case 14:
			circleNum = "⑭";
			break;
		case 15:
			circleNum = "⑮";
			break;
		case 16:
			circleNum = "⑯";
			break;
		case 17:
			circleNum = "⑰";
			break;
		case 18:
			circleNum = "⑱";
			break;
		case 19:
			circleNum = "⑲";
			break;
		case 20:
			circleNum = "⑳";
			break;
		case 21:
			circleNum = "㉑";
			break;
		case 22:
			circleNum = "㉒";
			break;
		case 23:
			circleNum = "㉓";
			break;
		case 24:
			circleNum = "㉔";
			break;
		case 25:
			circleNum = "㉕";
			break;
		case 26:
			circleNum = "㉖";
			break;
		case 27:
			circleNum = "㉗";
			break;
		case 28:
			circleNum = "㉘";
			break;
		case 29:
			circleNum = "㉙";
			break;
		case 30:
			circleNum = "㉚";
			break;
		case 31:
			circleNum = "㉛";
			break;
		case 32:
			circleNum = "㉜";
			break;
		case 33:
			circleNum = "㉝";
			break;
		case 34:
			circleNum = "㉞";
			break;
		case 35:
			circleNum = "㉟";
			break;
		case 36:
			circleNum = "㊱";
			break;
		case 37:
			circleNum = "㊲";
			break;
		case 38:
			circleNum = "㊳";
			break;
		case 39:
			circleNum = "㊴";
			break;
		case 40:
			circleNum = "㊵";
			break;
		case 41:
			circleNum = "㊶";
			break;
		case 42:
			circleNum = "㊷";
			break;
		case 43:
			circleNum = "㊸";
			break;
		case 44:
			circleNum = "㊹";
			break;
		case 45:
			circleNum = "㊺";
			break;
		case 46:
			circleNum = "㊻";
			break;
		case 47:
			circleNum = "㊼";
			break;
		case 48:
			circleNum = "㊽";
			break;
		case 49:
			circleNum = "㊾";
			break;
		case 50:
			circleNum = "㊿";
			break;
		default:
			circleNum = "×";
		}
		return circleNum;
	}

	static void print(String msg) {
		System.out.println(msg);
	}

	/**
	 * maxNumber以下の乱数を取得する
	 * @param maxNumber
	 * @return 0以上, maxNumber 以下の整数値
	 */
	static int getRandomNum(int maxNumber) {
		return (int) (Math.random() * (maxNumber));
	}


	/**
	 * 引数が行番号または列番号として有効な値がどうかを確認する処理。<br>
	 * 数値が指定範囲外の場合はfalseを返します。<br>
	 * 変換時にエラーになった場合はfalse を返します。<br>
	 * @param number 数字
	 * @param startRange  以上（を有効とする範囲開始値）
	 * @param endRange 以下（を有効とする範囲終了値）
	 * @return 有効な値ならtrueを返す。
	 */
	static boolean checkVaidNumber(String number, int startRange, int endRange) {
		int inputNum;
		try {
			inputNum = Integer.parseInt(number);
		} catch (Exception e) {
			return false;
		}
		return inputNum >= startRange && inputNum <= endRange;
	}

}
