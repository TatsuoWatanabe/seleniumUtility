package util.base.test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import util.base.TestBase;
import util.base.WebDriverFactory;

/**
 * Selectorクラスで提供されるメソッドのテストを行うためのクラスです。
 *
 * @author tatsuo1234567@gmail.com
 */
public class SelectorTest extends TestBase {

	@Override
	@Before
	public void setUp() {
		driver = WebDriverFactory.newFirefoxDriver();
		jsx = (JavascriptExecutor) driver;
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
	}

	@Override
	@After
	public void tearDown() {
		//sleep(1000);
		driver.quit();
	}

	/**
	 * getElementメソッドのテスト
	 */
	@Test
	public void getElementTest() {
		String expected = "Created HTML File";
		createTemporaryHtmlFileAndGo("<h1>" + expected + "</h1>");
		WebElement el = $("h1").getElement();
		saveScreenshot();
		assertThat("要素の取得ができること。", el.getText(), is(expected));
	}

	/**
	 * getElementsメソッドのテスト
	 */
	@Test
	public void getElementsTest() {
		int expected = 5;
		createTemporaryHtmlFileAndGo(
			"<div id=\"div1\">" +
				StringUtils.repeat("<p>The fox jumps over the lazy dog.</p>", expected) +
			"</div>" +
			"<div id=\"div2\">" +
				StringUtils.repeat("<p>Tran Kim Bach jumps over the lazy dog.</p>", expected) +
			"</div>"
		);
		List<WebElement> elements = $("div#div1 p").getElements();
		saveScreenshot();
		assertThat("復数の要素の取得ができること。", elements.size(), is(expected));
	}

	/**
	 * getElementByIndexメソッドのテスト
	 */
	@Test
	public void getElementByIndexTest() {
		String expected = "font-size: 400%;";
		String qbf = "The quick brown fox jumps over a lazy dog.";
		StringBuilder sb = new StringBuilder("");
		sb.append("<div id=\"div1\">");
		for(int i = 1; i <= 10; i++ ){
			sb.append("<p style=\"font-size:" + i + "00%;\">" + qbf + "</p>");
		}
		sb.append("</div>");
		createTemporaryHtmlFileAndGo(sb.toString());
		WebElement el = $("div#div1 p").getElementByIndex(4 - 1);
		saveScreenshot();
		assertThat("復数の要素の取得ができること。", el.getAttribute("style"), is(expected));
	}

	/**
	 * asSelectメソッドのテスト
	 */
	@Test
	public void asSelectTest() {
		String expected = "8";
		String qbf = "quick brown fox jumps over a lazy dog.";
		StringBuilder sb = new StringBuilder("");
		sb.append("<select id=\"s1\" class=\"form-control input-lg\">");
		for(int i = 1; i <= 10; i++ ) sb.append("<option value=\"" + i + "\">" + i + " " + qbf + "</option>");
		sb.append("</select>");
		createTemporaryHtmlFileAndGo(sb.toString());
		Select sel = $("select#s1").asSelect();
		sel.selectByIndex(Integer.valueOf(expected) - 1);
		saveScreenshot();
		assertThat("セレクトボックスの要素の取得ができること。", sel.getFirstSelectedOption().getAttribute("value"), is(expected));
	}

	/**
	 * countメソッドのテスト
	 */
	@Test
	public void countTest() {
		int expected = 64;
		createTemporaryHtmlFileAndGo(
			"<div id=\"div1\">" +
				StringUtils.repeat("<p>Tran Kim Bach jumps over the lazy dog.</p>", expected) +
			"</div>"
		);
		saveScreenshot();
		assertThat("該当要素数の取得ができること。", $("div#div1 p").count(), is(expected));
	}

	/**
	 * isElementPresentメソッドのテスト
	 */
	@Test
	public void isElementPresentTest01() {
		createTemporaryHtmlFileAndGo(
			"<div id=\"div1\">" +
				StringUtils.repeat("<p class=\"tran\">Tran Kim Bach jumps over the lazy dog.</p>", 64) +
			"</div>"
		);
		saveScreenshot();
		assertThat("指定要素の存在確認ができること。", $("div#div1 p.tran").isElementPresent(), is(true));
	}

