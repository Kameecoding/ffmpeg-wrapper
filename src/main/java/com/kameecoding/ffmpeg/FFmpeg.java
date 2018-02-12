package com.kameecoding.ffmpeg;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/** Created by kameecoding (kamee@kameecoding.com) on 2017-08-20. */
public class FFmpeg implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(FFmpeg.class);

    private ProcessBuilder processBuilder;
    private Process process;
    private File logfile;
    private boolean success;

    private FFmpeg() {}

    public static FFmpeg newInstance(String executable, List<String> args, File logfile) {
        FFmpeg ffmpeg = new FFmpeg();
        if (logfile != null) {
            ffmpeg.logfile = logfile;
        }
        List<String> arguments = new ArrayList<>(args);
        arguments.add(0, executable);
        ffmpeg.processBuilder = new ProcessBuilder(arguments);
        return ffmpeg;
    }

    @Override
    public void run() {
        try {
            if (logfile != null) {
                logfile.getParentFile().mkdirs();
                processBuilder.redirectOutput(logfile);
                processBuilder.redirectError(logfile);
            } else {
                processBuilder.redirectOutput(ProcessBuilder.Redirect.INHERIT);
                processBuilder.redirectError(ProcessBuilder.Redirect.INHERIT);

            }
            process = processBuilder.start();
            process.waitFor();
            // stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));
            success = true;

        } catch (Exception e) {
            LOGGER.error("FFmpeg failed", e);
        }
    }

    public boolean isSuccess() {
        return success;
    }
}
