package com.kameecoding.ffmpeg;

import com.kameecoding.ffmpeg.entity.AudioStream;
import com.kameecoding.ffmpeg.entity.FFProbeResult;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.*;

import java.io.IOException;

/**
 * Created by Andrej Kovac (kameecoding) <andrej.kovac.ggc@gmail.com> on 2017-09-24.
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

			/*output3 = IOUtils.toString(
					this.getClass().getResourceAsStream("probe_output3.json"),
					"UTF-8"
			);*/
		} catch (Exception e) {
			logger.error("Failed to read test files", e);
		}
	}

	@Test
	public void standardTestOutput1() {
		FFProbe probe = new FFProbe();

		FFProbeResult probeResult = probe.parseProbe(output1);

		assertEquals(2, probeResult.getAudios().size());

		AudioStream firstAudio = probeResult.getAudios().get(0);
		assertEquals("448000", firstAudio.getBitRate());
		assertEquals("ac3", firstAudio.getCodec().getName());
		assertEquals(6, firstAudio.getChannels());
		assertEquals("0:1", firstAudio.getStreamMapping());
		assertEquals("hun", firstAudio.getLanguage().getAlpha3());

		AudioStream secondAudio = probeResult.getAudios().get(1);
		assertEquals("1536000", secondAudio.getBitRate());
		assertEquals("dts", secondAudio.getCodec().getName());
		assertEquals(6, secondAudio.getChannels());
		assertEquals("0:2", secondAudio.getStreamMapping());
		assertEquals("eng", secondAudio.getLanguage().getAlpha3());
	}

	@Test
	public void standardTestOutput2() {
		FFProbe probe = new FFProbe();

		FFProbeResult probeResult = probe.parseProbe(output2);

		assertEquals(2, probeResult.getAudios().size());

		AudioStream firstAudio = probeResult.getAudios().get(0);
		assertEquals("3650025", firstAudio.getBitRate());
		assertEquals("dts", firstAudio.getCodec().getName());
		assertEquals(6, firstAudio.getChannels());
		assertEquals("0:1", firstAudio.getStreamMapping());
		assertEquals("eng", firstAudio.getLanguage().getAlpha3());

		AudioStream secondAudio = probeResult.getAudios().get(1);
		assertEquals("448000", secondAudio.getBitRate());
		assertEquals("ac3", secondAudio.getCodec().getName());
		assertEquals(6, secondAudio.getChannels());
		assertEquals("0:2", secondAudio.getStreamMapping());
		assertEquals("hun", secondAudio.getLanguage().getAlpha3());
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
}
