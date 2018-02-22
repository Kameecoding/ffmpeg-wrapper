package com.kameecoding.ffmpeg.callable;

import com.kameecoding.ffmpeg.dto.AudioStream;
import com.kameecoding.ffmpeg.dto.SubtitleStream;
import com.kameecoding.ffmpeg.dto.VideoStream;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ProbeResult extends OperationResult {

    List<SubtitleStream> subtitles = new ArrayList<>();
    List<AudioStream> audios = new ArrayList<>();
    VideoStream videoStream;
    Duration duration;

    ProbeResult() {}

    public List<SubtitleStream> getSubtitles() {
        return Collections.unmodifiableList(subtitles);
    }

    public List<AudioStream> getAudios() {
        return Collections.unmodifiableList(audios);
    }

    public Duration getDuration() {
        return duration;
    }

    public VideoStream getVideoStream() {
        return videoStream;
    }
}
