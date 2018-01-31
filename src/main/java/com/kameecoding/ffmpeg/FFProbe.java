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
package com.kameecoding.ffmpeg;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kameecoding.ffmpeg.entity.AudioCodec;
import com.kameecoding.ffmpeg.entity.AudioStream;
import com.kameecoding.ffmpeg.entity.FFProbeResult;
import com.kameecoding.ffmpeg.entity.Language;
import com.kameecoding.ffmpeg.entity.SubtitleStream;
import com.kameecoding.ffmpeg.entity.VideoStream;

/**
 * Created by Andrej Kovac (kameecoding) <kamee@kameecoding.com> on
 * 2017-08-20.
 */
public class FFProbe implements Runnable {
	private static final Logger LOGGER = LoggerFactory.getLogger(FFProbe.class);

	private ProcessBuilder processBuilder;
	private Process process;

	private boolean success;

	private BufferedReader stdInput;
	private FFProbeResult result;

	public FFProbe() {

	}

	public static FFProbe newInstance(String executable, List<String> args) {
		FFProbe ffProbe = new FFProbe();
		List<String> arguments = new ArrayList<>(args);
		arguments.add(0, executable);
		ffProbe.processBuilder = new ProcessBuilder(arguments);
		return ffProbe;
	}

	@Override
	public void run() {
		try {
			LOGGER.info("FFPRobe running");
			//processBuilder.redirectErrorStream();
			//processBuilder.redirectError(Redirect.INHERIT);
			process = processBuilder.start();
			stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));
			
			//stdError = new BufferedReader(new InputStreamReader(process.getErrorStream()));

			// read the output from the command
			// System.out.println("Here is the standard output of the command:\n");
			boolean finished = false;
			StringBuilder sb = new StringBuilder();
			String s = null;
			while (!finished) {
				
				if (stdInput.ready()) {
					s = stdInput.readLine();
					if (s == null) {
						break;
					}
					sb.append(s);
				} else if (!process.isAlive()) {
					break;
				}
			}

			s = sb.toString();
			result = parseProbe(s);
			success = true;
			LOGGER.info("FFPRobe Successfully finished");
		} catch (IOException e) {
			LOGGER.error("FFProbe failed", e);
		}
	}

	public static FFProbeResult parseProbe(String s) {
		FFProbeResult result = new FFProbeResult();
		JSONObject jsonObject = new JSONObject(s);
		JSONArray array = jsonObject.getJSONArray("streams");
		for (int i = 0; i < array.length(); ++i) {
			JSONObject currentObject = array.getJSONObject(i);

			if (currentObject.getString("codec_type").equals("video")) {
				VideoStream.VideoStreamFactory videoStreamFactory = new VideoStream.VideoStreamFactory();
				videoStreamFactory.heigh(currentObject.getInt("height"));
				videoStreamFactory.width(currentObject.getInt("width"));
				JSONObject tags = currentObject.getJSONObject("tags");
				if (JSONUtils.hasObject(currentObject, "bit_rate")) {
					videoStreamFactory.bitrate(currentObject.getString("bit_rate"));
				} else if (JSONUtils.hasObject(tags, "BPS")) {
					videoStreamFactory.bitrate(tags.getString("BPS"));
				}
				result.getVideos().add(videoStreamFactory.build());
			}

			if (currentObject.getString("codec_type").equals("audio")) {
				AudioStream.AudioStreamFactory audioStreamFactory = new AudioStream.AudioStreamFactory();
				AudioCodec codec = AudioCodec.getByNameIgnoreCase(currentObject.getString("codec_name"));
				audioStreamFactory.codec(codec);
				JSONObject tags = currentObject.getJSONObject("tags");
				audioStreamFactory.language(Language.getByCodeIgnoreCase(tags.getString("language")));
				if (JSONUtils.hasObject(currentObject, "bit_rate")) {
					audioStreamFactory.bitrate(currentObject.getString("bit_rate"));
				} else if (JSONUtils.hasObject(tags, "BPS")) {
					audioStreamFactory.bitrate(tags.getString("BPS"));
				}
				audioStreamFactory.channels(currentObject.getInt("channels"));
				audioStreamFactory.mapping(currentObject.getInt("index"));
				if (JSONUtils.hasObject(currentObject, "profile")) {
					audioStreamFactory.profile(currentObject.getString("profile"));
				}
				result.getAudios().add(audioStreamFactory.build());
			}

			if (currentObject.getString("codec_type").equals("subtitle")) {
				String codecName = null;
				if (currentObject.has("codec_name")) {
					//SKIP subtitles that cannot be extracted for now
					codecName = currentObject.getString("codec_name");
				}
				int mapping = currentObject.getInt("index");
				JSONObject tags = currentObject.getJSONObject("tags");
				String title = null;

				if (JSONUtils.hasObject(tags, "title")) {
					title = tags.getString("title").toLowerCase();
				}

				Language language = Language.getByCodeIgnoreCase(tags.getString("language"));
				boolean isForced = false;
				JSONObject disposition = currentObject.getJSONObject("disposition");
				int forced = disposition.getInt("forced");
				isForced = forced == 1 || ("forced".equals(title));
				result.getSubtitles().add(SubtitleStream.newInstance(mapping, language, isForced, codecName));
			}
		}

		JSONObject format = jsonObject.getJSONObject("format");

		String duration = format.getString("duration");
		Double dDuration = Double.valueOf(duration);

		result.setDuration(Duration.ofSeconds(dDuration.longValue()));

		return result;
	}

	public FFProbeResult getResult() {
		return result;
	}

	public boolean isSuccess() {
		return success;
	}
}