	/**
	 * isElementPresentメソッドのテスト
	 */
	@Test
	public void isElementPresentTest02() {
		createTemporaryHtmlFileAndGo(
			"<div id=\"div1\">" +
				StringUtils.repeat("<p class=\"tran\">Tran Kim Bach jumps over the lazy dog.</p>", 64) +
			"</div>"
		);
		saveScreenshot();
		assertThat("指定要素の非存在確認ができること。", $("div#div1 p.kim").isElementPresent(), is(false));
	}

	/**
	 * getValueメソッドのテスト
	 */
	@Test
	public void getValueTest() {
		String expected = "Tran Kim Bach jumps over the lazy dog.";
		createTemporaryHtmlFileAndGo(
			"<div id=\"div1\">" +
				"<input type=\"text\" value=\"" + expected + "\" class=\"form-control input-lg\" />" +
			"</div>"
		);
		saveScreenshot();
		assertThat("Value属性のString値が取得できること。", $("div#div1 input[type=\"text\"]").getValue(), is(expected));
	}

	/**
	 * clickメソッドのテスト
	 * @see http://cssdeck.com/labs/rainbow-css-background
	 */
	@Test
	public void clickTest() {
		String qbf = "The quick brown fox jumps over a lazy dog.";
		String expected = "Tran Kim Bach jumps over the rainbow.";
		createTemporaryHtmlFileAndGo(
			"<style>*{margin:0 auto;padding:0;}div{position:absolute;top:0;bottom:0;left:0;right:0;z-index:0;background:-webkit-linear-gradient(-55deg,#ffeeb0 30%,#a2d49f 30%,#a2d49f 40%,#c7c12f 40%,#c7c12f 50%,#f26247 50%,#f26247 60%,#ec2045 60%,#ec2045 70%,#ffeeb0 70% );background:-moz-linear-gradient(-55deg,#ffeeb0 30%,#a2d49f 30%,#a2d49f 40%,#c7c12f 40%,#c7c12f 50%,#f26247 50%,#f26247 60%,#ec2045 60%,#ec2045 70%,#ffeeb0 70% );background:linear-gradient(-55deg,#ffeeb0 30%,#a2d49f 30%,#a2d49f 40%,#c7c12f 40%,#c7c12f 50%,#f26247 50%,#f26247 60%,#ec2045 60%,#ec2045 70%,#ffeeb0 70% );}</style>" +
			"<div>" +
				"<button id=\"btn1\" onclick=\"$(this).text('" + expected + "')\" class=\"btn btn-info\">" + qbf + "</button>" +
			"</div>"
		);
		saveScreenshot();
		assertThat("clickイベントで変更後のボタンテキストのString値が取得できること。", $("#btn1").click().getText(), is(expected));
	}

	/**
	 * submitメソッドのテスト
	 */
	@Test
	public void submitTest() {
		String expected = "TranKimBach";
		String qbf = "The quick brown fox jumps over a lazy dog.";
		Pattern queryPattern = Pattern.compile(".+\\?q=(.+)");
		createTemporaryHtmlFileAndGo(
			"<form>" +
				"<input type=\"hidden\" name=\"q\" value=\"" + expected + "\" />" +
				"<button id=\"btn1\" type=\"submit\" class=\"btn btn-danger\">" + qbf + "</button>" +
			"</form>"
		);
		$("#btn1").submit();
		Matcher m = queryPattern.matcher(driver.getCurrentUrl());
		if(m.find()) System.out.println(m.group(1));
		saveScreenshot();
		assertThat("submitした値がURLのクエリパラメータとして取得できること。", m.group(1), is(expected) );
	}

	/**
	 * sendKeysメソッドのテスト
	 */
	@Test
	public void sendKeysTest() {
		String expected = "TranKimBach";
		createTemporaryHtmlFileAndGo(
			"<form>" +
				"<input id=\"text1\" type=\"text\" name=\"q\" value=\"\"  class=\"form-control input-lg\" />" +
				"<button id=\"btn1\" type=\"submit\" class=\"btn btn-success\">Submit Button</button>" +
			"</form>"
		);
		$("#text1").sendKeys(expected);
		saveScreenshot();
		assertThat("sendKeysで入力した値がtextboxに入力されること。", $("#text1").getValue(), is(expected));
	}

