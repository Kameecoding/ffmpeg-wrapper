package com.kameecoding.ffmpeg;

import com.kameecoding.ffmpeg.ExpectedKeys.AudioStreamKeys;
import com.kameecoding.ffmpeg.ExpectedKeys.SubtitleStreamKeys;
import com.kameecoding.ffmpeg.ExpectedKeys.VideoStreamKeys;
import com.kameecoding.ffmpeg.callable.ProbeResult;
import com.kameecoding.ffmpeg.callable.Prober;
import com.kameecoding.ffmpeg.dto.*;
import com.kameecoding.ffmpeg.enums.AudioCodec;
import com.neovisionaries.i18n.LanguageAlpha3Code;
import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class FFProbeTest {
    private static final String INPUT_PREFIX = "probe_output";
    private static final String OUTPUT_PREFIX = "expected_values";
    private static final String SUFFIX = ".json";
    private final int numberOfTests = 7;
    private Map<String, TestCase> testCases = new HashMap<>();
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private class TestCase {
        private ProbeResult probeResult;
        private JSONObject expectedResult;

        TestCase(ProbeResult probeResult, JSONObject expectedResult) {
            this.probeResult = probeResult;
            this.expectedResult = expectedResult;
        }

        ProbeResult getProbeResult() {
            return probeResult;
        }

        JSONObject getExpectedResult() {
            return expectedResult;
        }
    }

    @Before
    public void init() {
        try {
            for (int i = 0; i < numberOfTests; i++) {
                String inputName = INPUT_PREFIX + i + SUFFIX;
                String probe_output =
                        IOUtils.toString(this.getClass().getResourceAsStream(inputName), "UTF-8");
                String expected_values =
                        IOUtils.toString(
                                this.getClass().getResourceAsStream(OUTPUT_PREFIX + i + SUFFIX),
                                "UTF-8");
                TestCase testCase =
                        new TestCase(
                                Prober.parseProbe(probe_output), new JSONObject(expected_values));
                testCases.put(inputName, testCase);
            }
        } catch (Exception e) {
            logger.error("Failed to read test files", e);
        }
    }

    @Test
    public void probeOutputTest() {
        for (Map.Entry<String, TestCase> entry : testCases.entrySet()) {
            String name = entry.getKey();
            ProbeResult probeResult = entry.getValue().getProbeResult();
            JSONObject expected = entry.getValue().getExpectedResult();
            assertEquals(
                    name,
                    probeResult.getDuration(),
                    Duration.ofSeconds(expected.getLong(ExpectedKeys.DURATION.toString())));
            VideoStream videoStream = probeResult.getVideoStream();
            JSONObject video = expected.getJSONObject(ExpectedKeys.VIDEO.toString());
            testVideoStream(name, videoStream, video);
            JSONObject audio = expected.getJSONObject(ExpectedKeys.AUDIO.toString());
            testAudioStream(name, probeResult.getAudios(), audio);
            JSONObject subtitle = expected.getJSONObject(ExpectedKeys.SUBTITLE.toString());
            testSubtitleStream(name, probeResult.getSubtitles(), subtitle);
        }
    }

    private void testVideoStream(String name, VideoStream videoStream, JSONObject video) {
        assertEquals(name, video.getInt(VideoStreamKeys.WIDTH.toString()), videoStream.getWidth());
        assertEquals(
                name, video.getInt(VideoStreamKeys.HEIGHT.toString()), videoStream.getHeight());
        assertEquals(
                name,
                video.getString(VideoStreamKeys.BITRATE.toString()),
                videoStream.getBitrate());
    }

    private void testAudioStream(String name, List<AudioStream> audioStreams, JSONObject audios) {
        JSONArray array = audios.getJSONArray(ExpectedKeys.STREAMS.toString());
        assertEquals(name, array.length(), audioStreams.size());
        for (int i = 0; i < audioStreams.size(); i++) {
            JSONObject audio = array.getJSONObject(i);
            AudioStream audioStream = audioStreams.get(i);
            int mapping = audio.getInt(AudioStreamKeys.MAPPING.toString());
            assertEquals(name + " interator: " + i, mapping, audioStream.getStreamMapping());
            assertEquals(
                    name + " mapping: " + mapping,
                    audio.getString(AudioStreamKeys.BITRATE.toString()),
                    audioStream.getBitRate());
            assertEquals(
                    name + " mapping: " + mapping,
                    audio.getInt(AudioStreamKeys.CHANNELS.toString()),
                    audioStream.getChannels());
            assertEquals(
                    name + " mapping: " + mapping,
                    audio.getString(AudioStreamKeys.CODEC.toString()),
                    audioStream.getCodec().getName());
            assertEquals(
                    name + " mapping: " + mapping,
                    AudioCodec.getByNameIgnoreCase(
                            audio.getString(AudioStreamKeys.CODEC.toString())),
                    audioStream.getCodec());
            assertEquals(
                    name + " mapping: " + mapping,
                    audio.getString(AudioStreamKeys.LANGUAGE.toString()),
                    audioStream.getLanguage().getAlpha3B().name());
            assertEquals(
                    name + " mapping: " + mapping,
                    LanguageAlpha3Code.getByCodeIgnoreCase(
                            audio.getString(AudioStreamKeys.LANGUAGE.toString())),
                    audioStream.getLanguage().getAlpha3B());
            assertEquals(
                    name + " mapping: " + mapping,
                    audio.get(AudioStreamKeys.PROFILE.toString()),
                    audioStream.getProfile());
        }
    }

    private void testSubtitleStream(
            String name, List<SubtitleStream> subtitleStreams, JSONObject subtitles) {
        JSONArray array = subtitles.getJSONArray(ExpectedKeys.STREAMS.toString());
        assertEquals(name, array.length(), subtitleStreams.size());
        for (int i = 0; i < subtitleStreams.size(); i++) {
            JSONObject subtitle = array.getJSONObject(i);
            SubtitleStream subtitleStream = subtitleStreams.get(i);
            int mapping = subtitle.getInt(SubtitleStreamKeys.MAPPING.toString());
            assertEquals(name + " iterator: " + i, mapping, subtitleStream.getStreamMapping());
            assertEquals(
                    name + " mapping: " + mapping,
                    subtitle.getString(SubtitleStreamKeys.LANGUAGE.toString()),
                    subtitleStream.getLanguage().getAlpha3B().name());
            assertEquals(
                    name + " mapping: " + mapping,
                    LanguageAlpha3Code.getByCodeIgnoreCase(
                            subtitle.getString(SubtitleStreamKeys.LANGUAGE.toString())),
                    subtitleStream.getLanguage().getAlpha3B());
            assertEquals(
                    name + " mapping: " + mapping,
                    subtitle.getBoolean(SubtitleStreamKeys.FORCED.toString()),
                    subtitleStream.isForced());
            assertEquals(
                    name + " mapping: " + mapping,
                    subtitle.getString(SubtitleStreamKeys.CODEC.toString()),
                    subtitleStream.getCodecName());
        }
    }
}
