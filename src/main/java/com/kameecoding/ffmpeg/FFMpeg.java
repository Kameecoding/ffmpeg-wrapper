/** 
 * MIT License 
 * 
 * Copyright (c) 2018 Andrej Kovac (Kameecoding) 
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy 
 * of this software and associated documentation files (the "Software"), to deal 
 * in the Software without restriction, including without limitation the rights 
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell 
 * copies of the Software, and to permit persons to whom the Software is 
 * furnished to do so, subject to the following conditions: 
 *  
 * The above copyright notice and this permission notice shall be included in all 
 * copies or substantial portions of the Software. 
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR 
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, 
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE 
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER 
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, 
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE 
 * SOFTWARE. 
 */
package com.kameecoding.ffmpeg;

import java.io.IOException;
import java.util.List;

/**
 * Created by Andrej Kovac (kameecoding) <kamee@kameecoding.com> on 2017-08-20.
 */
public class FFMpeg implements Runnable {
	
	private ProcessBuilder processBuilder;
	private Process process;

	private boolean success;
	//private BufferedReader stdInput;
	//private BufferedReader stdError;

	private FFMpeg() {

	}

	public static FFMpeg newInstance(String executable, List<String> args) {
		FFMpeg ffmpeg = new FFMpeg();
		args.add(0, executable);
		ffmpeg.processBuilder.command(args);
		return ffmpeg;
	}

	@Override
	public void run() {
		try {
			process = processBuilder.start();
			//stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));
			success = true;
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public boolean isSuccess() {
		return success;
	}
}