	/**
	 * clearメソッドのテスト
	 */
	@Test
	public void clearTest() {
		createTemporaryHtmlFileAndGo(
			"<form>" +
				"<input id=\"text1\" type=\"text\" name=\"q\" value=\"TranKimBach\" class=\"form-control input-lg\" />" +
				"<button id=\"btn1\" type=\"submit\" class=\"btn btn-primary\">Submit Button</button>" +
			"</form>"
		);
		saveScreenshot();
		assertThat("textboxの値が空になること。", $("#text1").clear().getValue(), is(""));
	}

	/**
	 * getTagNameメソッドのテスト
	 * @see https://developer.mozilla.org/ja/docs/Web/HTML/Element/abbr
	 */
	@Test
	public void getTagNameTest() {
		createTemporaryHtmlFileAndGo(
			"<p id=\"p1\">Tran Kim Bach is the prime minister of the <abbr title=\"United Kingdom\">UK</abbr></p>"
		);
		saveScreenshot();
		assertThat("指定した要素のタグ名を取得できること。", $("p#p1>abbr").getTagName(), is("abbr"));
	}

	/**
	 * getAttributeメソッドのテスト
	 */
	@Test
	public void getAttributeTest() {
		String expected = "United Kingdom";
		createTemporaryHtmlFileAndGo(
			"<p id=\"p1\">Tran Kim Bach is the prime minister of the <abbr title=\"" + expected + "\">UK</abbr></p>"
		);
		saveScreenshot();
		assertThat("指定した属性の値を取得できること。", $("p#p1>abbr").getAttribute("title"), is(expected));
	}

	/**
	 * isSelectedメソッドのテスト
	 */
	@Test
	public void isSelectedTest01() {
		createTemporaryHtmlFileAndGo(
			"<form class=\"container\">" +
				"<div class=\"checkbox\"><label><input type=\"checkbox\" id=\"a\">Tran</label></div>" +
				"<div class=\"checkbox\"><label><input type=\"checkbox\" id=\"b\">Kim</label></div>" +
				"<div class=\"checkbox\"><label><input type=\"checkbox\" id=\"c\">Bach</label></div>" +
			"</form>"
		);
		saveScreenshot();
		assertThat("指定した属性の選択状態を取得できること。", $("input#a").click().isSelected(), is(true));
	}

	/**
	 * isSelectedメソッドのテスト
	 */
	@Test
	public void isSelectedTest02() {
		createTemporaryHtmlFileAndGo(
			"<form class=\"container\">" +
				"<div class=\"checkbox\"><label><input type=\"checkbox\" id=\"a\">Tran</label></div>" +
				"<div class=\"checkbox\"><label><input type=\"checkbox\" id=\"b\">Kim</label></div>" +
				"<div class=\"checkbox\"><label><input type=\"checkbox\" id=\"c\">Bach</label></div>" +
			"</form>"
		);
		$("input#a").click();
		$("input#c").click();
		saveScreenshot();
		assertThat("指定した属性の非選択状態を取得できること。", $("input#b").isSelected(), is(false));
	}

	/**
	 * isEnabledメソッドのテスト
	 */
	@Test
	public void isEnabledTest01() {
		createTemporaryHtmlFileAndGo(
			"<form class=\"container\">" +
				"<div class=\"checkbox\"><label><input type=\"checkbox\" id=\"a\" disabled>Tran</label></div>" +
				"<div class=\"checkbox\"><label><input type=\"checkbox\" id=\"b\">Kim</label></div>" +
				"<div class=\"checkbox\"><label><input type=\"checkbox\" id=\"c\" disabled>Bach</label></div>" +
			"</form>"
		);
		saveScreenshot();
		assertThat("指定した属性の有効状態判定ができること。", $("input#b").isEnabled(), is(true));
	}

	/**
	 * isEnabledメソッドのテスト
	 */
	@Test
	public void isEnabledTest02() {
		createTemporaryHtmlFileAndGo(
			"<form class=\"container\">" +
				"<div class=\"checkbox\"><label><input type=\"checkbox\" id=\"a\" disabled>Tran</label></div>" +
				"<div class=\"checkbox\"><label><input type=\"checkbox\" id=\"b\" disabled>Kim</label></div>" +
				"<div class=\"checkbox\"><label><input type=\"checkbox\" id=\"c\" disabled>Bach</label></div>" +
			"</form>"
		);
		saveScreenshot();
		assertThat("指定した属性の無効状態判定ができること。", $("input#b").isEnabled(), is(false));
	}

