package com.kameecoding.ffmpeg.entity;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

/** Created by Andrej Kovac kameecoding (kamee@kameecoding.com) on 2017-09-24. */
public class FFProbeResult {

    private final List<SubtitleStream> subtitles = new ArrayList<>();
    private final List<AudioStream> audios = new ArrayList<>();
    private VideoStream videoStream;
    private Duration duration;

    public FFProbeResult(VideoStream videoStream, Duration duration) {
        this.videoStream = videoStream;
        this.duration = duration;
    }

    public List<SubtitleStream> getSubtitles() {
        return subtitles;
    }

    public List<AudioStream> getAudios() {
        return audios;
    }

    public Duration getDuration() {
        return duration;
    }

    public VideoStream getVideoStream() {
        return videoStream;
    }
}
