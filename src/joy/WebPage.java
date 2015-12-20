package joy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class WebPage {
	private URL url;
	private String expression;

	WebPage(URL url) {
		this.url = url;
		expression = null;
	}

	public void setExpression(String expression) {
		this.expression = expression;
	}

	public void run() {
		try {
			URLConnection urlConnection = url.openConnection();
			urlConnection.connect();
			BufferedReader fin = new BufferedReader(new InputStreamReader(
					urlConnection.getInputStream(), "utf-8"));
			// 全行を lines に読み込み
			StringBuffer lines = new StringBuffer(4096);
			String line;
			while (fin != null) {
				line = fin.readLine();
				if (line == null)
					break;
				 System.out.println(line);
				lines.append(line);
				lines.append('\n');
			}
			// expression タグの内容の抽出
			Pattern elementPattern = Pattern.compile("<" + expression
					+ ".*?>(.+?)</" + expression + ">",
					Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
			Matcher matcher = elementPattern.matcher(lines.toString());
			while (matcher.find()) {
				String content = matcher.group(1);
				System.out.println(content);
			}
		} catch (IOException e) {
			System.err.println("I/O Error: " + e.toString());
		}
	}
}