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

package org.saycv.sgs.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class SgsObservableList<T> extends SgsObservableObject implements Observer {
    private final List<T> mList;
    private boolean mWatchValueChanged;

    public SgsObservableList(boolean watchValueChanged) {
        super();
        mList = new ArrayList<T>();
        if ((mWatchValueChanged = watchValueChanged)) {

        }
    }

    public SgsObservableList() {
        this(false);
    }

    public List<T> getList() {
        return mList;
    }

    public List<T> filter(SgsPredicate<T> predicate) {
        return SgsListUtils.filter(mList, predicate);
    }

    public boolean add(T object) {
        int location = mList.size();
        this.add(location, object);
        return true;
    }

    // FIXME: refactor
    public void add(T objects[]) {
        for (T object : objects) {
            mList.add(object);
            if (mWatchValueChanged && object instanceof Observable) {
                ((Observable) object).addObserver(this);
            }
        }
        super.setChangedAndNotifyObservers(null);
    }

    // FIXME: refactor
    public void add(Collection<T> list) {
        for (T object : list) {
            mList.add(object);
            if (mWatchValueChanged && object instanceof Observable) {
                ((Observable) object).addObserver(this);
            }
        }
        super.setChangedAndNotifyObservers(null);
    }

    public void add(int location, T object) {
        mList.add(location, object);
        if (mWatchValueChanged && object instanceof Observable) {
            ((Observable) object).addObserver(this);
        }
        super.setChangedAndNotifyObservers(null);
    }

    public T remove(int location) {
        T result = mList.remove(location);
        if (result != null && result instanceof Observable) {
            ((Observable) result).deleteObserver(this);
        }
        super.setChangedAndNotifyObservers(result);
        return result;
    }

    public boolean remove(T object) {
        if (object == null) {
            return false;
        }
        boolean result = mList.remove(object);
        if (result && object instanceof Observable) {
            ((Observable) object).deleteObserver(this);
        }
        super.setChangedAndNotifyObservers(result);
        return result;
    }

    public boolean removeAll(Collection<T> objects) {
        if (objects == null) {
            return false;
        }
        for (T object : objects) {
            if (object instanceof Observable) {
                ((Observable) object).deleteObserver(this);
            }
        }
        boolean result = mList.removeAll(objects);
        super.setChangedAndNotifyObservers(result);
        return result;
    }

    public void clear() {
        for (T object : mList) {
            if (object instanceof Observable) {
                ((Observable) object).deleteObserver(this);
            }
        }
        mList.clear();
        super.setChangedAndNotifyObservers(null);
    }

    public void setWatchValueChanged(boolean watchValueChanged) {
        mWatchValueChanged = watchValueChanged;
    }

    @Override
    public void update(Observable observable, Object data) {
        super.setChangedAndNotifyObservers(data);
    }
}
