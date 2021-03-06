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
package com.gote;

import org.joda.time.DateTime;

/**
 * 
 * Singleton with informations about application
 * 
 * @author SGirousse
 */
public class AppUtil {

  /** Name */
  public static final String APP_NAME = "Go Online Tournament Easy";

  /** Short name */
  public static final String APP_SHORTNAME = "GOTE";

  /** Version */
  public static final String APP_VERSION = "0.5.4";

  /** Authors */
  public static final String APP_AUTHOR = "Simeon GIROUSSE - simeon[dot]girousse[at]gmail[dot]com";

  /** Logo */
  public static final String APP_ICON_PATH = "resources/images/GOTE-icone.jpg";

  /** Init and reset date */
  public static final DateTime APP_INIT_DATE = new DateTime(1999, 1, 1, 0, 0);

  /** Path tournaments */
  public static final String PATH_TO_TOURNAMENTS = "tournaments/";

  /** Path save */
  public static final String PATH_TO_SAVE = "saving/";

  /** Path exports */
  public static final String PATH_TO_EXPORTS = "exports/";

  /** Path SGF */
  public static final String PATH_TO_SGFS = "sgfs/";

  /**
   * Private constructor
   */
  private AppUtil() {
  }

  /**
   * Singleton access point
   * 
   * @return AppUtil
   */
  public static AppUtil getInstance() {
    return AppUtilHolder.instance;
  }

  /**
   * Build the window title with the app shortname and version + ui name and possibly some extra
   * text
   * 
   * @param pWindowName String
   * @return String
   */
  public static final String buildWindowTitle(String pWindowName) {
    return APP_SHORTNAME + " v" + APP_VERSION + " - " + pWindowName;
  }

  /**
   * Build the window title with the app shortname and version + ui name and possibly some extra
   * text
   * 
   * @param pWindowName String
   * @param pWindowExtraText String
   * @return String
   */
  public static final String buildWindowTitle(String pWindowName, String pWindowExtraText) {
    return APP_SHORTNAME + " v" + APP_VERSION + " - " + pWindowName + " : " + pWindowExtraText;
  }

  /**
   * Holder for our singleton
   */
  private static class AppUtilHolder {
    /** Our App unique instance */
    private final static AppUtil instance = new AppUtil();
  }

}