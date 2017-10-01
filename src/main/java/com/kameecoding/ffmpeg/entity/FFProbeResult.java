package com.kameecoding.ffmpeg.entity;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andrej Kovac (kameecoding) <andrej.kovac.ggc@gmail.com> on 2017-09-24.
 */
public class FFProbeResult {

	private final List<SubtitleStream> subtitles = new ArrayList<>();
	private final List<AudioStream> audios = new ArrayList<>();
	private final List<VideoStream> videos = new ArrayList<>();
	private Duration duration;

	public List<SubtitleStream> getSubtitles() {
		return subtitles;
	}

	public List<AudioStream> getAudios() {
		return audios;
	}

	public Duration getDuration() {
		return duration;
	}

	public void setDuration(Duration duration) {
		this.duration = duration;
	}

	public List<VideoStream> getVideos() {
		return videos;
	}
}
