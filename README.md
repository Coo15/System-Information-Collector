# System Information Collector
A project for Project 1 course of SoICT - HUST
### 1. Description:
The System Information Collector is a versatile application designed to monitor and display critical system and hardware information on both Windows and Linux platforms. This application provides users with real-time insights into system performance metrics, including CPU, RAM, and disk usage, as well as network traffic details. It also offers a comprehensive view of running processes, services, and startup applications/tasks, along with detailed hardware information.

### 2. Features
- View performance of hardware component: CPU, RAM, Disk, Network, etc.
- View and manage (terminate) running processes.
- Locate startup apps/processes/task in host system.

### 3. Demo
#### View CPU performance
![CPU](https://github.com/Coo15/System-Information-Collector/assets/139579187/a8a5c8a4-0ac1-422f-99a0-ae4e3a7366f5)

#### View Disk performance
![Disk](https://github.com/Coo15/System-Information-Collector/assets/139579187/ab43e6f3-f4c6-48cc-82b6-52ca23eb9bab)

#### View Network
![Network](https://github.com/Coo15/System-Information-Collector/assets/139579187/f685abcc-fd3c-47ac-9063-cfeecae75478)

#### Manage Process
![Process](https://github.com/Coo15/System-Information-Collector/assets/139579187/26518587-2372-4c4c-9aa5-ec3ad3513bae)

#### Startup
![Startup](https://github.com/Coo15/System-Information-Collector/assets/139579187/2e8f81c7-5c84-45a2-93bf-1e349fb9cca4)

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

### 5. Download
You can download the JAR file directly at the [Releases page](https://github.com/Coo15/System-Information-Collector/releases).

### 6. Our team:
| Name              |  Student ID |  Email                        | 
|-------------------|-------------|-------------------------------|
| Dao Minh Quang    | 20225552    | quang.dm225552@sis.hust.edu.vn|
| Vu Duc Thang      | 20225553    | thang.vd225553@sis.hust.edu.vn|

### 7. License
Distributed under the MIT License. See [`LICENSE`](https://github.com/Coo15/System-Information-Collector/blob/main/LICENSE) for more information.
