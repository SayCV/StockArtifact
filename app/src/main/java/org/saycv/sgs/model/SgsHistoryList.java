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

package org.saycv.sgs.model;

import org.saycv.sgs.utils.SgsObservableList;
import org.saycv.sgs.utils.SgsPredicate;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.Collection;
import java.util.List;
import java.util.Observable;

@Root(name = "events")
public class SgsHistoryList {
    private final SgsObservableList<SgsHistoryEvent> mEvents;

    @SuppressWarnings("unused")
    @ElementList(name = "event", required = false, inline = true)
    private List<SgsHistoryEvent> mSerializableEvents;

    public SgsHistoryList() {
        mEvents = new SgsObservableList<SgsHistoryEvent>(true);
        mSerializableEvents = mEvents.getList();
    }

    public SgsObservableList<SgsHistoryEvent> getList() {
        return mEvents;
    }

    public void addEvent(SgsHistoryEvent e) {
        mEvents.add(0, e);
    }

    /*for (T object : mList) {
        if (object instanceof Observable) {
            ((Observable) object).deleteObserver(this);
        }
    }
    mList.clear();
    super.setChangedAndNotifyObservers(null);
    super.setChangedAndNotifyObservers(data);*/
    public void updateEvent(Observable observable, SgsHistoryEvent e) {
        if (mEvents != null) {
            mEvents.update(observable, e);
        }
    }

    public void removeEvent(SgsHistoryEvent e) {
        if (mEvents != null) {
            mEvents.remove(e);
        }
    }

    public void removeEvents(Collection<SgsHistoryEvent> events) {
        if (mEvents != null) {
            mEvents.removeAll(events);
        }
    }

    public void removeEvents(SgsPredicate<SgsHistoryEvent> predicate) {
        if (mEvents != null) {
            final List<SgsHistoryEvent> eventsToRemove = mEvents.filter(predicate);
            mEvents.removeAll(eventsToRemove);
        }
    }

    public void removeEvent(int location) {
        if (mEvents != null) {
            mEvents.remove(location);
        }
    }

    public void clear() {
        if (mEvents != null) {
            mEvents.clear();
        }
    }


}
