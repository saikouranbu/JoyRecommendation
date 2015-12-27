package joy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.openqa.selenium.WebElement;

public class JoyMain {
	private static long waitTime = 1000;

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
			historyRun(id1);

			System.out.print("相手のページのURLを入力してください：");
			// 相手のページのURL
			// https://www.joysound.com/utasuki/userpage/index.htm?usr=aaadbcf6d1d012bcc59cf1c3f92c0d53414d
			pageUrl2 = reader.readLine();
			id2 = pageUrl2.substring(56, pageUrl2.length()); // URLからユーザIDを抽出
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
	}

	public void historyRun(String id) {
		Thread th = new Thread();
		String urlFirst = "https://www.joysound.com/utasuki/userpage/history/index.htm?usr=";
		StringBuilder urlBuilder = new StringBuilder();
		urlBuilder.append(urlFirst);
		urlBuilder.append(id);
		String url = urlBuilder.toString();

		PhantomJs ph = PhantomJs.create();
		ArrayList<WebElement> historyList;
		ph.getDriver().get(url);
		try {
			th.sleep(waitTime);
			String historyStr = ph.getDriver().findElementByTagName("em")
					.getText();
			String historyNum = historyStr
					.substring(0, historyStr.length() - 1);
			int historyInt = Integer.parseInt(historyNum);

			String songwordstitle, name, artist, relation;
			WebElement nextButton;
			int j = 0;
			int p = 0;
			for (int i = 0; i < historyInt; i++, j++) {
				th.sleep(waitTime);

				if (i % 20 == 0 && i != 0) {
					p++;
					System.out.println("履歴" + p + "ページ目読み込み終了");
					ph.getDriver().get(
							url + "&startIndex=" + (i + 1)
									+ "&orderBy=0&sortOrder=desc");
					th.sleep(waitTime);
					j = 0;
				}
				try {
					historyList = (ArrayList<WebElement>) ph.getDriver()
							.findElementsByClassName("usk-block-link");
				} catch (Exception e) {
					break;
				}

				WebElement e = historyList.get(j);
				e.click();
				th.sleep(waitTime);

				songwordstitle = ph
						.getDriver()
						.findElementByXPath(
								"//div[@class='jp-cmp-song-words-title ng-binding']")
						.getText();
				name = songwordstitle.substring(0, songwordstitle.length() - 3);
				name = name.substring(1, name.length() - 1);
				artist = ph.getDriver()
						.findElementByClassName("jp-cmp-table-column-001")
						.getText();
				try {
					relation = ph
							.getDriver()
							.findElementByXPath(
									"//div[@data-ng-repeat='tieup in detail.tieupList']")
							.getText();
				} catch (Exception e1) {
					relation = "";
				}
				System.out.println(name + "," + artist + "," + relation);
				ph.getDriver().navigate().back();
				// System.out.println(".");
				// System.out.println(ph.getDriver().getPageSource());
			}
			System.out.println("履歴読み込み完了");

		} catch (InterruptedException e1) {
			// TODO 自動生成された catch ブロック
			e1.printStackTrace();
		} finally {
			ph.quit();
		}
	}
}
