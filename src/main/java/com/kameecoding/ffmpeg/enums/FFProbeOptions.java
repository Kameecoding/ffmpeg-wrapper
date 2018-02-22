package com.kameecoding.ffmpeg.enums;

public enum FFProbeOptions {
    SHOW_FORMAT("-show_format", ""),
    SHOW_STREAMS("-show_streams", ""),
    OUTPUT_FORMAT("-of", "", true);

    private final String opt;
    private final String description;
    private final boolean hasArgument;

    FFProbeOptions(String opt, String description, boolean hasArgument) {
        this.opt = opt;
        this.description = description;
        this.hasArgument = hasArgument;
    }

    FFProbeOptions(String opt, String description) {
        this.opt = opt;
        this.description = description;
        this.hasArgument = false;
    }

    public String getOpt() {
        return opt;
    }

    public String getDescription() {
        return description;
    }

    public boolean hasArgument() {
        return hasArgument;
    }
}
