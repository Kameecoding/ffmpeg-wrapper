package com.kameecoding.filebot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.regex.Pattern;


/**
 * Created by Andrej Kovac (kameecoding) <andrej.kovac.ggc@gmail.com> on 2017-08-20.
 */
public class FFProbe implements Runnable {

    private ProcessBuilder processBuilder;
    private Process process;
    private Pattern subtitlePattern = Pattern.compile("Stream #([0-9]):([0-9]+)\\(([a-z]{3,7})\\):[\\W]*sub",
                                                       Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
    private String oldName;
    private String newName;
    private boolean success;
    private boolean finished;
    private BufferedReader stdInput;
    private BufferedReader stdError;
    private FFProbe() {

    }

    public static FFProbe newInstance(String executable, List<String> args) {
        return new FFProbe();
    }

    @Override
    public void run() {
        try {
            process = processBuilder.start();

            stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));

            stdError = new BufferedReader(new InputStreamReader(process.getErrorStream()));

            // read the output from the command
            //System.out.println("Here is the standard output of the command:\n");
            StringBuilder sb = new StringBuilder();
            String s = null;
            while ((s = stdInput.readLine()) != null) {
                sb.append(s);
            }

            finished = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
