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
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.gote.pojo.Tournament;
import com.gote.util.ImportExportUtil;

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
    LOGGER.log(Level.INFO, "Export tournament " + pTournament.getTitle() + " in GOTE format is starting");

    // Build path to GOTE file
    String goteExportFileURI = ImportExportUtil.buildFileURI(getImportExportType(), pTournament.getTitle());

    // Make a copy
    ImportExportUtil.copyBeforeModification(Paths.get(goteExportFileURI));

    // Then transform data to xml
    pTournament.save(new File(goteExportFileURI));
  }

  @Override
  protected String getImportExportType() {
    return ImportExportUtil.IMPORTEXPORT_TYPE_GOTE;
  }
}
