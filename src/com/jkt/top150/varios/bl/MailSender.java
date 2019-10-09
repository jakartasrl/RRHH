package com.jkt.top150.varios.bl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.ResourceBundle;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.jkt.framework.da.IObjectServer;
import com.jkt.framework.util.ListObserver;
import com.jkt.top150.legajos.bm.Legajo;
import com.jkt.top150.objetivos.bm.LegajoEjer;

public class MailSender {

	public void enviarMail(int etapa, LegajoEjer evaluado, boolean mismoUsuario, boolean planeamiento) {
		try{
			final String BR = "<br>";
			ResourceBundle bundle = ResourceBundle.getBundle("com.jkt.top150.varios.bl.mail"); 
			Message msg           = this.crearMail(bundle);

			StringBuffer sb = new StringBuffer("Estimado: ");

			if(mismoUsuario)
				sb.append(evaluado.getEvaluador().getLegajo().getApNom());

			sb.append(BR).
			append("Le informamos que en la etapa " + this.getEtapa(etapa) + " la evaluación de '" + evaluado.getLegajo().getApNom() + "' ").
			append("se encuentra en el estado: ");

			String estado = "";
			if(mismoUsuario)
				estado = "<b>FIN EVALUADO</b>";
			else{
				if(!planeamiento)
					  estado = "<b>FIN EVALUADOR</b>";
				else estado = "<b>FIN PLANEAMIENTO</b>";
			}

			sb.append(estado + BR + BR);

			sb.append("Para ingresar a la evaluación acceda a través del siguiente link: " + bundle.getString("LINK") + BR).
			append("Muchas gracias - Desarrollo Directivo");

			if(mismoUsuario){
				msg.setFrom(new InternetAddress(evaluado.getLegajo().getMail())); //FROM
				this.addTo(msg, evaluado.getEvaluador().getLegajo().getMail());
			}
			else{
				IObjectServer server   = evaluado.getSesion().getObjectServer(Legajo.class);
				List listaPlaneamiento = (List) server.getObjects(Legajo.SELECT_PLANEAMIENTO, null, new ListObserver());

				if(!planeamiento){
					msg.setFrom(new InternetAddress(evaluado.getEvaluador().getLegajo().getMail())); //FROM EVALUADOR					

					Iterator it = listaPlaneamiento.iterator();
					while(it.hasNext()){
						Legajo leg = (Legajo) it.next();
						this.addTo(msg, leg.getMail());
					}
				}
				else {
					List adrs = new ArrayList();

					Iterator it = listaPlaneamiento.iterator();
					while(it.hasNext()){
						Legajo leg = (Legajo) it.next();
						InternetAddress add = this.newAddress(leg.getMail());
						if(add == null)
							continue;
						
						adrs.add(add);
					}
					
					InternetAddress[] ia = new InternetAddress[adrs.size()];
					for(int i = 0; i< adrs.size(); i++)
						ia[i] = (InternetAddress) adrs.get(i);

					msg.addFrom(ia); //FROM
					
					this.addTo(msg, evaluado.getEvaluador().getLegajo().getMail());
				}
			}

			msg.setContent(sb.toString() , "text/html");

			Transport.send(msg);
		}
		catch(Exception e){
			System.out.println(e.toString());
		}
	}

	private String getEtapa(int etapa){
		switch (etapa) {
		case EstadosHandler.CARGAOBJETIVOS:
			return "Carga Objetivos";
		case EstadosHandler.CARGAEVALUACIONES:
			return "Carga de Evaluaciones";
		case EstadosHandler.AVANCEOBJETIVOS:
			return "Carga de cumplimientos";
		case EstadosHandler.VERRESUMENGRAFICO:
			return "Resumen";
		}

		return "";
	}
	
	private void addTo(Message aMsg, String aAdr) throws MessagingException{
		InternetAddress add = this.newAddress(aAdr);
		if(add == null)
			return;
		
		aMsg.addRecipient(Message.RecipientType.TO, add);
	}
	
	private InternetAddress newAddress(String aAdr) {
		try{
			return new InternetAddress(aAdr);
		}
		catch(Exception e){
			return null;
		}
	}

	private Message crearMail(ResourceBundle bundle) throws Exception{
		boolean debug = false;

		try{
			debug = Boolean.valueOf(bundle.getString("debug")).booleanValue();
		}
		catch(Exception e){}


		//Set the host smtp address
		Properties props = new Properties();
		props.put("mail.smtp.host", bundle.getString("MAIL_SERVER"));

		// create some properties and get the default Session
		Session session = Session.getDefaultInstance(props, null);
		session.setDebug(debug);

		Message msg = new MimeMessage(session);
		msg.setSubject(bundle.getString("TITULO_MAIL"));

		return msg;
	}
}