package joy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class JoyMain {
	public static void main(String[] args) {
		JoyMain main = new JoyMain();
		main.run();
	}

	public void run() {
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				System.in));
		String pageP1, pageP2;
		try {
			System.out.print("あなたのページのURLを入力してください：");
			pageP1 = reader.readLine();
			System.out.print("相手のページのURLを入力してください：");
			pageP2 = reader.readLine();
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
	}
}