	/**
	 * findElementメソッドのテスト
	 */
	@Test
	public void findElementTest() {
		createTemporaryHtmlFileAndGo(
			"<form class=\"container\">" +
				"<div class=\"checkbox\"><label><input type=\"checkbox\" id=\"a\">Tran</label></div>" +
				"<div class=\"checkbox\"><label><input type=\"checkbox\" id=\"b\">Kim</label></div>" +
				"<div class=\"checkbox\"><label><input type=\"checkbox\" id=\"c\">Bach</label></div>" +
			"</form>"
		);
		saveScreenshot();
		assertThat("指定した要素の内包要素が取得できること。", $("form.container").findElement("#b").getAttribute("id"), is("b"));
	}

	/**
	 * findElementsメソッドのテスト
	 */
	@Test
	public void findElementsTest() {
		createTemporaryHtmlFileAndGo(
			"<form class=\"container\">" +
				"<div class=\"checkbox\"><label><input type=\"checkbox\" id=\"a\">Tran</label></div>" +
				"<div class=\"checkbox\"><label><input type=\"checkbox\" id=\"b\">Kim</label></div>" +
				"<div class=\"checkbox\"><label><input type=\"checkbox\" id=\"c\">Bach</label></div>" +
			"</form>"
		);
		saveScreenshot();
		assertThat(
			"指定した要素の内包要素集合が取得できること。",
			$("form.container").findElements("label").get(2).getText(), is("Bach")
		);
	}

	/**
	 * isDisplayedメソッドのテスト
	 */
	@Test
	public void isDisplayedTest01() {
		createTemporaryHtmlFileAndGo("<p id=\"p1\">Tran Kim Bach is the prime minister of the UK.</p>");
		saveScreenshot();
		assertThat("指定した要素の可視状態が取得できること。", $("#p1").isDisplayed(), is(true));
	}

	/**
	 * isDisplayedメソッドのテスト
	 */
	@Test
	public void isDisplayedTest02() {
		createTemporaryHtmlFileAndGo("<p id=\"p1\" class=\"hide\">Tran Kim Bach is the prime minister of the UK.</p>");
		assertThat("指定した要素の不可視状態が取得できること。", $("#p1").isDisplayed(), is(false));
	}

