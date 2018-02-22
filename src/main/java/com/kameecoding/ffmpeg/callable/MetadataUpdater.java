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
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.naming.OperationNotSupportedException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

public final class MetadataUpdater implements Callable<OperationResult> {
    private static final Logger LOGGER = LoggerFactory.getLogger(MetadataUpdater.class);

    private File executable;
    private File logFile;
    private File output;
    private File input;
    private boolean tempOutput;
    private List<String> argList = new ArrayList<>();

    private MetadataUpdater() {}

    @Override
    public OperationResult call() {
        OperationResult result = new OperationResult();
        result.result = ResultType.SUCCESS;
        FFmpeg metadataUpdater = FFmpeg.newInstance(executable, argList, logFile);
        metadataUpdater.run();
        if (!output.exists() || FileUtils.sizeOf(output) < 10L) {
            output.delete();
            LOGGER.error("Metadata update failed");
        }

        try {
            FileUtils.forceDelete(input);
        } catch (IOException e) {
            result.result = ResultType.FAILED;
            result.errorMessages.add("Failed to delete old file " + input.getAbsolutePath());
            LOGGER.error("Failed to delete old file {}", input.getAbsolutePath());
        }

        if (tempOutput) {
            try {
                FileUtils.moveFile(output, input);
            } catch (IOException e) {
                result.result = ResultType.FAILED;
                result.errorMessages.add(
                        "Failed to move updated file "
                                + input.getAbsolutePath()
                                + " to "
                                + output.getAbsolutePath());
                LOGGER.error(
                        "Failed to move updated file {} to {}",
                        input.getAbsolutePath(),
                        output.getAbsolutePath());
            }
        }

        return result;
    }

    public static class MetadataUpdaterConfigurator {
        private MetadataUpdater metadataUpdater = new MetadataUpdater();

        private FileMetadata fileMetadata;

        public MetadataUpdaterConfigurator(File executable, File input, FileMetadata fileMetadata) {
            metadataUpdater.executable = executable;
            metadataUpdater.input = input;
            this.fileMetadata = fileMetadata;
        }

        public MetadataUpdaterConfigurator logfile(File logfile) {
            metadataUpdater.logFile = logfile;
            return this;
        }

        public MetadataUpdaterConfigurator output(File output)
                throws OperationNotSupportedException {
            if (metadataUpdater.input.getAbsolutePath().equals(output.getAbsolutePath())) {
                throw new OperationNotSupportedException(
                        "input and output must be different files");
            }
            metadataUpdater.output = output;
            return this;
        }

        public MetadataUpdater configure() {
            metadataUpdater.argList.add("-y");
            metadataUpdater.argList.add("-i");
            metadataUpdater.argList.add(metadataUpdater.input.getAbsolutePath());
            metadataUpdater.argList.add("-codec");
            metadataUpdater.argList.add("copy");
            metadataUpdater.argList.add("-map");
            metadataUpdater.argList.add("0");
            for (Map.Entry<String, String> metadataEntry : fileMetadata.getMetadata()) {
                metadataUpdater.argList.add("-metadata");
                metadataUpdater.argList.add(
                        metadataEntry.getKey() + "=" + metadataEntry.getValue());
            }
            if (metadataUpdater.output != null) {
                metadataUpdater.argList.add(metadataUpdater.output.getAbsolutePath());
            } else {
                metadataUpdater.tempOutput = true;
                metadataUpdater.output =
                        new File(
                                FilenameUtils.getFullPath(metadataUpdater.input.getAbsolutePath())
                                        + "2"
                                        + metadataUpdater.input.getName());
                metadataUpdater.argList.add(metadataUpdater.output.getAbsolutePath());
            }

            return metadataUpdater;
        }
    }
}
