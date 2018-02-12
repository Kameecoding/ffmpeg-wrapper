package com.kameecoding.ffmpeg;

/**
 * Created by kameecoding (kamee@kameecoding.com) on 2018-02-11.
 */
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
