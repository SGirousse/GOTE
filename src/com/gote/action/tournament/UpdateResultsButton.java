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

import java.awt.Dimension;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.AbstractAction;
import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

import com.gote.downloader.GameDownloader;
import com.gote.downloader.kgs.KGSDownloader;
import com.gote.pojo.Tournament;
import com.gote.ui.tournament.TournamentUI;
import com.gote.util.Servers;
import com.gote.util.newtournament.NewTournamentUtil;
import com.gote.util.tournament.TournamentUtil;

public class UpdateResultsButton extends AbstractAction {

  /** Auto-generated UID */
  private static final long serialVersionUID = 2999373594677668898L;

  /** TMTournamentUI reference */
  private TournamentUI tournamentUI;

  /** Current tournament */
  private Tournament tournament;

  /** Label to show current messages */

  private JLabel jLabelMainText;

  public UpdateResultsButton(TournamentUI pTournamentUI, Tournament pTournament, String pLabel) {
    super(pLabel);
    tournamentUI = pTournamentUI;
    tournament = pTournament;
  }

  @Override
  public void actionPerformed(ActionEvent pActionEvent) {
    SwingWorker<Object, Void> swingWorker = new SwingWorker<Object, Void>() {

      @Override
      protected Object doInBackground() throws Exception {
        GameDownloader gameDownloader = null;
        if (tournament.getServerType().equals(Servers.KGS)) {
          gameDownloader = new KGSDownloader(tournament, getjLabelMainText());
        }
        updateTournament(gameDownloader);
        tournament.save();
        return null;
      }
    };

    JDialog dialog = new JDialog(tournamentUI, true);
    swingWorker.addPropertyChangeListener(new SwingWorkerCompletionWaiter(dialog));
    swingWorker.execute();
    // the dialog will be visible until the SwingWorker is done
    dialog.setVisible(true);

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

  /**
   * @return returns jLabel.
   */
  public JLabel getjLabelMainText() {
    return jLabelMainText;
  }

  /**
   * @param jLabel jLabel to set.
   */
  public void setjLabelMainText(JLabel jLabel) {
    this.jLabelMainText = jLabel;
  }

  private class SwingWorkerCompletionWaiter implements PropertyChangeListener {
    private JDialog dialog;

    public SwingWorkerCompletionWaiter(JDialog pJDialog) {
      this.dialog = pJDialog;

      setjLabelMainText(new JLabel("Etape 1/3 : Démarrage..."));

      Panel panel = new Panel();
      panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
      panel.add(new JLabel(TournamentUtil.WAITING_WINDOW_TITLE));
      panel.add(getjLabelMainText());

      dialog.setResizable(false);
      dialog.setTitle(TournamentUtil.WAITING_WINDOW_TITLE);
      dialog.setSize(new Dimension(400, 200));
      dialog.setLocationRelativeTo(null);
      dialog.setContentPane(panel);
    }

    public void propertyChange(PropertyChangeEvent event) {
      if ("state".equals(event.getPropertyName()) && SwingWorker.StateValue.DONE == event.getNewValue()) {
        dialog.setVisible(false);
        dialog.dispose();
      }
    }
  }
}
