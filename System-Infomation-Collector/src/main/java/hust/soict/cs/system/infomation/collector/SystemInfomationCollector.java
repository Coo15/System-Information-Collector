/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package hust.soict.cs.system.infomation.collector;

/**
 *
 * @author QUANG
 */
public class SystemInfomationCollector {

    public static void main(String[] args) {
        new SystemOverview();
        new Processes();
        new Services();
        new Network();
    }
}
