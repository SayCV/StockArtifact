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

package com.example.saycv.stockartifact.events;

public enum RadarsEventTypes {
    RADARS_EVENT_1(0),
    RADARS_EVENT_2(1),
    RADARS_EVENT_3(2),
    RADARS_EVENT_4(3),
    RADARS_EVENT_5(4);

    private final int value;

    private RadarsEventTypes(int value) {
        this.value = value;
    }

    // same to Override Enum.ValueOf(class, string).ordinal()
    public int getValue() {
        return value;
    }
}
