package com.kameecoding.ffmpeg.callable;

import com.kameecoding.ffmpeg.FFmpeg;
import com.kameecoding.ffmpeg.dto.SubtitleStream;
import com.kameecoding.ffmpeg.enums.ResultType;
import com.neovisionaries.i18n.LanguageAlpha3Code;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class SubtitleExtractor implements Callable<SubtitleExtractResult> {
    private static final Logger LOGGER = LoggerFactory.getLogger(MetadataUpdater.class);
    private File executable;
    private File logFile;
    private List<String> argList = new ArrayList<>();
    private File outputFile;

    private SubtitleExtractor() {}

    @Override
    public SubtitleExtractResult call() {
        SubtitleExtractResult result = new SubtitleExtractResult();
        result.result = ResultType.SUCCESS;
        FFmpeg extractor = FFmpeg.newInstance(executable, argList, logFile);
        extractor.run();
        if (outputFile.exists() && FileUtils.sizeOf(outputFile) > 2L) {
            result.output = outputFile;
        } else {
            result.result = ResultType.FAILED;
            result.errorMessages.add("Failed to extract subtitle");
            LOGGER.error("Failed to extract subtitle");
        }

        return result;
    }

    public static class SubtitleExtractorConfigurator {
        private SubtitleExtractor subtitleExtractor = new SubtitleExtractor();
        private File input;
        private File output;
        private SubtitleStream stream;

        public SubtitleExtractorConfigurator(File executable, File input, SubtitleStream stream) {
            subtitleExtractor.executable = executable;
            this.input = input;
            this.stream = stream;
        }

        public SubtitleExtractorConfigurator logfile(File logfile) {
            subtitleExtractor.logFile = logfile;
            return this;
        }

        public SubtitleExtractorConfigurator output(File output) {
            this.output = output;
            return this;
        }

        public SubtitleExtractor configure() {
            String basePath = FilenameUtils.getFullPath(input.getAbsolutePath());
            if (output != null) {
                basePath = FilenameUtils.getFullPath(output.getAbsolutePath());
            }
            StringBuilder output = new StringBuilder(basePath);
            output.append(FilenameUtils.getBaseName(input.getAbsolutePath()));
            output.append(".").append(stream.getLanguage().getAlpha3B());
            boolean forced = stream.isForced();
            if (forced) {
                output.append(".forced");
            }
            output.append(".srt");
            String outputPath = output.toString();
            LOGGER.info("Extracting subtitle to: {}", outputPath);
            subtitleExtractor.outputFile = new File(output.toString());
            subtitleExtractor.argList.add("-y");
            subtitleExtractor.argList.add("-i");
            subtitleExtractor.argList.add(input.getAbsolutePath());
            subtitleExtractor.argList.add("-c");
            subtitleExtractor.argList.add("copy");
            subtitleExtractor.argList.add("-map");
            subtitleExtractor.argList.add("0:" + String.valueOf(stream.getStreamMapping()));
            subtitleExtractor.argList.add(subtitleExtractor.outputFile.getAbsolutePath());

            return subtitleExtractor;
        }
    }
}
