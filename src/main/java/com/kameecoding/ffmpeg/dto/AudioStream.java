package com.kameecoding.ffmpeg.dto;

import com.neovisionaries.i18n.LanguageAlpha3Code;

public class AudioStream {

    private LanguageAlpha3Code language;
    private int streamMapping;
    private AudioCodec codec;
    private String bitRate;
    private int channels;
    private String profile;

    private AudioStream() {}

    public static class AudioStreamFactory {
        private AudioStream audioStream;

        public AudioStreamFactory() {
            audioStream = new AudioStream();
        }

        public AudioStreamFactory language(LanguageAlpha3Code language) {
            audioStream.language = language;
            return this;
        }

        public AudioStreamFactory mapping(int streamMapping) {
            audioStream.streamMapping = streamMapping;
            return this;
        }

        public AudioStreamFactory codec(AudioCodec codec) {
            audioStream.codec = codec;
            return this;
        }

        public AudioStreamFactory channels(int channels) {
            audioStream.channels = channels;
            return this;
        }

        public AudioStreamFactory bitrate(String bitrate) {
            audioStream.bitRate = bitrate;
            return this;
        }

        public AudioStreamFactory profile(String profile) {
            audioStream.profile = profile;
            return this;
        }

        public AudioStream build() {
            return audioStream;
        }
    }

    public LanguageAlpha3Code getLanguage() {
        return language;
    }

    public int getStreamMapping() {
        return streamMapping;
    }

    public AudioCodec getCodec() {
        return codec;
    }

    public String getBitRate() {
        return bitRate;
    }

    public int getChannels() {
        return channels;
    }

    public String getProfile() {
        return profile;
    }
}
