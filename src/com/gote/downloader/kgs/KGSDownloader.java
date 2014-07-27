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
package com.gote.downloader.kgs;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import javax.swing.JTextArea;

import org.apache.commons.io.FileUtils;
import org.joda.time.DateTime;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.gote.AppUtil;
import com.gote.downloader.GameDownloader;
import com.gote.pojo.Game;
import com.gote.pojo.Round;
import com.gote.pojo.Tournament;
import com.gote.util.xml.TournamentOpenGothaUtil;

/**
 * 
 * Class in charge to retrieve KGS games. Note : KGS archives have an "anti-bot" policy, so you
 * must wait some seconds before checking the next game.
 * 
 * @author SGirousse
 */
public class KGSDownloader extends GameDownloader {

  /** Class logger */
  private static Logger LOGGER = Logger.getLogger(KGSDownloader.class.getName());

  /** Const waiting time */
  private static final int WAITING_TIME = 5000;

  /**
   * Game td element regex
   * 
   * <pre>
   * Sample : <td><a href="http://files.gokgs.com/games/2013/5/21/MilanMilan-twoeye.sgf">Oui</a></td>
   * </pre>
   */
  private static String gameLinkRegex = "<td>\\s*<a\\shref[\\w\\p{Punct}\\s]*>(Yes|No|Oui|Non)</a></td>\\s*";

  /**
   * Player td element regex
   * 
   * <pre>
   * Sample : <td><a href="gameArchives.jsp?user=MilanMilan">MilanMilan
   * [9d]</a></td>
   * </pre>
   */
  private static String playerRegex = "<td>\\s*<a\\shref[\\w\\p{Punct}\\s]*>\\w*\\s*\\p{Punct}[0-9]*(k|d)\\p{Punct}</a></td>\\s*";

  /**
   * Regex of classic game line. It is not ultra-precise or developed, the main idea is just to
   * avoid fetching "no game" related lines
   */
  private static String regexGame = "<tr>\\s*"
      + gameLinkRegex
      + playerRegex
      + playerRegex
      + "<td>[\\w\\p{Punct}\\s]*</td>\\s*<td>[\\w\\p{Punct}\\s]*</td>\\s*<td>[\\w\\p{Punct}\\s]*</td>\\s*<td>[\\w\\p{Punct}\\s]*</td>\\s*</tr>";

  /** Map of players <-> Document available */
  private Map<String, List<Document>> playersArchives;

  public static final int GAMEURL = 0;

  public static final int WHITEURL = 1;

  public static final int BLACKURL = 2;

  public static final int SIZE = 3;

  public static final int TIME = 4;

  public static final int GAMETYPE = 5;

  public static final int RESULT = 6;

  public KGSDownloader(Tournament pTournament, JTextArea pJTextArea) {
    super(pTournament, pJTextArea);
  }

  @Override
  public void startDownload() {
    playersArchives = new HashMap<String, List<Document>>();
    for (Round round : tournament.getRounds()) {
      DateTime startDate = getStartDate(round);
      DateTime endDate = getEndDate(round);
      for (Game game : round.getToBePlayedGameList()) {
        // In order to avoid multiple archive access, once a player archive has been downloaded, it
        // is temporary stored
        List<Document> archivePage = new ArrayList<Document>();

        if (playersArchives.get(game.getWhite().getPseudo()) != null) {
          archivePage = playersArchives.get(game.getWhite().getPseudo());
          log(Level.INFO, "Game archive from white " + game.getWhite().getPseudo());
        } else if (playersArchives.get(game.getBlack().getPseudo()) != null) {
          log(Level.INFO, "Game archive from black " + game.getBlack().getPseudo());
          archivePage = playersArchives.get(game.getBlack().getPseudo());
        } else {
          // Get the archives and update games
          archivePage = getPlayerArchive(game.getWhite().getPseudo(), startDate, endDate);
          log(Level.INFO, "Archive page builded  with white pseudo " + game.getWhite().getPseudo());
          if (archivePage != null) {
            playersArchives.put(game.getWhite().getPseudo(), archivePage);
          }
        }

        if (archivePage == null) {
          log(Level.SEVERE, "An error occured, no update possible");
          continue;
        }

        // Finally update
        retrieveAndUpdateGame(game, archivePage);
      }
    }
  }

  @Override
  public boolean checkGameAccessAvailability() {
    return false;
  }

