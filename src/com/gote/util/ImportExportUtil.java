/**
 * Copyright 2014 Simeon GIROUSSE
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 *     
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.gote.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * Utility class to avoid code
 * 
 * @author sgirouss
 */
public class ImportExportUtil {

  /**
   * Read the file and returns its content
   * 
   * @param pFile File
   * @return String the content
   */
  public static String getFileContent(File pFile) {
    String content = new String();
    FileReader fileReader = null;

    try {
      fileReader = new FileReader(pFile);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
      return null;
    }

    BufferedReader bufferedReader = new BufferedReader(fileReader);
    String line = null;
    try {
      while ((line = bufferedReader.readLine()) != null) {
        content += line;
      }
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }

    try {
      fileReader.close();
    } catch (IOException e) {
      e.printStackTrace();
    }

    return content;
  }

}