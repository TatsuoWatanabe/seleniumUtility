package util.base;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;

/**
 * WebDriver型のInstanceを提供するためのファクトリクラスです。
 *
 * @author tatsuo1234567@gmail.com
 */
public class WebDriverFactory {

	/** 使用可能なWebDriverの列挙型定数です。 */
	public static enum Driver {CHROME, IE, ANDROID, HTMLUNIT, FIREFOX}

	/**
	 * WebDriver型のInstanceを生成します。
	 *
	 * @param selectedDriver WebDriverを指定するWebDriverFactory.Driver型定数。
	 * @return WebDriver WebDriver型オブジェクト。
	 * @throws ClassNotFoundException
	 */
	public static WebDriver newInstance(Driver selectedDriver) throws ClassNotFoundException {
		if(selectedDriver.equals(Driver.CHROME)) return newChromeDriver();
		else if(selectedDriver.equals(Driver.IE)) return newInternetExplorerDriver();
		else if(selectedDriver.equals(Driver.HTMLUNIT)) return newHtmlUnitDriver();
		else if(selectedDriver.equals(Driver.FIREFOX)) return newFirefoxDriver();
		else throw new ClassNotFoundException();
	}

	/**
	 * FirefoxDriverクラスのInstanceを生成します。
	 *
	 * @return FirefoxDriver型オブジェクト。
	 */
	public static FirefoxDriver newFirefoxDriver(){ return new FirefoxDriver(); }

	/**
	 * HtmlUnitDriverクラスのInstanceを生成します。
	 *
	 * @return HtmlUnitDriver型オブジェクト。
	 */
	public static HtmlUnitDriver newHtmlUnitDriver(){ return new HtmlUnitDriver(); }

	/**
	 * ChromeDriverクラスのInstanceを生成します。
	 *
	 * @return ChromeDriver型オブジェクト。
	 */
	public static ChromeDriver newChromeDriver(){
		System.setProperty("webdriver.chrome.driver", ProjectInfo.absolutePath + "/drivers/chromedriver.exe");
		return new ChromeDriver();
	}

	/**
	 * InternetExplorerDriverクラスのInstanceを生成します。
	 *
	 * @return InternetExplorerDriver型オブジェクト。
	 */
	public static InternetExplorerDriver newInternetExplorerDriver(){
		System.setProperty("webdriver.ie.driver", ProjectInfo.absolutePath + "/drivers/IEDriverServer.exe");
		return new InternetExplorerDriver();
	}

}
