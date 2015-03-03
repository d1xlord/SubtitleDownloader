package com.googlecode.opensubtitlesjapi;

public enum STATUS {

  OK("200", "OK"),
  PARTIAL_CONTENT("206", "Partial content; message"),
  MOVED("301", "Moved (host)"),
  UNAUTHORIZED("401", "Unauthorized"),
  INVALID_FORMAT("402", "Subtitles has invalid format"),
  HASH_NOT_MATCHING("403", "SubHashes (content and sent subhash) are not same!"),
  INVALID_LANGUAGE("404", "Subtitles has invalid language!"),
  MISSING_PARAMETERS("405", "Not all mandatory parameters was specified"),
  NO_SESSION("406", "No session"),
  DOWNLOAD_LIMIT("407", "Download limit reached"),
  INVALID_PARAMETERS("408", "Invalid parameters"),
  INVALID_METHOD("409", "Method not found"),
  UNKNOWN_ERROR("410", "Other or unknown error"),
  INVALID_USER_AGENT("411", "Empty or invalid useragent"),
  INVALID_FIELD_FORMAT("412", "%s has invalid format (reason)"),
  INVALID_IMDB_ID("413", "Invalid ImdbID"),
  UNKNOWN_USER_AGENT("414", "Unknown User Agent"),
  DISABLED_USER_AGENT("415", "Disabled user agent"),
  SERVICE_UNAVAILABLE("503", "Service Unavailable");

  private String code;
  private String description;

  STATUS(String code, String description) {
    this.code = code;
    this.description = description;
  }

  public boolean isSuccess() {
    if (code.startsWith("2")) {
      return true;
    } else {
      return false;
    }
  }

  public String getCode() {
    return code;
  }

  public String getDescription() {
    return description;
  }

  @Override
  public String toString() {
    return code;
  }

  public static STATUS fromCode(String code) {
    STATUS result = null;
    for (STATUS s : STATUS.values()) {
      if (s.getCode().equals(code)) {
        result = s;
        break;
      }
    }
    return result;
  }
}
