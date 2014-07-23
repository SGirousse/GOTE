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

import java.util.logging.Logger;

import com.gote.pojo.Tournament;

/**
 * 
 * Handle the export of the tournament to GOTE format
 * 
 * @author sgirouss
 */
public class ExportTournamentForGOTE extends ExportTournament {

  /** Class logger */
  private static Logger LOGGER = Logger.getLogger(ExportTournamentForGOTE.class.getName());

  @Override
  public void export(Tournament pTournament) {
    pTournament.save();

    // TODO check befor creation in order to copy older file before saving the tournament in its
    // current state
    // File file = new File(AppUtil.PATH_TO_TOURNAMENTS + pTournament.getTitle().trim() + "/" +
    // AppUtil.PATH_TO_SAVE
    // + pTournament.getTitle().trim() + "_" + new DateTime().toString("dd-MM-yyyy_hhmmss") +
    // FILE_EXTENSION_XML);
    //
    // FileWriter fileWriter;
    //
    // try {
    // fileWriter = new FileWriter(file);
    // fileWriter.write(pTournament.toXML());
    // fileWriter.close();
    // } catch (IOException e) {
    // LOGGER.log(Level.SEVERE, "Error during writing file", e);
    // }

  }
}
