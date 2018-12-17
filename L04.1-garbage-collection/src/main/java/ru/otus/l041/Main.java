package ru.otus.l041;

import java.lang.management.ManagementFactory;

import javax.management.MBeanServer;
import javax.management.ObjectName;

import ru.otus.l041.Benchmark;

public class Main {

	public static void main(String[] args) throws Exception {
		
		System.out.println("Starting PID: " + ManagementFactory.getRuntimeMXBean().getName());

        int size = 5 * 1000 * 1000;
        //int size = 50 * 1000 * 1000;//for OOM with -Xms512m
        //int size = 50 * 1000 * 100; //for small dump

        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
        ObjectName name = new ObjectName("ru.otus:type=Benchmark");
        Benchmark mbean = new Benchmark();
        mbs.registerMBean(mbean, name);
        
        GCMonitor.init();

        mbean.setSize(size);
        mbean.run();

        //Class<?> clazz = Object[].class;

	}

}
