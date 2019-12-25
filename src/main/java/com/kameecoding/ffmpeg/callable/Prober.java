package com.kameecoding.ffmpeg.callable;

import com.kameecoding.ffmpeg.FFProbe;
import com.kameecoding.ffmpeg.dto.AudioCodec;
import com.kameecoding.ffmpeg.dto.AudioStream;
import com.kameecoding.ffmpeg.dto.SubtitleStream;
import com.kameecoding.ffmpeg.dto.VideoStream;
import com.kameecoding.ffmpeg.enums.FFProbeOptions;
import com.kameecoding.ffmpeg.enums.ResultType;
import com.neovisionaries.i18n.LanguageAlpha3Code;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.time.Duration;
import java.util.Arrays;
import java.util.concurrent.Callable;

import static com.kameecoding.ffmpeg.enums.ResultType.FAILED;
import static com.kameecoding.ffmpeg.enums.ResultType.SUCCESS;
import static com.kameecoding.ffmpeg.enums.ResultType.UNKNOWN;

/**
 * Convenience class for probing a media file <br/>
 */
public class Prober implements Callable<ProbeResult> {
    private static final Logger LOGGER = LoggerFactory.getLogger(Prober.class);

    private File executable;
    private File input;
    private File logfile;

    private Prober() {
    }

    public static ProbeResult parseProbe(String s) {
        ProbeResult result = new ProbeResult();
        JSONObject jsonObject = new JSONObject(s);
        JSONArray array;
        try {
            array = jsonObject.getJSONArray("streams");
        } catch (Exception e) {
            LOGGER.error("No streams found in file", e);
            result.result= ResultType.FAILED;
            result.errorMessage = ExceptionUtils.getMessage(e);
            return result;
        }

        try {
            result.videoStream = parseVideoStream(array);
        } catch (Exception e) {
            LOGGER.error("No video stream found", e);
            result.result = FAILED;
            result.errorMessage = "No video stream found";
        }
        JSONObject format = jsonObject.getJSONObject("format");
        String duration = format.getString("duration");
        Double dDuration = Double.valueOf(duration);
        result.duration = Duration.ofSeconds(dDuration.longValue());

        for (int i = 0; i < array.length(); ++i) {
            JSONObject currentObject = array.getJSONObject(i);
            if (currentObject.getString("codec_type").equals("audio")) {
                parseAudio(result, currentObject);
            }

            if (currentObject.getString("codec_type").equals("subtitle")) {
                parseSubtitle(result, currentObject);
            }
        }
        if (result.result == UNKNOWN) {
            result.result = SUCCESS;
        }
        return result;
    }

    private static void parseSubtitle(ProbeResult result, JSONObject currentObject) {
        String codecName = null;
        if (currentObject.has("codec_name")) {
            // SKIP subtitles that cannot be extracted for now
            codecName = currentObject.getString("codec_name");
        }
        int mapping = currentObject.getInt("index");
        String title = null;
        LanguageAlpha3Code language = null;
        if (JSONUtils.hasObject(currentObject, "tags")) {
            JSONObject tags = currentObject.getJSONObject("tags");
            title = null;

            if (JSONUtils.hasObject(tags, "title")) {
                title = tags.getString("title").toLowerCase();
            }

            language = LanguageAlpha3Code.und;
            if (JSONUtils.hasObject(tags, "language")) {
                LanguageAlpha3Code.getByCodeIgnoreCase(tags.getString("language"));
            }
        } else {
            LOGGER.warn("No language information available");
        }

        boolean isForced = false;
        if (JSONUtils.hasObject(currentObject, "disposition")) {
            JSONObject disposition = currentObject.getJSONObject("disposition");
            int forced = disposition.getInt("forced");
            isForced = forced == 1 || "forced".equals(title);
        }
        result.subtitles.add(SubtitleStream.newInstance(mapping, language, isForced, codecName));
    }

