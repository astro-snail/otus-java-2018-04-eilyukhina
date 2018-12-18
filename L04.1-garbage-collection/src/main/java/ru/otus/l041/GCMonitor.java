package ru.otus.l041;

import com.sun.management.GarbageCollectionNotificationInfo;

import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.util.List;

import javax.management.Notification;
import javax.management.NotificationEmitter;
import javax.management.NotificationFilter;
import javax.management.NotificationListener;
import javax.management.openmbean.CompositeData;

@SuppressWarnings("restriction")
public class GCMonitor {
	
	private static List<GarbageCollectorMXBean> gcBeans = ManagementFactory.getGarbageCollectorMXBeans();
	
	public static void init() {
		
		// Subscribe to garbage collector notifications
		for (GarbageCollectorMXBean gcBean : gcBeans) {			
			
			@SuppressWarnings("serial")			
			NotificationFilter filter = new NotificationFilter() {
				@Override
				public boolean isNotificationEnabled(Notification notification) {
					return notification.getType().equals(GarbageCollectionNotificationInfo.GARBAGE_COLLECTION_NOTIFICATION);
				}
			};

			NotificationListener listener = new NotificationListener() {
				
				private long totalCount = 0;
				private long totalDuration = 0;
				
				@Override
				public void handleNotification(Notification notification, Object handback) {
					CompositeData cd = (CompositeData) notification.getUserData(); 
					GarbageCollectionNotificationInfo info = GarbageCollectionNotificationInfo.from(cd);
						
					System.out.println(info.getGcName() + ", action: " + info.getGcAction() + ", cause: " + info.getGcCause());
					System.out.println("Memory before: " + info.getGcInfo().getMemoryUsageBeforeGc());
					System.out.println("Memory after: " + info.getGcInfo().getMemoryUsageAfterGc());
					System.out.println("Duration: " + info.getGcInfo().getDuration());
					
					totalCount++;
					totalDuration += info.getGcInfo().getDuration();
					
					System.out.println("Total collection count: " + totalCount);
					System.out.println("Total collection time: " + totalDuration);
				}
			};
			
			NotificationEmitter emitter = (NotificationEmitter) gcBean;
			emitter.addNotificationListener(listener, filter, null);
			
			System.out.println("Listening to: " + gcBean.getName());
		}	
	}
}
