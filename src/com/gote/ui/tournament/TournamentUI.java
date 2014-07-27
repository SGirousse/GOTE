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
package com.gote.ui.tournament;

import java.awt.Dimension;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import com.gote.AppUtil;
import com.gote.action.tournament.ExportTournamentButton;
import com.gote.action.tournament.UpdateResultsButton;
import com.gote.pojo.Tournament;
import com.gote.ui.home.HomeUI;
import com.gote.util.tournament.TournamentUtil;

/**
 * 
 * UI in charge of managing a selected tournament and to export it
 * 
 * @author SGirousse
 */
public class TournamentUI extends JFrame implements WindowListener {

  /** Auto-generated UID */
  private static final long serialVersionUID = -3204049783640113974L;

  /** Reference to the main window */
  private HomeUI homeUI;

  /** Tournament managed */
  private Tournament tournament;

  /** Log Area */
  private JTextArea jTextAreaLog;

  /**
   * Default constructor
   * 
   * @param pHomeUI reference to the main window
   * @param pTournament Tournament selected/created
   */
  public TournamentUI(HomeUI pHomeUI, Tournament pTournament) {
    super();
    homeUI = pHomeUI;
    tournament = pTournament;
    build();
  }

  /**
   * Build the JFrame
   */
  private void build() {
    setTitle(AppUtil.buildWindowTitle(TournamentUtil.WINDOW_TITLE, tournament.getTitle()));
    ImageIcon img = new ImageIcon(AppUtil.APP_ICON_PATH);
    setIconImage(img.getImage());
    setSize(800, 600);
    setLocationRelativeTo(null);
    setResizable(false);
    setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    setContentPane(buildContentPanel());
    addWindowListener(this);
    pack();
  }

  /**
   * Build the panel
   * 
   * @return JPanel
   */
  private JPanel buildContentPanel() {
    JPanel panel = new JPanel();
    panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));

    jTextAreaLog = new JTextArea();
    jTextAreaLog.setLineWrap(true);
    jTextAreaLog.setWrapStyleWord(true);
    JScrollPane jScrollPane = new JScrollPane(jTextAreaLog);
    jScrollPane.setPreferredSize(new Dimension(620, 300));
    jScrollPane.setMaximumSize(new Dimension(620, 300));
    jScrollPane.setMinimumSize(new Dimension(620, 300));

    JButton jButtonUpdate = new JButton(new UpdateResultsButton(this, tournament, TournamentUtil.BUTTON_UPDATE_LABEL));
    JButton jButtonExport = new JButton(new ExportTournamentButton(tournament, TournamentUtil.BUTTON_EXPORT_LABEL));
    JButton jButtonAddToClip = new JButton("<html><center>Copier dans le presse-papier</center></html>");

    JPanel jPanelLogs = new JPanel();
    jPanelLogs.setLayout(new BoxLayout(jPanelLogs, BoxLayout.LINE_AXIS));
    jPanelLogs.setPreferredSize(new Dimension(750, 350));
    jPanelLogs.setMaximumSize(new Dimension(750, 350));
    jPanelLogs.setMinimumSize(new Dimension(750, 350));

    jPanelLogs.setBorder(BorderFactory.createTitledBorder("Logs"));
    jPanelLogs.add(jScrollPane);
    jPanelLogs.add(jButtonAddToClip);

    JPanel jPanelButtons = new JPanel();
    jPanelButtons.add(jButtonUpdate);
    jPanelButtons.add(jButtonExport);

    panel.add(jPanelButtons);
    panel.add(jPanelLogs);

    return panel;
  }

  public JTextArea getJTextAreaLog() {
    return jTextAreaLog;
  }

  @Override
  public void windowActivated(WindowEvent e) {
  }

  @Override
  public void windowClosed(WindowEvent e) {
    homeUI.setVisible(true);
  }

  @Override
  public void windowClosing(WindowEvent e) {
    int choice = JOptionPane.showConfirmDialog(this, TournamentUtil.CLOSE_WINDOW_MSG,
        TournamentUtil.CLOSE_WINDOW_TITLE, JOptionPane.YES_NO_OPTION);
    if (choice == JOptionPane.YES_OPTION) {
      this.dispose();
    }

  }

  @Override
  public void windowDeactivated(WindowEvent e) {
  }

  @Override
  public void windowDeiconified(WindowEvent e) {
  }

  @Override
  public void windowIconified(WindowEvent e) {
  }

  @Override
  public void windowOpened(WindowEvent e) {
  }
}
