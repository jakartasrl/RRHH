package com.jkt.top150.objetivos.bm.op;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Calendar;

import com.jkt.framework.da.IObjectServer;
import com.jkt.framework.persistence.DBPool;
import com.jkt.framework.request.Operation;
import com.jkt.framework.util.ExceptionDS;
import com.jkt.framework.util.MapDS;
import com.jkt.top150.capacidades.bm.EvalCapacidadGlobal;
import com.jkt.top150.capacidades.bm.EvalResumenGlobal;
import com.jkt.top150.capacidades.bm.ValorResumen;
import com.jkt.top150.objetivos.bm.Cumplimiento;
import com.jkt.top150.objetivos.bm.CumplimientoGlobal;
import com.jkt.top150.objetivos.bm.Etapa;
import com.jkt.top150.objetivos.bm.LegajoEjer;

public class ComparacionResumen extends Operation {
	
	private static final String NO_ASIGNADO = "<span style='color: blue'>NO ASIGNADO</span>";

	public Integer execute(MapDS aParams) throws Exception {
		String campos = "DECODE(t.est_evaluado_cumpl,0,'NO','SI') evaluado, DECODE(t.est_evaluador_cumpl,0,'NO','SI') evaluador, DECODE(t.est_planeamiento_cumpl,0,'NO','SI') planeamiento,";

		String s = sesion.getSchema();

		int ano = Calendar.getInstance().get(Calendar.YEAR);

		try{
			ano = aParams.getSimpleInteger("ano");
		}
		catch(Exception e){}
		
		Etapa etapa = Etapa.getEtapaActual(sesion);

		writer.addTabla("TDatos");

		StringBuffer sb = new StringBuffer().
		append("SELECT e.oid_leg_eje, l.legajo, l.apellido_pat || l.apellido_mat apellidos, l.nombres, ").
		append(        campos).
		append(        "l2.apellido_pat || l2.apellido_mat apellido_evaluador, ").
		append(        "l2.nombres nombre_evaluador ").
		append("  FROM " + s + "legajo l,").
		append(            s + "jkUsuario  u,").
		append(            s + "legajoejer e,").
		append(            s + "legajoejeretapa t,").
		append(            s + "legajo l2,").
		append(            s + "legajoejer e2, ").
		append(            s + "ejercicio eje, ").
		append(            s + "etapa eta, ").
		append(            s + "ejercicioetapas ejeta ").
		append(" WHERE e.oid_leg_eje   = t.oid_leg_eje").   
		append("   AND t.oid_eje_etapa = ejeta.oid_eje_etapa ").
		append("   AND ejeta.oid_etapa = " + etapa.getOID()).
		append("   AND eje.anio        = " + ano).
		append("   AND e.oid_eje       = eje.oid_eje").
		append("   AND e.oid_leg       = l.oid_leg").
		append(  " AND l.oid_usu_legajo= u.oid_usuario").
		append(  " AND u.activo        = 1").
		append("   AND e.es_evaluado  !=0 ").
		append("   AND e.oid_evaluador = e2.oid_leg_eje").
		append("   AND e2.oid_leg = l2.oid_leg ");

		if(aParams.containsKey("nombres") && aParams.getString("nombres").trim().length() > 0)
			sb.append(" AND upper(l.nombres) like " + this.getClaveLike(aParams, "nombres"));

		if(aParams.containsKey("apellido")&& aParams.getString("apellido").trim().length() > 0)
			sb.append(" AND upper(l.apellido_pat) like " + this.getClaveLike(aParams, "apellido"));

		if(aParams.containsKey("legajo")  && aParams.getString("legajo").trim().length() > 0)
			sb.append(" AND upper(l.legajo) like " + this.getClaveLike(aParams, "legajo"));

		if(aParams.containsKey("oid_evaluador") && aParams.getInteger("oid_evaluador").intValue() > 0)
			sb.append(" AND e.oid_evaluador = " + aParams.getInteger("oid_evaluador").intValue());

		sb.append(" UNION ").
		append("SELECT e.oid_leg_eje, l.legajo, l.apellido_pat || l.apellido_mat apellidos, l.nombres, ").
		append(        campos).
		append(        "'SIN EVALUADOR' apellido_evaluador, ").
		append(        "'SIN EVALUADOR' nombre_evaluador ").
		append("  FROM " + s + "legajo l,").
		append(            s + "jkUsuario  u,").
		append(            s + "legajoejer e,").
		append(            s + "legajoejeretapa t,").
		append(            s + "ejercicio eje, ").
		append(            s + "etapa eta, ").
		append(            s + "ejercicioetapas ejeta ").
		append(" WHERE e.oid_leg_eje   = t.oid_leg_eje").   
		append("   AND e.oid_eje       = eje.oid_eje").
		append("   AND t.oid_eje_etapa = ejeta.oid_eje_etapa ").
		append("   AND ejeta.oid_etapa = " +  etapa.getOID()).
		append("   AND eje.anio        = " + ano).
		append("   AND e.oid_leg       = l.oid_leg").
		append(  " AND l.oid_usu_legajo= u.oid_usuario").
		append(  " AND u.activo        = 1").
		append("   AND e.es_evaluado  !=0"). 
		append("   AND e.OID_EVALUADOR IS NULL ");

		if(aParams.containsKey("nombres") && aParams.getString("nombres").trim().length() > 0)
			sb.append(" AND upper(l.nombres) like " + this.getClaveLike(aParams, "nombres"));

		if(aParams.containsKey("apellido")&& aParams.getString("apellido").trim().length() > 0)
			sb.append(" AND upper(l.apellido_pat) like " + this.getClaveLike(aParams, "apellido"));

		if(aParams.containsKey("legajo")  && aParams.getString("legajo").trim().length() > 0)
			sb.append(" AND upper(l.legajo) like " + this.getClaveLike(aParams, "legajo"));

		if(aParams.containsKey("oid_evaluador") && aParams.getInteger("oid_evaluador").intValue() > 0)
			sb.append(" AND e.oid_evaluador = " + aParams.getInteger("oid_evaluador").intValue());

		sb.append(" ORDER BY 2,1");

		PreparedStatement ps = new DBPool().getPreparedStatement(sesion.getConnection(), sb.toString());
		
		IObjectServer server = sesion.getObjectServer(LegajoEjer.class);

		ResultSet rs = ps.executeQuery();

		while(rs.next()){
			LegajoEjer lEjer = (LegajoEjer) server.getObjectForView(new Integer(rs.getInt("oid_leg_eje")));
			
			writer.addFila();

			writer.addColumna("LEGAJO",       rs.getString("legajo"));
			writer.addColumna("NOMBRES",      rs.getString("apellidos") + ", " + rs.getString("nombres"));
			writer.addColumna("NOMBRES_EVAL", rs.getString("apellido_evaluador") + ", " + rs.getString("nombre_evaluador"));
			
			writer.addColumna("cumplimiento", Cumplimiento.getValorCumpliento(lEjer, etapa));
			
			CumplimientoGlobal cump = CumplimientoGlobal.getCumplimientoGlobal(lEjer, sesion);
			if(cump.getValorCumplimiento() != null)
				  writer.addColumna("cump_global", cump.getValorCumplimiento().getDescripcion());
			else writer.addColumna("cump_global", NO_ASIGNADO);
			
			EvalCapacidadGlobal eval = EvalCapacidadGlobal.getCapacidadGlobal(lEjer, sesion);
			if(eval.getValor() != null)
				  writer.addColumna("capac_global", eval.getValor().getDescripcion());
			else writer.addColumna("capac_global", NO_ASIGNADO);
			
			EvalResumenGlobal evalR = EvalResumenGlobal.getResumenGlobal(lEjer);
			if(evalR.getValor() != null)
				  writer.addColumna("res_global", evalR.getValor().getDescripcion());
			else writer.addColumna("res_global", NO_ASIGNADO);
			
			ValorResumen res = ValorResumen.getResumenGlobalEmpresa(lEjer); 
			if(res != null)
				  writer.addColumna("res_global_emp", res.getDescripcion());
			else writer.addColumna("res_global_emp", NO_ASIGNADO);
			
			boolean evaluado    = rs.getString("evaluado").equalsIgnoreCase("SI");
			boolean evaluador   = rs.getString("evaluador").equalsIgnoreCase("SI");
			boolean planeamiento= rs.getString("planeamiento").equalsIgnoreCase("SI");
			
			String estado = LegajoEjer.ACTIVO;
			
			if(evaluado) 
				estado = LegajoEjer.FIN_EVALUADO;
			
			if(evaluador)
				estado = LegajoEjer.FIN_EVALUADOR;
			
			if(planeamiento)
				estado = LegajoEjer.FIN_PLANEAMIENTO;
			
			if(evaluado && evaluador && planeamiento)
				estado = LegajoEjer.CERRADO;
			
			writer.addColumna("estado", estado);
			
			writer.addColumna("apellido_evaluador", rs.getString("apellido_evaluador"));
			writer.addColumna("nombre_evaluador", rs.getString("nombre_evaluador"));
		}

		ps.close();

		return OK;
	}

	private String getClaveLike(MapDS aParams, String aKey) throws ExceptionDS{
		String valor = aParams.getString(aKey).trim();
		return "'%" + valor.toUpperCase() + "%'";
	}
}