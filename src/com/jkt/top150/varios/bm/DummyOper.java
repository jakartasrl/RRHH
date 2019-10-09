package com.jkt.top150.varios.bm;

import com.jkt.framework.request.Operation;
import com.jkt.framework.util.MapDS;

public class DummyOper extends Operation {
   
   public Integer execute(MapDS aParams) throws Exception {
      return new Integer(0);
   }
}
