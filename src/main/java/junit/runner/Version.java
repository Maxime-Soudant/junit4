package junit.runner;

/**
 * This class defines the current version of JUnit
 */
public class Version {
	private Version() {
		// don't instantiate
	}

	public static String id() {
	    final String ID = "4.13.3-SNAPSHOT";
		return ID;
	}
	
	public static void main(String[] args) {
		System.out.println(id());
	    //logger.log(id());
	}
}
