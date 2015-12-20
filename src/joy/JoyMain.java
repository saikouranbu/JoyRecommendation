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
		String pageUrl1, pageUrl2;
		String id1, id2;

		try {
			System.out.print("あなたのページのURLを入力してください：");
			// 自分のページのURL
			// https://www.joysound.com/utasuki/userpage/index.htm?usr=31355ec2a6046f5b67aa547bed58275a2a47
			pageUrl1 = reader.readLine();
			id1 = pageUrl1.substring(56, pageUrl1.length()); // URLからユーザIDを抽出
			//System.out.println(id1);
			/*
			 * WebPage web1 = new WebPage(new URL(pageUrl1)); //
			 * web1.setExpression("/utasuki/index.htm"); web1.run();
			 */

			System.out.print("相手のページのURLを入力してください：");
			// 相手のページのURL
			// https://www.joysound.com/utasuki/userpage/index.htm?usr=aaadbcf6d1d012bcc59cf1c3f92c0d53414d
			pageUrl2 = reader.readLine();
			id2 = pageUrl2.substring(56, pageUrl2.length()); // URLからユーザIDを抽出
			//System.out.println(id2);
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
	}
}
