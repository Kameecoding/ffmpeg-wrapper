package com.kameecoding.ffmpeg.entity;

/**
 * Created by Andrej Kovac (kameecoding) <andrej.kovac.ggc@gmail.com> on 2017-08-20.
 */
public class SubtitleStream {

    private AudioLocale language;
    private String mappingCoordinates;

    private boolean forced;

    private SubtitleStream() {

    }

    public static SubtitleStream newInstance(String mappingCoordinates, AudioLocale language, boolean forced) {
        SubtitleStream subtitleStream = new SubtitleStream();

        subtitleStream.mappingCoordinates = mappingCoordinates;
        subtitleStream.language = language;
        subtitleStream.forced = forced;

        return subtitleStream;
    }

    public boolean isForced() {
        return forced;
    }
}
