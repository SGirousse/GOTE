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
package com.gote.ui.newtournament;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.gote.AppUtil;
import com.gote.action.newtournament.CreateButtonAction;
import com.gote.action.newtournament.FileChooserAction;
import com.gote.importexport.ImportTournament;
import com.gote.importexport.ImportTournamentFromOpenGotha;
import com.gote.pojo.Tournament;
import com.gote.ui.home.HomeUI;
import com.gote.util.newtournament.NewTournamentUtil;

/**
 * 
 * UI used to create/load a new tournament
 * 
 * @author SGirousse
 */
public class NewTournamentUI extends JFrame implements WindowListener {

  /** Auto-generated UID */
  private static final long serialVersionUID = -8132964378537435646L;

  /** Home UI reference */
  HomeUI homeUI;

  /** Tournament to be created */
  Tournament tournament;

  /** File path editor pane */
  JEditorPane jEditorPaneFilePath;

  /** Tournament title editor pane */
  JEditorPane jEditorPaneTournamentTitle;

  /** Tag editor pane */
  JEditorPane jEditorPaneTag;

  /** Create button */
  JButton jButtonCreateTournament;

  /** Window closing for creation of going back to the main window */
  boolean backToMainWindow;

  /**
   * Default constructor
   * 
   * @param tHomeUI HomeUI
   */
  public NewTournamentUI(HomeUI tHomeUI) {
    super();
    homeUI = tHomeUI;
    backToMainWindow = true;
    build();
  }

  /**
   * Build the JFrame
   */
  private void build() {
    setTitle(AppUtil.buildWindowTitle(NewTournamentUtil.WINDOW_TITLE));
    ImageIcon img = new ImageIcon(AppUtil.APP_NAME);
    setIconImage(img.getImage());
    setSize(800, 700);
    setLocationRelativeTo(null);
    setResizable(false);
    setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    setContentPane(buildContentPanel());
    pack();
    addWindowListener(this);
  }

