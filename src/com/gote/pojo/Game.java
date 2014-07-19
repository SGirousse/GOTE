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
package com.gote.pojo;

import org.dom4j.Element;

import com.gote.util.TournamentUtils;

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

  public void addToXML(Element pRoot) {
    Element game = pRoot.addElement(TournamentUtils.TAG_GAME);
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
