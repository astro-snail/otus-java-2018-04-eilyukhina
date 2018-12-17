package ru.otus.l041;

import com.sun.management.GarbageCollectionNotificationInfo;

import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.management.Notification;
import javax.management.NotificationEmitter;
import javax.management.NotificationListener;
import javax.management.openmbean.CompositeData;

@SuppressWarnings("restriction")
public class GCMonitor {
	
	public static void init(){
		
		List<GarbageCollectorMXBean> gcbeans = ManagementFactory.getGarbageCollectorMXBeans();

		//long totalCollections = 0L;
	    //long totalTime = 0L;
	    
	    Map<String, Map<String, Long>> metrics = new TreeMap<>();
		
		for (GarbageCollectorMXBean gcbean : gcbeans) {
			
			System.out.println(gcbean.getName() + " " + gcbean.getObjectName());
						
			metrics.put(gcbean.getName(), new TreeMap<>());
			
			NotificationEmitter emitter = (NotificationEmitter) gcbean;
			
			NotificationListener listener = new NotificationListener() {
				
				@Override
				public void handleNotification(Notification notification, Object handback) {
					
					System.out.println(notification.getType());
					
					if (notification.getType().equals(GarbageCollectionNotificationInfo.GARBAGE_COLLECTION_NOTIFICATION)) {
						GarbageCollectionNotificationInfo info = GarbageCollectionNotificationInfo.from((CompositeData) notification.getUserData());
						System.out.println(info.getGcName());
						System.out.println(info.getGcAction());
					}	
				}
			};
			
			emitter.addNotificationListener(listener, null, null);
		}	
	}
	/*
	   long totalCollections = 0L;
		    long totalTime = 0L;
		    for (GarbageCollectorMXBean mxBean : gcBeans) {
		        totalCollections += mxBean.getCollectionCount();
		        totalTime += mxBean.getCollectionTime();
		    }
	    	
	      NotificationEmitter emitter = (NotificationEmitter) gcbean;

	      NotificationListener listener = new NotificationListener() {
	        //keep a count of the total time spent in GCs
	        long totalGcDuration = 0;

	        //implement the notifier callback handler
	        @Override
	        public void handleNotification(Notification notification, Object handback) {
	          //we only handle GARBAGE_COLLECTION_NOTIFICATION notifications here
	          if (notification.getType().equals(GarbageCollectionNotificationInfo.GARBAGE_COLLECTION_NOTIFICATION)) {
	            //get the information associated with this notification
	            GarbageCollectionNotificationInfo info = GarbageCollectionNotificationInfo.from((CompositeData) notification.getUserData());
	            //get all the info and pretty print it
	            long duration = info.getGcInfo().getDuration();
	            String gctype = info.getGcAction();
	            if ("end of minor GC".equals(gctype)) {
	              gctype = "Young Gen GC";
	            } else if ("end of major GC".equals(gctype)) {
	              gctype = "Old Gen GC";
	            }
	            System.out.println();
	            System.out.println(gctype + ": - " + info.getGcInfo().getId()+ " " + info.getGcName() + " (from " + info.getGcCause()+") "+duration + " milliseconds; start-end times " + info.getGcInfo().getStartTime()+ "-" + info.getGcInfo().getEndTime());
	            //System.out.println("GcInfo CompositeType: " + info.getGcInfo().getCompositeType());
	            //System.out.println("GcInfo MemoryUsageAfterGc: " + info.getGcInfo().getMemoryUsageAfterGc());
	            //System.out.println("GcInfo MemoryUsageBeforeGc: " + info.getGcInfo().getMemoryUsageBeforeGc());

	            //Get the information about each memory space, and pretty print it
	            Map<String, MemoryUsage> membefore = info.getGcInfo().getMemoryUsageBeforeGc();
	            Map<String, MemoryUsage> mem = info.getGcInfo().getMemoryUsageAfterGc();
	            for (Entry<String, MemoryUsage> entry : mem.entrySet()) {
	              String name = entry.getKey();
	              MemoryUsage memdetail = entry.getValue();
	              long memInit = memdetail.getInit();
	              long memCommitted = memdetail.getCommitted();
	              long memMax = memdetail.getMax();
	              long memUsed = memdetail.getUsed();
	              MemoryUsage before = membefore.get(name);
	              long beforepercent = ((before.getUsed()*1000L)/before.getCommitted());
	              long percent = ((memUsed*1000L)/before.getCommitted()); //>100% when it gets expanded

	              System.out.print(name + (memCommitted==memMax?"(fully expanded)":"(still expandable)") +"used: "+(beforepercent/10)+"."+(beforepercent%10)+"%->"+(percent/10)+"."+(percent%10)+"%("+((memUsed/1048576)+1)+"MB) / ");
	            }
	            System.out.println();
	            totalGcDuration += info.getGcInfo().getDuration();
	            long percent = totalGcDuration*1000L/info.getGcInfo().getEndTime();
	            System.out.println("GC cumulated overhead "+(percent/10)+"."+(percent%10)+"%");
	          }
	        }
	      };

	      //Add the listener
	      emitter.addNotificationListener(listener, null, null);
	    }
	  }*/

}
