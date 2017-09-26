package com.kameecoding.ffmpeg;

import com.kameecoding.ffmpeg.entity.AudioStream;
import com.kameecoding.ffmpeg.entity.FFProbeResult;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;

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

	@Before
	public void init() {

		try {
			output1 = IOUtils.toString(
					this.getClass().getResourceAsStream("probe_output1.txt"),
					"UTF-8"
			);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void standardTestOutput1() {
		FFProbe probe = new FFProbe();

		FFProbeResult probeResult = probe.parseProbe(output1);

		assertEquals(2, probeResult.getAudios().size());

		AudioStream firstAudio = probeResult.getAudios().get(0);
		assertEquals(448, firstAudio.getBitRate());
		assertEquals("ac3", firstAudio.getCodec().getName());
		assertEquals("5.1", firstAudio.getChannels());
		assertEquals("0:1", firstAudio.getStreamMapping());
		assertEquals("hun", firstAudio.getLanguage().getAlpha3());
	}
}
