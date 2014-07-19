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
package com.gote.ui.newtournament;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.table.AbstractTableModel;

import com.gote.pojo.Round;

public class JRoundsTable extends AbstractTableModel {

  /** Auto-generated UID */
  private static final long serialVersionUID = 2210359300894240726L;

  /** Class logger */
  private static Logger LOGGER = Logger.getLogger(JRoundsTable.class.getName());

  /** Rounds */
  private List<Round> rounds = new ArrayList<Round>();

  /** Rounds table header */
  private String[] header = { "Numéro", "Date de début", "Date de fin" };

  public JRoundsTable() {
    super();
  }

  @Override
  public int getColumnCount() {
    return header.length;
  }

  @Override
  public int getRowCount() {
    return rounds.size();
  }

  @Override
  public String getColumnName(int pColumnIndex) {
    return header[pColumnIndex];
  }

  @Override
  public Object getValueAt(int pRowIndex, int pColumnIndex) {
    switch (pColumnIndex) {
    case 0:
      return rounds.get(pRowIndex).getNumber();
    case 1:
      return rounds.get(pRowIndex).getDateStart();
    case 2:
      return rounds.get(pRowIndex).getDateEnd();
    default:
      LOGGER.log(Level.WARNING, "Trying to access to unexistant row index. Maximum is " + getColumnCount());
      return null;
    }
  }

  /**
   * Add a round to the JTable and fire the event
   * 
   * @param pRound Round
   */
  public void addRound(Round pRound) {
    rounds.add(pRound);

    fireTableRowsInserted(rounds.size() - 1, rounds.size() - 1);
  }

  /**
   * Remove a table row and fire the event
   * 
   * @param pRowIndex int
   */
  public void removeRound(int pRowIndex) {
    rounds.remove(pRowIndex);

    fireTableRowsDeleted(pRowIndex, pRowIndex);
  }

  /**
   * Rounds getter
   * 
   * @return List<Round>
   */
  public List<Round> getRounds() {
    return rounds;
  }

  /**
   * Rounds setter
   * 
   * @param pRounds List<Round>
   */
  public void setRounds(List<Round> pRounds) {
    this.rounds = pRounds;
  }

}
