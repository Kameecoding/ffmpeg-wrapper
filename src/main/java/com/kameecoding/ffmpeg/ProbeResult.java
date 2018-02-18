package com.kameecoding.ffmpeg;

import com.kameecoding.ffmpeg.dto.AudioStream;
import com.kameecoding.ffmpeg.dto.SubtitleStream;
import com.kameecoding.ffmpeg.dto.VideoStream;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ProbeResult {

    List<SubtitleStream> subtitles = new ArrayList<>();
    List<AudioStream> audios = new ArrayList<>();
    VideoStream videoStream;
    Duration duration;
    Result result = Result.UNKNOWN;

    ProbeResult() {
    }

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

    public Result getResult() {
        return result;
    }

    public boolean isSuccess() {
        return Result.SUCCESS == result;
    }

    public enum Result {
        SUCCESS,
        FAILED,
        UNKNOWN
    }
}
