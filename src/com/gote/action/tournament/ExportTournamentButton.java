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

import com.gote.importexport.ExportTournament;
import com.gote.importexport.ExportTournamentForOpenGotha;
import com.gote.pojo.Tournament;

/**
 * 
 * Action handler when "export" button is pressed.
 * 
 * @author sgirouss
 */
public class ExportTournamentButton extends AbstractAction {

  /** Auto-generated UID */
  private static final long serialVersionUID = -6481085917022427777L;

  /** Tournament to be managed */
  Tournament tournament;

  public ExportTournamentButton(Tournament pTournament, String pLabel) {
    super(pLabel);
    tournament = pTournament;
  }

  @Override
  public void actionPerformed(ActionEvent pActionEvent) {
    ExportTournament exportTournament = new ExportTournamentForOpenGotha();
    exportTournament.export(tournament);
  }

}
