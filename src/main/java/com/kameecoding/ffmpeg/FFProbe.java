package com.kameecoding.ffmpeg;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.ProcessBuilder.Redirect;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class FFProbe implements Runnable {
  private static final Logger LOGGER = LoggerFactory.getLogger(FFProbe.class);

  private ProcessBuilder processBuilder;
  private boolean success = true;
  private Path logfile;
  private String output;

  private FFProbe() {
  }

  public static FFProbe newInstance(String executable, List<String> args, Path logfile) {
    FFProbe ffProbe = new FFProbe();
    if (logfile != null) {
      ffProbe.logfile = logfile;
    }
    List<String> arguments = new ArrayList<>(args);
    arguments.add(0, executable);
    ffProbe.processBuilder = new ProcessBuilder(arguments);
    return ffProbe;
  }

  @Override
  public void run() {
    try {
      LOGGER.trace("FFPRobe running");
      if (logfile != null) {
        Files.createDirectories(logfile.getParent());
        if (!Files.exists(logfile)) {
          Files.createFile(logfile);
        }
        processBuilder.redirectError(Redirect.appendTo(logfile.toFile()));
      } else {
        processBuilder.redirectError(Redirect.INHERIT);
      }
      Process process = processBuilder.start();
      BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));

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

      output = sb.toString();

      LOGGER.trace("FFPRobe Successfully finished");
    } catch (IOException e) {
      LOGGER.error("FFProbe failed", e);
      success = false;
    }
  }

  public boolean isSuccess() {
    return success;
  }

  public String getOutput() {
    return output;
  }
}
