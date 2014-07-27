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
import java.util.logging.Level;
import java.util.logging.Logger;

import org.dom4j.Element;
import org.joda.time.DateTime;

import com.gote.AppUtil;
import com.gote.util.xml.TournamentGOTEUtil;
import com.gote.util.xml.TournamentOpenGothaUtil;

/**
 * 
 * Representation of a round. Basically, if not start/end date will be set, then the program will
 * use Tournament dates intervals. If there is no date at all, the round will have no limit.
 * 
 * @author SGirousse
 */
public class Round {

  /** Class logger */
  private static Logger LOGGER = Logger.getLogger(Round.class.getName());

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
    setDateStart(AppUtil.APP_INIT_DATE);
    setDateEnd(AppUtil.APP_INIT_DATE);
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
   * @see TournamentGOTEUtil
   */
  public void toXML(Element pRoot) {
    // Create round element
    Element round = pRoot.addElement(TournamentGOTEUtil.TAG_ROUND);
    // Add its attributes
    round.addAttribute(TournamentGOTEUtil.ATT_ROUND_NUMBER, new Integer(getNumber()).toString());
    round.addAttribute(TournamentGOTEUtil.ATT_ROUND_DATESTART, getDateStart().toString());
    round.addAttribute(TournamentGOTEUtil.ATT_ROUND_DATEEND, getDateEnd().toString());

    // Add Games list
    Element games = round.addElement(TournamentGOTEUtil.TAG_GAMES);
    for (Game game : getGameList()) {
      game.toXML(games);
    }
  }

  /**
   * Load Round from XML
   * 
   * @param pRoot Element
   * @param pTournament Tournament
   */
  public void fromXML(Element pRoot, Tournament pTournament) {
    if (pRoot.attribute(TournamentGOTEUtil.ATT_ROUND_NUMBER) != null) {
      setNumber(Integer.parseInt(pRoot.attribute(TournamentGOTEUtil.ATT_ROUND_NUMBER).getValue()));
    } else {
      LOGGER.log(Level.WARNING, "The attribute " + TournamentGOTEUtil.ATT_ROUND_NUMBER + " is null");
    }

    if (pRoot.attribute(TournamentGOTEUtil.ATT_ROUND_DATESTART) != null) {
      setDateStart(new DateTime(pRoot.attribute(TournamentGOTEUtil.ATT_ROUND_DATESTART).getValue()));
    } else {
      LOGGER.log(Level.WARNING, "The attribute " + TournamentGOTEUtil.ATT_ROUND_DATESTART + " is null");
    }

    if (pRoot.attribute(TournamentGOTEUtil.ATT_ROUND_DATEEND) != null) {
      setDateEnd(new DateTime(pRoot.attribute(TournamentGOTEUtil.ATT_ROUND_DATEEND).getValue()));
    } else {
      LOGGER.log(Level.WARNING, "The attribute " + TournamentGOTEUtil.ATT_ROUND_DATEEND + " is null");
    }

    Element elementGames = pRoot.element(TournamentGOTEUtil.TAG_GAMES);
    if (elementGames != null) {
      List<Game> games = new ArrayList<Game>();
      @SuppressWarnings("unchecked")
      List<Element> gamesElements = elementGames.elements(TournamentGOTEUtil.TAG_GAME);
      if (gamesElements != null && !gamesElements.isEmpty()) {
        for (Element gameElement : gamesElements) {
          Game game = new Game();
          game.fromXML(gameElement, pTournament);
          games.add(game);
        }
      } else {
        LOGGER.log(Level.WARNING, "No round has been found");
      }
      setGameList(games);
    } else {
      LOGGER.log(Level.SEVERE, "The element " + TournamentGOTEUtil.TAG_GAMES + " is null");
    }
  }

  /**
   * Returns a game result of that round between 2 players. If no result is found,
   * TournamentOpenGothaUtil.VALUE_GAME_RESULT_UNKNOWN is returned
   * 
   * @param pBlackPlayer Player
   * @param pWhitePlayer Player
   * @return String
   */
  public String getGameResult(Player pBlackPlayer, Player pWhitePlayer) {
    for (Game game : getGameList()) {
      if (game.getBlack().equals(pBlackPlayer) && game.getWhite().equals(pWhitePlayer)) {
        return game.getResult();
      }
    }
    return TournamentOpenGothaUtil.VALUE_GAME_RESULT_UNKNOWN;
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
      // TODO should not add game with result like RESULT_WHITEWINS
      if (game.getResult() != null) {
        toBePlayedGames.add(game);
      }
    }
    return toBePlayedGames;
  }
}
