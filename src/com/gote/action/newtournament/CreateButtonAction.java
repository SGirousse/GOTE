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
package com.gote.action.newtournament;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.AbstractAction;
import javax.swing.SwingUtilities;

import com.gote.AppUtil;
import com.gote.importexport.ExportTournament;
import com.gote.importexport.ExportTournamentForGOTE;
import com.gote.ui.home.HomeUI;
import com.gote.ui.newtournament.NewTournamentUI;
import com.gote.ui.tournament.TournamentUI;

public class CreateButtonAction extends AbstractAction {

  /** Class logger */
  private static Logger LOGGER = Logger.getLogger(CreateButtonAction.class.getName());

  /** Auto-generated UID */
  private static final long serialVersionUID = -9058827321125663718L;

  /** Reference to the main window */
  private HomeUI homeUI;

  /** Reference to the new tournament window */
  private NewTournamentUI newTournamentUI;

  public CreateButtonAction(HomeUI pHomeUI, NewTournamentUI pNewTournamentUI, String pLabel) {
    super(pLabel);
    homeUI = pHomeUI;
    newTournamentUI = pNewTournamentUI;
  }

  @Override
  public void actionPerformed(ActionEvent pActionEvent) {
    if (createTournamentFolders()) {
      ExportTournament exportTournament = new ExportTournamentForGOTE();
      exportTournament.export(newTournamentUI.getTournament());
      copyOriginal();
      newTournamentUI.setBackToMainWindow(false);
      newTournamentUI.dispose();
      SwingUtilities.invokeLater(new Runnable() {
        public void run() {
          TournamentUI tournamentUI = new TournamentUI(homeUI, newTournamentUI.getTournament());
          tournamentUI.setVisible(true);
        }
      });
    } else {
      // TODO Add a pop-up
    }
  }

  /**
   * Create the folder which will host the tournament
   * 
   * @return true if the folder has been successfully created
   */
  private boolean createTournamentFolders() {
    String folderToCreate = AppUtil.PATH_TO_TOURNAMENTS + newTournamentUI.getTournament().getTitle();
    String folderSave = folderToCreate + "/" + AppUtil.PATH_TO_SAVE;
    String folderExport = folderToCreate + "/" + AppUtil.PATH_TO_EXPORTS;
    String folderSGF = folderToCreate + "/" + AppUtil.PATH_TO_SGFS;

    // Create the folder and check if everything is ok
    if ((new File(folderToCreate)).mkdirs()) {
      LOGGER.log(Level.INFO, "The folder " + folderToCreate + " has been created");
      if ((new File(folderSave)).mkdirs()) {
        LOGGER.log(Level.INFO, "The folder " + folderSave + " has been created");
      } else {
        LOGGER.log(Level.WARNING, "The folder " + folderSave + " has not been created");
      }
      if ((new File(folderExport)).mkdirs()) {
        LOGGER.log(Level.INFO, "The folder " + folderExport + " has been created");
      } else {
        LOGGER.log(Level.WARNING, "The folder " + folderExport + " has not been created");
      }
      if ((new File(folderSGF)).mkdirs()) {
        LOGGER.log(Level.INFO, "The folder " + folderSGF + " has been created");
      } else {
        LOGGER.log(Level.WARNING, "The folder " + folderSGF + " has not been created");
      }
      return true;
    }

    LOGGER.log(Level.SEVERE, "The folder " + folderToCreate + " has not been created");
    return false;
  }

  /**
   * Copy origin data in tournament folder, then it can be used for updates and also possible
   * checks
   */
  private void copyOriginal() {
    // TODO check the origin of the file, here it is assumed the file come from open-gotha
    Path source = Paths.get(newTournamentUI.getLoadFileText());
    Path target = Paths.get(AppUtil.PATH_TO_TOURNAMENTS + newTournamentUI.getTournament().getTitle() + "/"
        + AppUtil.PATH_TO_EXPORTS + "opengotha_" + newTournamentUI.getTournament().getTitle() + ".xml");
    try {
      Files.copy(source, target, REPLACE_EXISTING);
    } catch (IOException e) {
      LOGGER.log(Level.SEVERE, "Copy of opengotha from " + source + " to " + target + " failed", e);
    }
  }
}
