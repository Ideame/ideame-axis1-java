/*
 * Copyright 2001-2004 The Apache Software Foundation.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.axis.impl.registry;

import org.apache.axis.engine.Handler;
import org.apache.axis.registry.Flow;

import java.util.Vector;

/**
 * @author hemapani@opensource.lk
 */
public class FlowImpl implements Flow {
   protected Vector list = null; 
   public FlowImpl(){
        list = new Vector();
   }
   public void addHandler(Handler handler) {
       list.add(handler);
   }
   public Handler getHandler(int index) {
       return (Handler)list.get(index);
   }
   public int getHandlerCount() {
       return list.size();
   }
}