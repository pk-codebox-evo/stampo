/**
 * Copyright (C) 2015 digitalfondue (info@digitalfondue.ch)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ch.digitalfondue.stampo.command;

import ch.digitalfondue.stampo.ServeAndWatch;
import ch.digitalfondue.stampo.Stampo;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

@Parameters(separators = "=")
public class Serve extends Command {
  @Parameter(description = "port", names = "--port")
  int port = 8080;

  @Parameter(description = "hostname", names = "--hostname")
  String hostname = "localhost";

  @Parameter(description = "disable rebuild on change", names = "--disable-rebuild-on-change")
  boolean disableRebuildOnChange;
  
  @Parameter(description = "disable auto reload", names = "--disable-auto-reload")
  boolean disableAutoReload;

  @Override
  void runWithWorkingPath(String workingPath) {
    Runnable triggerBuild = getBuildRunnable(workingPath);
    triggerBuild.run();
    System.out.println("stampo serving at " + hostname + ":" + port);
    if (disableAutoReload) {
      System.out.println("auto-reload is disabled");
    }
    if (disableRebuildOnChange) {
      System.out.println("rebuild on change is disabled");
    }
    new ServeAndWatch(hostname, port, !disableRebuildOnChange, !disableAutoReload, new Stampo(workingPath).getConfiguration())
        .serve(triggerBuild);
  }
}