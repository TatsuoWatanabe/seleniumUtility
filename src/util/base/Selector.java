package util.base;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

/**
 * WebElementの取得・操作用ユーティリティークラス。
 *
 * @author tatsuo1234567@gmail.com
 */
public class Selector {
	/**
	 * cssセレクター文字列を格納します。
	 *
	 * @see <a href="http://selenium.googlecode.com/git/docs/api/java/org/openqa/selenium/By.html" target="_blank">By</a>
	 */
	protected String selector;
	/**
	 * WebDriverのinstanceを格納します。
	 *
	 * @see <a href="http://selenium.googlecode.com/git/docs/api/java/org/openqa/selenium/WebDriver.html" target="_blank">WebDriver</a>
	 */
	protected WebDriver driver;

	/**
	 * @param selector {@link util.base.Selector#selector}
	 * @param driver {@link util.base.Selector#driver}
	 */
	public Selector(String selector, WebDriver driver) {
		this.selector = selector;
		this.driver = driver;
	}

	/**
	 * selector文字列から取得可能なWebElementを一つ返します。
	 *
	 * @return 取得された要素。
	 */
	public WebElement getElement() { return driver.findElement(By.cssSelector(selector)); }

	/**
	 * selector文字列から取得可能なWebElementのリストを返します。
	 *
	 * @return 取得された要素のリスト。
	 */
	public List<WebElement> getElements() { return driver.findElements(By.cssSelector(selector)); }

	/**
	 * selector文字列から取得した要素リスト内の指定された位置にある要素を返します。
	 *
	 * @param index 指定する要素のインデックス 。
	 * @return 指定された位置にある要素
	 */
	public WebElement getElementByIndex(int index) { return getElements().get(index); }

	/**
	 * selector文字列からWebElementを取得し、Selectオブジェクトとして返します。<br />
	 * 取得したWebElementがselectタグの要素ではない場合は UnexpectedTagNameException が発生します。
	 *
	 * @return Selectオブジェクト
	 * @see <a href="http://selenium.googlecode.com/git/docs/api/java/org/openqa/selenium/support/ui/Select.html" target="_blank">Select</a>
	 */
	public Select asSelect() { return new Select(el()); }

	/**
	 * selector文字列にマッチした要素の数を返します。
	 *
	 * @return 要素数。
	 */
	public int count() { return getElements().size(); }

	/**
	 * selector文字列にマッチした要素が存在するかどうかを返します。
	 *
	 * @return 要素の存在を表す真偽値。
	 */
	public boolean isElementPresent() { return count() != 0; }

	/**
	 * 要素のvalue属性のString値を取得します。
	 */
	public String getValue() { return el().getAttribute("value"); }

	/**
	 * Click this element.<br />
	 * Selectorオブジェクト自身を返すのでメソッドチェーンを構築できます。
	 */
	public Selector click() { el().click(); return this; }

	/**
	 * If this current element is a form, or an element within a form, then this will be submitted to the remote server.<br />
	 * Selectorオブジェクト自身を返すのでメソッドチェーンを構築できます。
	 */
	public Selector submit() { el().submit(); return this; }

	/**
	 * Use this method to simulate typing into an element, which may set its value.<br />
	 * Selectorオブジェクト自身を返すのでメソッドチェーンを構築できます。
	 */
	public Selector sendKeys(String s) { el().sendKeys(s); return this; }

	/**
	 * If this element is a text entry element, this will clear the value.<br />
	 * Selectorオブジェクト自身を返すのでメソッドチェーンを構築できます。
	 */
	public Selector clear() { el().clear(); return this; }

	/**
	 * Get the tag name of this element.
	 */
	public String getTagName() { return el().getTagName(); }

	/**
	 * Get the value of a the given attribute of the element.
	 */
	public String getAttribute(String paramString) { return el().getAttribute(paramString); }

	/**
	 * Determine whether or not this element is selected or not.
	 */
	public Boolean isSelected() { return el().isSelected(); }

	/**
	 * Is the element currently enabled or not? This will generally return true for everything but disabled input elements.
	 */
	public Boolean isEnabled() { return el().isEnabled(); }

	/**
	 * Get the visible (i.e.
	 */
	public String getText() { return el().getText(); }

	/**
	 * Find the first WebElement using the given method.
	 */
	public WebElement findElement(String selector) { return el().findElement(By.cssSelector(selector)); }

	/**
	 * Find all elements within the current context using the given mechanism.
	 */
	public List<WebElement> findElements(String selector) { return el().findElements(By.cssSelector(selector)); }

	/**
	 * Is this element displayed or not? This method avoids the problem of having to parse an element's "style" attribute.
	 */
	public Boolean isDisplayed() { return el().isDisplayed(); }

	/**
	 * Where on the page is the top left-hand corner of the rendered element?
	 */
	public Point getLocation() { return el().getLocation(); }

	/**
	 * What is the width and height of the rendered element?
	 */
	public Dimension getSize() { return el().getSize(); }

	/**
	 * Get the value of a given CSS property.
	 */
	public String getCssValue(String paramString) { return el().getCssValue(paramString); }

	@Override
	public String toString() { return selector; }

	/**
	 * getElementの省略形です。
	 *
	 * @return <a href="http://selenium.googlecode.com/git/docs/api/java/org/openqa/selenium/WebElement.html" target="_blank">WebElement</a>
	 */
	private WebElement el(){ return getElement(); }

}
