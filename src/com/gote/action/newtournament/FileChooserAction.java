/**
 * Copyright 2014 Siméon GIROUSSE
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
package com.gote.action.newtournament;

import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;

import com.gote.importexport.ImportTournament;
import com.gote.pojo.Tournament;
import com.gote.ui.newtournament.NewTournamentUI;

public class FileChooserAction extends AbstractAction {

  /** Auto-generated UID */
  private static final long serialVersionUID = 415452192112127183L;

  /** NewTournamentUI reference */
  private NewTournamentUI newTournamentUI;

  /** Tournament to generate */
  private Tournament tournament;

  /**
   * Constructor for button selection
   * 
   * @param pNewTournamentUI NewTournamentUI
   * @param pTournament Tournament
   * @param pLabel Button label
   */
  public FileChooserAction(NewTournamentUI pNewTournamentUI, Tournament pTournament, String pLabel) {
    super(pLabel);
    newTournamentUI = pNewTournamentUI;
    tournament = pTournament;
  }

  @Override
  public void actionPerformed(ActionEvent pActionEvent) {
    JFileChooser jFileChooser = new JFileChooser();
    int returnValue = jFileChooser.showOpenDialog(newTournamentUI);

    if (returnValue == JFileChooser.APPROVE_OPTION) {
      File selectedFile = jFileChooser.getSelectedFile();
      newTournamentUI.setLoadFileText(selectedFile.toString());
      loadTournament(selectedFile);
    }
  }

  private void loadTournament(File pFile) {
    ImportTournament importer = newTournamentUI.getSelectedImport();
    tournament = importer.createTournamentFromConfig(pFile);
    newTournamentUI.setTournamentTitleText(tournament.getTitle());
    newTournamentUI.setTournament(tournament);
    newTournamentUI.enableTournamentCreation(true);
  }
}
