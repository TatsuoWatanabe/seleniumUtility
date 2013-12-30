package dynamicJUnit;

import java.util.List;

import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;

import util.base.TestBase;
import util.base.TestBase.MyTestWatcher;

/**
 * 動的テストケースの生成・実行を行うクラスです。
 *
 * @author tatsuo1234567@gmail.com
 * @see <a href="http://dev.classmethod.jp/testing/unittesting/junit-dynamic-test-case-using-runner/">[Developers.IO] テストケースを動的に生成してJUnitで実行する</a>
 */
public class DynamicTestsRunner extends Runner {
	/**
	 * 実装クラスのClassオブジェクトを格納します。
	 */
	private final Class<?> cls;
	/**
	 * TestCaseGeneratorインターフェースの実装クラスのInstanceを格納します。
	 */
	private final TestCaseGenerator generator;
	/**
	 * TestCaseGeneratorから生成されたTestCaseオブジェクトのリストを格納します。
	 */
	private final List<TestCase<?, ?>> testCases;
	/**
	 * テストケースの開始・終了・成功・失敗を監視するTestWatcherクラスの実装クラスのInstanceを格納します。
	 */
	private final MyTestWatcher watcher;
	/**
	 * @param klass
	 * @throws Throwable
	 */
	public DynamicTestsRunner(Class<?> klass) throws Throwable {
		cls = klass;
		generator = (TestCaseGenerator)cls.newInstance();
		testCases = generator.generateTestCases();
		watcher = new TestBase() {
			// ログファイル出力先フォルダ名にTestCaseGeneratorの実装クラス名を設定する。
			@Override
			protected String getLogSaveDirName() { return generator.getClass().getPackage().getName(); }
		}.createTestWatcher();
	}

	/**
	 * テストクラスの{@link dynamicJUnit.TestCaseGenerator#generateTestCases generateTestCases}メソッド
	 * で生成されたTestCaseオブジェクトを実行メソッドに渡します。
	 */
	@Override
	public void run(RunNotifier notifier) {
		Description des = getDescription();
		notifier.fireTestStarted(des);
		watcher.starting(des);
		generator.setUp();
		for (TestCase<?, ?> testCase : testCases) invokeTest(notifier, testCase);
		generator.tearDown();
		notifier.fireTestFinished(des);
		watcher.finished(des);
	}

	/**
	 * テストクラスのDescriptionを生成します。
	 */
	@Override
	public Description getDescription() {
		Description des = Description.createSuiteDescription(cls.getName());
		for (TestCase<?, ?> testCase : testCases) des.addChild(getDescription(testCase.name));
		return des;
	}

	/**
	 * テストケースのDescriptionオブジェクトを返します。
	 * @param testCaseName テストケースの名前。
	 * @return テストケースのDescriptionオブジェクト。
	 */
	private Description getDescription(String testCaseName) {
		return Description.createTestDescription(cls, testCaseName);
	}

	/**
	 * テストクラスの{@link dynamicJUnit.TestCaseGenerator#invokeTestCase invokeTestCase}メソッド
	 * にTestCaseオブジェクトを渡します。<br />
	 * テストの実行結果は
	 * {@link org.junit.runner.notification.RunNotifier RunNotifier}に渡されます。
	 *
	 * @param notifier テストの開始・終了・成功・失敗を通知するRunNotifierオブジェクト。
	 * @param testCase テストの名前・期待値・テストメソッドの引数を格納するTestCaseオブジェクト。
	 */
	private void invokeTest(RunNotifier notifier, TestCase<?, ?> testCase) {
		Description des = getDescription(testCase.name);
		watcher.starting(des);
		notifier.fireTestStarted(des);
		try{
			generator.invokeTestCase(testCase);
			watcher.succeeded(des);
		} catch (AssertionError e) {
			notifier.fireTestFailure(new Failure(des, e));
			watcher.failed(e, des);
		} catch (Throwable e) {
			e.printStackTrace();
			notifier.fireTestFailure(new Failure(des, e));
			watcher.failed(e, des);
		} finally {
			notifier.fireTestFinished(des);
			watcher.finished(des);
		}
	}
}