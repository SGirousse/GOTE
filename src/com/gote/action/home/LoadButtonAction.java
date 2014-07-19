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

import javax.swing.AbstractAction;

import com.gote.ui.home.HomeUI;

/**
 * 
 * Load button action event handler
 * 
 * @author SGirousse
 */
public class LoadButtonAction extends AbstractAction {

  /** Auto-generated UID */
  private static final long serialVersionUID = -1583231492094769828L;

  /** Home UI */
  private HomeUI homeUI;

  public LoadButtonAction(HomeUI pHomeUI, String pLabel) {
    super(pLabel);
    homeUI = pHomeUI;
  }

  @Override
  public void actionPerformed(ActionEvent pActionEvent) {
    // LOAD EXISTING TOURNAMENT
    homeUI.setVisible(false);
  }
}
