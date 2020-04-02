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
package com.kameecoding.ffmpeg.callable;

import java.nio.file.Path;

public class SubtitleExtractResult extends OperationResult {
  Path output;

  SubtitleExtractResult() {
  }

  public Path getOutput() {
    return output;
  }
}
