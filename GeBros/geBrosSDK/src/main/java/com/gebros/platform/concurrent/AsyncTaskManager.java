package com.gebros.platform.concurrent;

import android.os.AsyncTask;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * @author nairs77@joycity.com
 *
 */

public class AsyncTaskManager {

	private static boolean taskManaged = false;
	private static AtomicInteger ids = new AtomicInteger(0);
	private static Map<Integer, AsyncTask<?, ?, ?>> tasks = new ConcurrentHashMap<Integer, AsyncTask<?,?,?>>();
	
	public static int registerTask(AsyncTask<?, ?, ?> task) {
		int id = ids.getAndIncrement();
		tasks.put(id, task);
		return id;
	}
	
	public static boolean isManageTask() {
		return taskManaged;
	}
	
	public static void setForcedManaged(boolean forced) {
		AsyncTaskManager.taskManaged = forced;
	}

	public static void unregisterTask(int id) {
		tasks.remove(id);
	}
	
	public static synchronized boolean clearAllTasks(long timeout, TimeUnit unit) {
		
		Set<Entry<Integer, AsyncTask<?, ?, ?>>> entrySet = tasks.entrySet();
		
		if(!entrySet.isEmpty()) {
			int count = entrySet.size();
			
			int cancelled = 0;
			
			if(count > 0) {
				
				long incrementalTime = timeout / count;
				
				for (Entry<Integer, AsyncTask<?, ?, ?>> entry : entrySet) {
					AsyncTask<?, ?, ?> task = entry.getValue();
					
					try {
						task.get(incrementalTime, unit);
						cancelled++;
					}
					catch (Exception e) {
						if(task.cancel(true)) {
							cancelled++;
						}
					}
				}
				
				tasks.clear();
			}
			
			return cancelled == count;
		}
		else {
			return true;
		}
	}
}
