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
package com.gote.importexport;

import java.io.File;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.joda.time.DateTime;

import com.gote.pojo.Game;
import com.gote.pojo.Player;
import com.gote.pojo.Round;
import com.gote.pojo.Tournament;
import com.gote.pojo.TournamentRules;
import com.gote.util.ImportExportUtil;
import com.gote.util.xml.TournamentOpenGothaUtil;

/**
 * 
 * Class to import tournament from OpenGotha file. It has the following structure :
 * 
 * <pre>
 * <Tournament >
 * <Players>
 *   <Player agaExpirationDate="" agaId="" club="BuPe" country="HU" egfPin="16237463" ffgLicence="" ffgLicenceStatus="" firstName="Miklos" grade="12K" name="Csizmadia" participating="11111000001111111111" rank="11K" rating="1035" ratingOrigin="EGF" registeringStatus="FIN" smmsCorrection="0"/>
 * </Players>
 * <Games>
 *   <Game blackPlayer="ARFFMANRIINA" handicap="0" knownColor="true" result="RESULT_WHITEWINS" roundNumber="8" tableNumber="201" whitePlayer="SCHRAMMCHRISTINA"/>
 * </Games>
 * <TournamentParameterSet>
 *  <GeneralParameter SetbasicTime="150" beginDate="2013-07-28" canByoYomiTime="300" complementaryTimeSystem="STDBYOYOMI" director="" endDate="2013-08-10" fischerTime="10" genMMBar="4D" genMMFloor="20K" genMMS2ValueAbsent="1" genMMS2ValueBye="2" genNBW2ValueAbsent="0" genNBW2ValueBye="2" genRoundDownNBWMMS="true" komi="6.5" location="Olsztyn" name="EGC2013main" nbMovesCanTime="15" numberOfCategories="1" numberOfRounds="10" shortName="EGC2013" size="19" stdByoYomiTime="60" />
 *  <HandicapParameterSet hdBasedOnMMS="true" hdCeiling="9" hdCorrection="3" hdNoHdRankThreshold="30K"/>
 * </TournamentParameterSet>
 * <Tournament>
 * </pre>
 * 
 * @author SGirousse
 */
public class ImportTournamentFromOpenGotha extends ImportTournament {

  /** Class logger */
  private static Logger LOGGER = Logger.getLogger(ImportTournamentFromOpenGotha.class.getName());

  @Override
  public Tournament createTournamentFromConfig(File pFile) {

    LOGGER.log(Level.INFO, "A new tournament is going to be created from the file : " + pFile.getPath());

    Tournament tournament = new Tournament();
    String content = ImportExportUtil.getFileContent(pFile);

    if (content == null) {
      LOGGER.log(Level.SEVERE, "File \"" + pFile.getPath() + "\" content is null");
      return null;
    }

    SAXReader reader = new SAXReader();
    Document document;
    try {
      document = reader.read(new StringReader(content));
    } catch (DocumentException e) {
      LOGGER.log(Level.SEVERE, "DocumentException, creation stopped : " + e);
      return null;
    }

    Element pElementTournament = document.getRootElement();

    boolean initSuccess = initTournament(tournament, pElementTournament);

    if (initSuccess) {
      return tournament;
    } else {
      return null;
    }
  }

  public boolean initTournament(Tournament pTournament, Element pElementTournament) {

    boolean init = false;
    Element elTournamentRulesSet = null;
    Element elPlayers = null;
    Element elGames = null;

    elTournamentRulesSet = pElementTournament.element(TournamentOpenGothaUtil.TAG_TOURNAMENT_PARAMETER_SET);
    init = elTournamentRulesSet != null;
    if (init) {
      init = initTournamentRules(pTournament, elTournamentRulesSet);
    } else {
      LOGGER.log(Level.SEVERE, "While getting tournament parameters from open gotha file a problem occured.");
      return false;
    }

    if (init) {
      elPlayers = pElementTournament.element(TournamentOpenGothaUtil.TAG_PLAYERS);
      init = elPlayers != null;
    } else {
      LOGGER.log(Level.SEVERE, "During tournament rules initialization a problem occured.");
      return false;
    }
    if (init) {
      init = initPlayers(pTournament, elPlayers);
    } else {
      LOGGER.log(Level.SEVERE, "While getting players data from open gotha file a problem occured.");
      return false;
    }

    if (init) {
      elGames = pElementTournament.element(TournamentOpenGothaUtil.TAG_GAMES);
      init = elGames != null;
    } else {
      LOGGER.log(Level.SEVERE, "During players initialization a problem occured.");
      return false;
    }

    if (init) {
      init = initRounds(pTournament, elGames);
    } else {
      LOGGER.log(Level.SEVERE, "While getting games data from open gotha file a problem occured.");
      return false;
    }

    return init;
  }

