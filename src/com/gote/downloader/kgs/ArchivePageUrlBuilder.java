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

public class ArchivePageUrlBuilder extends KGSUtils {

  private String user;

  private String year;

  private String month;

  public ArchivePageUrlBuilder() {
    setUser("");
    setYear("");
    setMonth("");
  }

  public ArchivePageUrlBuilder(String pUser, String pYear, String pMonth) {
    setUser(pUser);
    setYear(pYear);
    setMonth(pMonth);
  }

  public String getUrl() {
    return KGS_ARCHIVES_URL + getUserArg() + KGS_URL_ARG_SEP + getYearArg() + KGS_URL_ARG_SEP + getMonthArg();
  }

  public String getUserArg() {
    return KGS_URL_ARG_USER + getUser();
  }

  public String getYearArg() {
    return KGS_URL_ARG_YEAR + getYear();
  }

  public String getMonthArg() {
    return KGS_URL_ARG_MONTH + getMonth();
  }

  public String getUser() {
    return user;
  }

  public void setUser(String user) {
    this.user = user;
  }

  public String getYear() {
    return year;
  }

  public void setYear(String year) {
    this.year = year;
  }

  public String getMonth() {
    return month;
  }

  public void setMonth(String month) {
    this.month = month;
  }

}