	/**
	 * getLocationメソッドのテスト
	 */
	@Test
	public void getLocationTest() {
		int x = 500;
		int y = 100;
		createTemporaryHtmlFileAndGo("<img id=\"img1\" style=\"position:absolute;\" onload=\"$(this).animate({top:400}, 'slow').animate({left:200}).animate({top:" + y + ",left:"+ x +"}, 'fast', 'linear');\" src=\"data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABkAAAAYCAYAAAAPtVbGAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAAJcEhZcwAADsMAAA7DAcdvqGQAAASNSURBVEhLzZNZTFRXHMaHYV8LgjMjqCwjywhDQVZlNewMODADzBQtq0oFS4ECsgjaIgQpaSONdIAKpVE0XTACNpBGi0kVY3nqg6ZNG/vQpn0wpI4sAzL9+r+XK4TVl8b0S77k3HP/5/ud8z/38vAK9P+A6OZmceXyDZSVtyMzqwZKZS2UGfVQqc6gsqoTX3w5Dp1ugaveWFtCBga+wYGwfCgoNCe3Bbl5rcjLJxecX7YioxHRMeUYHpngVq3XhpDFxUUUnWjGbhclEpOrkZxSi5RD9Tgkb4A8rQFpikbI08k0TpXXIza+imrVaG65wiWs1oaQxjMaCHek0UIVgkNL6DSlCI8sQ9TBSgo8hfjEGshSGxCXcIqdDwoupto34LgzC909w1zKitZBBgdvwZlOsHO3Cg6CCIjdcyDxLsS+wGJERL2LyOhKJMlOE6yGABXkcrbG2TUbTruyyAqMjd3j0pa0CqLTzSMkNJfaUgeBKA7GxjZwFWdjr88xhB54B9EHq3H4SBvU2a10ijpqVSPNlyKQTrLLWU0nycR2YRL89ynZrBdaBent/RrH32pGQGARrKy9wOPxIBS4IjoyG0EhJcjIOodMVTN9XU2orbvEti4mrgr+AUVwo80Id8ixzT6C1nqjt+8ql7oGkpn1NtIVFbQbGfh8E8SEe+FyRwIGuxPQfV4GtUKFBLqP+tN9SEquw7HjHyI4pBje0gLs8VATIBLW1lKYmjnRezmXugbi4rqfjhwFcwtnRAdvQ02BJypzJGg+6YeLdUH07I18ZQiiIo+i+OQFhO4voXsqhaeE2kT3Z2UtgYmpiDZoBpFIxKWugZhSgYWVOwwMjNBW4YT2o4aY+vMR5HI566dPHuP9Nw1RmGSLgIAjiIk9AQ9PGQTCIFhYutJ6O2qxEdtmxi+0CsLnm8LQyAbGRgZ4fNMbI++ZQDc7BV9fX9bMePisCdoKjfBBWzvcPSIgEEjIbrQxE1hbGi4DNoUwR7Q048PT2RT6B374ZUCIh0MK/K6dYs2Mmbkf+4TQNIRCfTibvkBThEktYGTIQ6DEfBng5LhJu2TJiUiJsIM6/jXoJ17H87tSPLnhiD+u2rBmxswc47sXhbhUYQ6BrQGUURYI9TFHaoQNrC34LESeksSlroH0dF5AU54Lfromgf573y2tG5diqM0FZSoHqGNtkSezQ2uxCE7bjVlIb1cHl7oG8kz7N74744vnt8Ogv+PzUk9+KsYPPWJUqu3xUakID7rFsLcxpH/LAXPTT7nUNRBGcw+vY7bfD4vjIdDf9t7Sfw164rdrHrjf6YZfB9wx2r4HfAMeNBoNl7akdRBGujtNmOn3x+ItAn27d9nOwqVWsBdLbWHmtENe+LlfzNaOVItxrlTFpaxoQwijhckezHRJsXAzGPpRuqNRL/riwqGfjFzxGMHp3dRXgZjWSPFsootbvVqbQhgtPBqGtsMPM71+mL8ehMXRQApeMjNm5ph32o8D2NrNtCWE0T/0A87f+wTTn6VD2yqGtsVlyTSe/jwD8/e7qb9arnpjvRTyX+gVQIB/AfD39YjRWmp9AAAAAElFTkSuQmCC\" />");
		sleep(2000);
		saveScreenshot();
		assertThat("指定した要素のページ上の位置が取得できること。", $("#img1").getLocation(), is(new Point(x, y)));
	}

