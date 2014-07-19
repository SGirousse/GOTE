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
package com.gote;

import java.util.logging.Logger;

import javax.swing.SwingUtilities;

import com.gote.ui.home.HomeUI;

/**
 * 
 * Main class and application entry point
 * 
 * @author SGirouss
 */
public class TournamentManager {

  /** Class logger */
  private static Logger LOGGER = Logger.getLogger(TournamentManager.class.getName());

  /**
   * Main function
   * 
   * @param args String[]
   */
  public static void main(String[] args) {
    LOGGER.info("[START] Application starting");
    TournamentManager tournamentManager = new TournamentManager();
    tournamentManager.startUI();
  }

  private void startUI() {
    LOGGER.info("Starting the UI \"HomeUI\"");
    // Start the application with the main ui
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        HomeUI homeUI = new HomeUI();
        homeUI.setVisible(true);
      }
    });
  }
}