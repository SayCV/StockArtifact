/*
 * Copyright (C) 2014, sayCV.
 *
 * Copyright 2014 The sayCV's Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.saycv.sgs.services;

import java.util.List;

import org.saycv.sgs.model.SgsHistoryEvent;
import org.saycv.sgs.utils.SgsObservableList;
import org.saycv.sgs.utils.SgsPredicate;


/**@page SgsHistoryService_page History Service
 * This service is used to store/retrieve history event (audio/video, messaging, ...). You should never create or start this service by yourself. <br />
 * An instance of this service could be retrieved like this:
 * @code
 * final ISgsHistoryService mHistoryService = SgsEngine.getInstance().getHistoryService();
 * @endcode
 * 
 */
public interface ISgsHistoryService extends ISgsBaseService{
	boolean load();
	/**
	 * Checks whether the service is loading the entries
	 * @return true if the entries are being loaded and false otherwise
	 */
	boolean isLoading();
	/**
	 * Adds new event into the history. The event will be put in front of the list.
	 * @param event the event to put into the list of events
	 */
	void addEvent(SgsHistoryEvent event);
	/**
	 * Updates and event and commit the changes.
	 * @param event the event to update
	 */
    void updateEvent(SgsHistoryEvent event);
    /**
     * Deletes an event from the history list
     * @param event the event to delete
     */
    void deleteEvent(SgsHistoryEvent event);
    /**
     * Deletes an event from the history list
     * @param location the location (zero-based index) of the event to remove from the history list
     */
    void deleteEvent(final int location);
    /**
     * Deletes events matching the given criteria from the history list
     * @param predicate the predicate function used to check if an event should be deleted or not
     * @code
     * // Delete all "File Transfer" events stored in the history list
     * final ISgsHistoryService historyService = SgsEngine.getInstance().getHistoryService();
     * historyService.deleteEvents(new SgsPredicate<SgsHistoryEvent>() {
			@Override
			public boolean apply(SgsHistoryEvent event) {
				// TODO Auto-generated method stub
				return event.getMediaType() == SgsMediaType.FileTransfer;
			}
		});
     * @endcode
     */
    void deleteEvents(SgsPredicate<SgsHistoryEvent> predicate);
    /**
     * Removes all events from the history list
     */
    void clear();
    /**
     * Gets the list of all stored events
     * @return an observable collection containing all the events
     * @sa @ref getEvents()
     */
    SgsObservableList<SgsHistoryEvent> getObservableEvents();
    /**
     * Gets the list of all stored events
     * @return a collection containing all the events
     * @sa @ref getObservableEvents()
     */
    List<SgsHistoryEvent> getEvents();
}
