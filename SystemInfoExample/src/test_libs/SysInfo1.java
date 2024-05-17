package test_libs;

public class SysInfo1 {
	public static void main(String[] args) {
		// Get the operating system name
        String osName = System.getProperty("os.name");
        System.out.println("Operating System: " + osName);

        // Get the operating system version
        String osVersion = System.getProperty("os.version");
        System.out.println("OS Version: " + osVersion);

        // Get the operating system architecture
        String osArch = System.getProperty("os.arch");
        System.out.println("OS Architecture: " + osArch);

        // Get the Java version
        String javaVersion = System.getProperty("java.version");
        System.out.println("Java Version: " + javaVersion);

        // Get the Java vendor
        String javaVendor = System.getProperty("java.vendor");
        System.out.println("Java Vendor: " + javaVendor);

        // Get the Java home directory
        String javaHome = System.getProperty("java.home");
        System.out.println("Java Home: " + javaHome);

        // Get the user's home directory
        String userHome = System.getProperty("user.home");
        System.out.println("User Home: " + userHome);

        // Get the user's current working directory
        String userDir = System.getProperty("user.dir");
        System.out.println("User Directory: " + userDir);
    }
}

