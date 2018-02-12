package com.kameecoding.ffmpeg;

/** Created by Andrej Kovac kameecoding (kamee@kameecoding.com) on 2017-08-20. */
public class SubtitleExtractor implements Runnable {

    private SubtitleExtractor() {}

    public static SubtitleExtractor newInstance() {
        return new SubtitleExtractor();
    }

    @Override
    public void run() {}
}
