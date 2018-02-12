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
