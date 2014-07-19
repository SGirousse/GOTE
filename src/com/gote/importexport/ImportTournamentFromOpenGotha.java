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
package com.gote.importexport;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.gote.pojo.Game;
import com.gote.pojo.Player;
import com.gote.pojo.Round;
import com.gote.pojo.Tournament;
import com.gote.pojo.TournamentRules;
import com.gote.util.Servers;

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

  public static final String TAG_TOURNAMENT = "Tournament";

  public static final String TAG_PLAYERS = "Players";

  public static final String TAG_PLAYER = "Player";

  public static final String ATTRIBUTE_PLAYER_NAME = "name";

  public static final String ATTRIBUTE_PLAYER_FIRSTNAME = "firstName";

  public static final String ATTRIBUTE_PLAYER_RANK = "rank";

  public static final String TAG_GAMES = "Games";

  public static final String TAG_GAME = "Game";

  public static final String ATTRIBUTE_GAME_ROUND = "roundNumber";

  public static final String ATTRIBUTE_GAME_WHITE_PLAYER = "blackPlayer";

  public static final String ATTRIBUTE_GAME_BLACK_PLAYER = "whitePlayer";

  public static final String ATTRIBUTE_GAME_HANDICAP = "handicap";

  public static final String ATTRIBUTE_GAME_RESULT = "result";

  public static final String TAG_BYE_PLAYERS = "ByePlayers";

  public static final String TAG_BYE_PLAYER = "ByePlayer";

  public static final String TAG_TOURNAMENT_PARAMETER_SET = "TournamentParameterSet";

  public static final String TAG_GENERAL_PARAMETER_SET = "GeneralParameterSet";

  public static final String ATTRIBUTE_GENERAL_PARAMETER_SET_NAME = "name";

  public static final String ATTRIBUTE_GENERAL_PARAMETER_SET_KOMI = "komi";

  public static final String ATTRIBUTE_GENERAL_PARAMETER_SET_SIZE = "size";

  public static final String ATTRIBUTE_GENERAL_PARAMETER_SET_TIME_SYSTEM = "complementaryTimeSystem";

  public static final String ATTRIBUTE_GENERAL_PARAMETER_SET_BASIC_TIME = "basicTime";

  public static final String ATTRIBUTE_GENERAL_PARAMETER_SET_BYOYOMI_TIME = "canByoYomiTime";

  public static final String ATTRIBUTE_GENERAL_PARAMETER_SET_BYOYOMI_COUNT = "stdByoYomiTime";

  public static final String ATTRIBUTE_GENERAL_PARAMETER_SET_BEGIN_DATE = "beginDate";

  public static final String ATTRIBUTE_GENERAL_PARAMETER_SET_END_DATE = "endDate";

  public static final String ATTRIBUTE_GENERAL_PARAMETER_SET_ROUND_NUMBER = "numberOfRounds";

  public static final String TAG_HANDICAP_PARAMETER_SET = "HandicapParameterSet";

  @Override
  public Tournament createTournamentFromConfig(File pFile) {

    Tournament tournament = new Tournament();
    String content = getFileContent(pFile);

    if (content == null) {
      System.out.println("[ERROR] Content is null");
      return null;
    }

    SAXReader reader = new SAXReader();
    Document document;
    try {
      document = reader.read(new StringReader(content));
    } catch (DocumentException e) {
      e.printStackTrace();
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

  /**
   * Read the file and returns its content
   * 
   * @param pFile File
   * @return String the content
   */
  private String getFileContent(File pFile) {
    String content = new String();
    FileReader fileReader = null;

    try {
      fileReader = new FileReader(pFile);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
      return null;
    }

    BufferedReader bufferedReader = new BufferedReader(fileReader);
    String line = null;
    try {
      while ((line = bufferedReader.readLine()) != null) {
        content += line;
      }
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }

    try {
      fileReader.close();
    } catch (IOException e) {
      e.printStackTrace();
    }

    return content;
  }

  public boolean initTournament(Tournament pTournament, Element pElementTournament) {

    boolean init = false;
    Element elTournamentRulesSet = null;
    Element elPlayers = null;
    Element elGames = null;

    pTournament.setServerType(Servers.KGS);

    elTournamentRulesSet = pElementTournament.element(TAG_TOURNAMENT_PARAMETER_SET);
    init = elTournamentRulesSet != null;
    if (init) {
      init = initTournamentRules(pTournament, elTournamentRulesSet);
    } else {
      System.out.println("[ERROR] While getting tournament parameters from open gotha file.");
      return false;
    }

    if (init) {
      elPlayers = pElementTournament.element(TAG_PLAYERS);
      init = elPlayers != null;
    } else {
      System.out.println("[ERROR] During tournament rules initialization");
      return false;
    }
    if (init) {
      init = initPlayers(pTournament, elPlayers);
    } else {
      System.out.println("[ERROR] While getting players infos from open gotha file.");
      return false;
    }

    if (init) {
      elGames = pElementTournament.element(TAG_GAMES);
      init = elGames != null;
    } else {
      System.out.println("[ERROR] During players initialization");
      return false;
    }

    if (init) {
      init = initRounds(pTournament, elGames);
    } else {
      System.out.println("[ERROR] While getting games infos from open gotha file.");
      return false;
    }

    return init;
  }

  public boolean initTournamentRules(Tournament pTournament, Element pElementTournamentRulesSet) {
    boolean init = true;
    Element elementGeneralParameters = pElementTournamentRulesSet.element(TAG_GENERAL_PARAMETER_SET);
    String komi = elementGeneralParameters.attribute(ATTRIBUTE_GENERAL_PARAMETER_SET_KOMI).getValue();
    String size = elementGeneralParameters.attribute(ATTRIBUTE_GENERAL_PARAMETER_SET_SIZE).getValue();
    String timeSystem = elementGeneralParameters.attribute(ATTRIBUTE_GENERAL_PARAMETER_SET_TIME_SYSTEM).getValue();
    String basicTime = elementGeneralParameters.attribute(ATTRIBUTE_GENERAL_PARAMETER_SET_BASIC_TIME).getValue();
    String byoYomiDuration = elementGeneralParameters.attribute(ATTRIBUTE_GENERAL_PARAMETER_SET_BYOYOMI_TIME)
        .getValue();
    String numberOfByoYomi = elementGeneralParameters.attribute(ATTRIBUTE_GENERAL_PARAMETER_SET_BYOYOMI_COUNT)
        .getValue();
    String name = elementGeneralParameters.attribute(ATTRIBUTE_GENERAL_PARAMETER_SET_NAME).getValue();
    String startDate = elementGeneralParameters.attribute(ATTRIBUTE_GENERAL_PARAMETER_SET_BEGIN_DATE).getValue();
    String endDate = elementGeneralParameters.attribute(ATTRIBUTE_GENERAL_PARAMETER_SET_END_DATE).getValue();
    String numberOfRounds = elementGeneralParameters.attribute(ATTRIBUTE_GENERAL_PARAMETER_SET_ROUND_NUMBER)
        .getValue();

    pTournament.setTitle(name);

    TournamentRules tournamentRules = new TournamentRules();
    tournamentRules.setKomi(komi);
    tournamentRules.setSize(size);
    tournamentRules.setTimeSystem(timeSystem);
    tournamentRules.setBasicTime(basicTime);
    tournamentRules.setByoYomiDuration(byoYomiDuration);
    tournamentRules.setNumberOfByoYomi(numberOfByoYomi);
    tournamentRules.setTag("");

    pTournament.setTournamentRules(tournamentRules);

    return init;
  }

  public boolean initPlayers(Tournament pTournament, Element pElementPlayers) {
    boolean init = false;
    @SuppressWarnings("unchecked")
    List<Element> listOfPlayers = (List<Element>) pElementPlayers.elements(TAG_PLAYER);
    init = (listOfPlayers != null && !listOfPlayers.isEmpty());
    if (init) {
      List<Player> tournamentPlayers = new ArrayList<Player>();

      for (Element playerElement : listOfPlayers) {
        Player player = new Player();
        player.setPseudo(playerElement.attribute(ATTRIBUTE_PLAYER_NAME).getValue());
        player.setFirstname(playerElement.attribute(ATTRIBUTE_PLAYER_FIRSTNAME).getValue());
        player.setRank(playerElement.attribute(ATTRIBUTE_PLAYER_RANK).getValue());
        tournamentPlayers.add(player);
      }

      pTournament.setParticipantsList(tournamentPlayers);
    }

    return init;
  }

  public boolean initRounds(Tournament pTournament, Element pElementGames) {
    boolean init = false;
    @SuppressWarnings("unchecked")
    List<Element> listOfGames = (List<Element>) pElementGames.elements(TAG_GAME);
    init = (listOfGames != null && !listOfGames.isEmpty());
    if (init) {

      List<Round> tournamentRounds = new ArrayList<Round>();

      for (Element gameElement : listOfGames) {

        // Check round game number, if it exists, add it to the round if it does not exists create
        // the round first
        String roundNumberAsText = gameElement.attribute(ATTRIBUTE_GAME_ROUND).getValue();

        int roundPlacement = -1;
        List<Game> games = new ArrayList<Game>();
        Round round = new Round();

        if (roundNumberAsText != null && !roundNumberAsText.isEmpty()) {
          roundPlacement = getRoundPlacement(tournamentRounds, new Integer(roundNumberAsText));
        } else {
          System.out.println("[WARN] Round number was null or empty");
        }

        if (roundPlacement > -1) {
          games = tournamentRounds.get(roundPlacement).getGameList();
        } else {
          round.setNumber(new Integer(roundNumberAsText));
        }

        Game game = new Game();

        Player black = pTournament.getParticipantByName(gameElement.attribute(ATTRIBUTE_GAME_BLACK_PLAYER).getValue());
        Player white = pTournament.getParticipantByName(gameElement.attribute(ATTRIBUTE_GAME_WHITE_PLAYER).getValue());
        String result = gameElement.attribute(ATTRIBUTE_GAME_RESULT).getValue();
        String handicap = gameElement.attribute(ATTRIBUTE_GAME_HANDICAP).getValue();

        game.setBlack(black);
        game.setBlack(white);
        game.setResult(result);
        game.setHandicap(handicap);

        games.add(game);

        if (roundPlacement < 0) {
          round.setGameList(games);
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
