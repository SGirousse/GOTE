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

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.joda.time.DateTime;

import com.gote.AppUtil;

/**
 * Utility class to avoid code
 * 
 * @author sgirouss
 */
public class ImportExportUtil {

  /** Class logger */
  private static Logger LOGGER = Logger.getLogger(ImportExportUtil.class.getName());

  /** Type GOTE */
  public static final String IMPORTEXPORT_TYPE_GOTE = "IMPORTEXPORT_TYPE_GOTE";

  /** Type EXCEL */
  public static final String IMPORTEXPORT_TYPE_EXCEL = "IMPORTEXPORT_TYPE_EXCEL";

  /** Type GOTHA */
  public static final String IMPORTEXPORT_TYPE_GOTHA = "IMPORTEXPORT_TYPE_GOTHA";

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

  /**
   * Make a copy of the current version of export before apply some updates or replacement
   * 
   * @param Path of the current document to copy
   * @return true if everything was ok during the copy process
   */
  public static boolean copyBeforeModification(Path pCurrentFile) {
    // Do not try if the current file path does not exists
    if (Files.exists(pCurrentFile, LinkOption.NOFOLLOW_LINKS)) {
      Path target = Paths.get(pCurrentFile.getParent() + "/"
          + pCurrentFile.getFileName().toString().replace(".xml", new DateTime().toString("ddMMyyyyhhmm") + ".xml"));
      try {
        Files.copy(pCurrentFile, target, REPLACE_EXISTING);
      } catch (IOException e) {
        LOGGER.log(Level.SEVERE, "Copy from " + pCurrentFile + " to " + target + " failed", e);
        return false;
      }
    }
    return true;
  }

  /**
   * URI builder which can be called anywhere in the application and so make title generation
   * generic.
   * 
   * @param pType Type of import/export
   * @param pTournament The <code>Tournament</code> title which is managed.
   * @return The URI builded
   */
  public static String buildFileURI(String pType, String pTournamentTitle) {
    String uri = AppUtil.PATH_TO_TOURNAMENTS;

    if (pType.equals(IMPORTEXPORT_TYPE_GOTE)) {
      uri += pTournamentTitle + "/" + AppUtil.PATH_TO_SAVE + pTournamentTitle.trim() + ".xml";
    } else if (pType.equals(IMPORTEXPORT_TYPE_GOTHA)) {
      uri += pTournamentTitle + "/" + AppUtil.PATH_TO_EXPORTS + "opengotha_" + pTournamentTitle + ".xml";
    } else if (pType.equals(IMPORTEXPORT_TYPE_EXCEL)) {
      uri += pTournamentTitle + "/" + AppUtil.PATH_TO_EXPORTS + "results_" + pTournamentTitle + ".xlsx";
    } else {
      LOGGER.log(Level.WARNING, "This is too much abstract for me. ");
    }
    return uri;
  }
}