  /**
   * Init tournament rules
   * 
   * @param pTournament Tournament being builded
   * @param pElementTournamentRulesSet Element in OpenGotha document
   * @return boolean, true if everything worked as expected
   */
  public boolean initTournamentRules(Tournament pTournament, Element pElementTournamentRulesSet) {
    LOGGER.log(Level.INFO, "Tournament rules initialization");
    boolean init = true;
    TournamentRules tournamentRules = new TournamentRules();

    Element elementGeneralParameters = pElementTournamentRulesSet
        .element(TournamentOpenGothaUtil.TAG_GENERAL_PARAMETER_SET);
    if (elementGeneralParameters == null) {
      return false;
    }

    if (elementGeneralParameters.attribute(TournamentOpenGothaUtil.ATTRIBUTE_GENERAL_PARAMETER_SET_KOMI) != null) {
      tournamentRules.setKomi(elementGeneralParameters.attribute(
          TournamentOpenGothaUtil.ATTRIBUTE_GENERAL_PARAMETER_SET_KOMI).getValue());
    }

    if (elementGeneralParameters.attribute(TournamentOpenGothaUtil.ATTRIBUTE_GENERAL_PARAMETER_SET_SIZE) != null) {
      tournamentRules.setSize(elementGeneralParameters.attribute(
          TournamentOpenGothaUtil.ATTRIBUTE_GENERAL_PARAMETER_SET_SIZE).getValue());
    }

    if (elementGeneralParameters.attribute(TournamentOpenGothaUtil.ATTRIBUTE_GENERAL_PARAMETER_SET_TIME_SYSTEM) != null) {
      tournamentRules.setTimeSystem(elementGeneralParameters.attribute(
          TournamentOpenGothaUtil.ATTRIBUTE_GENERAL_PARAMETER_SET_TIME_SYSTEM).getValue());
    }

    if (elementGeneralParameters.attribute(TournamentOpenGothaUtil.ATTRIBUTE_GENERAL_PARAMETER_SET_BASIC_TIME) != null) {
      tournamentRules.setBasicTime(elementGeneralParameters.attribute(
          TournamentOpenGothaUtil.ATTRIBUTE_GENERAL_PARAMETER_SET_BASIC_TIME).getValue());
    }

    if (elementGeneralParameters.attribute(TournamentOpenGothaUtil.ATTRIBUTE_GENERAL_PARAMETER_SET_BYOYOMI_TIME) != null) {
      tournamentRules.setByoYomiDuration(elementGeneralParameters.attribute(
          TournamentOpenGothaUtil.ATTRIBUTE_GENERAL_PARAMETER_SET_BYOYOMI_TIME).getValue());
    }

    if (elementGeneralParameters.attribute(TournamentOpenGothaUtil.ATTRIBUTE_GENERAL_PARAMETER_SET_BYOYOMI_COUNT) != null) {
      tournamentRules.setNumberOfByoYomi(elementGeneralParameters.attribute(
          TournamentOpenGothaUtil.ATTRIBUTE_GENERAL_PARAMETER_SET_BYOYOMI_COUNT).getValue());
    }

    pTournament.setTournamentRules(tournamentRules);

    if (elementGeneralParameters.attribute(TournamentOpenGothaUtil.ATTRIBUTE_GENERAL_PARAMETER_SET_NAME) != null) {
      pTournament.setTitle(elementGeneralParameters.attribute(
          TournamentOpenGothaUtil.ATTRIBUTE_GENERAL_PARAMETER_SET_NAME).getValue());
    } else {
      LOGGER.log(Level.WARNING, "No title found for the tournament");
    }

    if (elementGeneralParameters.attribute(TournamentOpenGothaUtil.ATTRIBUTE_GENERAL_PARAMETER_SET_BEGIN_DATE) != null) {
      pTournament.setStartDate(new DateTime(elementGeneralParameters.attribute(
          TournamentOpenGothaUtil.ATTRIBUTE_GENERAL_PARAMETER_SET_BEGIN_DATE).getValue()));
    } else {
      LOGGER.log(Level.WARNING, "No start date tournament");
    }

    if (elementGeneralParameters.attribute(TournamentOpenGothaUtil.ATTRIBUTE_GENERAL_PARAMETER_SET_END_DATE) != null) {
      pTournament.setEndDate(new DateTime(elementGeneralParameters.attribute(
          TournamentOpenGothaUtil.ATTRIBUTE_GENERAL_PARAMETER_SET_END_DATE).getValue()));
    } else {
      LOGGER.log(Level.WARNING, "No end date tournament");
    }

    // String numberOfRounds = elementGeneralParameters.attribute(
    // TournamentOpenGothaUtil.ATTRIBUTE_GENERAL_PARAMETER_SET_ROUND_NUMBER).getValue();

    return init;
  }

