package test_libs;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.OperatingSystemMXBean;
import java.lang.management.RuntimeMXBean;

public class SysInfo2 {
	public static void main(String[] args) {
        // Get the MemoryMXBean
        MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
        // Print heap memory usage
        System.out.println("Heap Memory Usage: " + memoryMXBean.getHeapMemoryUsage());
        // Print non-heap memory usage
        System.out.println("Non-Heap Memory Usage: " + memoryMXBean.getNonHeapMemoryUsage());
        OperatingSystemMXBean osMXBean = ManagementFactory.getOperatingSystemMXBean();
        // Print the operating system name
        System.out.println("Operating System Name: " + osMXBean.getName());
        // Print the number of processors
        System.out.println("Number of Processors: " + osMXBean.getAvailableProcessors());
        // Get the RuntimeMXBean
        RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
        // Print the Java virtual machine name
        System.out.println("JVM Name: " + runtimeMXBean.getVmName());
        // Print the Java virtual machine vendor
        System.out.println("JVM Vendor: " + runtimeMXBean.getVmVendor());
        // Print the Java virtual machine version
        System.out.println("JVM Version: " + runtimeMXBean.getVmVersion());
        // Print the Java runtime name
        System.out.println("Java Runtime Name: " + runtimeMXBean.getSpecName());
        // Print the Java runtime version
        System.out.println("Java Runtime Version: " + runtimeMXBean.getSpecVersion());
        // Print the JVM uptime
        System.out.println("JVM Uptime: " + runtimeMXBean.getUptime() + " milliseconds");
    }
}
