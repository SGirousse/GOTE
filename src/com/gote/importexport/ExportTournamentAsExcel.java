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

import com.gote.pojo.Tournament;
import com.gote.util.ImportExportUtil;

public class ExportTournamentAsExcel extends ExportTournament {

  @Override
  public void export(Tournament pTournament) {
    // String excelFileURI = ImportExportUtil.buildFileURI(getImportExportType(), pTournament);
  }

  @Override
  public String getImportExportType() {
    return ImportExportUtil.IMPORTEXPORT_TYPE_EXCEL;
  }
}
