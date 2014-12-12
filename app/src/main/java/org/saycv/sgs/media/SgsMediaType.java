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

package org.saycv.sgs.media;

public enum SgsMediaType {
    None,
    Audio,
    Video,
    AudioVideo,
    SMS,
    Chat,
    FileTransfer,
    Radars;

    public static boolean isAudioVideoType(SgsMediaType type) {
        return type == Audio || type == AudioVideo || type == Video;
    }

    public static boolean isFileTransfer(SgsMediaType type) {
        return type == FileTransfer;
    }

    public static boolean isChat(SgsMediaType type) {
        return type == Chat;
    }

    public static boolean isMsrpType(SgsMediaType type) {
        return isFileTransfer(type) || isChat(type);
    }

    public static boolean isRadarsType(SgsMediaType type) {
        return type == Radars;
    }
}