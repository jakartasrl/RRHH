package com.jkt.top150.seguridad.bm.op;

import com.jkt.framework.request.Operation;
import com.jkt.framework.util.MapDS;

public class LogOut extends Operation {

   public Integer execute(MapDS aParams) throws Exception {
      request.getSession().invalidate();
      
      writer.addTabla("FinSession");
      
      return new Integer(0);
   }
}