  /**
   * Init players list
   * 
   * @param pTournament Tournament being builded
   * @param pElementPlayers Element in OpenGotha document
   * @return boolean, true if everything worked as expected
   */
  public boolean initPlayers(Tournament pTournament, Element pElementPlayers) {
    LOGGER.log(Level.INFO, "Players initialization");
    boolean init = false;
    @SuppressWarnings("unchecked")
    List<Element> listOfPlayers = (List<Element>) pElementPlayers.elements(TournamentOpenGothaUtil.TAG_PLAYER);
    init = (listOfPlayers != null && !listOfPlayers.isEmpty());
    if (init) {
      List<Player> tournamentPlayers = new ArrayList<Player>();

      for (Element playerElement : listOfPlayers) {
        Player player = new Player();
        player.setPseudo(playerElement.attribute(TournamentOpenGothaUtil.ATTRIBUTE_PLAYER_NAME).getValue());
        player.setFirstname(playerElement.attribute(TournamentOpenGothaUtil.ATTRIBUTE_PLAYER_FIRSTNAME).getValue());
        player.setRank(playerElement.attribute(TournamentOpenGothaUtil.ATTRIBUTE_PLAYER_RANK).getValue());
        tournamentPlayers.add(player);
      }

      pTournament.setParticipantsList(tournamentPlayers);
    }

    return init;
  }

  /**
   * Init rounds and games
   * 
   * @param pTournament Tournament being builded
   * @param pElementGames Element in OpenGotha document
   * @return boolean, true if everything worked as expected
   */
  public boolean initRounds(Tournament pTournament, Element pElementGames) {
    LOGGER.log(Level.INFO, "Rounds initialization");
    boolean init = false;
    @SuppressWarnings("unchecked")
    List<Element> listOfGames = (List<Element>) pElementGames.elements(TournamentOpenGothaUtil.TAG_GAME);
    init = (listOfGames != null && !listOfGames.isEmpty());
    if (init) {

      List<Round> tournamentRounds = new ArrayList<Round>();

      for (Element gameElement : listOfGames) {

        // Check round game number, if it exists, add it to the round if it does not exists create
        // the round first
        String roundNumberAsText = gameElement.attribute(TournamentOpenGothaUtil.ATTRIBUTE_GAME_ROUND).getValue();

        int roundPlacement = -1;
        List<Game> games = new ArrayList<Game>();
        Round round = new Round();

        if (roundNumberAsText != null && !roundNumberAsText.isEmpty()) {
          roundPlacement = getRoundPlacement(tournamentRounds, new Integer(roundNumberAsText));
        } else {
          LOGGER.log(Level.WARNING, "No round number in configuration file. Line is : " + gameElement.toString());
        }

        if (roundPlacement > -1) {
          games = tournamentRounds.get(roundPlacement).getGameList();
        } else {
          LOGGER.log(Level.INFO, "Round " + roundNumberAsText + " is new and will be created.");
          round.setNumber(new Integer(roundNumberAsText));
        }

        Game game = new Game();

        Player black = pTournament.getParticipantWithCompleteName(gameElement.attribute(
            TournamentOpenGothaUtil.ATTRIBUTE_GAME_BLACK_PLAYER).getValue());
        Player white = pTournament.getParticipantWithCompleteName(gameElement.attribute(
            TournamentOpenGothaUtil.ATTRIBUTE_GAME_WHITE_PLAYER).getValue());
        String result = gameElement.attribute(TournamentOpenGothaUtil.ATTRIBUTE_GAME_RESULT).getValue();
        String handicap = gameElement.attribute(TournamentOpenGothaUtil.ATTRIBUTE_GAME_HANDICAP).getValue();

        game.setBlack(black);
        game.setWhite(white);
        game.setResult(result);
        game.setHandicap(handicap);
        games.add(game);

        if (roundPlacement < 0) {
          round.setGameList(games);
          round.setDateStart(pTournament.getStartDate());
          round.setDateEnd(pTournament.getEndDate());
          tournamentRounds.add(round);
        }

      }

      pTournament.setRounds(tournamentRounds);
    }

    return init;
  }

  /**
   * Get the round placement in the list with its number. Return -1 if no round has been found with
   * that number.
   * 
   * @param pListOfRounds List<Round> of the tournament
   * @param pRoundNumber Round number used to found Round placement
   * @return Position of the round
   */
  public int getRoundPlacement(List<Round> pListOfRounds, int pRoundNumber) {
    for (int i = 0; i < pListOfRounds.size(); i++) {
      if (pListOfRounds.get(i).getNumber() == pRoundNumber) {
        return i;
      }
    }
    return -1;
  }
}
