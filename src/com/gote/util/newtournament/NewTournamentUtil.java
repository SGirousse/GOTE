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
package com.gote.util.newtournament;

/**
 * 
 * Utility for new tournaments
 * 
 * @author SGirousse
 */
public final class NewTournamentUtil {

  /**
   * UI elements
   */

  /** Window title */
  public static final String WINDOW_TITLE = "Tournament manager - Création de tournoi";

  /**
   * Message Box const elements
   */

  /** Const title */
  public static final String CLOSE_WINDOW_TITLE = "Fermeture";

  /** Const text */
  public static final String CLOSE_WINDOW_MSG = "Confirmez-vous la fermeture de la fenêtre de création de tournoi ?\nToutes les configurations seront perdues.";

  /**
   * Buttons
   */

  /** Load config file button */
  public static final String BUTTON_LOAD_FILE_LABEL = "Charger une configuration";

  public static final String BUTTON_CREATE_LABEL = "Créer";

  public static final String BUTTON_CANCEL_LABEL = "Annuler";

  /**
   * Labels
   */

  /** Load config file label */
  public static final String LABEL_LOAD_FILE = "Configuration";

  /** Tournament source file label */
  public static final String LABEL_TOURNAMENT_SOURCE = "Sélection du serveur";

  /** Tournament title label */
  public static final String LABEL_TOURNAMENT_TITLE = "Titre du tournoi";

  /** Tournament tag label */
  public static final String LABEL_TOURNAMENT_TAG = "Tag";

  /** Tournament tag help label */
  public static final String LABEL_TOURNAMENT_TAG_HELP = "Permet la récupération des parties (Exemple : #TRN_GOON)";

  /**
   * Round table
   */
  public static final String ROUND_NUMBER = "Ronde";

  public static final String ROUND_START_DATE = "Date de début";

  public static final String ROUND_END_DATE = "Date de fin";

  public static String[] ROUND_TABLE_HEADERS = { ROUND_NUMBER, ROUND_START_DATE, ROUND_END_DATE };

  /**
   * Border
   */
  public static final String BORDER_TITLE_CONF = "Configuration";

  public static final String BORDER_TITLE_CRITERIA = "Critères";

  public static final String BORDER_TITLE_SOURCE = "Source";

  public static final String BORDER_TITLE_ROUNDS = "Tableau des rondes";
}