	/**
	 * getSizeメソッドのテスト
	 */
	@Test
	public void getSizeTest() {
		String imgData = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABkAAAAYCAYAAAAPtVbGAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAAJcEhZcwAADsMAAA7DAcdvqGQAAASNSURBVEhLzZNZTFRXHMaHYV8LgjMjqCwjywhDQVZlNewMODADzBQtq0oFS4ECsgjaIgQpaSONdIAKpVE0XTACNpBGi0kVY3nqg6ZNG/vQpn0wpI4sAzL9+r+XK4TVl8b0S77k3HP/5/ud8z/38vAK9P+A6OZmceXyDZSVtyMzqwZKZS2UGfVQqc6gsqoTX3w5Dp1ugaveWFtCBga+wYGwfCgoNCe3Bbl5rcjLJxecX7YioxHRMeUYHpngVq3XhpDFxUUUnWjGbhclEpOrkZxSi5RD9Tgkb4A8rQFpikbI08k0TpXXIza+imrVaG65wiWs1oaQxjMaCHek0UIVgkNL6DSlCI8sQ9TBSgo8hfjEGshSGxCXcIqdDwoupto34LgzC909w1zKitZBBgdvwZlOsHO3Cg6CCIjdcyDxLsS+wGJERL2LyOhKJMlOE6yGABXkcrbG2TUbTruyyAqMjd3j0pa0CqLTzSMkNJfaUgeBKA7GxjZwFWdjr88xhB54B9EHq3H4SBvU2a10ijpqVSPNlyKQTrLLWU0nycR2YRL89ynZrBdaBent/RrH32pGQGARrKy9wOPxIBS4IjoyG0EhJcjIOodMVTN9XU2orbvEti4mrgr+AUVwo80Id8ixzT6C1nqjt+8ql7oGkpn1NtIVFbQbGfh8E8SEe+FyRwIGuxPQfV4GtUKFBLqP+tN9SEquw7HjHyI4pBje0gLs8VATIBLW1lKYmjnRezmXugbi4rqfjhwFcwtnRAdvQ02BJypzJGg+6YeLdUH07I18ZQiiIo+i+OQFhO4voXsqhaeE2kT3Z2UtgYmpiDZoBpFIxKWugZhSgYWVOwwMjNBW4YT2o4aY+vMR5HI566dPHuP9Nw1RmGSLgIAjiIk9AQ9PGQTCIFhYutJ6O2qxEdtmxi+0CsLnm8LQyAbGRgZ4fNMbI++ZQDc7BV9fX9bMePisCdoKjfBBWzvcPSIgEEjIbrQxE1hbGi4DNoUwR7Q048PT2RT6B374ZUCIh0MK/K6dYs2Mmbkf+4TQNIRCfTibvkBThEktYGTIQ6DEfBng5LhJu2TJiUiJsIM6/jXoJ17H87tSPLnhiD+u2rBmxswc47sXhbhUYQ6BrQGUURYI9TFHaoQNrC34LESeksSlroH0dF5AU54Lfromgf573y2tG5diqM0FZSoHqGNtkSezQ2uxCE7bjVlIb1cHl7oG8kz7N74744vnt8Ogv+PzUk9+KsYPPWJUqu3xUakID7rFsLcxpH/LAXPTT7nUNRBGcw+vY7bfD4vjIdDf9t7Sfw164rdrHrjf6YZfB9wx2r4HfAMeNBoNl7akdRBGujtNmOn3x+ItAn27d9nOwqVWsBdLbWHmtENe+LlfzNaOVItxrlTFpaxoQwijhckezHRJsXAzGPpRuqNRL/riwqGfjFzxGMHp3dRXgZjWSPFsootbvVqbQhgtPBqGtsMPM71+mL8ehMXRQApeMjNm5ph32o8D2NrNtCWE0T/0A87f+wTTn6VD2yqGtsVlyTSe/jwD8/e7qb9arnpjvRTyX+gVQIB/AfD39YjRWmp9AAAAAElFTkSuQmCC";
		int w = 40;
		int h = 100;
		createTemporaryHtmlFileAndGo(
			"<img id=\"img1\" style=\"position:absolute;\" onload=\"$(this).animate({top:500,width:" + w +",height:"+ h + "}).animate({left:500}).animate({top:0,left:0});\" src=\"" + imgData + "\" />" +
			"<img id=\"img2\" style=\"position:absolute;\" onload=\"$(this).animate({top:400}).animate({left:200}).animate({top:100,left:500});\" src=\"" + imgData + "\" />"
		);
		sleep(2000);
		saveScreenshot();
		assertThat("指定した要素のサイズが取得できること。", $("#img1").getSize(), is(new Dimension(w, h)));
	}

	/**
	 * getCssValueメソッドのテスト
	 * @see http://codepen.io/diegopardo/pen/dGlfC
	 */
	@Test
	public void getCssValue() {
		String expected = "0.15s";
		createTemporaryHtmlFileAndGo(
			"<style>html{background:#ccd0d4}h1.loader{text-align:center;text-transform:uppercase;font-family:'Nunito',sans-serif;font-size:4.6875em;color:transparent;letter-spacing:.01em}.loader span{text-shadow:0 0 2px rgba(204,208,212,0.9),0 15px 25px rgba(0,0,0,0.3),0 -2px 3px rgba(0,0,0,0.1);animation:loading .85s ease-in-out infinite alternate}@keyframes loading{to{text-shadow:0 0 2px rgba(204,208,212,0.2),0 0 3px rgba(0,0,0,0.02),0 0 0 rgba(0,0,0,0)}}.loader span:nth-child(2){animation-delay:"+ expected + "}.loader span:nth-child(3){animation-delay:.30s}</style>" +
			"<h1 class=\"loader\"><span>Tran</span> <span>Kim</span> <span>Bach</span></h1>"
		);
		sleep(2000);
		saveScreenshot();
		assertThat(
			"指定した要素のCSSの指定したプロパティの値が取得できること。",
			$(".loader span:nth-child(2)").getCssValue("animation-delay"), is(expected)
		);
	}

}
