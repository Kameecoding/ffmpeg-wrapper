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
pacakge com.kameecoding.ffmpeg.entity;

/**
 * Created by Andrej Kovac (kameecoding) <andrej.kovac.ggc@gmail.com> on 29/09/2017.
 */
public class VideoStream {

	private int width;
	private int height;
	private String bitrate;


	private VideoStream() {

	}

	public static class VideoStreamFactory {

		private VideoStream videoStream;

		public VideoStreamFactory() {
			videoStream = new VideoStream();
		}

		public VideoStreamFactory width(int width) {
			videoStream.width = width;
			return this;
		}

		public VideoStreamFactory heigh(int height) {
			videoStream.height = height;
			return this;
		}

		public VideoStreamFactory bitrate(String bitrate) {
			videoStream.bitrate = bitrate;
			return this;
		}

		public VideoStreamFactory duration(String duration) {

			return this;
		}

		public VideoStream build() {
			return videoStream;
		}
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public String getBitrate() {
		return bitrate;
	}
}
