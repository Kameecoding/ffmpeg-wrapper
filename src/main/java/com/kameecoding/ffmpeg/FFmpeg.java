package com.kameecoding.ffmpeg;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class FFmpeg implements Runnable {
  private static final Logger LOGGER = LoggerFactory.getLogger(FFmpeg.class);

  private ProcessBuilder processBuilder;
  private Process process;
  private Path logfile;
  private boolean success;

  private FFmpeg() {
  }

  public static FFmpeg newInstance(Path executable, List<String> args, Path logfile) {
    FFmpeg ffmpeg = new FFmpeg();
    if (logfile != null) {
      ffmpeg.logfile = logfile;
    }
    List<String> arguments = new ArrayList<>(args);
    arguments.add(0, executable.toAbsolutePath().toString());
    ffmpeg.processBuilder = new ProcessBuilder(arguments);
    return ffmpeg;
  }

  @Override
  public void run() {
    try {
      if (logfile != null) {
        Files.createDirectories(logfile.getParent());
        if (!Files.exists(logfile)) {
          Files.createFile(logfile);
        }
        processBuilder.redirectOutput(
            ProcessBuilder.Redirect.appendTo(logfile.toAbsolutePath().toFile()));
        processBuilder.redirectError(
            ProcessBuilder.Redirect.appendTo(logfile.toAbsolutePath().toFile()));
      } else {
        processBuilder.redirectOutput(ProcessBuilder.Redirect.INHERIT);
        processBuilder.redirectError(ProcessBuilder.Redirect.INHERIT);
      }
      process = processBuilder.start();
      process.waitFor();
      success = true;

    } catch (Exception e) {
      LOGGER.error("FFmpeg failed", e);
    }
  }

  public boolean isSuccess() {
    return success;
  }
}
