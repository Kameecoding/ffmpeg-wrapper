package com.kameecoding.ffmpeg.entity;

/**
 * Immutable object describing video stream
 *
 * <p>Created by Andrej Kovac kameecoding (kamee@kameecoding.com) on 29/09/2017.
 */
public class VideoStream {

    private int width;
    private int height;
    private String bitrate;

    private VideoStream() {}

    public static class VideoStreamFactory {

        private VideoStream videoStream;

        public VideoStreamFactory() {
            videoStream = new VideoStream();
        }

        public VideoStreamFactory width(int width) {
            videoStream.width = width;
            return this;
        }

        public VideoStreamFactory heigh(int height) {
            videoStream.height = height;
            return this;
        }

        public VideoStreamFactory bitrate(String bitrate) {
            videoStream.bitrate = bitrate;
            return this;
        }

        public VideoStreamFactory duration(String duration) {

            return this;
        }

        public VideoStream build() {
            return videoStream;
        }
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public String getBitrate() {
        return bitrate;
    }
}
