--En la version anterior el campo_cump_real se almacenaba a nivel objetivo-etapa
--Ahora lo ponemos a nivel objetivo, igualmente este script pasa los posibles valores asignados 
--al nivel correspondiente

alter table objetivo add CUMP_REAL VARCHAR2(255);

update objetivo set cump_real = (select distinct cump_real 
                                   from cumplimiento 
                                  where cumplimiento.oid_obj = objetivo.oid_obj);
                                  
alter table cumplimiento drop column cump_real;
