package com.kameecoding.filebot;

import java.util.List;

/**
 * Created by Andrej Kovac (kameecoding) <andrej.kovac.ggc@gmail.com> on 2017-08-20.
 */
public class FFMpeg implements Runnable {


    private FFMpeg() {

    }

    public static FFMpeg newInstance(String executable, List<String> args) {
        return new FFMpeg();
    }

    @Override
    public void run() {

    }
}
