package joy;

import java.io.File;
import java.net.URL;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.phantomjs.PhantomJSDriver;

/**
 * A little PhantomJS Service Wrapper
 */
public class PhantomJs {
	public static void main(String[] args) {
        PhantomJs ph = create();
        ph.driver.get("https://www.joysound.com/utasuki/userpage/history/index.htm?usr=aaadbcf6d1d012bcc59cf1c3f92c0d53414d");
        System.out.println(ph.driver.toString());
        // Inpu要素を名前で検索します。
        //WebElement element = driver.findElement(By.id("usk-block-link"));
        // 検索キーワードを入力
        //element.sendKeys("Cheese!");
        // Submitします。WebDriverが自動的にFormを見つけます。
        //element.submit();
        // ページタイトルを確認します。
        System.out.println("test");
        ph.driver.close();
        //System.out.println(element.getText());
        //System.out.println("Page title is: " + driver.getTitle());
    }
    public static PhantomJs create(PhantomJSDriver driver) {
        PhantomJs phantomJs;

        phantomJs = new PhantomJs();

        phantomJs.driver = driver;

        return phantomJs;
    }

    public static PhantomJs create() {
        return create(new PhantomJSDriver());
    }

    private PhantomJSDriver driver;

    public void changeBrowserDimensions(int width, int height) {
        driver.manage().window().setSize(new Dimension(width, height));
    }

    public String executeScript(String script, String... args){
        return String.valueOf(driver.executePhantomJS(script, args));
    }

    public File takeScreenshot(URL url) {
        driver.get(url.toString());
        return driver.getScreenshotAs(OutputType.FILE);
    }
}