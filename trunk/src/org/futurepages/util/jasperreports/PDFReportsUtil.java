package org.futurepages.util.jasperreports;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;

import org.futurepages.core.persistence.Dao;

/**
 *
 * @author angelo
 */
public class PDFReportsUtil {


	/**
	 * Para utilizar SQL
	 *
	 * @param fileName
	 * @param inputStream
	 * @param params
	 * @param conexao
	 * @param response
	 * @return
	 * @throws JRException
	 * @throws IOException
	 */
	public static OutputStream createPDF(String fileName, InputStream inputStream, Map<String, Object> params, Connection conexao, HttpServletResponse response) throws JRException, IOException {
		JasperPrint report = JasperFillManager.fillReport(inputStream,params,Dao.getInstance().session().connection());
		response.setContentType("application/pdf");
		response.setHeader("Content-Disposition","inline; filename="+fileName);
		OutputStream out = response.getOutputStream();				
        JRExporter exporter = new JRPdfExporter();
		exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, fileName);
        exporter.setParameter(JRExporterParameter.JASPER_PRINT, report);
        exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, out);
        exporter.exportReport();
		return out;
    }

	/**
	 * Para passar List de objetos.
	 *
	 * @param fileName
	 * @param inputStream
	 * @param params
	 * @param list
	 * @param response
	 * @return
	 * @throws JRException
	 * @throws IOException
	 */
	public static OutputStream createPDF(String fileName, InputStream inputStream, Map<String, Object> params, List list, HttpServletResponse response) throws JRException, IOException {
		JRDataSource dsCollection = new JRBeanCollectionDataSource(list);
		JasperPrint report = JasperFillManager.fillReport(inputStream,params,dsCollection);

		//inline serve para abrir no target. attachment serve para abir janela de download
		response.setHeader("Content-Disposition","inline; filename="+fileName);
		response.setContentType("application/pdf");

		OutputStream out = response.getOutputStream();				
        JRExporter exporter = new JRPdfExporter();
		exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, fileName);
        exporter.setParameter(JRExporterParameter.JASPER_PRINT, report);
        exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, out);
        exporter.exportReport();
		return out;
    }
	public static OutputStream createPDF(String fileName,	JasperPrint report, HttpServletResponse response) throws JRException, IOException {
		response.setHeader("Content-Disposition","inline; filename="+fileName);
		response.setContentType("application/pdf");
		OutputStream out = response.getOutputStream();				
        JRExporter exporter = new JRPdfExporter();
		exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, fileName);
        exporter.setParameter(JRExporterParameter.JASPER_PRINT, report);
        exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, out);
        exporter.exportReport();
		return out;
    }
	public static JasperPrint jasperPrint( InputStream inputStream, Map<String, Object> parameters, List lista) throws JRException {
		JRDataSource dsCollection = new JRBeanCollectionDataSource(lista);
		JasperPrint jp = JasperFillManager.fillReport(inputStream, parameters, dsCollection);
		return jp;
	}
}
