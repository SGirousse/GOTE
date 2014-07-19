/**
 * Copyright 2014 Siméon GIROUSSE
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
package com.gote.util;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 
 * Static list of servers
 * 
 * @author SGirousse
 */
public final class Servers {

  /** Nothing selected */
  public static final String NO_SERVER = "NO_SERVER";

  /** KGS server */
  public static final String KGS = "KGS";

  /** OGS server */
  public static final String OGS = "OGS";

  /** List of servers */
  public static List<String> SERVERS = Collections.unmodifiableList(Arrays.asList(new String[] { KGS, OGS }));

}
