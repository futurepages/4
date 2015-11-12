package org.futurepages.util.jasperreports;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by iileandro on 28/10/14.
 */
public abstract class AbstractReport<T>  implements Serializable {

	private HashMap<String,Object> params;

	private List<? extends AbstractReport> list;

	public abstract T getBean();

	public AbstractReport getReport(){
		return this;
	}

	protected abstract String getJasperPath();

	protected abstract String getName();

	public List<? extends AbstractReport> getList() {
		return list;
	}

	public void setList(List<? extends AbstractReport>  list) {
		this.list = list;
	}

	protected abstract void initParams();

	public HashMap<String, Object> getParams(){
		if(params==null){
			initParams();
		}
		return params;
	}

	public void setParams(HashMap<String,Object> params) {
		this.params = params;
	}


	/**
	 *
	 * @param response
	 * @throws IOException
	 * @throws JRException
	 */
	public void createPDF(HttpServletResponse response) throws IOException, JRException {

		InputStream inputStream = AbstractReport.class.getResourceAsStream(getJasperPath());
		OutputStream out = null;

		try {
			out = PDFReportsUtil.createPDF(getName(), inputStream, getParams(), this.theList(), response);
			inputStream.close();
		}
		finally {
			if (out != null){
				out.close();
			}
		}

	}

	/**
	 * Imprime em aplicação stand-alone.
	 *
	 * @throws IOException
	 * @throws JRException
	 */
	public void jasperPrint() throws IOException, JRException {

		InputStream inputStream = getClass().getResourceAsStream(getJasperPath());

		OutputStream out = null;

		try {
			JasperPrint jp = PDFReportsUtil.jasperPrint(inputStream, getParams(), theList());
			inputStream.close();

			JasperViewer viewer = new JasperViewer(jp, true);
			viewer.show();

		} finally {
			if (out != null) {
				out.close();
			}
		}
	}



	protected List theList(){
		if(this.getList()==null){
			List theListOfOne = (new ArrayList<AbstractReport>());
			theListOfOne.add(this);
			return theListOfOne;
		}else{
			return list;
		}
	}


}
