package project.others;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import util.base.Selector;
import util.base.WebDriverFactory;

/**
 * <a href="https://www.google.co.jp/" target="_blank">google検索</a>の実験を行います。
 *
 * @author tatsuo1234567@gmail.com
 */
public class Sample extends util.base.TestBase {

	@Override
	@Before
	public void setUp() {
		driver = WebDriverFactory.newFirefoxDriver();
		jsx = (JavascriptExecutor) driver;
		baseUrl = "https://www.google.co.jp/";
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		driver.get(baseUrl);
	}

	@Override
	@After
	public void tearDown() {
		sleep(3000);
		driver.quit();
	}

	/**
	 * Google検索テスト
	 */
	@Test
	public void test01() {
		Selector txtSearch = $("#lst-ib");
		Selector btnSearch = $("[name=\"btnK\"]");
		String word = "Tran Bach";
		String resultTitle = word + " - Google 検索";

		txtSearch.sendKeys(word);
		btnSearch.click();
		
		new WebDriverWait(driver, 3).until(ExpectedConditions.titleIs(resultTitle));
		saveScreenshot();
		assertThat("検索結果のページタイトルが表示されること", driver.getTitle(), is(resultTitle));

	}

	/**
	 * Google天気検索テスト
	 */
	@Test
	public void test02() {
		Selector txtSearch = $("#lst-ib");
		Selector btnSearch = $("[name=\"btnK\"]");
		Selector spanTemperature = $("#wob_tm");
		String prefix = "weather:";
		String word = "福岡県福岡市";

		txtSearch.sendKeys(prefix + word);
		btnSearch.click();

		saveScreenshot();
		assertThat("福岡市の気温が15度以下であること",  Integer.parseInt(spanTemperature.getText()) <= 15 , is(true));
	}

	/**
	 * Google計算機機能テスト
	 */
	@Test
	public void test03(){
		int num1 = 8;
		int num2 = 8;
		String op = "+";
		Integer calcResult = 0;
		String word = String.valueOf(num1) + op + String.valueOf(num2);

		Selector txtSearch = $("#lst-ib");
		Selector btnSearch = $("[name=\"btnK\"]");
		Selector spanCalculated = $("#cwos");

		calcResult = calcTwoValues(num1, num2, op);

		txtSearch.sendKeys(word);
		btnSearch.click();
		saveScreenshot();
		assertThat("計算結果が正しいこと。", spanCalculated.getText(), is(calcResult.toString()));
	}

	/**
	 * Google計算機機能テスト
	 * Google検索で 4 * 20 - 5 を計算、取得した計算結果が正しいこと。
	 */
	@Test
	public void test04(){
		int num1 = 4;
		int num2 = 20;
		int num3 = 5;
		String op1 = "*";
		String op2 = "-";
		Integer calcResult = 0;
		String word = String.valueOf(num1) + op1 + String.valueOf(num2) + op2 + String.valueOf(num3);

		Selector txtSearch = $("#lst-ib");
		Selector btnSearch = $("[name=\"btnK\"]");
		Selector spanCalculated = $("#cwos");

		calcResult = calcTwoValues(num1, num2, op1);
		calcResult = calcTwoValues(calcResult, num3, op2);

		txtSearch.sendKeys(word);
		btnSearch.click();
		saveScreenshot();
		assertThat("計算結果が正しいこと。", spanCalculated.getText(), is(calcResult.toString()));
	}

	/**
	 * 2つの値を演算した結果を返します。
	 *
	 * @param num1
	 * @param num2
	 * @param op
	 * @return intvalue
	 */
	private int calcTwoValues(int num1, int num2, String op){
		if(op.equals("+")){
			return num1 + num2;
		}else if(op.equals("-")){
			return num1 - num2;
		}else if(op.equals("*")){
			return num1 * num2;
		}else if(op.equals("/")){
			return num1 / num2;
		}else{
			return 0;
		}
	}

}
