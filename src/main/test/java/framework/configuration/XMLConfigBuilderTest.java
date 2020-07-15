package framework.configuration;

/*
 * Copyright (C) 2020 tiansheng All rights reserved.
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

/*
 * Creates on 2020/6/2.
 */

import org.jiakesimk.minipika.components.configuration.XMLConfigBuilder;
import org.jiakesimk.minipika.framework.strategy.FindStrategy;
import org.xml.sax.InputSource;

/**
 * @author tiansheng
 */
public class XMLConfigBuilderTest {

  public static void main(String[] args) {
    InputSource istream = FindStrategy.getConfigInputStream();
    XMLConfigBuilder configBuilder = new XMLConfigBuilder("minipika2.xml");
  }

}