  /**
   * Build the panel
   * 
   * @return JPanel
   */
  private JPanel buildContentPanel() {
    JPanel panel = new JPanel();
    panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));

    // LABELS
    JLabel jLabelFilePath = new JLabel(NewTournamentUtil.LABEL_LOAD_FILE + " :");
    jLabelFilePath.setPreferredSize(new Dimension(100, 25));
    JLabel jLabelTournamentTitle = new JLabel(NewTournamentUtil.LABEL_TOURNAMENT_TITLE + " :");
    jLabelTournamentTitle.setPreferredSize(new Dimension(100, 25));
    JLabel jLabelTag = new JLabel(NewTournamentUtil.LABEL_TOURNAMENT_TAG + " :");
    jLabelTag.setPreferredSize(new Dimension(100, 25));
    JLabel jLabelLocation = new JLabel(NewTournamentUtil.LABEL_TOURNAMENT_SOURCE + " :");
    jLabelLocation.setPreferredSize(new Dimension(100, 25));
    JLabel jLabelTagHelper = new JLabel(NewTournamentUtil.LABEL_TOURNAMENT_TAG_HELP);
    jLabelTagHelper.setPreferredSize(new Dimension(400, 25));
    jLabelTagHelper.setFont(new Font("Arial", Font.ITALIC, 12));

    // EDITORS
    jEditorPaneFilePath = new JEditorPane();
    jEditorPaneFilePath.setEditable(false);
    jEditorPaneFilePath.setPreferredSize(new Dimension(440, 25));
    jEditorPaneTournamentTitle = new JEditorPane();
    jEditorPaneTournamentTitle.setPreferredSize(new Dimension(620, 25));
    jEditorPaneTag = new JEditorPane();
    jEditorPaneTag.setPreferredSize(new Dimension(240, 25));

    // ROUNDS TABLE

    // BUTTONS
    JButton jButtonFilePath = new JButton(new FileChooserAction(this, tournament,
        NewTournamentUtil.BUTTON_LOAD_FILE_LABEL));
    jButtonCreateTournament = new JButton(new CreateButtonAction(homeUI, this, NewTournamentUtil.BUTTON_CREATE_LABEL));
    jButtonCreateTournament.setEnabled(false);
    JButton jButtonCancel = new JButton(NewTournamentUtil.BUTTON_CANCEL_LABEL);

    // PANELS
    JPanel jPanelConf = new JPanel();
    jPanelConf.setLayout(new FlowLayout(FlowLayout.LEFT));
    jPanelConf.setBorder(BorderFactory.createTitledBorder(NewTournamentUtil.BORDER_TITLE_CONF));
    jPanelConf.setPreferredSize(new Dimension(750, 70));
    jPanelConf.setMaximumSize(new Dimension(750, 70));
    jPanelConf.setMinimumSize(new Dimension(750, 70));
    jPanelConf.add(jLabelFilePath);
    jPanelConf.add(jEditorPaneFilePath);
    jPanelConf.add(jButtonFilePath);

    JPanel jPanelCriteria = new JPanel();
    jPanelCriteria.setLayout(new FlowLayout(FlowLayout.LEFT));
    jPanelCriteria.setBorder(BorderFactory.createTitledBorder(NewTournamentUtil.BORDER_TITLE_CRITERIA));
    jPanelCriteria.setPreferredSize(new Dimension(750, 140));
    jPanelCriteria.setMaximumSize(new Dimension(750, 140));
    jPanelCriteria.setMinimumSize(new Dimension(750, 140));
    JPanel jPanelTitle = new JPanel();
    jPanelTitle.add(jLabelTournamentTitle);
    jPanelTitle.add(jEditorPaneTournamentTitle);
    JPanel jPanelTag = new JPanel();
    jPanelTag.add(jLabelTag);
    jPanelTag.add(jEditorPaneTag);
    jPanelTag.add(jLabelTagHelper);
    jPanelCriteria.add(jPanelTitle);
    jPanelCriteria.add(jPanelTag);

    JPanel jPanelLocation = new JPanel();
    jPanelLocation.setLayout(new FlowLayout(FlowLayout.LEFT));
    jPanelLocation.setBorder(BorderFactory.createTitledBorder(NewTournamentUtil.BORDER_TITLE_SOURCE));
    jPanelLocation.setPreferredSize(new Dimension(750, 70));
    jPanelLocation.setMaximumSize(new Dimension(750, 70));
    jPanelLocation.setMinimumSize(new Dimension(750, 70));

    JPanel jPanelRounds = new JPanel();
    jPanelRounds.setLayout(new FlowLayout(FlowLayout.LEFT));
    jPanelRounds.setBorder(BorderFactory.createTitledBorder(NewTournamentUtil.BORDER_TITLE_ROUNDS));
    jPanelRounds.setPreferredSize(new Dimension(750, 70));
    jPanelRounds.setMaximumSize(new Dimension(750, 70));
    jPanelRounds.setMinimumSize(new Dimension(750, 70));

    JPanel jPanelButtons = new JPanel();
    jPanelButtons.setLayout(new FlowLayout(FlowLayout.RIGHT));
    jPanelButtons.add(jButtonCreateTournament);
    jPanelButtons.add(jButtonCancel);

    panel.add(jPanelConf);
    panel.add(jPanelCriteria);
    panel.add(jPanelLocation);
    panel.add(jPanelRounds);
    panel.add(jPanelButtons);

    return panel;
  }

  /**
   * Set file path in path editor pane
   * 
   * @param pText File path
   */
  public void setLoadFileText(String pText) {
    jEditorPaneFilePath.setText(pText);
  }

  /**
   * Set tournament name in tournament title editor pane
   * 
   * @param pText Tournament title
   */
  public void setTournamentTitleText(String pText) {
    jEditorPaneTournamentTitle.setText(pText);
  }

  /**
   * Set tag in editor pane
   * 
   * @param pText Tag
   */
  public void setTagText(String pText) {
    jEditorPaneTag.setText(pText);
  }

  /**
   * Select the correct ImportTournament instance according to UI selection.
   * 
   * @return ImportTournament
   */
  public ImportTournament getSelectedImport() {
    // TODO Change this to switch case or something when it will be more available options
    return new ImportTournamentFromOpenGotha();
  }

  /**
   * Enable "create" button to allow Tournament creation
   * 
   * @param pEnable boolean
   */
  public void enableTournamentCreation(boolean pEnable) {
    jButtonCreateTournament.setEnabled(pEnable);
  }

  @Override
  public void windowActivated(WindowEvent e) {

  }

  @Override
  public void windowClosed(WindowEvent e) {
    homeUI.setVisible(backToMainWindow);
  }

  @Override
  public void windowClosing(WindowEvent e) {
    int choice = JOptionPane.showConfirmDialog(this, NewTournamentUtil.CLOSE_WINDOW_MSG,
        NewTournamentUtil.CLOSE_WINDOW_TITLE, JOptionPane.YES_NO_OPTION);
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

  /**
   * Tournament getter
   * 
   * @return Tournament
   */
  public Tournament getTournament() {
    return tournament;
  }

  /**
   * Tournament setter
   * 
   * @param tournament pTournament
   */
  public void setTournament(Tournament pTournament) {
    this.tournament = pTournament;
  }

  /**
   * Back to homeUI getter
   * 
   * @return boolean
   */
  public boolean isBackToMainWindow() {
    return backToMainWindow;
  }

  /**
   * Back to homeUI setter
   * 
   * @param pBackToMainWindow boolean
   */
  public void setBackToMainWindow(boolean pBackToMainWindow) {
    this.backToMainWindow = pBackToMainWindow;
  }

}
