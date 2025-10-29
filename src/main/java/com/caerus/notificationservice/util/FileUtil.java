package com.caerus.notificationservice.util;

import java.nio.file.Files;
import java.nio.file.Path;
import org.springframework.stereotype.Component;

@Component
public class FileUtil {

  public static String readFileFromResources(String filepath, String fileName) throws Exception {
    return Files.readString(Path.of(filepath + fileName));
  }
}
