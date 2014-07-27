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

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.joda.time.DateTime;

import com.gote.AppUtil;
import com.gote.util.Servers;
import com.gote.util.xml.TournamentGOTEUtil;
import com.gote.util.xml.TournamentOpenGothaUtil;

/**
 * 
 * Representation of a Tournament.
 * 
 * @author SGirousse
 */
public class Tournament {
  /** Class logger */
  private static Logger LOGGER = Logger.getLogger(Tournament.class.getName());

  /** Tournament title */
  private String title;

  /** List of round being played */
  private List<Round> rounds;

  /** List of players in the tournament */
  private List<Player> participantsList;

  /** Tournament type */
  private String serverType;

  /** Rules and settings of the tournament */
  private TournamentRules tournamentRules;

  /** Start date of the tournament */
  private DateTime startDate;

  /** End date of the tournament */
  private DateTime endDate;

  /**
   * Default constructor and initializations
   */
  public Tournament() {
    setTitle(new String());
    setRounds(new ArrayList<Round>());
    setParticipantsList(new ArrayList<Player>());
    setServerType(Servers.NO_SERVER);
    setTournamentRules(new TournamentRules());
    setStartDate(AppUtil.APP_INIT_DATE);
    setEndDate(AppUtil.APP_INIT_DATE);
  }

  /**
   * Save tournament in a new xml file
   */
  public void save() {
    // TODO check befor creation in order to copy older file before saving the tournament in its
    // current state
    File file = new File(AppUtil.PATH_TO_TOURNAMENTS + getTitle().trim() + "/" + AppUtil.PATH_TO_SAVE
        + getTitle().trim() + ".xml");

    FileWriter fileWriter;

    try {
      fileWriter = new FileWriter(file);
      fileWriter.write(toXML());
      fileWriter.close();
    } catch (IOException e) {
      LOGGER.log(Level.SEVERE, "Error during writing file " + file.getName(), e);
    }
    LOGGER.log(Level.INFO, "File " + file.getName() + " saved");
  }

  /**
   * Transform Tournament in a formatted XML
   * 
   * @return XML to write as a String
   */
  public String toXML() {
    // Create document
    Document doc = DocumentHelper.createDocument();

    // Create tournament element
    Element root = doc.addElement(TournamentGOTEUtil.TAG_TOURNAMENT);
    // Add attributes
    root.addAttribute(TournamentGOTEUtil.ATT_TOURNAMENT_NAME, getTitle());
    root.addAttribute(TournamentGOTEUtil.ATT_TOURNAMENT_SERVER, getServerType());
    root.addAttribute(TournamentGOTEUtil.ATT_TOURNAMENT_DATESTART, getStartDate().toString());
    root.addAttribute(TournamentGOTEUtil.ATT_TOURNAMENT_DATEEND, getEndDate().toString());

    // Add rules
    getTournamentRules().toXML(root);

    // Add players
    Element players = root.addElement(TournamentGOTEUtil.TAG_PLAYERS);
    for (Player player : getParticipantsList()) {
      player.toXML(players);
    }
    // Add rounds
    Element rounds = root.addElement(TournamentGOTEUtil.TAG_ROUNDS);
    for (Round round : getRounds()) {
      round.toXML(rounds);
    }

    StringWriter out = new StringWriter(1024);
    XMLWriter writer;
    try {
      writer = new XMLWriter(OutputFormat.createPrettyPrint());
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
      return null;
    }
    writer.setWriter(out);
    try {
      writer.write(doc);
    } catch (IOException e) {
      e.printStackTrace();
    }

    // Return the friendly XML
    return out.toString();
  }

  /**
   * Load Tournament from XML
   * 
   * @param pRoot Tournament element
   */
  public void fromXML(Element pRoot) {
    if (pRoot.attribute(TournamentGOTEUtil.ATT_TOURNAMENT_NAME) != null) {
      setTitle(pRoot.attribute(TournamentGOTEUtil.ATT_TOURNAMENT_NAME).getValue());
    } else {
      LOGGER.log(Level.SEVERE, "The attribute " + TournamentGOTEUtil.ATT_TOURNAMENT_NAME + " is null");
    }

    if (pRoot.attribute(TournamentGOTEUtil.ATT_TOURNAMENT_SERVER) != null) {
      setServerType(pRoot.attribute(TournamentGOTEUtil.ATT_TOURNAMENT_SERVER).getValue());
    } else {
      LOGGER.log(Level.SEVERE, "The attribute " + TournamentGOTEUtil.ATT_TOURNAMENT_NAME + " is null");
    }

    if (pRoot.attribute(TournamentGOTEUtil.ATT_TOURNAMENT_DATESTART) != null) {
      setStartDate(new DateTime(pRoot.attribute(TournamentGOTEUtil.ATT_TOURNAMENT_DATESTART).getValue()));
    } else {
      LOGGER.log(Level.SEVERE, "The attribute " + TournamentGOTEUtil.ATT_TOURNAMENT_DATESTART + " is null");
    }

    if (pRoot.attribute(TournamentGOTEUtil.ATT_TOURNAMENT_DATEEND) != null) {
      setEndDate(new DateTime(pRoot.attribute(TournamentGOTEUtil.ATT_TOURNAMENT_DATEEND).getValue()));
    } else {
      LOGGER.log(Level.SEVERE, "The attribute " + TournamentGOTEUtil.ATT_TOURNAMENT_DATEEND + " is null");
    }

    Element elementTournamentRules = pRoot.element(TournamentGOTEUtil.TAG_RULES);
    if (elementTournamentRules != null) {
      TournamentRules tournamentRules = new TournamentRules();
      tournamentRules.fromXML(elementTournamentRules);
      setTournamentRules(tournamentRules);
    } else {
      LOGGER.log(Level.SEVERE, "The element " + TournamentGOTEUtil.TAG_RULES + " is null");
    }

    Element elementPlayers = pRoot.element(TournamentGOTEUtil.TAG_PLAYERS);
    if (elementPlayers != null) {
      List<Player> players = new ArrayList<Player>();
      @SuppressWarnings("unchecked")
      List<Element> playersElements = elementPlayers.elements(TournamentGOTEUtil.TAG_PLAYER);
      if (playersElements != null && !playersElements.isEmpty()) {
        for (Element playerElement : playersElements) {
          Player player = new Player();
          player.fromXML(playerElement);
          players.add(player);
        }
      } else {
        LOGGER.log(Level.WARNING, "No player has been found");
      }
      setParticipantsList(players);
    } else {
      LOGGER.log(Level.SEVERE, "The element " + TournamentGOTEUtil.TAG_ROUNDS + " is null");
    }

    Element elementRounds = pRoot.element(TournamentGOTEUtil.TAG_ROUNDS);
    if (elementRounds != null) {
      List<Round> rounds = new ArrayList<Round>();
      @SuppressWarnings("unchecked")
      List<Element> roundsElements = elementRounds.elements(TournamentGOTEUtil.TAG_ROUND);
      if (roundsElements != null && !roundsElements.isEmpty()) {
        for (Element roundElement : roundsElements) {
          Round round = new Round();
          round.fromXML(roundElement, this);
          rounds.add(round);
        }
      } else {
        LOGGER.log(Level.WARNING, "No round has been found");
      }
      setRounds(rounds);
    } else {
      LOGGER.log(Level.SEVERE, "The element " + TournamentGOTEUtil.TAG_ROUNDS + " is null");
    }
  }

