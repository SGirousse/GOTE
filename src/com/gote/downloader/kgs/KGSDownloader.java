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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JTextArea;

import org.joda.time.DateTime;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.gote.downloader.GameDownloader;
import com.gote.pojo.Game;
import com.gote.pojo.Round;
import com.gote.pojo.Tournament;

/**
 * 
 * Class in charge to retrieve KGS games. Note : KGS archives have an "anti-bot" policy, so you
 * must wait some seconds before checking the next game.
 * 
 * @author SGirousse
 */
public class KGSDownloader extends GameDownloader {

  /** Const waiting time */
  private static final int WAITING_TIME = 5000;

  /** Class logger */
  private static Logger LOGGER = Logger.getLogger(KGSDownloader.class.getName());

  /** Map of players <-> Document available */
  private Map<String, Document> playersArchives;

  public KGSDownloader(Tournament pTournament, JTextArea pJTextArea) {
    super(pTournament, pJTextArea);
  }

  @Override
  public void startDownload() {
    playersArchives = new HashMap<String, Document>();
    for (Round round : tournament.getRounds()) {
      DateTime startDate = getStartDate(round);
      DateTime endDate = getEndDate(round);
      for (Game game : round.getToBePlayedGameList()) {
        // In order to avoid multiple archive access, once a player archive has been downloaded, it
        // is temporary stored
        Document archivePage = null;

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

  public Document getPlayerArchive(String pPlayer, DateTime pStartDate, DateTime pEndDate) {
    List<Game> listGames = new ArrayList<Game>();
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

    log(Level.FINE, "date : " + pStartDate);
    log(Level.FINE, "date : " + pEndDate);
    // For each year until the end year or the end of the current year
    for (int year = pStartDate.getYear(); year <= minEndYear; year++) {

      log(Level.FINE, "" + year);
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
        log(Level.FINE, "mois " + month);
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

      return archivePageManager.getArchivePage();
    }
    return null;
  }

  private void retrieveAndUpdateGame(Game pGame, Document pPlayerArchivePage) {
    Elements tableRows = pPlayerArchivePage.select("tr");

    for (Element e : tableRows) {
      LOGGER.log(Level.INFO, "[TRACE] New row checked " + e.toString());

      // "Visible", "Blanc", "Noir", "Genre", "Debutee le", "Type", "Resultat"
      Elements tableCells = e.getElementsByTag("td");

      if (tableCells != null && tableCells.size() > 0) {
        String gameUrl = isPublicGame(tableCells.first());

        // May check with time if you can leave or continue
        if (gameUrl != null && !gameUrl.isEmpty()) {
          if (gameUrl.contains(pGame.getBlack().getPseudo()) && gameUrl.contains(pGame.getWhite().getPseudo())) {
            System.out.println("Game : " + tableCells.toString());
            pGame.setGameUrl(gameUrl);
            // File sgf = new File("KGSTM-Test1");
            // URL url = new URL(gameUrl);
            // FileUtils.copyURLToFile(url,sgf);
          }
        } else {
          log(Level.INFO, "La partie " + tableCells
              + " n'est pas visible ou un probleme a eu lieu lors de la recuperation de l'url");
        }
      } else {
        log(Level.INFO, "Un probleme a empeche l'analyse de la partie " + e);
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

  private DateTime getStartDate(Round pRound) {
    DateTime startDate = pRound.getDateStart();
    if (startDate == null || startDate == new DateTime(1999, 1, 1, 0, 0)) {
      // startDate = tournament.getStartDate();
      if (startDate == null) {
        log(Level.WARNING, "No start date, archives will be fetched since 01/01/2000. This will be long.");
        startDate = new DateTime(2000, 1, 1, 0, 0);
      }
    }
    return startDate;
  }

  private DateTime getEndDate(Round pRound) {
    DateTime endDate = pRound.getDateEnd();
    if (endDate == null || endDate == new DateTime(1999, 1, 1, 0, 0)) {
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
}
