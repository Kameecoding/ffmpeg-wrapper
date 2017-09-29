package com.kameecoding.ffmpeg.entity;

/**
 * Created by Andrej Kovac (kameecoding) <andrej.kovac.ggc@gmail.com> on 2017-08-20.
 */
public class AudioStream {

	private AudioLocale language;
    private String streamMapping;
    private AudioCodec codec;
    private String bitRate;
	private int channels;

    private AudioStream() {

    }

	public static class AudioStreamFactory {
    	private AudioStream audioStream;

    	public AudioStreamFactory() {
		    audioStream = new AudioStream();
	    }

	    public AudioStreamFactory language(AudioLocale language) {
		    audioStream.language = language;
    		return this;
	    }

	    public AudioStreamFactory mapping(String streamMapping) {
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

	    public AudioStream build() {
    		return audioStream;
	    }
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

	public String getBitRate() {
		return bitRate;
	}

	public int getChannels() {
		return channels;
	}
}
