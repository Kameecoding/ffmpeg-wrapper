package com.kameecoding.ffmpeg;

public class SubtitleExtractor implements Runnable {

    private SubtitleExtractor() {}

    public static SubtitleExtractor newInstance() {
        return new SubtitleExtractor();
    }

    @Override
    public void run() {}
}
