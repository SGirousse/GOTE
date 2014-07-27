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

import java.io.File;
import java.io.StringReader;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.gote.pojo.Tournament;
import com.gote.util.ImportExportUtil;

public class ImportTournamentFromGOTE extends ImportTournament {

  /** Class logger */
  private static Logger LOGGER = Logger.getLogger(ImportTournamentFromGOTE.class.getName());

  @Override
  public Tournament createTournamentFromConfig(File pFile) {
    LOGGER.log(Level.INFO, "Loading tournament from file " + pFile);

    Tournament tournament = new Tournament();
    String content = ImportExportUtil.getFileContent(pFile);
    if (content == null) {
      LOGGER.log(Level.SEVERE, "File \"" + pFile.getPath() + "\" content is null");
      return null;
    }

    SAXReader reader = new SAXReader();
    Document document;
    try {
      document = reader.read(new StringReader(content));
    } catch (DocumentException e) {
      LOGGER.log(Level.SEVERE, "DocumentException, creation stopped : " + e);
      return null;
    }

    Element pElementTournament = document.getRootElement();

    boolean initSuccess = initTournament(tournament, pElementTournament);

    if (initSuccess) {
      return tournament;
    } else {
      return null;
    }
  }

  private boolean initTournament(Tournament pTournament, Element pElementTournament) {

    pTournament.fromXML(pElementTournament);

    return true;
  }
}
