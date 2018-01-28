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

import com.kameecoding.ffmpeg.entity.AudioStream;
import com.kameecoding.ffmpeg.entity.FFProbeResult;
import com.kameecoding.ffmpeg.entity.SubtitleStream;
import com.kameecoding.ffmpeg.entity.VideoStream;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by Andrej Kovac (kameecoding) <kamee@kameecoding.com> on 2017-09-24.
 */
public class FFProbeTest {
	private String output1;
	private String output2;
	private String output3;
	private String output4;
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Before
	public void init() {

		try {
			output1 = IOUtils.toString(
					this.getClass().getResourceAsStream("probe_output1.json"),
					"UTF-8"
			);

			output2 = IOUtils.toString(
					this.getClass().getResourceAsStream("probe_output2.json"),
					"UTF-8"
			);

			output3 = IOUtils.toString(
					this.getClass().getResourceAsStream("probe_output3.json"),
					"UTF-8"
			);
			
			output4 = IOUtils.toString(
					this.getClass().getResourceAsStream("probe_output4.json"),
					"UTF-8"
			);
		} catch (Exception e) {
			logger.error("Failed to read test files", e);
		}
	}

	@Test
	public void standardTestOutput1() {
		FFProbe probe = new FFProbe();

		FFProbeResult probeResult = probe.parseProbe(output1);

		assertEquals(probeResult.getDuration(), Duration.ofSeconds(6647L));

		assertEquals(1, probeResult.getVideos().size());
		VideoStream videoStream = probeResult.getVideos().get(0);
		assertEquals(1920, videoStream.getWidth());
		assertEquals(804, videoStream.getHeight());
		assertEquals("12191830", videoStream.getBitrate());

		assertEquals(2, probeResult.getAudios().size());

		AudioStream firstAudio = probeResult.getAudios().get(0);
		assertEquals("448000", firstAudio.getBitRate());
		assertEquals("ac3", firstAudio.getCodec().getName());
		assertEquals(6, firstAudio.getChannels());
		assertEquals(1, firstAudio.getStreamMapping());
		assertEquals("hun", firstAudio.getLanguage().getAlpha3());
		assertNull(firstAudio.getProfile());

		AudioStream secondAudio = probeResult.getAudios().get(1);
		assertEquals("1536000", secondAudio.getBitRate());
		assertEquals("dts", secondAudio.getCodec().getName());
		assertEquals(6, secondAudio.getChannels());
		assertEquals(2, secondAudio.getStreamMapping());
		assertEquals("eng", secondAudio.getLanguage().getAlpha3());
		assertEquals("DTS", secondAudio.getProfile());

		assertEquals(3, probeResult.getSubtitles().size());

		SubtitleStream firstSub = probeResult.getSubtitles().get(0);
		assertEquals(3, firstSub.getStreamMapping());
		assertEquals("hun", firstSub.getLanguage().getAlpha3());
		assertTrue(firstSub.isForced());

		SubtitleStream secondSub = probeResult.getSubtitles().get(1);
		assertEquals(4, secondSub.getStreamMapping());
		assertEquals("hun", secondSub.getLanguage().getAlpha3());
		assertFalse(secondSub.isForced());

		SubtitleStream thirdSub = probeResult.getSubtitles().get(2);
		assertEquals(5, thirdSub.getStreamMapping());
		assertEquals("eng", thirdSub.getLanguage().getAlpha3());
		assertFalse(thirdSub.isForced());
	}

	@Test
	public void standardTestOutput2() {
		FFProbe probe = new FFProbe();

		FFProbeResult probeResult = probe.parseProbe(output2);
		assertEquals(probeResult.getDuration(), Duration.ofSeconds(7134L));

		assertEquals(1, probeResult.getVideos().size());
		VideoStream videoStream = probeResult.getVideos().get(0);
		assertEquals(3840, videoStream.getWidth());
		assertEquals(1606, videoStream.getHeight());
		assertEquals("61000888", videoStream.getBitrate());

		assertEquals(2, probeResult.getAudios().size());
		AudioStream firstAudio = probeResult.getAudios().get(0);
		assertEquals("3650025", firstAudio.getBitRate());
		assertEquals("dts", firstAudio.getCodec().getName());
		assertEquals(6, firstAudio.getChannels());
		assertEquals(1, firstAudio.getStreamMapping());
		assertEquals("eng", firstAudio.getLanguage().getAlpha3());
		assertEquals("DTS-HD MA",firstAudio.getProfile());

		AudioStream secondAudio = probeResult.getAudios().get(1);
		assertEquals("448000", secondAudio.getBitRate());
		assertEquals("ac3", secondAudio.getCodec().getName());
		assertEquals(6, secondAudio.getChannels());
		assertEquals(2, secondAudio.getStreamMapping());
		assertEquals("hun", secondAudio.getLanguage().getAlpha3());
		assertNull(secondAudio.getProfile());

		assertEquals(3, probeResult.getSubtitles().size());

		SubtitleStream firstSub = probeResult.getSubtitles().get(0);
		assertEquals(3, firstSub.getStreamMapping());
		assertEquals("hun", firstSub.getLanguage().getAlpha3());
		assertFalse(firstSub.isForced());

		SubtitleStream secondSub = probeResult.getSubtitles().get(1);
		assertEquals(4, secondSub.getStreamMapping());
		assertEquals("hun", secondSub.getLanguage().getAlpha3());
		assertTrue(secondSub.isForced());

		SubtitleStream thirdSub = probeResult.getSubtitles().get(2);
		assertEquals(5, thirdSub.getStreamMapping());
		assertEquals("eng", thirdSub.getLanguage().getAlpha3());
		assertFalse(thirdSub.isForced());
	}

	public void standardTestOutput3() {
		FFProbe probe = new FFProbe();

		FFProbeResult probeResult = probe.parseProbe(output3);

		assertEquals(1, probeResult.getAudios().size());

		AudioStream firstAudio = probeResult.getAudios().get(0);
		assertEquals("448000", firstAudio.getBitRate());
		assertEquals("aac", firstAudio.getCodec().getName());
		assertEquals(2, firstAudio.getChannels());
		assertEquals("0:1", firstAudio.getStreamMapping());
		assertEquals("jpn", firstAudio.getLanguage().getAlpha3());
	}
	
	public void standardTestOutput4() {
		
	}
	
	public void standardTestOutput5() {
	
	}
}
