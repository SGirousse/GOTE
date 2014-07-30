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
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class ArchivePageManager extends KGSUtils {

  /** Class logger */
  private static Logger LOGGER = Logger.getLogger(ArchivePageManager.class.getName());

  /** Url of the page */
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
    } catch (IOException e) {
      LOGGER.log(Level.SEVERE, "IOException, connexion impossible to " + getPageUrl(), e);
      return null;
    } catch (IllegalArgumentException e) {
      LOGGER.log(Level.SEVERE, "The URL \"" + getPageUrl() + "\" was malformed", e);
      return null;
    }

    return doc;
  }

  public String getPageUrl() {
    return pageUrl;
  }

  public void setPageUrl(String pageUrl) {
    this.pageUrl = pageUrl;
  }
}
