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

import java.util.ArrayList;
import java.util.List;

import org.dom4j.Element;
import org.joda.time.DateTime;

import com.gote.util.TournamentXMLUtil;

/**
 * 
 * Representation of a round. Basically, if not start/end date will be set, then the program will
 * use Tournament dates intervals. If there is no date at all, the round will have no limit.
 * 
 * @author SGirousse
 */
public class Round {

  /** Round number */
  private int number;

  /** Round start date */
  private DateTime dateStart;

  /** Round end date */
  private DateTime dateEnd;

  /** List of game played in that round */
  private List<Game> gameList;

  /**
   * Default constructor
   */
  public Round() {
    setNumber(0);
    setDateStart(new DateTime(1999, 1, 1, 0, 0));
    setDateEnd(new DateTime(1999, 1, 1, 0, 0));
    setGameList(new ArrayList<Game>());
  }

  /**
   * Constructor with parameters
   * 
   * @param pNumber number of the round
   * @param pDateStart start date of the round
   * @param pDateEnd end date of the round
   * @param pGameList list of games played in that round
   */
  public Round(int pNumber, DateTime pDateStart, DateTime pDateEnd, List<Game> pGameList) {
    setNumber(pNumber);
    setDateStart(pDateStart);
    setDateEnd(pDateEnd);
    setGameList(pGameList);
  }

  /**
   * Transform Round to XML format
   * 
   * @param pRoot "Rounds" element
   * @see TournamentXMLUtil
   */
  public void toXML(Element pRoot) {
    // Create round element
    Element round = pRoot.addElement(TournamentXMLUtil.TAG_ROUND);
    // Add its attributes
    round.addAttribute(TournamentXMLUtil.ATT_ROUND_NUMBER, new Integer(getNumber()).toString());
    round.addAttribute(TournamentXMLUtil.ATT_ROUND_DATESTART, getDateStart().toString());
    round.addAttribute(TournamentXMLUtil.ATT_ROUND_DATEEND, getDateEnd().toString());

    // Add Games list
    Element games = round.addElement(TournamentXMLUtil.TAG_GAMES);
    for (Game game : getGameList()) {
      game.toXML(games);
    }
  }

  /**
   * Number getter
   * 
   * @return number
   */
  public int getNumber() {
    return number;
  }

  /**
   * Number setter
   * 
   * @param pNumber int
   */
  public void setNumber(int pNumber) {
    this.number = pNumber;
  }

  /**
   * Start date getter
   * 
   * @return Date
   */
  public DateTime getDateStart() {
    return dateStart;
  }

  /**
   * Date setter
   * 
   * @param pDateStart DateTime
   */
  public void setDateStart(DateTime pDateStart) {
    this.dateStart = pDateStart;
  }

  /**
   * End date getter
   * 
   * @return DateTime
   */
  public DateTime getDateEnd() {
    return dateEnd;
  }

  /**
   * End date setter
   * 
   * @param pDateEnd DateTime
   */
  public void setDateEnd(DateTime pDateEnd) {
    this.dateEnd = pDateEnd;
  }

  /**
   * Game list
   * 
   * @return List<Game>
   */
  public List<Game> getGameList() {
    return gameList;
  }

  /**
   * List<Game> getter
   * 
   * @param pGameList List<Game>
   */
  public void setGameList(List<Game> pGameList) {
    this.gameList = pGameList;
  }

  /**
   * Get list of game still to be played
   * 
   * @return List<Game>
   */
  public List<Game> getToBePlayedGameList() {
    List<Game> toBePlayedGames = new ArrayList<Game>();
    for (Game game : getGameList()) {
      if (game.getResult() != null) {
        toBePlayedGames.add(game);
      }
    }
    return toBePlayedGames;
  }
}
