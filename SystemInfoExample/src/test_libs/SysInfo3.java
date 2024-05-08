package test_libs;

import oshi.SystemInfo;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.util.FormatUtil;
import oshi.hardware.GlobalMemory;
import oshi.hardware.CentralProcessor;

public class SysInfo3 {
	public static void main(String[] args) {
        SystemInfo systemInfo = new SystemInfo();
        HardwareAbstractionLayer hardware = systemInfo.getHardware();

        // Memory Information
        GlobalMemory memory = hardware.getMemory();
        System.out.println("Memory Information:");
        System.out.println("Total Memory: " + FormatUtil.formatBytes(memory.getTotal()));
        System.out.println("Available Memory: " + FormatUtil.formatBytes(memory.getAvailable()));

        // Processor Information
        CentralProcessor processor = hardware.getProcessor();
        System.out.println("\nProcessor Information:");
        System.out.println("Processor Name: " + processor.getProcessorIdentifier().getName());
        System.out.println("Physical Cores: " + processor.getPhysicalProcessorCount());
        System.out.println("Logical Cores: " + processor.getLogicalProcessorCount());
	}
}