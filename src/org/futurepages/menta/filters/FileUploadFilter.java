package org.futurepages.menta.filters;

import org.apache.commons.fileupload.DiskFileUpload;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUpload;
import org.apache.commons.fileupload.FileUploadException;
import org.futurepages.core.exception.AppLogger;
import org.futurepages.menta.core.action.Action;
import org.futurepages.menta.core.context.Context;
import org.futurepages.menta.core.context.SessionContext;
import org.futurepages.menta.core.control.InvocationChain;
import org.futurepages.menta.core.filter.Filter;
import org.futurepages.menta.core.input.Input;
import org.futurepages.menta.exceptions.ServletUserException;

import javax.servlet.http.HttpServletRequest;
import java.util.Iterator;
import java.util.List;

/**
 * A filter for handling File Uploading and Multipart Content requests.<br/>
 * When the Filter detect that the filtered request is a Multipart Content
 * request, he handles the multipart content, separating which itens are form
 * itens and which itens are files to upload. The form itens are inject into the
 * input as String values and the files itens are injected as a
 * org.apache.commons.fileupload.FileItem object.<br/> This filter uses the
 * Jakarta Commons FileUpload library for handling the Multipart Content
 * request, so it is necessary to include its jar into the \WEB-INF\lib
 * directory of the web application.
 *
 *
 */
public class FileUploadFilter implements Filter {

	private static final DiskFileUpload DEFAULT_UPLOAD = new DiskFileUpload();

	private DiskFileUpload upload;

	public FileUploadFilter() {
		this.upload = DEFAULT_UPLOAD;
	}

	/**
	 * Creates a new FileUploadFilter and configures the FileUpload attributes.
	 *
	 * @param maxInMemorySize
	 *            the maxInMemorySize FileUpload attribute. See
	 *            org.apache.commons.fileupload.DiskFileUpload.setMaxInMemorySize
	 *            for details.
	 * @param maxInRequestSize
	 *            the sizeMax FileUpload attribute. See
	 *            org.apache.commons.fileupload.DiskFileUpload.setSizeMax for
	 *            details.
	 * @param tempDir
	 *            the repositoryPath FileUpload attribute. See
	 *            org.apache.commons.fileupload.DiskFileUpload.setRepositoryPath
	 *            for details.
	 */
	public FileUploadFilter(int maxInMemorySize, int maxInRequestSize,
			String tempDir) {
		upload = new DiskFileUpload();
		upload.setSizeThreshold(maxInMemorySize);
		upload.setSizeMax(maxInRequestSize);
		upload.setRepositoryPath(tempDir);

	}

	/**
	 * Because we are using the FileUpload from Jakarta Commons, and because it
	 * requires the HttpServletRequest, there is nothing we can do about it
	 * besides fetching the HttpServletRequest and giving it to the FileUpload
	 * class.
	 *
	 * @return The HttpServletRequest of the current request!
	 */
	protected HttpServletRequest getRequest(Action action) {

		Context session = action.getSession();

		if (!(session instanceof SessionContext))
			return null;

		return ((SessionContext) action.getSession()).getRequest();

	}

	@Override
	public String filter(InvocationChain chain) throws Exception {

		Action action = chain.getAction();

		Input input = action.getInput();

		HttpServletRequest req = getRequest(action);

		if (req == null) {

			throw new FilterException(
					"I was not possible to fetch HttpServletRequest inside FileUploadFilter!!!");
		}

		try {
			if (FileUpload.isMultipartContent(req)) {
				//DESCOMENTE PARA TESTAR FALHA DE UPLOAD, COMO ESTÁ SENDO TRATADO O RETORNO.
//				if(true){
//					throw new NullPointerException("ou");
//				}
				List items = upload.parseRequest(req);
				Iterator iter = items.iterator();

				while (iter.hasNext()) {

					FileItem item = (FileItem) iter.next();

					if (item.isFormField()) {
						String name = item.getFieldName();
						Object value = input.getValue(name);

						if (value == null) {
							input.setValue(name, item.getString());
						} else if (value instanceof String) {
							String s = (String) value;
							String[] array = new String[2];
							array[0] = s;
							array[1] = item.getString();
							input.setValue(name, array);
						} else if (value instanceof String[]) {
							String[] s = (String[]) value;
							String[] array = new String[s.length + 1];
							System.arraycopy(s, 0, array, 0, s.length);
							array[array.length - 1] = item.getString();
							input.setValue(name, array);
						} else {
							throw new FilterException(
									"Error trying to add a field value!");
						}
					} else {
						String name = item.getFieldName();

						if (item.getSize()>0) {
							input.setValue(name, item);
						} else {
							input.removeValue(name);
						}

					}
				}
			}
		} catch (Exception e) {
			throw new ServletUserException(e.getMessage());
		}

		return chain.invoke();
	}

	@Override
	public void destroy() {

	}
}
