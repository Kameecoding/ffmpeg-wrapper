package com.kameecoding.ffmpeg.entity;

/**
 * Created by Andrej Kovac (kameecoding) <andrej.kovac.ggc@gmail.com> on 2017-08-20.
 */
public class SubtitleStream {

    private AudioLocale language;
    private String streamMapping;

    private boolean forced;

    private SubtitleStream() {

    }

    public static SubtitleStream newInstance(String streamMapping, AudioLocale language, boolean forced) {
        SubtitleStream subtitleStream = new SubtitleStream();

        subtitleStream.streamMapping = streamMapping;
        subtitleStream.language = language;
        subtitleStream.forced = forced;

        return subtitleStream;
    }

	public AudioLocale getLanguage() {
		return language;
	}

	public String getStreamMapping() {
		return streamMapping;
	}

	public boolean isForced() {
        return forced;
    }
}
