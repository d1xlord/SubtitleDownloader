/*
 *  Copyright 2011 daniele.belletti@gmail.com.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */

package com.googlecode.opensubtitlesjapi;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;
import org.apache.log4j.Logger;
import org.apache.ws.commons.util.Base64;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;

/**
 *
 * @author daniele.belletti@gmail.com
 */
public class OpenSubtitlesAPI {

  private static final Logger log = Logger.getLogger(OpenSubtitlesAPI.class);
  private static final String USER_AGENT = "OSTestUserAgent";
  //private static final String USER_AGENT = "OS Test User Agent";
  private static final String END_POINT = "http://api.opensubtitles.org/xml-rpc";
  private static final String LANGUAGE = "en";
  private XmlRpcClient client;

  public OpenSubtitlesAPI() {
    client = new XmlRpcClient();
    XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
    try {
      config.setServerURL(new URL(END_POINT));
    } catch (MalformedURLException e) {
      log.error("MalformedURLException " + END_POINT, e);
    }
    client.setConfig(config);
    client.setTypeFactory(new TypeFactory(client));
  }

  public String login(String username, String password) throws OpenSubtitlesException {
    List<String> params = new ArrayList<String>();
    params.add(username);
    params.add(password);
    params.add(LANGUAGE);
    params.add(USER_AGENT);
    Map<String, Object> result = executeAPI(API.LOGIN, params);
    if (result.get("token") == null) {
      throw new OpenSubtitlesException("login error: token is null");
    }
    String token = (String) result.get("token");
    return token;
  }

  public void logout(String token) throws OpenSubtitlesException {
    List<String> params = new ArrayList<String>();
    params.add(token);
    executeAPI(API.LOGOUT, params);
  }

  public List<Map<String, Object>> search(String token, File movie, LANGUAGE language) throws OpenSubtitlesException {
    LANGUAGE[] languages = { language };
    return search(token, movie, languages);
  }

  public List<Map<String, Object>> search(String token, File movie, LANGUAGE[] languages) throws OpenSubtitlesException {
    List<Map<String, Object>> results = new ArrayList<Map<String, Object>>();
    try {
      List<Object> params = new ArrayList<Object>();
      List<Object> searches = new ArrayList<Object>();
      Map<String, Object> search = new HashMap<String, Object>();
      StringBuilder sb = new StringBuilder(languages[0].toString().toLowerCase());
      for (int i = 1; i < languages.length; i++) {
        sb.append(',').append(languages[i].toString().toLowerCase());
      }
      search.put("sublanguageid", sb.toString());
      search.put("moviehash", OpenSubtitlesHasher.computeHash(movie));
      long l = movie.length();
      Double d = new Double(l);
      search.put("moviebytesize", d);
      searches.add(search);
      params.add(token);
      params.add(searches);
      Map<String, Object> temp = executeAPI(API.SEARCH, params);
      Object resObject = (Object) temp.get("data");
      if (resObject instanceof Object[]) {
        Object[] data = (Object[]) resObject;
        for (Object o : data) {
          results.add((Map<String, Object>) o);
        }
      } else if (resObject instanceof Boolean) {
        Boolean result = (Boolean) resObject;
        if (!result) {
          log.info("No results found for " + movie.getAbsolutePath());
        }
      } else {
        throw new Exception("Not know object type: " + resObject.getClass());
      }
    } catch (Exception e) {
      throw new OpenSubtitlesException("search error", e);
    }
    return results;
  }

  public Map<Integer,byte[]> download(String token, Integer... id) throws OpenSubtitlesException {
    Map<Integer,byte[]> result = new HashMap<Integer,byte[]>();
    List<Object> params = new ArrayList<Object>();
    List<Integer> ids = new ArrayList<Integer>();
    ids.addAll(Arrays.asList(id));
    params.add(token);
    params.add(ids);
    Map<String, Object> temp = executeAPI(API.DOWNLOAD, params);
    Object[] data = (Object[]) temp.get("data");
    try {
      for (Object o : data) {
        temp = (Map<String, Object>) o;
        byte[] decodedBytes = base64decode((String) temp.get("data"));
        byte[] subtitle = gunzip(decodedBytes);
        result.put((Integer) temp.get("idsubtitlefile"), subtitle);
      }
    } catch (Exception e) {
      throw new OpenSubtitlesException("download error",e);
    }
    return result;
  }

  private byte[] base64decode(String encoded) throws IOException {
    byte[] output = Base64.decode(encoded);
    return output;
  }

  private byte[] gunzip(byte[] compressed) throws IOException {
    GZIPInputStream gis = new GZIPInputStream(new ByteArrayInputStream(compressed));
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    OutputStream out = new BufferedOutputStream(baos);
    byte[] buffer = new byte[1024];
    while (true) {
      synchronized (buffer) {
        int amountRead = gis.read(buffer);
        if (amountRead == -1) {
          break;
        }
        out.write(buffer, 0, amountRead);
      }
    }
    out.flush();
    out.close();
    return baos.toByteArray();
  }

  private Map<String, Object> executeAPI(API api, List<?> params) throws OpenSubtitlesException {
    Map<String, Object> result = null;
    try {
      result = (Map) getClient().execute(api.toString(), params);
      String statusString = (String) result.get("status");
      STATUS status = STATUS.fromCode(getCode(statusString));
      if (!status.isSuccess()) {
        throw new OpenSubtitlesException(statusString);
      }
    } catch (Exception ex) {
      throw new OpenSubtitlesException("excuteAPI error: " + api, ex);
    }
    return result;
  }

  private String getCode(String statusMessage) {
    return statusMessage.split(" ", 2)[0];
  }

  private String getMessage(String statusMessage) {
    return statusMessage.split(" ", 2)[1];
  }

  /**
   * @return the client
   */
  private XmlRpcClient getClient() {
    return client;
  }

  private enum API {

    LOGIN("LogIn"),
    SEARCH("SearchSubtitles"),
    DOWNLOAD("DownloadSubtitles"),
    LOGOUT("LogOut");
    private String functionName;

    API(String functionName) {
      this.functionName = functionName;
    }

    @Override
    public String toString() {
      return functionName;
    }
  }
}
