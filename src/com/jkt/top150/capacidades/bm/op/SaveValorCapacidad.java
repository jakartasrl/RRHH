package com.jkt.top150.capacidades.bm.op;

import java.util.Iterator;

import com.jkt.framework.persistence.Transaccion;
import com.jkt.framework.request.Operation;
import com.jkt.framework.util.ExceptionDelete;
import com.jkt.framework.util.MapDS;
import com.jkt.framework.util.Registro;
import com.jkt.framework.util.Tabla;
import com.jkt.top150.capacidades.bm.ValorCapacidad;

public class SaveValorCapacidad extends Operation {

	public Integer execute(MapDS aParams) throws Exception {
		Transaccion tran = new Transaccion(this.getConnection());

		Iterator it = ((Tabla) aParams.get("ValorCapacidad")).getRegitros().iterator();
		int orden   = 1;
		while(it.hasNext()){
			Registro next = (Registro) it.next();

			ValorCapacidad valor = (ValorCapacidad) next.getObject(sesion, "oid", ValorCapacidad.class);
			try{
				if(!next.containsKey("activo") || !next.getBoolean("activo").booleanValue())
					this.tratarBorrado(false, valor);
				else {
					if(next.getBoolean("activo").booleanValue() && !valor.isActivo())
						valor.setForUpdate();
				}

				valor.setCodigo(next.getString("codigo"));
				valor.setDescripcion(next.getString("descripcion"));
				valor.setDescExtendida(next.getString("desc_ext"));
				valor.setValorNumerico(next.getDouble("valorNumerico").doubleValue());
				valor.setValoracionGlobal(next.containsKey("valoracion_global") && next.getBoolean("valoracion_global").booleanValue());
				valor.setOrden(orden++);
			}
			catch(ExceptionDelete e){}

			tran.addObject(valor);
		}

		tran.save();

		return new Integer(0);
	}
}