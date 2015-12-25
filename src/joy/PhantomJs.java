package joy;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.phantomjs.PhantomJSDriver;

/**
 * A little PhantomJS Service Wrapper
 */
public class PhantomJs {
	private static PhantomJSDriver driver;
	private static PhantomJs phantomJs;

	public static void main(String[] args) {
		PhantomJs ph = create(new PhantomJSDriver());
		ArrayList<WebElement> list;
		ph.driver
				.get("https://www.joysound.com/utasuki/userpage/history/index.htm?usr=aaadbcf6d1d012bcc59cf1c3f92c0d53414d");
		String historyStr = ph.driver.findElementByTagName("em").getText();
		String historyNum = historyStr.substring(0, historyStr.length() - 1);
		int historyInt = Integer.parseInt(historyNum);
		System.out.println(historyInt);
		// System.out.println(ph.driver.getPageSource());
		/*
		 * list = (ArrayList<WebElement>)
		 * ph.driver.findElementsByClassName("usk-block-link"); for(WebElement e
		 * : list){ System.out.println(e.getText()); }
		 */
		ph.driver.quit();

		// System.out.println(element.getText());
		// System.out.println("Page title is: " + driver.getTitle());
	}

	public static PhantomJs create(PhantomJSDriver driver) {
		phantomJs = new PhantomJs();

		phantomJs.driver = driver;

		return phantomJs;
	}

	public static PhantomJs create() {
		return create(new PhantomJSDriver());
	}

	public PhantomJSDriver getDriver(){
		return driver;
	}

	public void quit(){
		driver.quit();
	}

	public void changeBrowserDimensions(int width, int height) {
		driver.manage().window().setSize(new Dimension(width, height));
	}

	public String executeScript(String script, String... args) {
		return String.valueOf(driver.executePhantomJS(script, args));
	}

	public File takeScreenshot(URL url) {
		driver.get(url.toString());
		return driver.getScreenshotAs(OutputType.FILE);
	}
}