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
package ch.digitalfondue.stampo.renderer;

import java.nio.file.Path;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import ch.digitalfondue.stampo.StampoGlobalConfiguration;
import ch.digitalfondue.stampo.processor.FileResourceParameters;
import ch.digitalfondue.stampo.processor.FileResourceProcessorOutput;
import ch.digitalfondue.stampo.processor.LayoutParameters;
import ch.digitalfondue.stampo.processor.LayoutProcessorOutput;
import ch.digitalfondue.stampo.resource.Directory;
import ch.digitalfondue.stampo.resource.FileResource;

public interface Renderer {

  void registerLayoutRenderer(Directory root, StampoGlobalConfiguration configuration,
      Map<String, Function<LayoutParameters, LayoutProcessorOutput>> extensionProcessor);

  void registerResourceRenderer(
      Directory root,
      StampoGlobalConfiguration configuration,
      Map<String, Function<FileResourceParameters, FileResourceProcessorOutput>> extensionProcessor);
  
  
  List<String> resourceExtensions();
  Map<String, String> extensionTransformMapping();
  
  static FileResource getContentFileResource(Path template, Path contentDir, Directory root) {
    if (!template.startsWith(contentDir)) {
      throw new IllegalArgumentException();
    }
    Path relativeContent = contentDir.relativize(template);
    Directory base = root;
    for (Iterator<Path> d = relativeContent.iterator(); d.hasNext();) {
      Path p = d.next();
      if (d.hasNext()) {
        base = base.getDirectories().get(p.toString());
      } else {
        return base.getFiles().get(p.toString());
      }
    }
    throw new IllegalArgumentException("not found");
  }
}
