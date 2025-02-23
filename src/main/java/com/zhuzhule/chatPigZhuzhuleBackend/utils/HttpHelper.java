package com.zhuzhule.chatPigZhuzhuleBackend.utils;

import java.io.*;
import java.nio.charset.Charset;
import javax.servlet.http.HttpServletRequest;

/**
 * @author <a href="mailto:lanxing@chances.com.cn">lanxing</a>
 */
public class HttpHelper {
  public static String getBodyString(HttpServletRequest request) throws IOException {
    StringBuilder sb = new StringBuilder();
    InputStream inputStream = null;
    BufferedReader reader = null;
    try {
      inputStream = request.getInputStream();
      reader = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("UTF-8")));

      char[] bodyCharBuffer = new char[1024];
      int len = 0;
      while ((len = reader.read(bodyCharBuffer)) != -1) {
        sb.append(new String(bodyCharBuffer, 0, len));
      }
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      if (inputStream != null) {
        try {
          inputStream.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
      if (reader != null) {
        try {
          reader.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
    return sb.toString();
  }

  //  public static String getResponse(HttpServletResponse response) throws IOException {
  //    StringBuilder sb = new StringBuilder();
  //    OutputStream outputStream = null;
  //    BufferedWriter writer = null;
  //    response.getOutputStream()
  //    try {
  //      outputStream = response.getOutputStream();
  //
  //      writer = new BufferedReader(new OutputStreamWriter(outputStream,
  // Charset.forName("UTF-8")));
  //
  //      char[] bodyCharBuffer = new char[1024];
  //      int len = 0;
  //      while ((len = writer.read(bodyCharBuffer)) != -1) {
  //        sb.append(new String(bodyCharBuffer, 0, len));
  //      }
  //    } catch (IOException e) {
  //      e.printStackTrace();
  //    } finally {
  //      if (inputStream != null) {
  //        try {
  //          inputStream.close();
  //        } catch (IOException e) {
  //          e.printStackTrace();
  //        }
  //      }
  //      if (reader != null) {
  //        try {
  //          reader.close();
  //        } catch (IOException e) {
  //          e.printStackTrace();
  //        }
  //      }
  //    }
  //    return sb.toString();
  //  }
}
