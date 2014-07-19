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

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.gote.AppUtil;
import com.gote.action.home.NewButtonAction;
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
    ImageIcon img = new ImageIcon(AppUtil.APP_NAME);
    setIconImage(img.getImage());
    setSize(400, 400);
    setLocationRelativeTo(null);
    setResizable(false);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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

    JButton jButtonNewTournament = new JButton(new NewButtonAction(this, HomeUtil.BUTTON_LABEL_NEW_TOURNAMENT));
    panel.add(jButtonNewTournament);

    return panel;
  }

  @Override
  public void windowActivated(WindowEvent e) {
  }

  @Override
  public void windowClosed(WindowEvent e) {
  }

  @Override
  public void windowClosing(WindowEvent e) {
    LOGGER.log(Level.INFO,"[END] Application closing");
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
