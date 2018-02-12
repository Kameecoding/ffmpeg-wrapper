package com.kameecoding.ffmpeg;

import com.kameecoding.ffmpeg.entity.*;
import com.neovisionaries.i18n.LanguageAlpha3Code;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.ProcessBuilder.Redirect;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

/**
 * kameecoding (kamee@kameecoding.com) on 2017-08-20.
 */
public class FFProbe implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(FFProbe.class);

    private ProcessBuilder processBuilder;
    private boolean success;
    private FFProbeResult result;
    private File logfile;

    private FFProbe() {}

    public static FFProbe newInstance(String executable, List<String> args, File logfile) {
        FFProbe ffProbe = new FFProbe();
        if (logfile != null) {
            ffProbe.logfile = logfile;
        }
        List<String> arguments = new ArrayList<>(args);
        arguments.add(0, executable);
        ffProbe.processBuilder = new ProcessBuilder(arguments);
        return ffProbe;
    }

    public static FFProbeResult parseProbe(String s) {

        JSONObject jsonObject = new JSONObject(s);
        JSONArray array = jsonObject.getJSONArray("streams");
        VideoStream videoStream = null;
        try {
            videoStream = parseVideoStream(array);
        } catch (Exception e) {
            LOGGER.error("No video stream found", e);
        }
        JSONObject format = jsonObject.getJSONObject("format");
        String duration = format.getString("duration");
        Double dDuration = Double.valueOf(duration);
        FFProbeResult result =
                new FFProbeResult(videoStream, Duration.ofSeconds(dDuration.longValue()));

        for (int i = 0; i < array.length(); ++i) {
            JSONObject currentObject = array.getJSONObject(i);
            if (currentObject.getString("codec_type").equals("audio")) {
                parseAudio(result, currentObject);
            }

            if (currentObject.getString("codec_type").equals("subtitle")) {
                parseSubtitle(result, currentObject);
            }
        }

        return result;
    }

    private static void parseSubtitle(FFProbeResult result, JSONObject currentObject) {
        String codecName = null;
        if (currentObject.has("codec_name")) {
            // SKIP subtitles that cannot be extracted for now
            codecName = currentObject.getString("codec_name");
        }
        int mapping = currentObject.getInt("index");
        JSONObject tags = currentObject.getJSONObject("tags");
        String title = null;

        if (JSONUtils.hasObject(tags, "title")) {
            title = tags.getString("title").toLowerCase();
        }

        LanguageAlpha3Code language =
                LanguageAlpha3Code.getByCodeIgnoreCase(tags.getString("language"));
        JSONObject disposition = currentObject.getJSONObject("disposition");
        int forced = disposition.getInt("forced");
        boolean isForced = forced == 1 || "forced".equals(title);
        result.getSubtitles()
                .add(SubtitleStream.newInstance(mapping, language, isForced, codecName));
    }

    private static void parseAudio(FFProbeResult result, JSONObject currentObject) {
        AudioStream.AudioStreamFactory audioStreamFactory = new AudioStream.AudioStreamFactory();
        AudioCodec codec = AudioCodec.getByNameIgnoreCase(currentObject.getString("codec_name"));
        audioStreamFactory.codec(codec);
        JSONObject tags = null;
        if (JSONUtils.hasObject(currentObject, "tags")) {
            tags = currentObject.getJSONObject("tags");
        }
        if (tags != null && JSONUtils.hasObject(tags, "language")) {
            audioStreamFactory.language(
                    LanguageAlpha3Code.getByCodeIgnoreCase(tags.getString("language")));
        } else {
            audioStreamFactory.language(LanguageAlpha3Code.undefined);
        }

        if (JSONUtils.hasObject(currentObject, "bit_rate")) {
            audioStreamFactory.bitrate(currentObject.getString("bit_rate"));
        } else if (tags != null && JSONUtils.hasObject(tags, "BPS")) {
            audioStreamFactory.bitrate(tags.getString("BPS"));
        } else {
            LOGGER.error("Unable to determine audio bitrate");
            //TODO no bitrate
        }

        audioStreamFactory.channels(currentObject.getInt("channels"));
        audioStreamFactory.mapping(currentObject.getInt("index"));
        if (JSONUtils.hasObject(currentObject, "profile")) {
            audioStreamFactory.profile(currentObject.getString("profile"));
        }
        result.getAudios().add(audioStreamFactory.build());
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
    public void run() {
        try {
            LOGGER.trace("FFPRobe running");
            if (logfile != null) {
                logfile.getParentFile().mkdirs();
                processBuilder.redirectError(logfile);
            } else {
                processBuilder.redirectError(Redirect.INHERIT);
            }
            Process process = processBuilder.start();
            BufferedReader stdInput =
                    new BufferedReader(new InputStreamReader(process.getInputStream()));

            StringBuilder sb = new StringBuilder();
            String s;
            while (true) {
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
            LOGGER.trace("FFPRobe Successfully finished");
        } catch (IOException e) {
            LOGGER.error("FFProbe failed", e);
        }
    }

    public FFProbeResult getResult() {
        return result;
    }

    public boolean isSuccess() {
        return success;
    }
}
