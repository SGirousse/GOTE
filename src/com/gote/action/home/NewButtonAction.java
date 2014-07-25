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
package com.gote.action.home;

import java.awt.event.ActionEvent;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.AbstractAction;
import javax.swing.Icon;

import com.gote.ui.home.HomeUI;
import com.gote.ui.newtournament.NewTournamentUI;

/**
 * 
 * Class in charge of new tournament button actions
 * 
 * @author SGirousse
 * @see TMHomeUI
 */
public class NewButtonAction extends AbstractAction {

  /** Class logger */
  private static Logger LOGGER = Logger.getLogger(NewButtonAction.class.getName());

  /** Auto-generated UID */
  private static final long serialVersionUID = 2759732123023231019L;

  /** Home UI */
  private HomeUI homeUI;

  public NewButtonAction(HomeUI pHomeUI, String pLabel, Icon pIcon) {
    super(pLabel, pIcon);
    homeUI = pHomeUI;
  }

  @Override
  public void actionPerformed(ActionEvent pActionEvent) {
    LOGGER.log(Level.INFO, "New tournament button clicked");
    homeUI.setVisible(false);
    NewTournamentUI newTournamentUI = new NewTournamentUI(homeUI);
    newTournamentUI.setVisible(true);
  }
}
