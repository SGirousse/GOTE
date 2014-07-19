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
package com.gote.action.tournament;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

import com.gote.downloader.GameDownloader;
import com.gote.downloader.kgs.KGSDownloader;
import com.gote.pojo.Tournament;
import com.gote.ui.tournament.TournamentUI;
import com.gote.util.Servers;
import com.gote.util.newtournament.NewTournamentUtil;

public class UpdateResultsButton extends AbstractAction {

  /** Auto-generated UID */
  private static final long serialVersionUID = 2999373594677668898L;

  /** TMTournamentUI reference */
  private TournamentUI tournamentUI;

  /** Current tournament */
  private Tournament tournament;

  public UpdateResultsButton(TournamentUI pTournamentUI, Tournament pTournament, String pLabel) {
    super(pLabel);
    tournamentUI = pTournamentUI;
    tournament = pTournament;
  }

  @Override
  public void actionPerformed(ActionEvent pActionEvent) {
    GameDownloader gameDownloader = null;
    if (tournament.getServerType().equals(Servers.KGS)) {
      gameDownloader = new KGSDownloader(tournament);
    }
    updateTournament(gameDownloader);
  }

  /**
   * Update tournament with game downloader
   * 
   * @param pGameDownloader GameDownloader
   */
  private void updateTournament(GameDownloader pGameDownloader) {
    if (pGameDownloader != null) {
      pGameDownloader.startDownload();
    } else {
      if (JOptionPane.showConfirmDialog(tournamentUI, NewTournamentUtil.CLOSE_WINDOW_MSG,
          NewTournamentUtil.CLOSE_WINDOW_TITLE, JOptionPane.OK_OPTION, JOptionPane.ERROR_MESSAGE) == JOptionPane.OK_OPTION) {
        System.out.println("[ERROR] GameDownloader is null. No update !");
      }
    }
  }

}
