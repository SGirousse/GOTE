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

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.SwingUtilities;

import com.gote.ui.home.HomeUI;
import com.gote.ui.newtournament.NewTournamentUI;
import com.gote.ui.tournament.TournamentUI;

public class CreateButtonAction extends AbstractAction {

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
    newTournamentUI.setBackToMainWindow(false);
    newTournamentUI.dispose();
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        TournamentUI tournamentUI = new TournamentUI(homeUI, newTournamentUI.getTournament());
        tournamentUI.setVisible(true);
      }
    });
  }

}
