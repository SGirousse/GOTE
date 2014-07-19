/**
 * Copyright 2014 Sim�on GIROUSSE
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
package com.gote.pojo;

/**
 * 
 * Representation of a player
 * 
 * @author SGirousse
 */
public class Player {
  /** Player pseudo */
  private String pseudo;

  /** Player firstname */
  private String firstname;

  /** Player rank */
  private String rank;

  /**
   * Pseudo getter
   * 
   * @return String
   */
  public String getPseudo() {
    return pseudo;
  }

  /**
   * Pseudo setter
   * 
   * @param pPseudo String
   */
  public void setPseudo(String pPseudo) {
    this.pseudo = pPseudo;
  }

  public String getFirstname() {
    return firstname;
  }

  public void setFirstname(String firstname) {
    this.firstname = firstname;
  }

  /**
   * Rank getter
   * 
   * @return String "1d", "20k", etc.
   */
  public String getRank() {
    return rank;
  }

  /**
   * Rank setter
   * 
   * @param pRank String "1d", "20k", etc.
   */
  public void setRank(String pRank) {
    this.rank = pRank;
  }

}
