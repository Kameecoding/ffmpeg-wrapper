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

import com.kameecoding.ffmpeg.enums.ResultType;

public class OperationResult {
    protected ResultType result = ResultType.UNKNOWN;
    protected String errorMessage;
  protected Throwable cause;

    OperationResult() {}

    public ResultType getResult() {
      return result;
    }

  public boolean isSuccess() {
    return ResultType.SUCCESS == result;
  }

  public String getErrorMessage() {
    return errorMessage;
  }

  public Throwable getCause() {
    return cause;
  }
}
