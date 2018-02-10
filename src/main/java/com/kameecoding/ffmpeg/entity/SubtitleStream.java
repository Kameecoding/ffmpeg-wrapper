/* * *  MIT License * * <p>Copyright (c) 2018 Andrej Kovac (Kameecoding) * * <p>Permission is hereby granted, free of charge, to any person obtaining a copy of this software * and associated documentation files (the "Software"), to deal in the Software without restriction, * including without limitation the rights to use, copy, modify, merge, publish, distribute, * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is *  furnished to do so, subject to the following conditions: * * <p>The above copyright notice and this permission notice shall be included in all copies or *  substantial portions of the Software. * * <p>THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING * BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE. * */
package com.kameecoding.ffmpeg.entity;

import com.neovisionaries.i18n.LanguageAlpha3Code;

/**
 * Immutable object describing subtitle stream
 *
 * <p>Created by Andrej Kovac kameecoding (kamee@kameecoding.com) on 2017-08-20.
 */
public class SubtitleStream {

    private LanguageAlpha3Code language;
    private int streamMapping;
    private String codecName;

    private boolean forced;

    private SubtitleStream() {}

    public static SubtitleStream newInstance(
            int streamMapping, LanguageAlpha3Code language, boolean forced, String codecName) {
        SubtitleStream subtitleStream = new SubtitleStream();

        subtitleStream.streamMapping = streamMapping;
        subtitleStream.language = language;
        subtitleStream.forced = forced;
        subtitleStream.codecName = codecName;

        return subtitleStream;
    }

    public LanguageAlpha3Code getLanguage() {
        return language;
    }

    public int getStreamMapping() {
        return streamMapping;
    }

    public boolean isForced() {
        return forced;
    }

    public String getCodecName() {
        return codecName;
    }
}
