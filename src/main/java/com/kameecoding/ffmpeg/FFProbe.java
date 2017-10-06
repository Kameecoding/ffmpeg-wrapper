package com.kameecoding.ffmpeg;

import com.kameecoding.ffmpeg.entity.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.Duration;
import java.util.List;


/**
 * Created by Andrej Kovac (kameecoding) <andrej.kovac.ggc@gmail.com> on 2017-08-20.
 */
public class FFProbe implements Runnable {

    private ProcessBuilder processBuilder;
    private Process process;

    private boolean success;
    private boolean finished;
    private BufferedReader stdInput;
    private BufferedReader stdError;


    FFProbe() {

    }

    public static FFProbe newInstance(String executable, String... args) {
        return new FFProbe();
    }

    @Override
    public void run() {
        try {

            process = processBuilder.start();

            stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));

            stdError = new BufferedReader(new InputStreamReader(process.getErrorStream()));

            // read the output from the command
            //System.out.println("Here is the standard output of the command:\n");
            StringBuilder sb = new StringBuilder();
            String s = null;
            while ((s = stdInput.readLine()) != null) {
                sb.append(s);
            }

            s = sb.toString();
	        parseProbe(s);
            finished = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

	FFProbeResult parseProbe(String s) {
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
                audioStreamFactory.mapping("0:" + String.valueOf(currentObject.getInt("index")));
                result.getAudios().add(audioStreamFactory.build());
            }

            if (currentObject.getString("codec_type").equals("subtitle")) {
                String mapping = "0:" + String.valueOf(currentObject.getInt("index"));
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
                result.getSubtitles().add(SubtitleStream.newInstance(mapping, language, isForced));
            }
        }

        JSONObject format = jsonObject.getJSONObject("format");

		String duration = format.getString("duration");
		Double dDuration = Double.valueOf(duration);

		result.setDuration(Duration.ofSeconds(dDuration.longValue()));

		return result;
	}
}
