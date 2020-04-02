package com.kameecoding.ffmpeg.callable;

import com.kameecoding.ffmpeg.FFmpeg;
import com.kameecoding.ffmpeg.dto.SubtitleStream;
import com.kameecoding.ffmpeg.enums.ResultType;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class SubtitleExtractor implements Callable<SubtitleExtractResult> {
  private static final Logger LOGGER = LoggerFactory.getLogger(MetadataUpdater.class);
  private Path executable;
  private Path logFile;
  private List<String> argList = new ArrayList<>();
  private Path outputFile;

  private SubtitleExtractor() {
  }

  @Override
  public SubtitleExtractResult call() throws IOException {
    SubtitleExtractResult result = new SubtitleExtractResult();
    result.result = ResultType.SUCCESS;
    FFmpeg extractor = FFmpeg.newInstance(executable, argList, logFile);
    extractor.run();
    if (Files.exists(outputFile) && Files.size(outputFile) > 2L) {
      result.output = outputFile;
    } else {
      result.result = ResultType.FAILED;
      result.errorMessage = "Failed to extract subtitle";
      LOGGER.error("Failed to exttoStract subtitle");
    }

    return result;
  }

  public static class SubtitleExtractorConfigurator {
    private SubtitleExtractor subtitleExtractor = new SubtitleExtractor();
    private Path input;
    private Path output;
    private SubtitleStream stream;

    public SubtitleExtractorConfigurator(Path executable, Path input, SubtitleStream stream) {
      subtitleExtractor.executable = executable;
      this.input = input;
      this.stream = stream;
    }

    public SubtitleExtractorConfigurator logfile(Path logfile) {
      subtitleExtractor.logFile = logfile;
      return this;
    }

    public SubtitleExtractorConfigurator output(Path output) {
      this.output = output;
      return this;
    }

    public SubtitleExtractor configure() {
      String basePath = FilenameUtils.getFullPath(input.toAbsolutePath().toString());
      if (output != null) {
        basePath = FilenameUtils.getFullPath(output.toAbsolutePath().toString());
      }
      StringBuilder output = new StringBuilder(basePath);
      output.append(FilenameUtils.getBaseName(input.toAbsolutePath().toString()));
      output.append(".").append(stream.getLanguage().getAlpha3B());
      boolean forced = stream.isForced();
      if (forced) {
        output.append(".forced");
      }
      output.append(".srt");
      String outputPath = output.toString();
      LOGGER.info("Extracting subtitle to: {}", outputPath);
      subtitleExtractor.outputFile = Paths.get(output.toString());
      subtitleExtractor.argList.add("-y");
      subtitleExtractor.argList.add("-i");
      subtitleExtractor.argList.add(input.toAbsolutePath().toString());
      subtitleExtractor.argList.add("-c");
      subtitleExtractor.argList.add("copy");
      subtitleExtractor.argList.add("-map");
      subtitleExtractor.argList.add("0:" + String.valueOf(stream.getStreamMapping()));
      subtitleExtractor.argList.add(subtitleExtractor.outputFile.toAbsolutePath().toString());

      return subtitleExtractor;
    }
  }
}
