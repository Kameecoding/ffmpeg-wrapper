package com.kameecoding.ffmpeg.entity;

/**
 * Created by Andrej Kovac (kameecoding) <andrej.kovac.ggc@gmail.com> on 2017-09-24.
 */
public enum AudioCodec {
        AAC("aac"),
        AC3("ac3"),
        AMR("amr"),
        EAC3("eac3"),
        MP3("mp3"),
        WMA("wma"),
        UNKOWN("unkown");


        private final String name;

        AudioCodec(String name) {
                this.name = name;
        }

        public static AudioCodec getByName(String name) {
            for (AudioCodec codec : AudioCodec.values()) {
            	if (codec.name.equals(name)) {
            		return codec;
	            }
            }

            return null;
        }

        public String getName() {
                return name;
        }
}
