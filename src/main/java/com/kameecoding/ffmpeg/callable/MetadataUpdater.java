/*
 * Some open source application
 *
 * Copyright 2018 by it's authors.
 *
 * Licensed under GNU General Public License 3.0 or later.
 * Some rights reserved. See LICENSE, AUTHORS.
 *
 * @license GPL-3.0+ <https://opensource.org/licenses/GPL-3.0>
 */
package com.kameecoding.ffmpeg.callable;

import com.kameecoding.ffmpeg.FFmpeg;
import com.kameecoding.ffmpeg.dto.FileMetadata;
import com.kameecoding.ffmpeg.enums.ResultType;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.naming.OperationNotSupportedException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import static com.kameecoding.ffmpeg.enums.ResultType.FAILED;

public final class MetadataUpdater implements Callable<OperationResult> {
  private static final Logger LOGGER = LoggerFactory.getLogger(MetadataUpdater.class);

  private Path executable;
  private Path logFile;
  private Path output;
  private Path input;
  private boolean tempOutput;
  private List<String> argList = new ArrayList<>();

  private MetadataUpdater() {
  }

  @Override
  public OperationResult call() {
    OperationResult result = new OperationResult();
    result.result = ResultType.SUCCESS;
    FFmpeg metadataUpdater = FFmpeg.newInstance(executable, argList, logFile);
    metadataUpdater.run();
    try {
      if (!Files.exists(output) || Files.size(output) < 10L) {
        Files.deleteIfExists(output);
        LOGGER.error("Metadata update failed");
      }
    } catch (IOException e) {
      result.result = FAILED;
      result.errorMessage = "Metadata Updated created a small file";
      result.cause = e;
      return result;
    }

    try {
      Files.deleteIfExists(input);
    } catch (IOException e) {
      result.result = FAILED;
      result.errorMessage = "Failed to delete old file " + input.toAbsolutePath();
      result.cause = e;
      LOGGER.error("Failed to delete old file {}", input.toAbsolutePath());
      return result;
    }

    if (tempOutput) {
      try {
        Files.move(output, input, StandardCopyOption.REPLACE_EXISTING);
      } catch (IOException e) {
        result.result = FAILED;
        result.errorMessage =
            "Failed to move updated file "
                + input.toAbsolutePath()
                + " to "
                + output.toAbsolutePath();
        result.cause = e;
        LOGGER.error(
            "Failed to move updated file {} to {}",
            input.toAbsolutePath(),
            output.toAbsolutePath());
      }
    }

    return result;
  }

  public static class MetadataUpdaterConfigurator {
    private MetadataUpdater metadataUpdater = new MetadataUpdater();

    private FileMetadata fileMetadata;

    public MetadataUpdaterConfigurator(Path executable, Path input, FileMetadata fileMetadata) {
      metadataUpdater.executable = executable;
      metadataUpdater.input = input;
      this.fileMetadata = fileMetadata;
    }

    public MetadataUpdaterConfigurator logfile(Path logfile) {
      metadataUpdater.logFile = logfile;
      return this;
    }

    public MetadataUpdaterConfigurator output(Path output) throws OperationNotSupportedException {
      if (metadataUpdater.input.toAbsolutePath().equals(output.toAbsolutePath())) {
        throw new OperationNotSupportedException("input and output must be different files");
      }
      metadataUpdater.output = output;
      return this;
    }

    public MetadataUpdater configure() {
      metadataUpdater.argList.add("-y");
      metadataUpdater.argList.add("-i");
      metadataUpdater.argList.add(metadataUpdater.input.toAbsolutePath().toString());
      metadataUpdater.argList.add("-codec");
      metadataUpdater.argList.add("copy");
      metadataUpdater.argList.add("-map");
      metadataUpdater.argList.add("0");
      for (Map.Entry<String, String> metadataEntry : fileMetadata.getMetadata()) {
        metadataUpdater.argList.add("-metadata");
        metadataUpdater.argList.add(metadataEntry.getKey() + "=" + metadataEntry.getValue());
      }
      if (metadataUpdater.output != null) {
        metadataUpdater.argList.add(metadataUpdater.output.toAbsolutePath().toString());
      } else {
        metadataUpdater.tempOutput = true;
        metadataUpdater.output =
            Paths.get(
                FilenameUtils.getFullPath(metadataUpdater.input.toAbsolutePath().toString())
                    + "2"
                    + metadataUpdater.input.getFileName().toString());
        metadataUpdater.argList.add(metadataUpdater.output.toAbsolutePath().toString());
      }

      return metadataUpdater;
    }
  }
}
