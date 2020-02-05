/*
 * Some open source application
 *
 * Copyright 2018 by it's authors.
 *
 * Licensed under GNU General Public License 3.0 or later.
 * Some rights reserved. See LICENSE, AUTHORS.
 *
 * @license GPL-3.0+ <https://opensource.org/licenses/GPL-3.0>
 */
package com.kameecoding.ffmpeg.dto;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class FileMetadata {
    private Map<String, String> metadata = new HashMap<>();

    public FileMetadata() {
    }

    public void setMetadata(String key, String value) {
        metadata.put(key, value);
    }

    public Set<Map.Entry<String, String>> getMetadata() {
        return metadata.entrySet();
    }
}
