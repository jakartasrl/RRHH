package com.jkt.top150.varios.bl;

import java.util.Comparator;

import com.jkt.top150.objetivos.bm.LegajoEjer;

public class ListOrderer implements Comparator{

	public int compare(Object arg0, Object arg1) {
		try{
			LegajoEjer leg0 = (LegajoEjer) arg0;
			LegajoEjer leg1 = (LegajoEjer) arg1;

			return leg0.getLegajo().getApNom().compareTo(leg1.getLegajo().getApNom());
		}
		catch(Exception e){}

		return 0;
	}
}