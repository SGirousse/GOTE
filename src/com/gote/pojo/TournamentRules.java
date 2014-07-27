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

import java.util.logging.Level;
import java.util.logging.Logger;

import org.dom4j.Element;

import com.gote.util.xml.TournamentGOTEUtil;

/**
 * 
 * Representation of game rules use to check if the game is valid or not.
 * 
 * @author SGirousse
 */
public class TournamentRules {

  /** Class logger */
  private static Logger LOGGER = Logger.getLogger(TournamentRules.class.getName());

  /** TAG used to retrieve game of the tournament */
  private String tag;

  /** Basic komi to be used in the tournament */
  private String komi;

  /** Board size */
  private String size;

  /** Time system (e.g. "Japanese", "ING", ...) */
  private String timeSystem;

  /** Main time */
  private String basicTime;

  /** Byo-yomi duration */
  private String byoYomiDuration;

  /** Number of byoyomi periods */
  private String numberOfByoYomi;

  /**
   * Transform rules to xml
   * 
   * @param pRoot element "Tournaments"
   * @see TournamentGOTEUtil
   */
  public void toXML(Element pRoot) {
    // Create tournament rules element
    Element rules = pRoot.addElement(TournamentGOTEUtil.TAG_RULES);
    // Add attributes
    rules.addAttribute(TournamentGOTEUtil.ATT_RULES_TAG, getTag());
    rules.addAttribute(TournamentGOTEUtil.ATT_RULES_KOMI, getKomi());
    rules.addAttribute(TournamentGOTEUtil.ATT_RULES_SIZE, getSize());
    rules.addAttribute(TournamentGOTEUtil.ATT_RULES_TIMESYSTEM, getTimeSystem());
    rules.addAttribute(TournamentGOTEUtil.ATT_RULES_BASICTIME, getBasicTime());
    rules.addAttribute(TournamentGOTEUtil.ATT_RULES_BYODURATION, getByoYomiDuration());
    rules.addAttribute(TournamentGOTEUtil.ATT_RULES_BYOPERIODS, getNumberOfByoYomi());
  }

  /**
   * Load TournamentRules from XML
   * 
   * @param pRoot Element
   */
  public void fromXML(Element pRoot) {
    if (pRoot.attribute(TournamentGOTEUtil.ATT_RULES_TAG) != null) {
      setTag(pRoot.attribute(TournamentGOTEUtil.ATT_RULES_TAG).getValue());
    } else {
      LOGGER.log(Level.WARNING, "The attribute " + TournamentGOTEUtil.ATT_RULES_TAG + " is null");
    }

    if (pRoot.attribute(TournamentGOTEUtil.ATT_RULES_KOMI) != null) {
      setKomi(pRoot.attribute(TournamentGOTEUtil.ATT_RULES_KOMI).getValue());
    } else {
      LOGGER.log(Level.WARNING, "The attribute " + TournamentGOTEUtil.ATT_RULES_KOMI + " is null");
    }

    if (pRoot.attribute(TournamentGOTEUtil.ATT_RULES_SIZE) != null) {
      setSize(pRoot.attribute(TournamentGOTEUtil.ATT_RULES_SIZE).getValue());
    } else {
      LOGGER.log(Level.WARNING, "The attribute " + TournamentGOTEUtil.ATT_RULES_SIZE + " is null");
    }

    if (pRoot.attribute(TournamentGOTEUtil.ATT_RULES_TIMESYSTEM) != null) {
      setTimeSystem(pRoot.attribute(TournamentGOTEUtil.ATT_RULES_TIMESYSTEM).getValue());
    } else {
      LOGGER.log(Level.WARNING, "The attribute " + TournamentGOTEUtil.ATT_RULES_TIMESYSTEM + " is null");
    }

    if (pRoot.attribute(TournamentGOTEUtil.ATT_RULES_BASICTIME) != null) {
      setBasicTime(pRoot.attribute(TournamentGOTEUtil.ATT_RULES_BASICTIME).getValue());
    } else {
      LOGGER.log(Level.WARNING, "The attribute " + TournamentGOTEUtil.ATT_RULES_BASICTIME + " is null");
    }

    if (pRoot.attribute(TournamentGOTEUtil.ATT_RULES_BYODURATION) != null) {
      setByoYomiDuration(pRoot.attribute(TournamentGOTEUtil.ATT_RULES_BYODURATION).getValue());
    } else {
      LOGGER.log(Level.WARNING, "The attribute " + TournamentGOTEUtil.ATT_RULES_BYODURATION + " is null");
    }

    if (pRoot.attribute(TournamentGOTEUtil.ATT_RULES_BYOPERIODS) != null) {
      setNumberOfByoYomi(pRoot.attribute(TournamentGOTEUtil.ATT_RULES_BYOPERIODS).getValue());
    } else {
      LOGGER.log(Level.WARNING, "The attribute " + TournamentGOTEUtil.ATT_RULES_BYOPERIODS + " is null");
    }
  }

  /**
   * Tag getter
   * 
   * @return String
   */
  public String getTag() {
    return tag;
  }

  /**
   * Tag setter
   * 
   * @param pTag String
   */
  public void setTag(String pTag) {
    this.tag = pTag;
  }

  public String getKomi() {
    return komi;
  }

  public void setKomi(String komi) {
    this.komi = komi;
  }

  public String getSize() {
    return size;
  }

  public void setSize(String size) {
    this.size = size;
  }

  public String getTimeSystem() {
    return timeSystem;
  }

  public void setTimeSystem(String timeSystem) {
    this.timeSystem = timeSystem;
  }

  public String getBasicTime() {
    return basicTime;
  }

  public void setBasicTime(String basicTime) {
    this.basicTime = basicTime;
  }

  public String getByoYomiDuration() {
    return byoYomiDuration;
  }

  public void setByoYomiDuration(String byoYomiDuration) {
    this.byoYomiDuration = byoYomiDuration;
  }

  public String getNumberOfByoYomi() {
    return numberOfByoYomi;
  }

  public void setNumberOfByoYomi(String numberOfByoYomi) {
    this.numberOfByoYomi = numberOfByoYomi;
  }
}
