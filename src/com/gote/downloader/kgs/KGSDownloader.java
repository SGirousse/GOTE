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

import org.joda.time.DateTime;
import org.jsoup.nodes.Document;

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

  /** Map of players <-> Document available */
  private Map<String, Document> playersArchives;

  public KGSDownloader(Tournament pTournament) {
    super(pTournament);
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

        System.out.println(game.getWhite().getRank());
        System.out.println(game.getResult());

        if (playersArchives.get(game.getWhite().getPseudo()) != null) {
          archivePage = playersArchives.get(game.getWhite().getPseudo());
        } else if (playersArchives.get(game.getBlack().getPseudo()) != null) {
          archivePage = playersArchives.get(game.getBlack().getPseudo());
        } else {
          // Get the archives and update games
          // archivePage = getPlayerArchive(game.getWhite().getPseudo(), startDate, endDate);
          // if (archivePage != null) {
          // playersArchives.put(game.getWhite().getPseudo(), archivePage);
          // }
        }

        if (archivePage == null) {
          System.out.println("ERROR ** an error occured, no update possible");
          continue;
        }
        // Finally update
        retrieveAndUpdateGame(game, archivePage);
      }
    }
  }

  @Override
  public boolean checkGameAccessAvailability() {
    // TODO Auto-generated method stub
    return false;
  }

  public Document getPlayerArchive(String pPlayer, DateTime pStartDate, DateTime pEndDate) {
    List<Game> listGames = new ArrayList<Game>();
    ArchivePageUrlBuilder archivePageUrlBuilder;

    ArchivePageManager archivePageManager;

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
      for (int month = firstMonth; month < lastMonth; month++) {
        // Wait for a amount of time, then KGS will not block the access
        try {
          Thread.sleep(WAITING_TIME);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
        archivePageUrlBuilder = new ArchivePageUrlBuilder(pPlayer, new Integer(year).toString(),
            new Integer(month).toString());
        archivePageManager = new ArchivePageManager(archivePageUrlBuilder.getUrl());
        return archivePageManager.getArchivePage();
      }
    }
    return null;
  }

  private void retrieveAndUpdateGame(Game pGame, Document pPlayerArchivePage) {
  }

  private DateTime getStartDate(Round pRound) {
    DateTime startDate = pRound.getDateStart();
    if (startDate == null || startDate == new DateTime(1999, 1, 1, 0, 0)) {
      // startDate = tournament.getStartDate();
      if (startDate == null) {
        System.out.println("WARNING ** No start date, archives will be fetched since 01/01/2000. This will be long.");
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
}
