package com.kameecoding.ffmpeg;

import com.kameecoding.ffmpeg.wrapper.FFProbeOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Arrays;
import java.util.concurrent.Callable;

/**
 * Convenience class for probing a media file <br/>
 */
public class Prober implements Callable<ProbeResult> {
    private static final Logger LOGGER = LoggerFactory.getLogger(Prober.class);

    private File executable;
    private File input;
    private File logfile;

    private Prober() {
    }

    @Override
    public ProbeResult call() throws Exception {
        FFProbe probe =
                FFProbe.newInstance(
                        executable.getAbsolutePath(),
                        Arrays.asList(
                                FFProbeOptions.SHOW_FORMAT.getOpt(),
                                FFProbeOptions.SHOW_STREAMS.getOpt(),
                                FFProbeOptions.OUTPUT_FORMAT.getOpt(),
                                "json",
                                input.getAbsolutePath()), logfile);
        probe.run();
        return probe.getResult();
    }

    public static class ProberFactory {
        private Prober prober;

        public ProberFactory() {
            prober = new Prober();
        }

        public ProberFactory executable(String executable) {
            prober.executable = new File(executable);
            return this;
        }

        public ProberFactory executable(File executable) {
            prober.executable = executable;
            return this;
        }

        public ProberFactory input(String input) {
            prober.input = new File(input);
            return this;
        }

        public ProberFactory input(File input) {
            prober.input = input;
            return this;
        }

        public ProberFactory logfile(String logfile) {
            prober.logfile = new File(logfile);
            return this;
        }

        public ProberFactory logfile(File logfile) {
            prober.logfile = logfile;
            return this;
        }

        public Prober build() {
            return prober;
        }
    }
}