  public List<Document> getPlayerArchive(String pPlayer, DateTime pStartDate, DateTime pEndDate) {
    List<Document> archivesPages = new ArrayList<Document>();
    ArchivePageUrlBuilder archivePageUrlBuilder;

    ArchivePageManager archivePageManager = new ArchivePageManager();

    // Found the maximum checkable year and month to avoid checking inexistant page (time lost and
    // more exception possibilities)
    int minEndMonth = 1;
    int minEndYear = pStartDate.getYear();
    DateTime today = new DateTime();
    if (pEndDate.getYear() > today.getYear()) {
      minEndMonth = today.getMonthOfYear();
      minEndYear = today.getYear();
    } else if (pEndDate.getYear() < today.getYear()) {
      minEndMonth = pEndDate.getMonthOfYear();
      minEndYear = pEndDate.getYear();
    } else {
      minEndMonth = pEndDate.getMonthOfYear() < today.getMonthOfYear() ? pEndDate.getMonthOfYear() : today
          .getMonthOfYear();
      minEndYear = today.getYear();
    }

    // For each year until the end year or the end of the current year
    for (int year = pStartDate.getYear(); year <= minEndYear; year++) {

      // Found intervals in order to avoid time lost and inexistant pages access
      int firstMonth = 1;
      int lastMonth = 13;
      if (year == pStartDate.getYear()) {
        firstMonth = pStartDate.getMonthOfYear();
      }
      if (year == minEndYear) {
        lastMonth = minEndMonth;
      }

      // For each month
      for (int month = firstMonth; month <= lastMonth; month++) {
        // Wait for a amount of time, then KGS will not block the access
        try {
          Thread.sleep(WAITING_TIME);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
        archivePageUrlBuilder = new ArchivePageUrlBuilder(pPlayer, new Integer(year).toString(),
            new Integer(month).toString());
        archivePageManager = new ArchivePageManager(archivePageUrlBuilder.getUrl());
      }

      Document doc = archivePageManager.getArchivePage();
      if (doc != null) {
        archivesPages.add(doc);
      }
    }
    return archivesPages;
  }

  /**
   * Try to found out if a game has been already played or by looking into archives page by page.
   * 
   * @param pGame Game to found and update
   * @param pPlayerArchivePages List of archive pages
   */
  private void retrieveAndUpdateGame(Game pGame, List<Document> pPlayerArchivePages) {
    for (Document playerArchivePage : pPlayerArchivePages) {
      Elements tableRows = playerArchivePage.select("tr");

      for (Element row : tableRows) {
        if (Pattern.matches(regexGame, row.toString())) {
          // LOGGER.log(Level.INFO, "[TRACE] New row checked " + row.toString());

          // "Visible", "Blanc", "Noir", "Genre", "Debutee le", "Type", "Resultat"
          Elements tableCells = row.getElementsByTag("td");

          String gameUrl = isPublicGame(tableCells.get(GAMEURL));

          // May check with time if you can leave or continue
          if (gameUrl != null && !gameUrl.isEmpty()) {
            if (gameUrl.toLowerCase().contains(pGame.getBlack().getPseudo().toLowerCase())
                && gameUrl.toLowerCase().contains(pGame.getWhite().getPseudo().toLowerCase())) {
              pGame.setGameUrl(gameUrl);
              pGame.setResult(getStdResultFromKGSResult(tableCells.get(RESULT).text()));
              File sgf = new File(AppUtil.PATH_TO_TOURNAMENTS + tournament.getTitle() + "/" + AppUtil.PATH_TO_SGFS
                  + tournament.getTitle().trim() + "_round" + pGame.getBlack().getPseudo() + "_"
                  + pGame.getWhite().getPseudo() + ".sgf");
              try {
                URL url = new URL(gameUrl);
                FileUtils.copyURLToFile(url, sgf);
              } catch (MalformedURLException e) {
                log(Level.WARNING, "URL " + gameUrl + " malformee", e);
              } catch (IOException e) {
                log(Level.WARNING, "Erreur lors de l'ecriture du fichier", e);
              }

              // Leave the process
              return;
            }
          } else {
            log(Level.INFO, "La partie " + tableCells
                + " n'est pas visible ou un probleme a eu lieu lors de la recuperation de l'url");
          }
        }
      }
    }
  }

  /**
   * Check if a game is public, if yes, then the URL of that game will be sent back.
   * 
   * @param pCell Element which represents the first KGS archives column
   * @return link of the SGF or null
   */
  public String isPublicGame(Element pCell) {
    Elements a = pCell.getElementsByTag("a");

    if (a != null && a.size() > 0) {
      // Check if it is a visible game
      if (a.html().equals(KGSUtils.KGS_TAG_FR_YES)) {
        return a.attr("href");
      }
    }

    return null;
  }

  /**
   * From KGS result, tells the game result
   * 
   * @param pKGSResult String as "W+0.5"
   * @return TournamentOpenGothaUtil Result value
   */
  public String getStdResultFromKGSResult(String pKGSResult) {
    if (Pattern.matches("(w|W)[\\w\\p{Punct}\\s]*", pKGSResult)) {
      return TournamentOpenGothaUtil.VALUE_GAME_RESULT_WHITEWINS;
    } else if (Pattern.matches("(b|B)[\\w\\p{Punct}\\s]*", pKGSResult)) {
      return TournamentOpenGothaUtil.VALUE_GAME_RESULT_BLACKWINS;
    }

    return TournamentOpenGothaUtil.VALUE_GAME_RESULT_UNKNOWN;
  }

  private DateTime getStartDate(Round pRound) {
    DateTime startDate = pRound.getDateStart();
    if (startDate == null || startDate == AppUtil.APP_INIT_DATE) {
      // startDate = tournament.getStartDate();
      if (startDate == null) {
        log(Level.WARNING, "No start date, archives will be fetched since 01/01/2000. This will be long.");
        startDate = AppUtil.APP_INIT_DATE;
      }
    }
    return startDate;
  }

  private DateTime getEndDate(Round pRound) {
    DateTime endDate = pRound.getDateEnd();
    if (endDate == null || endDate == AppUtil.APP_INIT_DATE) {
      // startDate = tournament.getEndDate();
      if (endDate == null) {
        endDate = new DateTime();
      }
    }
    return endDate;
  }

  @Override
  protected void log(Level pLogLevel, String pLogText) {
    super.log(pLogLevel, pLogText);
    LOGGER.log(pLogLevel, pLogText);
  }

  @Override
  protected void log(Level pLogLevel, String pLogText, Exception pException) {
    super.log(pLogLevel, pLogText);
    LOGGER.log(pLogLevel, pLogText, pException);
  }
}
