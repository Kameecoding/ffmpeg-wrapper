package com.kameecoding.ffmpeg.enums;

public enum AudioCodec {
    AAC("aac"),
    AC3("ac3"),
    AMR("amr"),
    EAC3("eac3"),
    MP3("mp3"),
    WMA("wma"),
    DTS("dts"),
    TRUEHD("truehd"),
    UNKOWN("unkown");

    private final String name;

    AudioCodec(String name) {
        this.name = name;
    }

    public static AudioCodec getByNameIgnoreCase(String name) {
        return getByName(name.toLowerCase());
    }

    public static AudioCodec getByName(String name) {
        for (AudioCodec codec : AudioCodec.values()) {
            if (codec.name.equals(name)) {
                return codec;
            }
        }

        return UNKOWN;
    }

    public String getName() {
        return name;
    }
}
