package util.base;


/**
 * プロジェクト自体の情報を提供します。
 *
 * @author tatsuo1234567@gmail.com
 */
public final class ProjectInfo {

	/**
	 * プロジェクトの絶対パスが格納されます。<br />
	 * このプロパティは読み取り専用です。
	 */
	public static final String absolutePath = System.getProperty("user.dir");

    /**
     *  システムプロパティの一覧を出力します。
     */
	public static final void printProperties(){ System.getProperties().list(System.out); }

}