    private static void parseAudio(ProbeResult result, JSONObject currentObject) {
        AudioStream.AudioStreamFactory audioStreamFactory = new AudioStream.AudioStreamFactory();
        AudioCodec codec = AudioCodec.getByNameIgnoreCase(currentObject.getString("codec_name"));
        audioStreamFactory.codec(codec);
        JSONObject tags = null;
        if (JSONUtils.hasObject(currentObject, "tags")) {
            tags = currentObject.getJSONObject("tags");
        }
        if (tags != null && JSONUtils.hasObject(tags, "language")) {
            audioStreamFactory.language(
                    LanguageAlpha3Code.ace.getAlpha3B().getByCodeIgnoreCase(tags.getString("language")));
        } else {
            audioStreamFactory.language(LanguageAlpha3Code.und);
        }

        if (JSONUtils.hasObject(currentObject, "bit_rate")) {
            audioStreamFactory.bitrate(currentObject.getString("bit_rate"));
        } else if (tags != null && JSONUtils.hasObject(tags, "BPS")) {
            audioStreamFactory.bitrate(tags.getString("BPS"));
        } else {
            LOGGER.error("Unable to determine audio bitrate");
            // TODO no bitrate
            // result.result = FAILED;
        }

        audioStreamFactory.channels(currentObject.getInt("channels"));
        audioStreamFactory.mapping(currentObject.getInt("index"));
        if (JSONUtils.hasObject(currentObject, "profile")) {
            audioStreamFactory.profile(currentObject.getString("profile"));
        }
        result.audios.add(audioStreamFactory.build());
    }

    private static VideoStream parseVideoStream(JSONArray array) throws Exception {
        for (int i = 0; i < array.length(); ++i) {
            JSONObject currentObject = array.getJSONObject(i);

            if (currentObject.getString("codec_type").equals("video")) {
                VideoStream.VideoStreamFactory videoStreamFactory =
                        new VideoStream.VideoStreamFactory();
                videoStreamFactory.heigh(currentObject.getInt("height"));
                videoStreamFactory.width(currentObject.getInt("width"));
                if (JSONUtils.hasObject(currentObject, "bit_rate")) {
                    videoStreamFactory.bitrate(currentObject.getString("bit_rate"));
                } else if (JSONUtils.hasObject(currentObject, "tags")
                        && JSONUtils.hasObject(currentObject.getJSONObject("tags"), "BPS")) {
                    videoStreamFactory.bitrate(
                            currentObject.getJSONObject("tags").getString("BPS"));
                }

                return videoStreamFactory.build();
            }
        }

        throw new Exception("No video stream found");
    }

    @Override
    public ProbeResult call() throws Exception {
        FFProbe probe =
                FFProbe.newInstance(
                        executable.getAbsolutePath(),
                        Arrays.asList(
                                FFProbeOptions.SHOW_FORMAT.getOpt(),
                                FFProbeOptions.SHOW_STREAMS.getOpt(),
                                FFProbeOptions.OUTPUT_FORMAT.getOpt(),
                                "json",
                                input.getAbsolutePath()), logfile);
        probe.run();
        String output = probe.getOutput();
        return parseProbe(output);
    }

    public static class ProberFactory {
        private Prober prober;

        public ProberFactory() {
            prober = new Prober();
        }

        public ProberFactory executable(String executable) {
            prober.executable = new File(executable);
            return this;
        }

        public ProberFactory executable(File executable) {
            prober.executable = executable;
            return this;
        }

        public ProberFactory input(String input) {
            prober.input = new File(input);
            return this;
        }

        public ProberFactory input(File input) {
            prober.input = input;
            return this;
        }

        public ProberFactory logfile(String logfile) {
            prober.logfile = new File(logfile);
            return this;
        }

        public ProberFactory logfile(File logfile) {
            prober.logfile = logfile;
            return this;
        }

        public Prober build() {
            return prober;
        }
    }
}
