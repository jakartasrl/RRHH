package com.jkt.top150.objetivos.bm.op;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Calendar;

import com.jkt.framework.persistence.DBPool;
import com.jkt.framework.request.Operation;
import com.jkt.framework.util.ExceptionDS;
import com.jkt.framework.util.MapDS;

public class ConsultaAvanceObjetivos extends Operation {

	public Integer execute(MapDS aParams) throws Exception {
		int opcion = 1;
		String cond   = " AND carga_obj = 1";
		String campos = "DECODE(t.est_evaluado_c_obj,0,'NO','SI') evaluado, DECODE(t.est_evaluador_c_obj,0,'NO','SI') evaluador, DECODE(t.est_planeamiento_c_obj,0,'NO','SI') planeamiento,";

		try{
			opcion = aParams.getSimpleInteger("opcion");
		}
		catch(Exception e){}

		switch(opcion){
		case 2:{
			campos = "DECODE(t.est_evaluado_cumpl,0,'NO','SI') evaluado, DECODE(t.est_evaluador_cumpl,0,'NO','SI') evaluador, DECODE(t.est_planeamiento_cumpl,0,'NO','SI') planeamiento,";
			cond   = " AND carga_cumpl = 1 AND carga_resumen = 0 ";
			break;
		}
		case 3:{
			campos = "DECODE(t.est_evaluado_capac,0,'NO','SI') evaluado, DECODE(t.est_evaluador_capac,0,'NO','SI') evaluador, DECODE(t.est_planeamiento_capac,0,'NO','SI') planeamiento,";
			cond = " AND carga_resumen = 1 ";
			break;
		}
		}

		String s = sesion.getSchema();

		int ano = Calendar.getInstance().get(Calendar.YEAR);

		try{
			ano = aParams.getSimpleInteger("ano");
		}
		catch(Exception e){}

		writer.addTabla("TDatos");

		StringBuffer sb = new StringBuffer().
		append("SELECT l.legajo, l.apellido_pat || l.apellido_mat apellidos, l.nombres, ").
		append(        campos).
		append(        "l2.legajo legajo_evaluador, ").
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
		append(  " AND t.oid_eje_etapa = ejeta.oid_eje_etapa ").
		append(  " AND eta.oid_etapa   = ejeta.oid_etapa ").
		append(  " AND eje.anio    = " + ano).
		append(    cond ).
		append(  " AND e.oid_eje       = eje.oid_eje").
		append(  " AND e.oid_leg       = l.oid_leg").
		append(  " AND l.oid_usu_legajo= u.oid_usuario").
		append(  " AND u.activo        = 1").
		append(  " AND e.es_evaluado  != 0 ").
		append(  " AND e.oid_evaluador = e2.oid_leg_eje").
		append(  " AND e2.oid_leg = l2.oid_leg ");

		if(aParams.containsKey("nombres") && aParams.getString("nombres").trim().length() > 0)
			sb.append(" AND upper(l.nombres) like " + this.getClaveLike(aParams, "nombres"));

		if(aParams.containsKey("apellido")&& aParams.getString("apellido").trim().length() > 0)
			sb.append(" AND upper(l.apellido_pat) like " + this.getClaveLike(aParams, "apellido"));

		if(aParams.containsKey("legajo")  && aParams.getString("legajo").trim().length() > 0)
			sb.append(" AND upper(l.legajo) like " + this.getClaveLike(aParams, "legajo"));

		if(aParams.containsKey("oid_evaluador") && aParams.getInteger("oid_evaluador").intValue() > 0)
			sb.append(" AND e.oid_evaluador = " + aParams.getInteger("oid_evaluador").intValue());

		sb.append(" UNION ").
		append("SELECT l.legajo, l.apellido_pat || l.apellido_mat apellidos, l.nombres, ").
		append(        campos).
		append(        "'0'             legajo_evaluador, ").
		append(        "'SIN EVALUADOR' apellido_evaluador, ").
		append(        "'SIN EVALUADOR' nombre_evaluador ").
		append("  FROM " + s + "legajo l,").
		append(            s + "jkUsuario  u,").
		append(            s + "legajoejer e,").
		append(            s + "legajoejeretapa t,").
		append(            s + "ejercicio eje, ").
		append(            s + "etapa eta, ").
		append(            s + "ejercicioetapas ejeta ").
		append(" WHERE e.oid_leg_eje = t.oid_leg_eje").   
		append(  " AND e.oid_eje = eje.oid_eje").
		append(  " AND t.oid_eje_etapa = ejeta.oid_eje_etapa ").
		append(  " AND eta.oid_etapa   = ejeta.oid_etapa ").
		append(  " AND eje.anio  = " + ano).
		append(    cond ).
		append(  " AND e.oid_leg       = l.oid_leg").
		append(  " AND l.oid_usu_legajo= u.oid_usuario").
		append(  " AND u.activo        = 1").
		append(  " AND e.es_evaluado  !=0"). 
		append(  " AND e.OID_EVALUADOR IS NULL ");

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

		ResultSet rs = ps.executeQuery();

		while(rs.next()){
			writer.addFila();

			writer.addColumna("LEGAJO",       rs.getString("legajo"));
			writer.addColumna("APELLIDOS",    rs.getString("apellidos"));
			writer.addColumna("NOMBRES",      rs.getString("nombres"));
			writer.addColumna("evaluado",     rs.getString("evaluado"));
			writer.addColumna("evaluador",    rs.getString("evaluador"));
			writer.addColumna("planeamiento", rs.getString("planeamiento"));

			writer.addColumna("legajo_evaluador", rs.getString("legajo_evaluador"));
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