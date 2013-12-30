package util.base.test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.io.File;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.JavascriptExecutor;

import util.base.TestBase;
import util.base.WebDriverFactory;

/**
 * TestBaseクラスで提供されるメソッドのテストを行うためのクラスです。
 *
 * @author tatsuo1234567@gmail.com
 */
public class TestBaseTest extends TestBase {
	@Override
	@Before
	public void setUp() {
		driver = WebDriverFactory.newFirefoxDriver();
		//driver = WebDriverFactory.newInstance(WebDriverFactory.Driver.CHROME);
		jsx = (JavascriptExecutor) driver;
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
	}

	@Override
	@After
	public void tearDown() {
		//sleep(5000);
		driver.quit();
	}

	/**
	 * $メソッドのテスト
	 */
	@Test
	public void $Test() {
		String expected = "Created HTML File";
		createTemporaryHtmlFileAndGo("<h1>" + expected + "</h1>");
		assertThat("要素の取得ができること。", $("h1").getText(), is(expected));
	}

	/**
	 * createTemporaryHtmlFileメソッドのテスト
	 */
	@Test
	public void createTemporaryHtmlFileTest() {
		String expected = "Created HTML File";
		File htmlFile = createTemporaryHtmlFileAndGo("<h1>" + expected + "</h1>");
		assertThat("HTMLファイルの生成・表示ができること。", htmlFile.exists(), is(true));
	}

	/**
	 * isAlertPresentメソッドのテスト
	 */
	@Test
	public void isAlertPresentTest01() {
		createTemporaryHtmlFileAndGo("<h1>Created HTML File</h1>");
		alert("alert is here.", 0, false);
		assertThat("アラートが存在判定ができること。", isAlertPresent(), is(true));
	}

	/**
	 * isAlertPresentメソッドのテスト
	 */
	@Test
	public void isAlertPresentTest02() {
		createTemporaryHtmlFileAndGo("<h1>Created HTML File</h1>");
		assertThat("アラートが非存在判定ができること。", isAlertPresent(), is(false));
	}

	/**
	 * closeAlertAndGetItsTextTestメソッドのテスト
	 */
	@Test
	public void closeAlertAndGetItsTextTest01() {
		createTemporaryHtmlFileAndGo("<h1>Created HTML File</h1>");
		alert("alert is here.", 0, false);
		closeAlertAndGetItsText();
		assertThat("アラートが閉じていること。", isAlertPresent(), is(false));
	}

	/**
	 * closeAlertAndGetItsTextTestメソッドのテスト
	 */
	@Test
	public void closeAlertAndGetItsTextTest02() {
		String expected = "alert is here.";
		createTemporaryHtmlFileAndGo("<h1>Created HTML File</h1>");
		alert(expected, 0, false);
		String fromAlert = closeAlertAndGetItsText();
		System.out.println("String fromAlert = " + fromAlert);
		assertThat("期待する文字列が取得されること。", fromAlert, is(expected));
	}

	/**
	 * closeAlertAndGetItsTextTestメソッドのテスト
	 */
	@Test
	public void closeAlertAndGetItsTextTest03() {
		boolean expected = true;
		createTemporaryHtmlFileAndGo("<h1>Created HTML File</h1>");
		closeAlertAndGetItsText();
		assertThat("アラートが存在しなくてもエラーが発生しないこと。", true, is(expected));
	}
}
