package com.kameecoding.ffmpeg;

import com.kameecoding.ffmpeg.entity.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by Andrej Kovac (kameecoding) <andrej.kovac.ggc@gmail.com> on 2017-08-20.
 */
public class FFProbe implements Runnable {

    private ProcessBuilder processBuilder;
    private Process process;
    private Pattern subtitlePattern =
		    Pattern.compile("Stream #([0-9]:[0-9]+)\\(([a-z]{3,7})\\):[\\W]*(?:sub|subtitle).*?metadata:([\\s]" +
				    "*?title[\\s]*?:[\\s]*?forced)?", Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
    private Pattern audioPattern =
		    Pattern.compile("Stream #([0-9]:[0-9]+)\\(([a-z]{3,7})\\):[\\s]*?audio:[\\s]*?([a-z0-9]{3})(?:.*?)?,[\\s]*?[0-9]{4,6}[\\s]*?[a-z]*?" +
				    ",[\\s]*?([0-9\\.]{3}).*?([0-9]{3,4})[\\s]*?kb/s[\\s]*?(\\(default\\))?",
		            Pattern.DOTALL | Pattern.CASE_INSENSITIVE);

    private boolean success;
    private boolean finished;
    private BufferedReader stdInput;
    private BufferedReader stdError;

    FFProbe() {

    }

    public static FFProbe newInstance(String executable, List<String> args) {
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
		Matcher m;
		m = audioPattern.matcher(s);

		String mapping;
		int bitRate;
		AudioLocale code;
		AudioCodec codec;
		String channels;
		Locale loc = new Locale("eng");
		while (m.find()) {
			mapping = m.group(1);
			code = AudioLocale.getByCodeIgnoreCase(m.group(2));
			codec = AudioCodec.getByName(m.group(3));
			channels = m.group(4);
			bitRate = Integer.parseInt(m.group(5));
			result.getAudios().add(AudioStream.newInstance(code, mapping, codec, bitRate, channels));
		}

		m = subtitlePattern.matcher(s);

		while (m.find()) {
			result.getSubtitles().add(SubtitleStream.newInstance(m.group(1), AudioLocale.getByCode(m.group(2)), m.group(3) != null));
		    success = true;
		}

		return result;
	}
}
