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
package com.gote.util.xml;

/**
 * 
 * Utility class for xml OpenGotha tags
 * 
 * @author sgirouss
 */
public class TournamentOpenGothaUtil {

  public static final String TAG_TOURNAMENT = "Tournament";

  public static final String TAG_PLAYERS = "Players";

  public static final String TAG_PLAYER = "Player";

  public static final String ATTRIBUTE_PLAYER_NAME = "name";

  public static final String ATTRIBUTE_PLAYER_FIRSTNAME = "firstName";

  public static final String ATTRIBUTE_PLAYER_RANK = "rank";

  public static final String TAG_GAMES = "Games";

  public static final String TAG_GAME = "Game";

  public static final String ATTRIBUTE_GAME_ROUND = "roundNumber";

  public static final String ATTRIBUTE_GAME_WHITE_PLAYER = "whitePlayer";

  public static final String ATTRIBUTE_GAME_BLACK_PLAYER = "blackPlayer";

  public static final String ATTRIBUTE_GAME_HANDICAP = "handicap";

  public static final String ATTRIBUTE_GAME_RESULT = "result";

  public static final String VALUE_GAME_RESULT_UNKNOWN = "RESULT_UNKNOWN";

  public static final String VALUE_GAME_RESULT_WHITEWINS = "RESULT_WHITEWINS";

  public static final String VALUE_GAME_RESULT_BLACKWINS = "RESULT_BLACKWINS";

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

}
