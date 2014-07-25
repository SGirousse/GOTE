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
package com.gote.ui.home;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;

import com.gote.AppUtil;
import com.gote.action.home.NewButtonAction;
import com.gote.importexport.ImportTournament;
import com.gote.importexport.ImportTournamentFromGOTE;
import com.gote.pojo.Tournament;
import com.gote.ui.tournament.TournamentUI;
import com.gote.util.home.HomeUtil;

/**
 * 
 * Main and Home UI, it allows user to create or to select an existing tournament
 * 
 * @author SGirousse
 */
public class HomeUI extends JFrame implements WindowListener {

  /** Auto-generated UID */
  private static final long serialVersionUID = -6832908983056266059L;

  /** Class logger */
  private static Logger LOGGER = Logger.getLogger(HomeUI.class.getName());

  /** List of tournaments */
  JTable jTableSavedTournament;

  /**
   * Default constructor
   */
  public HomeUI() {
    super();
    build();
  }

  /**
   * Build the JFrame
   */
  private void build() {
    setTitle(AppUtil.buildWindowTitle(HomeUtil.WINDOW_TITLE));
    ImageIcon img = new ImageIcon(AppUtil.APP_ICON_PATH);
    setIconImage(img.getImage());
    setSize(400, 400);
    setLocationRelativeTo(null);
    setResizable(false);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setContentPane(buildContentPanel());
    getContentPane().setBackground(Color.WHITE);
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

    JLabel jLabelTitle = new JLabel("GOTE");
    jLabelTitle.setFont(new Font("Arial", Font.BOLD, 60));
    jLabelTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
    JLabel jLabelName = new JLabel("Go Online Tournament Easy");
    jLabelName.setFont(new Font("Arial", Font.BOLD, 15));
    jLabelName.setAlignmentX(Component.CENTER_ALIGNMENT);
    JLabel jLabelLoadGame = new JLabel("Ouvrir un tournoi existant : ");
    jLabelLoadGame.setAlignmentX(Component.RIGHT_ALIGNMENT);

    ImageIcon img = new ImageIcon("resources/images/GOTE-button-white.jpg");
    JButton jButtonNewTournament = new JButton(new NewButtonAction(this, HomeUtil.BUTTON_LABEL_NEW_TOURNAMENT, img));
    jButtonNewTournament.setBackground(Color.WHITE);

    String[] header = { "Charger un tournoi existant" };
    jTableSavedTournament = new JTable(getExistingTournaments(), header);
    final HomeUI homeUI = this;
    jTableSavedTournament.addMouseListener(new MouseAdapter() {
      public void mousePressed(MouseEvent me) {
        JTable table = (JTable) me.getSource();
        Point p = me.getPoint();
        int row = table.rowAtPoint(p);
        if (me.getClickCount() == 2) {
          LOGGER.log(Level.INFO, "Open existing tournament");
          setVisible(false);
          TournamentUI tournamentUI = new TournamentUI(homeUI, loadTournament(table.getValueAt(row, 0).toString()));
          tournamentUI.setVisible(true);
        }
      }
    });

    jButtonNewTournament.setAlignmentX(Component.CENTER_ALIGNMENT);
    panel.add(jLabelTitle);
    panel.add(jLabelName);
    panel.add(new JSeparator(JSeparator.HORIZONTAL), BorderLayout.LINE_START);
    panel.add(Box.createRigidArea(new Dimension(0, 10)));
    panel.add(jButtonNewTournament);
    panel.add(Box.createRigidArea(new Dimension(0, 10)));
    panel.add(new JSeparator(JSeparator.HORIZONTAL), BorderLayout.LINE_START);
    panel.add(Box.createRigidArea(new Dimension(0, 10)));
    panel.add(Box.createHorizontalGlue());
    JScrollPane jScrollPane = new JScrollPane(jTableSavedTournament);
    jScrollPane.setPreferredSize(new Dimension(350, 200 /*
                                                         * getExistingTournaments().length * 16 +
                                                         * 25
                                                         */));
    jScrollPane.setMaximumSize(new Dimension(350, 200 /* getExistingTournaments().length * 16 + 25 */));
    jScrollPane.setMinimumSize(new Dimension(350, 200 /* getExistingTournaments().length * 16 + 25 */));
    panel.add(jScrollPane, BorderLayout.CENTER);
    panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

    return panel;
  }

  /**
   * Get the list of the already existing tournaments
   * 
   * @return tournament list for JTable
   */
  public String[][] getExistingTournaments() {

    String[][] tournamentsData = {};
    List<String> tournamentTitles = new ArrayList<String>();

    File[] list = new File(AppUtil.PATH_TO_TOURNAMENTS).listFiles();
    if (list != null) {
      for (int i = 0; i < list.length; i++) {
        if (list[i].isDirectory()) {
          tournamentTitles.add(list[i].getName());
        }
      }
      tournamentsData = new String[tournamentTitles.size()][1];
      for (int i = 0; i < tournamentTitles.size(); i++) {
        tournamentsData[i][0] = tournamentTitles.get(i);
      }
    } else {
      LOGGER.log(Level.INFO, "No existing tournament");
    }
    return tournamentsData;
  }

  /**
   * Import tournament
   * 
   * @param folderName Tournament name used to name the folder containing it
   * @return a Tournament instance
   */
  public Tournament loadTournament(String folderName) {
    ImportTournament importTournament = new ImportTournamentFromGOTE();
    return importTournament.createTournamentFromConfig(new File(AppUtil.PATH_TO_TOURNAMENTS + folderName + "/"
        + AppUtil.PATH_TO_SAVE + folderName + ".xml"));
  }

  @Override
  public void windowActivated(WindowEvent e) {
  }

  @Override
  public void windowClosed(WindowEvent e) {
  }

  @Override
  public void windowClosing(WindowEvent e) {
    LOGGER.log(Level.INFO, "[END] Application closing");
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
