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

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ArchivePageManager extends KGSUtils {

  private String pageUrl;

  public ArchivePageManager() {
    this.setPageUrl("");
  }

  public ArchivePageManager(String pPageUrl) {
    this.setPageUrl(pPageUrl);
  }

  public Document getArchivePage() {
    Document doc = null;
    try {
      doc = Jsoup.connect(getPageUrl()).get();
      Elements tableRows = doc.select("tr");

      for (Element e : tableRows) {
        // System.out.println("[TRACE] New row checked " + e.toString());

        // "Visible", "Blanc", "Noir", "Genre", "Debutee le", "Type", "Resultat"
        Elements tableCells = e.getElementsByTag("td");

        if (tableCells != null && tableCells.size() > 0) {
          String gameUrl = isPublicGame(tableCells.first());

          // May check with time if you can leave or continue
          if (gameUrl != null && !gameUrl.isEmpty()) {
            System.out.println("[TRACE] Game visible, url is : " + gameUrl);
            if (gameUrl.contains("lambic12")) {
              System.out.println("Game : " + tableCells.toString());
              // File sgf = new File("KGSTM-Test1");
              // URL url = new URL(gameUrl);
              // FileUtils.copyURLToFile(url,sgf);
            }
          } else {
            // System.out.println("[TRACE] Private game : unchecked");
          }
        } else {
          // System.out.println("[ERROR] No td element in that row");
        }

      }

    } catch (IOException e) {
      e.printStackTrace();
    }

    return doc;
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
      if (a.html().equals(KGS_TAG_FR_YES)) {
        return a.attr("href");
      }
      // System.out.println("[TRACE] Game visible = false");
    }

    // System.out.println("[ERROR] No <a> element in cell : " + pCell.toString());
    return null;
  }

  public String getPageUrl() {
    return pageUrl;
  }

  public void setPageUrl(String pageUrl) {
    this.pageUrl = pageUrl;
  }
}
