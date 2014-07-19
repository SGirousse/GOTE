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

import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.json.JSONObject;

import com.gote.util.Servers;
import com.gote.util.TournamentUtils;

/**
 * 
 * Representation of a Tournament.
 * 
 * @author SGirousse
 */
public class Tournament {

  /** Unique tournament id */
  private String id;

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
    setId("-1");
    setTitle(new String());
    setRounds(new ArrayList<Round>());
    setParticipantsList(new ArrayList<Player>());
    setServerType(Servers.NO_SERVER);
    setTournamentRules(new TournamentRules());
  }

  public void fromJSON() {

  }

  public String toJSON() {
    JSONObject tournament = new JSONObject();

    return tournament.toString();
  }

  public void save() throws IOException {
    FileWriter writer = new FileWriter(getTitle() + ".xml");
    writer.write(toXML());
    writer.flush();
    writer.close();
  }

  public String toXML() {
    // Create document
    Document doc = DocumentHelper.createDocument();

    Element root = doc.addElement(TournamentUtils.TAG_TOURNAMENT);
    Element rounds = root.addElement(TournamentUtils.TAG_ROUNDS);

    for (Round round : getRounds()) {
      round.addToXML(rounds);
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

  public void fromXML() {

  }

  /**
   * Get a player from participants list with they name. If none is found, null is returned.
   * 
   * @param name Pseudo of player
   * @return Player
   */
  public Player getParticipantByName(String name) {
    for (Player player : getParticipantsList()) {

      // System.out.println("SEARCH *** compare " + player.getPseudo() + player.getFirstname());
      // System.out.println("SEARCH *** with    " + name);
      if (name.equalsIgnoreCase(player.getPseudo() + player.getFirstname())) {
        return player;
      }
    }
    System.out.println("FAILURE");
    return null;
  }

  /**
   * Id setter
   * 
   * @return String
   */
  public String getId() {
    return id;
  }

  /**
   * Id getter
   * 
   * @param pId String
   */
  public void setId(String pId) {
    this.id = pId;
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

/**
 * public String toXML() {
 * 
 * // Create document Document doc = DocumentHelper.createDocument();
 * 
 * // Add first tag Element root = doc.addElement(SavedSearchCustomHelper.TAG_ELEMENT_SEARCH);
 * 
 * // Add all the elements under main tag Element advSearch =
 * root.addElement(SavedSearchCustomHelper.TAG_ELEMENT_ADVSEARCH);
 * 
 * // Add each saved advanced search elements if (getTitle() != null) {
 * advSearch.addElement(SavedSearchCustomHelper.TAG_ELEMENT_TITLE).addText(getTitle()); } if
 * (getQuery() != null) {
 * advSearch.addElement(SavedSearchCustomHelper.TAG_ELEMENT_QUERY).addText(getQuery()); } if
 * (getTerms() != null) {
 * advSearch.addElement(SavedSearchCustomHelper.TAG_ELEMENT_TERMS).addText(getTerms()); } if
 * (getAuthor() != null) {
 * advSearch.addElement(SavedSearchCustomHelper.TAG_ELEMENT_AUTHOR).addText(getAuthor()); } if
 * (isGlobalSearch != null) {
 * advSearch.addElement(SavedSearchCustomHelper.TAG_ELEMENT_IS_GLOBAL_SEARCH).addText(
 * Boolean.toString(getIsGlobalSearch())); } else { // Default behavior
 * advSearch.addElement(SavedSearchCustomHelper
 * .TAG_ELEMENT_IS_GLOBAL_SEARCH).addText(Boolean.toString(false)); }
 * 
 * StringWriter out = new StringWriter(1024); XMLWriter writer; try { writer = new
 * XMLWriter(OutputFormat.createPrettyPrint()); } catch (UnsupportedEncodingException e) {
 * LOGGER.error("Problem while getting writer : " + e); return null; } writer.setWriter(out); try {
 * writer.write(doc); } catch (IOException e) { LOGGER.error("Error while writing into document : "
 * + e); }
 * 
 * // Return the friendly XML return out.toString();
 * 
 * }
 * 
 * @Override public AbstractSavedSearchCustom fromXML(String pXML) {
 * 
 *           // Reader for create doc from xml
 * 
 *           // Get the root element Element rootElement = document.getRootElement();
 * 
 *           // Get advsearch element Element advSearch =
 *           rootElement.element(SavedSearchCustomHelper.TAG_ELEMENT_ADVSEARCH);
 * 
 *           // Get title Element titleElement =
 *           advSearch.element(SavedSearchCustomHelper.TAG_ELEMENT_TITLE); if (titleElement !=
 *           null) { this.title = titleElement.getText(); }
 * 
 *           // Get keywords Element termsElement =
 *           advSearch.element(SavedSearchCustomHelper.TAG_ELEMENT_TERMS); if (termsElement !=
 *           null) { this.terms = termsElement.getText(); }
 * 
 *           // Get author Element authorElement =
 *           advSearch.element(SavedSearchCustomHelper.TAG_ELEMENT_AUTHOR); if (authorElement !=
 *           null) { this.author = authorElement.getText(); }
 * 
 *           // Get query content Element queryElement =
 *           advSearch.element(SavedSearchCustomHelper.TAG_ELEMENT_QUERY); if (queryElement !=
 *           null) { this.query = queryElement.getText(); }
 * 
 *           // Get isGlobalActive param Element isGlobalSearchElement =
 *           advSearch.element(SavedSearchCustomHelper.TAG_ELEMENT_AUTHOR); if
 *           (isGlobalSearchElement != null) { this.isGlobalSearch =
 *           Boolean.parseBoolean(isGlobalSearchElement.getText()); }
 * 
 *           return this;
 * 
 *           }
 */
