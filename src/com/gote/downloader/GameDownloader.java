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
package com.gote.downloader;

import java.util.logging.Level;

import javax.swing.JLabel;

import com.gote.pojo.Game;
import com.gote.pojo.Tournament;

/**
 * Interface in charge of game download
 * 
 * @author SGirousse
 */
public abstract class GameDownloader {

  /** Tournament reference with all the need informations for download */
  protected Tournament tournament;

  /** JTextArea to show logs */
  private JLabel jLabel;

  /**
   * Constructor
   * 
   * @param pTournament Tournament reference with all the need informations for download
   */
  public GameDownloader(Tournament pTournament, JLabel pJLabel) {
    tournament = pTournament;
    jLabel = pJLabel;
  }

  /**
   * This is in charge to download games (files), convert them in Game format and update
   * Tournament.
   * 
   * @see Game
   */
  public abstract void startDownload();

  /**
   * Check if the game can be accessed
   * 
   * @return boolean
   */
  public abstract boolean checkGameAccessAvailability();

  /**
   * Write log in jTextArea
   * 
   * @param pLogLevel Level
   * @param pLogText The text to write
   */
  protected void log(Level pLogLevel, String pLogText) {
    jLabel.setText("<html>" + pLogText + "</html>");
  }

  /**
   * Write log in jTextArea
   * 
   * @param pLogLevel Level
   * @param pLogText The text to write
   * @param pException Exception
   */
  protected void log(Level pLogLevel, String pLogText, Exception pException) {
    jLabel.setText("<html>" + pLogText + "</html>");
  }

  /**
   * Get tournament
   * 
   * @return Tournament
   */
  public Tournament getTournament() {
    return tournament;
  }
}
