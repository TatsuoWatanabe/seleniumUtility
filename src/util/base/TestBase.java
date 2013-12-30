package util.base;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.openqa.selenium.Alert;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

/**
 * テストケースクラス用の基底クラス。<br />
 * テストケースの作成に有用な機能を提供します。
 *
 * @author tatsuo1234567@gmail.com
 */
public abstract class TestBase {
	/**
	 * Selenium API documentを参照:
	 * <a href="http://selenium.googlecode.com/git/docs/api/java/org/openqa/selenium/WebDriver.html" target="_blank">WebDriver</a>
	 */
	protected WebDriver driver;
	/**
	 * Selenium API documentを参照:
	 * <a href="http://selenium.googlecode.com/git/docs/api/java/org/openqa/selenium/JavascriptExecutor.html" target="_blank">JavascriptExecutor</a>
	 */
	protected JavascriptExecutor jsx;
	protected String baseUrl;
	/**
	 * saveScreenShotメソッドで取得した画像ファイルの保存先ディレクトリのパスを指定します。<br />
	 * 実装クラスでこの変数の値をOverrideすることで、保存先のパスを変更することができます。
	 */
	protected String screenshotSavePath = ProjectInfo.absolutePath + "/logs/screenshot/";
	/**
	 * watcherオブジェクトのログ出力の保存先ディレクトリのパスを指定します。<br />
	 * このディレクトリの配下に分類されたディレクトリが配置されます。
	 */
	protected String testWacherLogSavePath = ProjectInfo.absolutePath + "/logs/log/";
	/**
	 * watcherオブジェクトのログ出力の保存先ディレクトリ内の分類されたディレクトリのパスを返します。<br />
	 * このディレクトリの配下にログファイルが出力されます。<br />
	 * 実装クラスでこのメソッドをOverrideすることで、保存先のパスを変更することができます。
	 */
	protected String getLogSaveDirName() { return this.getClass().getPackage().getName();}

	/**
	 * テスト実行中のイベント発生時のアクションを定義するクラスです。<br />
	 * 主にログの出力に使用します。
	 *
	 * @author tatsuo1234567@gmail.com
	 * @see <a href="http://junit-team.github.io/junit/javadoc/latest/org/junit/rules/TestWatcher.html" target="_blank">TestWatcher</a>
	 */
	public final class MyTestWatcher extends TestWatcher {
		@Override public void starting(Description des) { p("Starting", des); }
		@Override public void finished(Description des) { p("Finished", des); }
		@Override public void succeeded(Description des) { p("Succeeded", des); }
		@Override public void failed(Throwable e, Description des) { p("Failed", des, e.getMessage()); }

		/**
		 * 現在時刻とラベルとメッセージを連結した文字列を出力して行を終了します。
		 *
		 * @param label ラベル
		 * @param msg メッセージ
		 */
		private void p(String label, String msg) { println(d("yyyy-MM-dd HH:mm:ss.SSS") + " " + label + " : " + msg);}
		private void p(String label, Description des) { p(label, des.getDisplayName()); }
		private void p(String label, Description des, String msg) { p(label, des.getDisplayName() + "\n" + msg); }

		/**
		 * 現在時刻を指定された形式に整形して文字列として返します。
		 *
		 * @param pattern 日付と時刻のフォーマットを記述するパターン
		 * @return 整形日付文字列
		 */
		private String d(String pattern) { return new SimpleDateFormat(pattern).format(new Date()); }

