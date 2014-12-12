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

public class SgsListUtils {
    public static <T> List<T> filter(Collection<T> list, SgsPredicate<T> predicate) {
        List<T> result = new ArrayList<T>();
        if (list != null) {
            for (T element : list) {
                if (predicate.apply(element)) {
                    result.add(element);
                }
            }
        }
        return result;
    }

    public static <T> T getFirstOrDefault(Collection<T> list, SgsPredicate<T> predicate) {
        if (list != null) {
            for (T element : list) {
                if (predicate.apply(element)) {
                    return element;
                }
            }
        }
        return null;
    }
}
