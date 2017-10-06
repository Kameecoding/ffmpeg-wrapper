package com.kameecoding.ffmpeg.entity;

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
