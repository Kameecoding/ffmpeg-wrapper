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
pacakge com.kameecoding.ffmpeg.entity;

/**
 * Created by Andrej Kovac (kameecoding) <andrej.kovac.ggc@gmail.com> on 2017-09-24.
 */
public enum Language {
	ENG("eng","en"),
	HUN("hun","hu"),
	UNKNOWN("unk","un");

	private final String alpha3;
	private final String alpha2;

	Language(String code3, String code2){
		this.alpha3 = code3;
		this.alpha2 = code2;
	}

	public static Language getByCode(String locale) {
		if (locale.length() == 3) {
			return getByCode3(locale);
		} else if (locale.length() == 2) {
			return getByCode2(locale);
		} else {
			return UNKNOWN;
		}
	}

	private static Language getByCode2(String code) {
		for (Language locale : Language.values()) {
			if (locale.alpha2.equals(code)) {
				return locale;
			}
		}

		return UNKNOWN;
	}

	private static Language getByCode3(String code) {
		for (Language locale : Language.values()) {
			if (locale.alpha3.equals(code)) {
				return locale;
			}
		}

		return UNKNOWN;
	}

	public static Language getByCodeIgnoreCase(String group) {
		String lowerCase = group.toLowerCase();
		return getByCode(lowerCase);
	}

	public String getAlpha3() {
		return alpha3;
	}

	public String getAlpha2() {
		return alpha2;
	}
}
