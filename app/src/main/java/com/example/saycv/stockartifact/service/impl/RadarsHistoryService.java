package com.example.saycv.stockartifact.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.saycv.logger.Log;
import org.saycv.sgs.SgsEngine;
import org.saycv.sgs.model.SgsHistoryEvent;
import org.saycv.sgs.model.SgsHistoryList;
import org.saycv.sgs.services.ISgsHistoryService;
import org.saycv.sgs.services.impl.SgsBaseService;

import org.saycv.sgs.utils.SgsObservableList;
import org.saycv.sgs.utils.SgsPredicate;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;


public class RadarsHistoryService extends SgsBaseService implements ISgsHistoryService {
	private final static String TAG = RadarsHistoryService.class.getCanonicalName();
	private final static String HISTORY_FILE = "RadarsHistory.xml";
	
	private File mHistoryFile;
	private SgsHistoryList mEventsList;
	private final Serializer mSerializer;
	private boolean mLoadingHistory;
	
	public RadarsHistoryService(){
		super();
		
		mSerializer = new Persister();
		mEventsList = new SgsHistoryList();
	}
	
	@Override
	public boolean start() {
		Log.d(TAG, "Starting...");
		boolean result = true;
		
		/*	http://code.google.com/p/dalvik/wiki/JavaxPackages
	     * Ensure the factory implementation is loaded from the application
	     * classpath (which contains the implementation classes), rather than the
	     * system classpath (which doesn't).
	     */
		Thread.currentThread().setContextClassLoader(getClass().getClassLoader());
		
		mHistoryFile = new File(String.format("%s/%s", SgsEngine.getInstance().getStorageService().getCurrentDir(), RadarsHistoryService.HISTORY_FILE));
		if(!mHistoryFile.exists()){
			try {
				mHistoryFile.createNewFile();
				result = compute(); /* to create an empty but valid document */
			} catch (IOException e) {
				Log.e(TAG, e.toString());
				mHistoryFile = null;
				result =  false;
			}
		}
		
		return result;
	}

	@Override
	public boolean stop() {
		Log.d(TAG, "Stopping");
		return true;
	}

	@Override
	public boolean load(){
		boolean result = true;
		
		try {
			mLoadingHistory = true;
			Log.d(TAG, "Loading history");
			mEventsList = mSerializer.read(mEventsList.getClass(), mHistoryFile);
			Log.d(TAG, "History loaded");
		} catch (Exception ex) {
			ex.printStackTrace();
			result = false;
            mLoadingHistory = false;
		}
        mLoadingHistory = false;
		return result;
	}
	
	@Override
	public boolean isLoading() {
		return mLoadingHistory;
	}

	@Override
	public void addEvent(SgsHistoryEvent event) {
		mEventsList.addEvent(event);
		new Thread(new Runnable(){
			@Override
			public void run() {
				compute();
			}
		}).start();
	}

	@Override
	public void updateEvent(SgsHistoryEvent event) {
		Log.e(TAG, "Not impleented");
		//throw new Exception("Not implemented");
        mEventsList.updateEvent(getObservableEvents(), event);
        new Thread(new Runnable(){
            @Override
            public void run() {
                compute();
            }
        }).start();
	}

	@Override
	public void deleteEvent(SgsHistoryEvent event) {
		mEventsList.removeEvent(event);
		new Thread(new Runnable(){
			@Override
			public void run() {
				compute();
			}
		}).start();
	}

	@Override
	public void deleteEvent(int location) {
		mEventsList.removeEvent(location);
		new Thread(new Runnable(){
			@Override
			public void run() {
				compute();
			}
		}).start();
	}
	
	@Override
	public void deleteEvents(SgsPredicate<SgsHistoryEvent> predicate){
		mEventsList.removeEvents(predicate);
		new Thread(new Runnable(){
			@Override
			public void run() {
				compute();
			}
		}).start();
	}

	@Override
	public void clear() {
		mEventsList.clear();
		new Thread(new Runnable(){
			@Override
			public void run() {
				compute();
			}
		}).start();
	}

	@Override
	public List<SgsHistoryEvent> getEvents() {
		return mEventsList.getList().getList();
	}
	
	@Override
	public SgsObservableList<SgsHistoryEvent> getObservableEvents() {
		return mEventsList.getList();
	}
	
	private synchronized boolean compute(){
		synchronized(this){
			if(mHistoryFile == null || mSerializer == null){
				Log.e(TAG, "Invalid arguments");
				return false;
			}
			try{
				mSerializer.write(mEventsList, mHistoryFile);
			}
			catch (Exception e) {
				e.printStackTrace();
				return false;
			}
			return true;
		}
	}
}
