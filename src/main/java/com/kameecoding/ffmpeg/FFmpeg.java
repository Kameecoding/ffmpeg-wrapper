/*
 *
 *  MIT License
 *
 * <p>Copyright (c) 2018 Andrej Kovac (Kameecoding)
 *
 * <p>Permission is hereby granted, free of charge, to any person obtaining a copy of this software
 * and associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 * <p>The above copyright notice and this permission notice shall be included in all copies or
 *  substantial portions of the Software.
 *
 * <p>THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING
 * BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 */
package com.kameecoding.ffmpeg;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/** Created by kameecoding (kamee@kameecoding.com) on 2017-08-20. */
public class FFmpeg implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(FFmpeg.class);

    private ProcessBuilder processBuilder;
    private Process process;

    private boolean success;
    // private BufferedReader stdInput;
    // private BufferedReader stdError;

    private FFmpeg() {}

    public static FFmpeg newInstance(String executable, List<String> args) {
        FFmpeg ffmpeg = new FFmpeg();
        args.add(0, executable);
        ffmpeg.processBuilder = new ProcessBuilder(args);
        return ffmpeg;
    }

    @Override
    public void run() {
        try {
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
