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
package com.gote.ui.tournament;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.gote.AppUtil;
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
  HomeUI homeUI;

  /** Tournament managed */
  Tournament tournament;

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
    ImageIcon img = new ImageIcon(AppUtil.APP_NAME);
    setIconImage(img.getImage());
    setSize(800, 700);
    setLocationRelativeTo(null);
    setResizable(false);
    setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    setContentPane(buildContentPanel());
    addWindowListener(this);
  }

  /**
   * Build the panel
   * 
   * @return JPanel
   */
  private JPanel buildContentPanel() {
    JPanel panel = new JPanel();
    
    JButton jButtonUpdate = new JButton(new UpdateResultsButton(this, homeUI, tournament, TournamentUtil.BUTTON_UPDATE_LABEL));
    JButton jButtonExport = new JButton(TournamentUtil.BUTTON_EXPORT_LABEL);
    
    panel.add(jButtonUpdate);
    panel.add(jButtonExport);
    
    return panel;
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