		/**
		 * 標準出力にStringを出力して行を終了します。<br />
		 * また、同じ内容をログファイルに書き込みます。
		 *
		 * @param s 出力されるString値。
		 */
		private void println(String s) {
			System.out.println(s);
			String logSavePath = getLogSavePath();
			String logFileName = d("yyyy-MM-dd") + ".test.log";
			try{
				File file = new File(logSavePath + "/" + logFileName);
				PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));
				pw.println(s);
				pw.close();
			}catch(IOException e){
				System.out.println(e);
			}
		}
	}

	/**
	 * ログファイル保存先ディレクトリのパスを返します。<br />
	 * ディレクトリが存在しない場合は新規作成されます。<br />
	 * 実装クラスでこのメソッドをOverrideすることで、保存先のパスを変更することができます。
	 *
	 * @return ログファイル保存先ディレクトリのパス。
	 */
	protected String getLogSavePath() {
		File dir = new File(testWacherLogSavePath + "/" + getLogSaveDirName() + "/");
		dir.mkdirs();
		return dir.getAbsolutePath();
	}

	/**
	 * スクリーンショット保存先ディレクトリのパスを返します。<br />
	 * ディレクトリが存在しない場合は新規作成されます。<br />
	 * 実装クラスでこのメソッドをOverrideすることで、保存先のパスを変更することができます。
	 *
	 * @return スクリーンショット保存先ディレクトリのパス。
	 */
	protected String getScreenshotSavePath() {
		String packageName = this.getClass().getPackage().getName();
		File dir = new File(screenshotSavePath + "/" + packageName + "/");
		dir.mkdirs();
		return dir.getAbsolutePath();
	}

	/**
	 * MyTestWatcherの新しいInstanceを返します。
	 *
	 * @return MyTestWatcher型のオブジェクト。
	 */
	public MyTestWatcher createTestWatcher() { return new MyTestWatcher(); }

	@Rule
	public TestRule watcher = createTestWatcher();

	@Before
	public void setUp() {
		driver = WebDriverFactory.newFirefoxDriver();
		jsx = (JavascriptExecutor) driver;
		baseUrl = "about:blank";
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		driver.get(baseUrl);
	}

	@After
	public void tearDown() {
		driver.quit();
	}

	/**
	 * Selectorクラスの新しいinstanceを生成します。
	 *
	 * @param selector
	 * @return newSelector
	 */
	protected Selector $(String selector){
		return new Selector(selector, driver);
	}

	/**
	 * 指定されたミリ秒数の間、スリープ (一時的に実行を停止) させます。
	 *
	 * @param mills
	 * @see <a href="http://docs.oracle.com/javase/jp/7/api/java/lang/Thread.html" target="_blank">Thread</a>
	 */
	protected void sleep(long mills){
		try{
			java.lang.Thread.sleep(mills);
		}catch(Exception e){

		}
	}

	/**
	 * WebDriverでjavascriptのalert関数を実行します。
	 *
	 * @param s
	 * @see <a href="http://selenium.googlecode.com/git/docs/api/java/org/openqa/selenium/Alert.html" target="_blank">Alert</a>
	 */
	protected void alert(String s){
		alert(s, 1000);
	}
	protected void alert(String s, long mills){
		alert(s, mills, true);
	}
	protected void alert(String s, long mills, boolean afterClose){
		jsx.executeScript("alert('" + s + "')", "");
		sleep(mills);
		if(afterClose){ closeAlertAndGetItsText(); }
	}

	/**
	 * <a href="http://selenium.googlecode.com/git/docs/api/java/org/openqa/selenium/TakesScreenshot.html" target="_blank">
	 * getScreenshotAsメソッド
	 * </a>をシンプルに扱うためのラッパーです。
	 */
	protected void saveScreenshot() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		String date = sdf.format(new Date());
		String url = driver.getCurrentUrl().replaceAll("[\\/:*\\?\"<>\\|]", "_");
		String ext = ".png";
		String path = getScreenshotSavePath() + "/" + date + "_" + url + ext;

		try {
			if (driver instanceof TakesScreenshot) {
				File tmpFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
				org.openqa.selenium.io.FileHandler.copy(tmpFile, new File(path));
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 一時ファイルディレクトリにhtmlファイルを生成します。<br />
	 * 対象サイトが存在しないテストケースに使用します。
	 *
	 * @param htmlBody htmlのbodyタグ内に出力される文字列。
	 * @return 生成されたhtmlファイルのオブジェクト。
	 */
	protected File createTemporaryHtmlFile(String htmlBody) {
		String html =
		  "<!DOCTYPE html>"
		+ "<html>"
		+ " <head>"
		+ "  <meta charset=\"UTF-8\">"
		+ "  <title>Created Temporary Html File</title>"
		+ "  <script src=\"http://code.jquery.com/jquery-1.10.1.min.js\"></script>"
		+ "  <link href=\"http://netdna.bootstrapcdn.com/bootstrap/3.0.0/css/bootstrap.min.css\" rel=\"stylesheet\">"
		+ " </head>"
		+ " <body>"
		+ htmlBody
		+ " </body>"
		+ "</html>";

		File file = null;
		try{
			file = File.createTempFile(this.getClass().getPackage().getName(), ".html");
			file.deleteOnExit();
			FileWriter writer = new FileWriter(file);
			writer.write(html);
			writer.close();
			System.out.println("HTML File Created. " + file.getAbsolutePath());
		}catch(IOException e){
			System.out.println(e);
		}

		return file;
	}

	/**
	 * 一時ファイルディレクトリにhtmlファイルを生成してブラウザで表示します。
	 *
	 * @param htmlBody
	 * @return htmlファイルのオブジェクト
	 */
	protected File createTemporaryHtmlFileAndGo(String htmlBody) {
		File htmlFile = createTemporaryHtmlFile(htmlBody);
		driver.get("file:///" + htmlFile.getAbsolutePath());
		return htmlFile;
	}

	/**
	 * alertウィンドウの存在を確認します。
	 *
	 * @see <a href="http://selenium.googlecode.com/git/docs/api/java/org/openqa/selenium/Alert.html" target="_blank">Alert</a>
	 */
	protected boolean isAlertPresent() {
		try {
			driver.switchTo().alert();
			return true;
		} catch (NoAlertPresentException e) {
			return false;
		}
	}

	/**
	 * テキストを取得してalertウィンドウを閉じます。
	 *
	 * @return alertのテキスト。
	 * @see <a href="http://selenium.googlecode.com/git/docs/api/java/org/openqa/selenium/Alert.html" target="_blank">Alert</a>
	 */
	protected String closeAlertAndGetItsText() {
		String alertText = "";
		if(isAlertPresent()){
			Alert alert = driver.switchTo().alert();
			alertText = alert.getText();
			alert.accept();
		}
		return alertText;
	}
}
