package com.jkt.top150.varios.bm.op;

import com.jkt.framework.da.IObjectServer;
import com.jkt.framework.persistence.IDB;
import com.jkt.framework.request.Operation;
import com.jkt.framework.util.ExceptionDS;
import com.jkt.framework.util.ExceptionValidacion;
import com.jkt.framework.util.IObserver;
import com.jkt.framework.util.MapDS;
import com.jkt.framework.util.XMLTableMaker;
import com.jkt.top150.capacidades.bm.EvalCapacidadGlobal;
import com.jkt.top150.capacidades.bm.EvalResumenGlobal;
import com.jkt.top150.capacidades.bm.ValorCapacidad;
import com.jkt.top150.capacidades.bm.ValorResumen;
import com.jkt.top150.legajos.bm.Legajo;
import com.jkt.top150.objetivos.bl.ValCumpGlobal;
import com.jkt.top150.objetivos.bm.Cumplimiento;
import com.jkt.top150.objetivos.bm.CumplimientoGlobal;
import com.jkt.top150.objetivos.bm.Etapa;
import com.jkt.top150.seguridad.bm.UsuarioRRHH;
import com.jkt.top150.varios.bl.EstadosHandler;
import com.jkt.top150.varios.bl.HeaderWriter;
import com.jkt.top150.varios.bl.LegajoGetter;
import com.jkt.top150.varios.bl.LegajoVarios;
import com.jkt.top150.varios.bm.ConcVarios;

public class ArmarGrafico extends Operation implements IObserver{

	private XMLTableMaker maker;
	private Legajo legajo;

	public Integer execute(MapDS aParams) throws Exception{
		LegajoGetter getter = new LegajoGetter(sesion);
		legajo = getter.execute(aParams);

		this.validar();
		
		this.tomarValoresEvaluacion();

		writer.addTabla("Conceptos");
		IObjectServer server = sesion.getObjectServer(ConcVarios.class);
		server.getObjects(IDB.SELECT_ACTIVOS, null, this);

		writer.addTabla("ValoresResumen");
		server = sesion.getObjectServer(ValorResumen.class);
		server.getObjects(IDB.SELECT_ACTIVOS, null, this);
		
		if(legajo.esMismoLegajoSession())
			writer.addTabla("evaluado");

		this.writeHeader();

		return new Integer(0);
	}
	
	private void validar() throws ExceptionDS{
		UsuarioRRHH user = (UsuarioRRHH) sesion.getLogin().getUsuario(); 
		if(user.isPlanificacion() && user.getLegajo().getOID() != legajo.getOID())
			throw new ExceptionValidacion("Planeamiento no puede acceder a las competencias ni al resumen de ningun evaluado");		
	}

	private void tomarValoresEvaluacion() throws ExceptionDS{
		writer.addTabla("TResumen");
		writer.addFila();

		ValorCapacidad valorC = EvalCapacidadGlobal.getCapacidadGlobal(legajo.getLegajoEjer(), sesion).getValor();
		if(valorC != null)
			  writer.addColumna("valor_cap_glob", valorC.getDescripcion() + "- (" + valorC.getCodigo() + ")");
		else writer.addColumna("valor_cap_glob", "");
		
		ValCumpGlobal valor = CumplimientoGlobal.getCumplimientoGlobal(legajo.getLegajoEjer(), sesion).getValorCumplimiento();
		if(valor != null)
			  writer.addColumna("valor_cump_glob", valor.getDescripcion() + "- (" + valor.getCodigo() + ")");
		else writer.addColumna("valor_cump_glob", "");

		writer.addColumna("valor_cumplimiento", Cumplimiento.getValorCumpliento(legajo.getLegajoEjer(), Etapa.getEtapaActual(sesion)));
		
		ValorResumen vr = ValorResumen.getResumenGlobalEmpresa(legajo.getLegajoEjer());
		if(vr != null)
			  writer.addColumna("valor_mapeo", vr.getDescripcion());
		else writer.addColumna("valor_mapeo", "No Asignado");
	}

	private void writeHeader() throws ExceptionDS{
		HeaderWriter hw = new HeaderWriter(sesion);
		hw.write(writer, legajo, EstadosHandler.VERRESUMENGRAFICO);
	}

	public Object getResult(){
		return null;
	}

	public void notify(Object aObj) throws ExceptionDS{
		if(aObj instanceof ConcVarios){
			ConcVarios conc = (ConcVarios) aObj; 
			writer.addFila();
			writer.addColumna("oid_conc",       conc.getOID());
			writer.addColumna("codigo",         conc.getCodigo());
			writer.addColumna("descripcion",    conc.getDescripcion());
			writer.addColumna("tipo_evaluador", conc.isTipoEvaluador());

			maker = new XMLTableMaker("Comentarios_" + conc.getOID(), this);

			conc.getComentarios(this, legajo);

			return;
		}
		if(aObj instanceof LegajoVarios){
			LegajoVarios varios = (LegajoVarios) aObj;

			maker.addFila();
			maker.addColumna("oid_varios", varios.getOID());
			maker.addColumna("comentario", varios.getTexto());

			return;
		}

		if(aObj instanceof ValorResumen){
			this.writeValorResumen((ValorResumen) aObj);
			return;
		}
	}

	private void writeValorResumen(ValorResumen valor) throws ExceptionDS{
		writer.addFila();
		writer.addColumna("valor_numerico",   valor.getValorNumerico());
		writer.addColumna("codigo",           valor.getCodigo());
		writer.addColumna("descripcion",      valor.getDescripcion());
		writer.addColumna("desc_ext",         valor.getDescExtendida());
		writer.addColumna("oid_valor",        valor.getOID());
		
		int oid = 0;
		EvalResumenGlobal eval = EvalResumenGlobal.getResumenGlobal(legajo.getLegajoEjer());
		if(eval != null && eval.getValor() != null)
			oid = eval.getValor().getOID();
			
		writer.addColumna("oid_valor_global", oid);
	}	
}