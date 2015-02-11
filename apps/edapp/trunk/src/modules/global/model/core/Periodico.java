package modules.global.model.core;

import java.io.Serializable;
import java.util.Calendar;

public interface Periodico extends Serializable{

	public Calendar getDataHoraInicio();
	public void setDataHoraInicio(Calendar dataInicio);
	
	public Calendar getDataHoraFim();
	public void setDataHoraFim(Calendar dataFim);
}
