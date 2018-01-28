/** 
 * MIT License 
 * 
 * Copyright (c) 2018 Andrej Kovac (Kameecoding) 
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy 
 * of this software and associated documentation files (the "Software"), to deal 
 * in the Software without restriction, including without limitation the rights 
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell 
 * copies of the Software, and to permit persons to whom the Software is 
 * furnished to do so, subject to the following conditions: 
 *  
 * The above copyright notice and this permission notice shall be included in all 
 * copies or substantial portions of the Software. 
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR 
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, 
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE 
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER 
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, 
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE 
 * SOFTWARE. 
 */
package com.kameecoding.ffmpeg.entity;

/**
 * Created by Andrej Kovac (kameecoding) <kamee@kameecoding.com> on 2017-08-20.
 */
public class AudioStream {

	private Language language;
	private int streamMapping;
	private AudioCodec codec;
	private String bitRate;
	private int channels;
	private String profile;

	private AudioStream() {

	}

	public static class AudioStreamFactory {
		private AudioStream audioStream;

		public AudioStreamFactory() {
			audioStream = new AudioStream();
		}

		public AudioStreamFactory language(Language language) {
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

	public Language getLanguage() {
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
