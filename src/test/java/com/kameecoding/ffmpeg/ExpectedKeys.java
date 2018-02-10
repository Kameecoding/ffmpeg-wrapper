/*
 *  MIT License
 *
 * <p>Copyright (c) 2018 Andrej Kovac (Kameecoding)
 *
 * <p>Permission is hereby granted, free of charge, to any person obtaining a copy of this software
 * and associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 * <p>The above copyright notice and this permission notice shall be included in all copies or
 *  substantial portions of the Software.
 *
 * <p>THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING
 * BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 */
package com.kameecoding.ffmpeg;

public enum ExpectedKeys {
    DURATION,
    VIDEO,
    AUDIO,
    SUBTITLE,
    STREAMS;

    public enum VideoStreamKeys {
        WIDTH,
        HEIGHT,
        BITRATE;

        @Override
        public String toString() {

            return super.toString().toLowerCase();
        }
    }

    public enum AudioStreamKeys {
        MAPPING,
        BITRATE,
        CODEC,
        CHANNELS,
        LANGUAGE,
        PROFILE;

        @Override
        public String toString() {
            return super.toString().toLowerCase();
        }
    }

    public enum SubtitleStreamKeys {
        MAPPING,
        LANGUAGE,
        FORCED,
        CODEC;

        @Override
        public String toString() {
            return super.toString().toLowerCase();
        }
    }

    @Override
    public String toString() {
        return super.toString().toLowerCase();
    }
}
