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

import com.gote.AppUtil;
import com.gote.util.Servers;
import com.gote.util.TournamentXMLUtil;

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

  /**
   * Default constructor and initializations
   */
  public Tournament() {
    setTitle(new String());
    setRounds(new ArrayList<Round>());
    setParticipantsList(new ArrayList<Player>());
    setServerType(Servers.NO_SERVER);
    setTournamentRules(new TournamentRules());
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
    Element root = doc.addElement(TournamentXMLUtil.TAG_TOURNAMENT);
    // Add attributes
    root.addAttribute(TournamentXMLUtil.ATT_TOURNAMENT_NAME, getTitle());
    root.addAttribute(TournamentXMLUtil.ATT_TOURNAMENT_SERVER, getServerType());

    // Add rules
    getTournamentRules().toXML(root);

    // Add players
    Element players = root.addElement(TournamentXMLUtil.TAG_PLAYERS);
    for (Player player : getParticipantsList()) {
      player.toXML(players);
    }
    // Add rounds
    Element rounds = root.addElement(TournamentXMLUtil.TAG_ROUNDS);
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
    if (pRoot.attribute(TournamentXMLUtil.ATT_TOURNAMENT_NAME) != null) {
      setTitle(pRoot.attribute(TournamentXMLUtil.ATT_TOURNAMENT_NAME).getValue());
    } else {
      LOGGER.log(Level.SEVERE, "The attribute " + TournamentXMLUtil.ATT_TOURNAMENT_NAME + " is null");
    }

    if (pRoot.attribute(TournamentXMLUtil.ATT_TOURNAMENT_SERVER) != null) {
      setServerType(pRoot.attribute(TournamentXMLUtil.ATT_TOURNAMENT_SERVER).getValue());
    } else {
      LOGGER.log(Level.SEVERE, "The attribute " + TournamentXMLUtil.ATT_TOURNAMENT_NAME + " is null");
    }

    Element elementTournamentRules = pRoot.element(TournamentXMLUtil.TAG_RULES);
    if (elementTournamentRules != null) {
      TournamentRules tournamentRules = new TournamentRules();
      tournamentRules.fromXML(elementTournamentRules);
      setTournamentRules(tournamentRules);
    } else {
      LOGGER.log(Level.SEVERE, "The element " + TournamentXMLUtil.TAG_RULES + " is null");
    }

    Element elementPlayers = pRoot.element(TournamentXMLUtil.TAG_PLAYERS);
    if (elementPlayers != null) {
      List<Player> players = new ArrayList<Player>();
      @SuppressWarnings("unchecked")
      List<Element> playersElements = elementPlayers.elements(TournamentXMLUtil.TAG_PLAYER);
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
      LOGGER.log(Level.SEVERE, "The element " + TournamentXMLUtil.TAG_ROUNDS + " is null");
    }

    Element elementRounds = pRoot.element(TournamentXMLUtil.TAG_ROUNDS);
    if (elementRounds != null) {
      List<Round> rounds = new ArrayList<Round>();
      @SuppressWarnings("unchecked")
      List<Element> roundsElements = elementRounds.elements(TournamentXMLUtil.TAG_ROUND);
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
      LOGGER.log(Level.SEVERE, "The element " + TournamentXMLUtil.TAG_ROUNDS + " is null");
    }
  }

  /**
   * Get a player from participants list with they name specialized for opengotha. If none is
   * found, null is returned.
   * 
   * @param pName Pseudo of player
   * @return Player
   */
  public Player getParticipantByNameOpenGotha(String pName) {
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
  public Player getParticipantByName(String pName) {
    for (Player player : getParticipantsList()) {
      if (pName.equalsIgnoreCase(player.getPseudo())) {
        return player;
      }
    }
    LOGGER.log(Level.WARNING, "No participant found with name " + pName);
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

}