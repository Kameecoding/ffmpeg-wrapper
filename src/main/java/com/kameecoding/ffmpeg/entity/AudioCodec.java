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
 * Created by Andrej Kovac (kameecoding) <kamee@kameecoding.com> on 2017-09-24.
 */
public enum AudioCodec {
	AAC("aac"),
	AC3("ac3"),
	AMR("amr"),
	EAC3("eac3"),
	MP3("mp3"),
	WMA("wma"),
	DTS("dts"),
	UNKOWN("unkown");

	private final String name;

	AudioCodec(String name) {
		this.name = name;
	}

	public static AudioCodec getByNameIgnoreCase(String name) {
		return getByName(name.toLowerCase());
	}

	public static AudioCodec getByName(String name) {
		for (AudioCodec codec : AudioCodec.values()) {
			if (codec.name.equals(name)) {
				return codec;
			}
		}

		return UNKOWN;
	}

	public String getName() {
		return name;
	}
}
