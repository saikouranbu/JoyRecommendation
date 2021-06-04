package joy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Vector;

import org.openqa.selenium.WebElement;

public class JoyMain {
	final private static long waitTime = 1000; // ページ読み込み待ち時間。PhJSがシャットダウンされるときは長くする。
	final private static int rankMaxNum = 10; // ランキングを何位まで表示させるか
	private String friendName; // 相手のユーザ名
	private HashSet<String> nameSet; // 自分の曲名一覧
	private HashSet<String> artistSet; // 自分のアーティスト名一覧
	private HashSet<String> relationSet; // 自分の関連情報一覧
	private HashMap<String, Integer> nameMap; // 相手の曲名一覧
	private HashMap<String, Integer> artistMap; // 相手のアーティスト名一覧
	private HashMap<String, Integer> relationMap; // 相手の関連情報一覧

	private PhantomJs ph;

	public static void main(String[] args) {
		JoyMain main = new JoyMain();
		main.run();
		JoyDAO.getInstance().exit();
	}

	public void run() {
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				System.in));
		String pageUrl1, pageUrl2;
		String id1, id2;

		ph = PhantomJs.create();

		nameSet = new HashSet<String>();
		artistSet = new HashSet<String>();
		relationSet = new HashSet<String>();
		nameMap = new HashMap<String, Integer>();
		artistMap = new HashMap<String, Integer>();
		relationMap = new HashMap<String, Integer>();
		try {
			System.out.print("あなたのページのURLを入力してください：");
			// 自分のページのURL
			// https://www.joysound.com/utasuki/userpage/index.htm?usr=???
			pageUrl1 = reader.readLine();
			id1 = pageUrl1.substring(56, pageUrl1.length()); // URLからユーザIDを抽出

			System.out.print("相手のページのURLを入力してください：");
			// 相手のページのURL
			// https://www.joysound.com/utasuki/userpage/index.htm?usr=???
			pageUrl2 = reader.readLine();
			id2 = pageUrl2.substring(56, pageUrl2.length()); // URLからユーザIDを抽出

			historyRun(id1, 1);
			favMusicRun(id1, 1);
			favArtistRun(id1, 1);
			historyRun(id2, 2);
			favMusicRun(id2, 2);
			favArtistRun(id2, 2);
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		} finally {
			ph.quit();
		}
		// System.out.println(nameSet);
		// System.out.println(artistSet);
		// System.out.println(relationSet);
		// System.out.println(nameMap);
		// System.out.println(artistMap);
		// System.out.println(relationMap);

		printScore();
		printScoreOnlyFriendData();
	}

	public void historyRun(String userId, int userNum) {
		Thread th = new Thread();
		String urlFirst = "https://www.joysound.com/utasuki/userpage/history/index.htm?usr=";
		StringBuilder urlBuilder = new StringBuilder();
		urlBuilder.append(urlFirst);
		urlBuilder.append(userId);
		String url = urlBuilder.toString();

		ArrayList<WebElement> historyList;
		ph.getDriver().get(url);
		try {
			th.sleep(waitTime);

			if (userNum == 2) { // 相手のユーザネームを取得
				String historyUserName = ph.getDriver()
						.findElementByTagName("h1").getText();
				friendName = historyUserName.substring(0,
						historyUserName.length() - 6);
			}

			String historyStr = ph.getDriver().findElementByTagName("em")
					.getText();
			String historyNum = historyStr
					.substring(0, historyStr.length() - 1);
			int historyInt = Integer.parseInt(historyNum);

			String songUrl, id, songwordstitle, name, artist, relation;
			Vector<Music> v;
			ArrayList<String> musicURL = new ArrayList<String>();
			Music m;
			int j = 0;
			int p = 0;

			historyList = (ArrayList<WebElement>) ph.getDriver()
					.findElementsByClassName("usk-block-link");
			for (WebElement e : historyList) {
				musicURL.add(e.getAttribute("href"));
			}

			for (int i = 0; i < historyInt - 1; i++, j++) {
				if (i % 20 == 0 && i != 0) {
					p++;
					System.out.println("履歴" + p + "ページ目読み込み終了");
					ph.getDriver().get(
							url + "&startIndex=" + (i + 1)
									+ "&orderBy=0&sortOrder=desc");
					th.sleep(waitTime);

					historyList = (ArrayList<WebElement>) ph.getDriver()
							.findElementsByClassName("usk-block-link");
					musicURL.clear();
					for (WebElement e : historyList) {
						musicURL.add(e.getAttribute("href"));
					}
					j = 0;
					// break;
				}

				songUrl = musicURL.get(j);
				id = songUrl.substring(41, songUrl.indexOf("?"));
				v = JoyDAO.getInstance().select(id);
				if (!v.isEmpty()) {
					m = v.get(0);
					if (userNum == 1) {
						nameSet.add(m.getName() + " // " + m.getArtist());
						artistSet.add(m.getArtist());
						if (!m.getRelation().equals("")) {
							relationSet.add(m.getRelation());
						}
					} else {
						if (nameMap.containsKey(m.getName() + " // "
								+ m.getArtist())) {
							nameMap.put(
									m.getName() + " // " + m.getArtist(),
									nameMap.get(m.getName() + " // "
											+ m.getArtist()) + 1);
						} else {
							nameMap.put(m.getName() + " // " + m.getArtist(), 1);
						}
						if (artistMap.containsKey(m.getArtist())) {
							artistMap.put(m.getArtist(),
									artistMap.get(m.getArtist()) + 1);
						} else {
							artistMap.put(m.getArtist(), 1);
						}
						if (!m.getRelation().equals("")) {
							if (relationMap.containsKey(m.getRelation())) {
								relationMap.put(m.getRelation(),
										relationMap.get(m.getRelation()) + 1);
							} else {
								relationMap.put(m.getRelation(), 1);
							}
						}
					}
					// System.out.println(id + "," + m.getName() + ","
					// + m.getArtist() + "," + m.getRelation());
				} else {
					ph.getDriver().get(songUrl);
					th.sleep(waitTime);

					try {
						songwordstitle = ph
								.getDriver()
								.findElementByXPath(
										"//div[@class='jp-cmp-song-words-title ng-binding']")
								.getText();
					} catch (Exception e) {
						ph.getDriver().navigate().back();
						th.sleep(waitTime);
						continue;
					}
					name = songwordstitle.substring(0,
							songwordstitle.length() - 3);
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
					JoyDAO.getInstance().insert(id, name, artist, relation);
					if (userNum == 1) {
						nameSet.add(name + " // " + artist);
						artistSet.add(artist);
						if (!relation.equals("")) {
							relationSet.add(relation);
						}
					} else {
						if (nameMap.containsKey(name + " // " + artist)) {
							nameMap.put(name + " // " + artist,
									nameMap.get(name + " // " + artist) + 1);
						} else {
							nameMap.put(name + " // " + artist, 1);
						}
						if (artistMap.containsKey(artist)) {
							artistMap.put(artist, artistMap.get(artist) + 1);
						} else {
							artistMap.put(artist, 1);
						}
						if (!relation.equals("")) {
							if (relationMap.containsKey(relation)) {
								relationMap.put(relation,
										relationMap.get(relation) + 1);
							} else {
								relationMap.put(relation, 1);
							}
						}
					}
					// System.out.println(name + "," + artist + "," + relation);
				}
				// break;
			}
			System.out.println("履歴読み込み完了");

		} catch (InterruptedException e1) {
			// TODO 自動生成された catch ブロック
			e1.printStackTrace();
		}
	}

	public void favMusicRun(String userId, int userNum) {
		Thread th = new Thread();
		String urlFirst = "https://www.joysound.com/utasuki/userpage/mysong.htm?usr=";
		StringBuilder urlBuilder = new StringBuilder();
		urlBuilder.append(urlFirst);
		urlBuilder.append(userId);
		String url = urlBuilder.toString();

		ArrayList<WebElement> favMusicList;
		ph.getDriver().get(url);
		try {
			th.sleep(waitTime);

			String favMusicStr = ph.getDriver().findElementByTagName("em")
					.getText();
			String favMusicNum = favMusicStr.substring(0,
					favMusicStr.length() - 1);
			int favMusicInt = Integer.parseInt(favMusicNum);

			String songUrl, id, songwordstitle, name, artist, relation;
			Vector<Music> v;
			ArrayList<String> musicURL = new ArrayList<String>();
			Music m;
			int j = 0;
			int p = 0;

			favMusicList = (ArrayList<WebElement>) ph.getDriver()
					.findElementsByClassName("usk-block-link");
			for (WebElement e : favMusicList) {
				musicURL.add(e.getAttribute("href"));
			}

			for (int i = 0; i < favMusicInt - 1; i++, j++) {
				if (i % 20 == 0 && i != 0) {
					p++;
					System.out.println("マイうた" + p + "ページ目読み込み終了");
					ph.getDriver().get(
							url + "&startIndex=" + (i + 1)
									+ "&orderBy=0&sortOrder=desc");
					th.sleep(waitTime);

					favMusicList = (ArrayList<WebElement>) ph.getDriver()
							.findElementsByClassName("usk-block-link");
					musicURL.clear();
					for (WebElement e : favMusicList) {
						musicURL.add(e.getAttribute("href"));
					}
					j = 0;
					// break;
				}

				songUrl = musicURL.get(j);
				id = songUrl.substring(41, songUrl.indexOf("?"));
				v = JoyDAO.getInstance().select(id);
				if (!v.isEmpty()) {
					m = v.get(0);
					if (userNum == 1) {
						nameSet.add(m.getName() + " // " + m.getArtist());
						artistSet.add(m.getArtist());
						if (!m.getRelation().equals("")) {
							relationSet.add(m.getRelation());
						}
					} else {
						if (nameMap.containsKey(m.getName() + " // "
								+ m.getArtist())) {
							nameMap.put(
									m.getName() + " // " + m.getArtist(),
									nameMap.get(m.getName() + " // "
											+ m.getArtist()) + 5);
						} else {
							nameMap.put(m.getName() + " // " + m.getArtist(), 5);
						}
						if (artistMap.containsKey(m.getArtist())) {
							artistMap.put(m.getArtist(),
									artistMap.get(m.getArtist()) + 2);
						} else {
							artistMap.put(m.getArtist(), 2);
						}
						if (!m.getRelation().equals("")) {
							if (relationMap.containsKey(m.getRelation())) {
								relationMap.put(m.getRelation(),
										relationMap.get(m.getRelation()) + 3);
							} else {
								relationMap.put(m.getRelation(), 3);
							}
						}
					}
					// System.out.println(id + "," + m.getName() + ","
					// + m.getArtist() + "," + m.getRelation());
				} else {
					ph.getDriver().get(songUrl);
					th.sleep(waitTime);

					try {
						songwordstitle = ph
								.getDriver()
								.findElementByXPath(
										"//div[@class='jp-cmp-song-words-title ng-binding']")
								.getText();
					} catch (Exception e) {
						ph.getDriver().navigate().back();
						th.sleep(waitTime);
						continue;
					}
					name = songwordstitle.substring(0,
							songwordstitle.length() - 3);
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
					JoyDAO.getInstance().insert(id, name, artist, relation);
					if (userNum == 1) {
						nameSet.add(name + " // " + artist);
						artistSet.add(artist);
						if (!relation.equals("")) {
							relationSet.add(relation);
						}
					} else {
						if (nameMap.containsKey(name + " // " + artist)) {
							nameMap.put(name + " // " + artist,
									nameMap.get(name + " // " + artist) + 5);
						} else {
							nameMap.put(name + " // " + artist, 5);
						}
						if (artistMap.containsKey(artist)) {
							artistMap.put(artist, artistMap.get(artist) + 2);
						} else {
							artistMap.put(artist, 2);
						}
						if (!relation.equals("")) {
							if (relationMap.containsKey(relation)) {
								relationMap.put(relation,
										relationMap.get(relation) + 3);
							} else {
								relationMap.put(relation, 3);
							}
						}
					}
					// System.out.println(name + "," + artist + "," + relation);
				}
				// break;
			}
			System.out.println("マイうた読み込み完了");

		} catch (InterruptedException e1) {
			// TODO 自動生成された catch ブロック
			e1.printStackTrace();
		}
	}

	public void favArtistRun(String userId, int userNum) {
		Thread th = new Thread();
		String urlFirst = "https://www.joysound.com/utasuki/userpage/myartist.htm?usr=";
		StringBuilder urlBuilder = new StringBuilder();
		urlBuilder.append(urlFirst);
		urlBuilder.append(userId);
		String url = urlBuilder.toString();

		ArrayList<WebElement> favArtistList;
		ph.getDriver().get(url);
		try {
			th.sleep(waitTime);

			String favArtistStr = ph.getDriver().findElementByTagName("em")
					.getText();
			String favArtistNum = favArtistStr.substring(0,
					favArtistStr.length() - 1);
			int favArtistInt = Integer.parseInt(favArtistNum);

			String artist;
			WebElement ele;
			int j = 0;
			int p = 0;
			for (int i = 0; i < favArtistInt - 1; i++, j++) {
				if (i % 20 == 0 && i != 0) {
					p++;
					System.out.println("マイアーティスト" + p + "ページ目読み込み終了");
					ph.getDriver().get(
							url + "&startIndex=" + (i + 1)
									+ "&orderBy=0&sortOrder=desc");
					th.sleep(waitTime);
					j = 0;
					// break;
				}

				favArtistList = (ArrayList<WebElement>) ph
						.getDriver()
						.findElementsByXPath(
								"//span[@class='usk-bold-txt usk-sbig-txt usk-break']");

				try {
					ele = favArtistList.get(j);
				} catch (Exception e) {
					break;
				}

				artist = ele.getText();
				// System.out.println(artist);

				if (userNum == 1) {
					artistSet.add(artist);
				} else {
					if (artistMap.containsKey(artist)) {
						artistMap.put(artist, artistMap.get(artist) + 10);
					} else {
						artistMap.put(artist, 10);
					}
				}
			}
			System.out.println("マイアーティスト読み込み完了");

		} catch (InterruptedException e1) {
			// TODO 自動生成された catch ブロック
			e1.printStackTrace();
		}
	}

	public void printScore() {
		System.out.println("あなたが歌える" + friendName + "にウケると推定される曲ランキング\n");
		System.out.println("評価Pt\t曲名 // アーティスト名");

		List<Entry<String, Integer>> nameEntries = new ArrayList<Entry<String, Integer>>(
				nameMap.entrySet());

		Collections.sort(nameEntries, new JoyComparator());

		int count = 0;
		for (Entry<String, Integer> e : nameEntries) {
			if (nameSet.contains(e.getKey())) {
				count++;
				System.out.println(e.getValue() + "\t" + e.getKey());
			}
			if (count >= rankMaxNum) {
				break;
			}
		}

		System.out.println("\n");

		System.out.println("あなたが歌える" + friendName + "にウケると推定されるアーティストランキング\n");
		System.out.println("評価Pt\tアーティスト名");

		List<Entry<String, Integer>> artistEntries = new ArrayList<Entry<String, Integer>>(
				artistMap.entrySet());

		Collections.sort(artistEntries, new JoyComparator());

		count = 0;
		for (Entry<String, Integer> e : artistEntries) {
			if (artistSet.contains(e.getKey())) {
				count++;
				System.out.println(e.getValue() + "\t" + e.getKey());
			}
			if (count >= rankMaxNum) {
				break;
			}
		}

		System.out.println("\n");

		System.out.println("あなたが歌える" + friendName + "にウケると推定される関連情報ランキング\n");
		System.out.println("評価Pt\t関連情報");

		List<Entry<String, Integer>> relationEntries = new ArrayList<Entry<String, Integer>>(
				relationMap.entrySet());

		Collections.sort(relationEntries, new JoyComparator());

		count = 0;
		for (Entry<String, Integer> e : relationEntries) {
			if (relationSet.contains(e.getKey())) {
				count++;
				System.out.println(e.getValue() + "\t" + e.getKey());
			}
			if (count >= rankMaxNum) {
				break;
			}
		}

		System.out.println("\n\n");

	}

	public void printScoreOnlyFriendData() {
		System.out.println(friendName + "にウケると推定される曲ランキング\n");
		System.out.println("評価Pt\t曲名 // アーティスト名");

		List<Entry<String, Integer>> nameEntries = new ArrayList<Entry<String, Integer>>(
				nameMap.entrySet());

		Collections.sort(nameEntries, new JoyComparator());

		int count = 0;
		for (Entry<String, Integer> e : nameEntries) {
			count++;
			System.out.println(e.getValue() + "\t" + e.getKey());
			if (count >= rankMaxNum) {
				break;
			}
		}

		System.out.println("\n");

		System.out.println(friendName + "にウケると推定されるアーティストランキング\n");
		System.out.println("評価Pt\tアーティスト名");

		List<Entry<String, Integer>> artistEntries = new ArrayList<Entry<String, Integer>>(
				artistMap.entrySet());

		Collections.sort(artistEntries, new JoyComparator());

		count = 0;
		for (Entry<String, Integer> e : artistEntries) {
			count++;
			System.out.println(e.getValue() + "\t" + e.getKey());
			if (count >= rankMaxNum) {
				break;
			}
		}

		System.out.println("\n");

		System.out.println(friendName + "にウケると推定される関連情報ランキング\n");
		System.out.println("評価Pt\t関連情報");

		List<Entry<String, Integer>> relationEntries = new ArrayList<Entry<String, Integer>>(
				relationMap.entrySet());

		Collections.sort(relationEntries, new JoyComparator());

		count = 0;
		for (Entry<String, Integer> e : relationEntries) {
			count++;
			System.out.println(e.getValue() + "\t" + e.getKey());
			if (count >= rankMaxNum) {
				break;
			}
		}

		System.out.println("\n\n");

	}

	class JoyComparator implements Comparator<Entry<String, Integer>> {

		@Override
		public int compare(Entry<String, Integer> o1, Entry<String, Integer> o2) {
			// TODO 自動生成されたメソッド・スタブ
			// return o1.getValue().compareTo(o2.getValue()); // 昇順
			return o2.getValue().compareTo(o1.getValue()); // 降順
		}
	}
}
