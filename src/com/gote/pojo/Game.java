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
package com.gote.pojo;

import org.dom4j.Element;

import com.gote.util.TournamentXMLUtil;

/**
 * 
 * Representation of a Game of Go.
 * 
 * @author SGirousse
 */
public class Game {
  private Player white;

  private Player black;

  private String gameUrl;

  private String result;

  private String komi;

  private String handicap;

  /**
   * Default constructor and initialization
   */
  public Game() {
    setWhite(new Player());
    setBlack(new Player());
    setGameUrl(new String());
    setResult(new String());
    setKomi(new String());
    setHandicap(new String());
  }

  /**
   * Transform Game to XML
   * 
   * @param pRoot "Games" element
   * @see TournamentXMLUtil
   */
  public void toXML(Element pRoot) {
    // Create game element
    Element game = pRoot.addElement(TournamentXMLUtil.TAG_GAME);
    // Add its attributes
    game.addAttribute(TournamentXMLUtil.ATT_GAME_WHITEPLAYER, getWhite().getPseudo());
    game.addAttribute(TournamentXMLUtil.ATT_GAME_BLACKPLAYER, getBlack().getPseudo());
    game.addAttribute(TournamentXMLUtil.ATT_GAME_URL, getGameUrl());
    game.addAttribute(TournamentXMLUtil.ATT_GAME_KOMI, getKomi());
    game.addAttribute(TournamentXMLUtil.ATT_GAME_HANDICAP, getHandicap());
    game.addAttribute(TournamentXMLUtil.ATT_GAME_RESULT, getResult());
  }

  public Player getWhite() {
    return white;
  }

  public void setWhite(Player white) {
    this.white = white;
  }

  public Player getBlack() {
    return black;
  }

  public void setBlack(Player black) {
    this.black = black;
  }

  public String getGameUrl() {
    return gameUrl;
  }

  public void setGameUrl(String gameUrl) {
    this.gameUrl = gameUrl;
  }

  public String getResult() {
    return result;
  }

  public void setResult(String result) {
    this.result = result;
  }

  public String getKomi() {
    return komi;
  }

  public void setKomi(String komi) {
    this.komi = komi;
  }

  public String getHandicap() {
    return handicap;
  }

  public void setHandicap(String handicap) {
    this.handicap = handicap;
  }
}
