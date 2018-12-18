package ru.otus.l041;

import java.lang.management.ManagementFactory;

import javax.management.MBeanServer;
import javax.management.ObjectName;

import ru.otus.l041.Benchmark;

//-Xms256m
//-Xmx512m 
//-XX:+HeapDumpOnOutOfMemoryError
//-XX:HeapDumpPath=${workspace_loc}

public class Main {

	public static void main(String[] args) throws Exception {
		
		System.out.println("Starting PID: " + ManagementFactory.getRuntimeMXBean().getName());

        int size = 1 * 1000 * 1000;

        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
        ObjectName name = new ObjectName("ru.otus:type=Benchmark");
        Benchmark mbean = new Benchmark();
        mbs.registerMBean(mbean, name);
        
        GCMonitor.init();

        mbean.setSize(size);
        mbean.run();
	}
}