  /**
   * Get a player from participants list with they name specialized for opengotha. If none is
   * found, null is returned.
   * 
   * @param pName Pseudo of player
   * @return Player
   */
  public Player getParticipantWithCompleteName(String pName) {
    for (Player player : getParticipantsList()) {
      if (pName.equalsIgnoreCase(player.getPseudo() + player.getFirstname())) {
        return player;
      }
    }
    LOGGER.log(Level.WARNING, "No participant found with name " + pName);
    return null;
  }

  /**
   * Get a player from participants list with they name. If none is found, null is returned.
   * 
   * @param pName Pseudo of player
   * @return Player
   */
  public Player getParticipantWithPseudo(String pName) {
    for (Player player : getParticipantsList()) {
      if (pName.equalsIgnoreCase(player.getPseudo())) {
        return player;
      }
    }
    LOGGER.log(Level.WARNING, "No participant found with name " + pName);
    return null;
  }

  /**
   * Game result returned for a game played between 2 players at a given round
   * 
   * @param pRoundNumber int round number
   * @param pBlackPlayer String black player complete name
   * @param pWhitePlayer String white player complete name
   * @return String game result
   * @see TournamentOpenGothaUtil
   */
  public String getGameResultWithCompleteName(int pRoundNumber, String pBlackPlayer, String pWhitePlayer) {
    Round round = getRoundWithNumber(pRoundNumber);
    if (round != null) {
      Player black = getParticipantWithCompleteName(pBlackPlayer);
      if (black != null) {
        Player white = getParticipantWithCompleteName(pWhitePlayer);
        if (white != null) {
          return round.getGameResult(black, white);
        }
      }
    }
    return TournamentOpenGothaUtil.VALUE_GAME_RESULT_UNKNOWN;
  }

  /**
   * Return a Round of the Tournament with the number in parameter
   * 
   * @param pRoundNumber int
   * @return Round or null of no Round matches
   */
  public Round getRoundWithNumber(int pRoundNumber) {
    for (Round round : getRounds()) {
      if (round.getNumber() == pRoundNumber) {
        return round;
      }
    }
    return null;
  }

  /**
   * Tournament title getter
   * 
   * @return String
   */
  public String getTitle() {
    return title;
  }

  /**
   * Tournament title setter
   * 
   * @param pTitle String
   */
  public void setTitle(String pTitle) {
    this.title = pTitle;
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

  /**
   * List of players getter
   * 
   * @return List<Player>
   */
  public List<Player> getParticipantsList() {
    return participantsList;
  }

  /**
   * List of players setter
   * 
   * @param pParticipantsList List<Player>
   */
  public void setParticipantsList(List<Player> pParticipantsList) {
    this.participantsList = pParticipantsList;
  }

  /**
   * Server type getter
   * 
   * @return String
   * @see com.tournament.util.Servers
   */
  public String getServerType() {
    return serverType;
  }

  /**
   * Server type setter
   * 
   * @param pServerType String
   */
  public void setServerType(String pServerType) {
    this.serverType = pServerType;
  }

  /**
   * Tournament rules getter
   * 
   * @return TournamentRules
   */
  public TournamentRules getTournamentRules() {
    return tournamentRules;
  }

  /**
   * Tournament rules setter
   * 
   * @param pTournamentRules TournamentRules
   */
  public void setTournamentRules(TournamentRules pTournamentRules) {
    this.tournamentRules = pTournamentRules;
  }

  /**
   * @return returns startDate.
   */
  public DateTime getStartDate() {
    return startDate;
  }

  /**
   * @param startDate startDate to set.
   */
  public void setStartDate(DateTime startDate) {
    this.startDate = startDate;
  }

  /**
   * @return returns endDate.
   */
  public DateTime getEndDate() {
    return endDate;
  }

  /**
   * @param endDate endDate to set.
   */
  public void setEndDate(DateTime endDate) {
    this.endDate = endDate;
  }

}