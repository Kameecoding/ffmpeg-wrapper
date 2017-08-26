package com.kameecoding.filebot.wrapper;

/**
 * Created by Andrej Kovac (kameecoding) <andrej.kovac.ggc@gmail.com> on 2017-08-20.
 */
public enum FFMpegOptions {
    overwrite("-y","overwrite output files"),
    map("-map","set input stream mapping",true);

    private final String opt;
    private final String description;
    private final boolean hasArgument;

    FFMpegOptions(String opt, String description, boolean hasArgument) {
        this.opt = opt;
        this.description = description;
        this.hasArgument = hasArgument;
    }

    FFMpegOptions(String opt, String description) {
        this.opt = opt;
        this.description = description;
        this.hasArgument = false;
    }

    public String getOpt() {
        return opt;
    }

    public boolean hasArgument() {
        return hasArgument;
    }
}
