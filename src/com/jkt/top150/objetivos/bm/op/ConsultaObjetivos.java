package com.jkt.top150.objetivos.bm.op;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.Calendar;

import com.jkt.framework.persistence.DBPool;
import com.jkt.framework.util.ExceptionDS;
import com.jkt.framework.util.MapDS;

public class ConsultaObjetivos extends CargaObjetivos{

	public Integer execute(MapDS aParams) throws Exception {
		String s = sesion.getSchema();

		int ano = Calendar.getInstance().get(Calendar.YEAR);

		try{
			ano = aParams.getSimpleInteger("ano");
		}
		catch(Exception e){}

		writer.addTabla("TDatos");

		StringBuffer sb = new StringBuffer().
		append("SELECT a.legajo, trim(a.apellido_pat) || trim(a.apellido_mat) || ',' || trim(a.nombres) nombres, "). 
		append("       NVL(c.descripcion, 'sin descripcion') descripcion,").
		append("			NVL(c.cuantificacion, '0') cuantificacion, ").
		append("			NVL(c.ponderacion, '0') ponderacion, ").
		append("			NVL(c.fuente, '  ') fuente,").
		append("			NVL(c.aspectos_cual, '  '), aspectos_cual,").
		append("			NVL((SELECT cuantificacion from " + s + "cuantificacion x WHERE oid_cuan = (SELECT MAX(OID_CUAN) FROM " + s + "cuantificacion aux WHERE orden = 1 and aux.oid_obj = c.oid_obj)),'0') cuantificacion_1,").
		append("			NVL((SELECT cuantificacion from " + s + "cuantificacion x WHERE oid_cuan = (SELECT MAX(OID_CUAN) FROM " + s + "cuantificacion aux WHERE orden = 2 and aux.oid_obj = c.oid_obj)),'0') cuantificacion_2,").
		append("			NVL((SELECT cuantificacion from " + s + "cuantificacion x WHERE oid_cuan = (SELECT MAX(OID_CUAN) FROM " + s + "cuantificacion aux WHERE orden = 3 and aux.oid_obj = c.oid_obj)),'0') cuantificacion_3,").
		append("			NVL((SELECT cuantificacion from " + s + "cuantificacion x WHERE oid_cuan = (SELECT MAX(OID_CUAN) FROM " + s + "cuantificacion aux WHERE orden = 4 and aux.oid_obj = c.oid_obj)),'0') cuantificacion_4,").
		append("			NVL((SELECT cuantificacion from " + s + "cuantificacion x WHERE oid_cuan = (SELECT MAX(OID_CUAN) FROM " + s + "cuantificacion aux WHERE orden = 5 and aux.oid_obj = c.oid_obj)),'0') cuantificacion_5 ").
		append("  FROM " + s + "legajo a, " + s + "legajoejer b, " + s + "objetivo c, " + s + "ejercicio d, " + s + "jkUsuario e ").
		append(" WHERE a.OID_LEG = b.OID_LEG").
		append("   AND b.oid_eje = d.oid_eje ").
		append("   AND d.anio    = " + ano).
		append("   AND b.oid_leg_eje    = c.oid_leg_eje ").
		append("   AND a.oid_usu_legajo = e.oid_usuario ").
		append("   AND e.activo         = 1 ");

		if(aParams.containsKey("nombres") && aParams.getString("nombres").trim().length() > 0)
			sb.append(" AND upper(a.nombres) like " + this.getClaveLike(aParams, "nombres"));

		if(aParams.containsKey("apellido")&& aParams.getString("apellido").trim().length() > 0)
			sb.append(" AND upper(a.apellido_pat) like " + this.getClaveLike(aParams, "apellido"));

		if(aParams.containsKey("legajo")  && aParams.getString("legajo").trim().length() > 0)
			sb.append(" AND upper(a.legajo) like " + this.getClaveLike(aParams, "legajo"));

		if(aParams.containsKey("oid_evaluador") && aParams.getInteger("oid_evaluador").intValue() > 0)
			sb.append(" AND b.oid_evaluador = " + aParams.getInteger("oid_evaluador").intValue());

		sb.append(" ORDER BY a.apellido_pat, a.nombres, c.nro_obj");

		PreparedStatement ps = new DBPool().getPreparedStatement(sesion.getConnection(), sb.toString());

		ResultSet rs = ps.executeQuery();
		ResultSetMetaData meta = rs.getMetaData();

		while(rs.next()){
			writer.addFila();

			for(int i = 1; i<=meta.getColumnCount(); i++ )
				writer.addColumna(meta.getColumnName(i).toUpperCase(), rs.getObject(i));
		}

		ps.close();

		return OK;
	}

	private String getClaveLike(MapDS aParams, String aKey) throws ExceptionDS{
		String valor = aParams.getString(aKey).trim();
		return "'%" + valor.toUpperCase() + "%'";
	}
}