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
package com.gote.importexport;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.joda.time.DateTime;

import com.gote.AppUtil;
import com.gote.pojo.Tournament;
import com.gote.util.ImportExportUtil;
import com.gote.util.xml.TournamentOpenGothaUtil;

/**
 * 
 * Here the class which handle the export to opengotha file. It update the existing file.
 * 
 * @author sgirouss
 */
public class ExportTournamentForOpenGotha extends ExportTournament {

  /** Class logger */
  private static Logger LOGGER = Logger.getLogger(ExportTournamentForOpenGotha.class.getName());

  @Override
  public void export(Tournament pTournament) {
    // Build path to opengotha file
    String openGothaExportFileURI = AppUtil.PATH_TO_TOURNAMENTS + pTournament.getTitle() + "/"
        + AppUtil.PATH_TO_EXPORTS + "opengotha_" + pTournament.getTitle() + ".xml";

    // Make a copy
    copyBeforeModidy(Paths.get(openGothaExportFileURI));

    // Get the file and its content
    File file = new File(openGothaExportFileURI);
    String content = ImportExportUtil.getFileContent(file);
    if (content == null) {
      LOGGER.log(Level.SEVERE, "File \"" + file.getPath() + "\" content is null");
      return;
    }

    // Get the Document object
    Document document = getDocumentFromContent(content);
    if (document == null) {
      return;
    }

    // Update Document
    updateDocument(document, pTournament);

    // Write Document
    updateFile(file, document);
  }

  /**
   * Save the file before deleting it
   * 
   * @param currentFile Path to the OpenGotha file to copy
   */
  private void copyBeforeModidy(Path currentFile) {
    Path target = Paths.get(currentFile.getParent() + "/"
        + currentFile.getFileName().toString().replace(".xml", new DateTime().toString("ddMMyyyyhhmm") + ".xml"));
    try {
      Files.copy(currentFile, target, REPLACE_EXISTING);
    } catch (IOException e) {
      LOGGER.log(Level.SEVERE, "Copy of opengotha from " + currentFile + " to " + target + " failed", e);
    }
  }

  /**
   * Build a Document object from File content
   * 
   * @param pContent String
   * @return Document
   */
  private Document getDocumentFromContent(String pContent) {
    SAXReader reader = new SAXReader();
    try {
      return reader.read(new StringReader(pContent));
    } catch (DocumentException e) {
      LOGGER.log(Level.SEVERE, "DocumentException, creation stopped : " + e);
      return null;
    }
  }

  /**
   * Update a document according to Tournament state
   * 
   * @param pDocument Document
   * @param pTournament Tournament
   */
  private void updateDocument(Document pDocument, Tournament pTournament) {
    Element pElementTournament = pDocument.getRootElement();
    Element elementGames = pElementTournament.element(TournamentOpenGothaUtil.TAG_GAMES);
    @SuppressWarnings("unchecked")
    List<Element> listOfGames = (List<Element>) elementGames.elements(TournamentOpenGothaUtil.TAG_GAME);
    for (Element game : listOfGames) {
      // Check if there is no result for the game before checking games
      if (game.attribute(TournamentOpenGothaUtil.ATTRIBUTE_GAME_RESULT) != null
          && !TournamentOpenGothaUtil.VALUE_GAME_RESULT_UNKNOWN.equals(game.attribute(
              TournamentOpenGothaUtil.ATTRIBUTE_GAME_RESULT).getValue())) {
        if (game.attribute(TournamentOpenGothaUtil.ATTRIBUTE_GAME_ROUND) != null
            && game.attribute(TournamentOpenGothaUtil.ATTRIBUTE_GAME_BLACK_PLAYER) != null
            && game.attribute(TournamentOpenGothaUtil.ATTRIBUTE_GAME_WHITE_PLAYER) != null) {
          game.addAttribute(
              TournamentOpenGothaUtil.ATTRIBUTE_GAME_RESULT,
              pTournament.getGameResultWithCompleteName(Integer.parseInt(game.attribute(
                  TournamentOpenGothaUtil.ATTRIBUTE_GAME_ROUND).getValue()),
                  game.attribute(TournamentOpenGothaUtil.ATTRIBUTE_GAME_BLACK_PLAYER).getValue(),
                  game.attribute(TournamentOpenGothaUtil.ATTRIBUTE_GAME_WHITE_PLAYER).getValue()));
        } else {
          LOGGER.log(Level.SEVERE,
              "Either the round number, black player name or white player name has not been found for game : " + game
                  + ". Update impossible");
        }
      }
    }
  }

  /**
   * Update a file with a Document element
   * 
   * @param pFile File
   * @param pDocument Document
   */
  private void updateFile(File pFile, Document pDocument) {
    FileWriter fileWriter;

    try {
      fileWriter = new FileWriter(pFile);
      fileWriter.write(pDocument.asXML());
      fileWriter.close();
    } catch (IOException e) {
      LOGGER.log(Level.SEVERE, "Error during writing file " + pFile.getName(), e);
    }
    LOGGER.log(Level.INFO, "File " + pFile.getName() + " updated");
  }
}