package com.kameecoding.ffmpeg.entity;

/**
 * Created by Andrej Kovac (kameecoding) <andrej.kovac.ggc@gmail.com> on 2017-08-20.
 */
public class AudioStream {

	private AudioLocale language;
    private String streamMapping;
    private AudioCodec codec;
    private int bitRate;
	private String channels;

    private AudioStream() {

    }

    public static AudioStream newInstance(AudioLocale language, String streamMapping, AudioCodec codec, int bitRate, String channels) {
        AudioStream audio = new AudioStream();
	    audio.language = language;
	    audio.streamMapping = streamMapping;
	    audio.codec = codec;
	    audio.bitRate = bitRate;
	    audio.channels = channels;

        return audio;
    }

	public AudioLocale getLanguage() {
		return language;
	}

	public String getStreamMapping() {
		return streamMapping;
	}

	public AudioCodec getCodec() {
		return codec;
	}

	public int getBitRate() {
		return bitRate;
	}

	public String getChannels() {
		return channels;
	}
}
