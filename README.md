# System Information Collector
A project for Project 1 course of SoICT - HUST
### 1. Topic:
Programming and building a program to collect System Information 

### 2. Description:
- Collect information about RAM, CPU, Process, Network traffic, etc.
- Works on both Windows and Linux

### 3. Features
- View performance of hardware component: CPU, RAM, Disk, Network, etc.
- View and manage (terminate) running processes.
- Locate startup apps/processes/task in host system.

### 4. Code structure
```
main/java
├── main
│   └── MainApp.java
├── network
│   └── NetworkIFTab.java
├── performance
│   ├── CPU.java
│   ├── Memory.java
│   └── Disk.java
├── startup
│   ├── CronTab.java
│   ├── LinuxApps.java
│   ├── WinApps.java
│   ├── WinStartupFolder.java
│   ├── TaskSchedule.java
│   └── autorunsc.exe
└── tabs
    ├── About.java
    ├── Performance.java
    ├── Processes.java
    ├── Services.java
    ├── Startup.java
    ├── SystemOverview.java
    └── Network.java
```

### 6. Download
You can download the JAR file directly at the [Releases page](https://github.com/Coo15/System-Information-Collector/releases).

### 5. Our team:
| Name              |  Student ID |  Email                        | 
|-------------------|-------------|-------------------------------|
| Dao Minh Quang    | 20225552    | quang.dm225552@sis.hust.edu.vn|
| Vu Duc Thang      | 20225553    | thang.vd225553@sis.hust.edu.vn|

### 6. License
Distributed under the MIT License. See [`LICENSE`](https://github.com/Coo15/System-Information-Collector/blob/main/LICENSE) for more information.
