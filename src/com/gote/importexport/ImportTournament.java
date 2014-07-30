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
package com.gote.importexport;

import java.io.File;

import com.gote.pojo.Tournament;

/**
 * 
 * Abstract class for all ImportTournament behavior
 * 
 * @author sgirouss
 */
public abstract class ImportTournament {

  /**
   * Taking a configuration data source in entry, this build a new tournament.
   * 
   * @param pFile File used to create the tournament
   * @return A new <code>Tournament</code>
   */
  public abstract Tournament createTournamentFromConfig(File pFile);
}
