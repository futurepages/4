package org.futurepages.menta.tags.core.webcomponent;

import org.futurepages.menta.annotations.Tag;
import org.futurepages.menta.annotations.TagAttribute;
import org.futurepages.menta.core.tags.build.ContentTypeEnum;
import org.futurepages.menta.filters.HeadTitleFilter;
import org.futurepages.util.Is;
import org.futurepages.util.The;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

@Tag(bodyContent = ContentTypeEnum.SCRIPTLESS)
public final class WebContainer extends SimpleTagSupport {

	static ThreadLocal<WebContainer> threadLocal = new ThreadLocal();
	
	@TagAttribute
	private String xmlns;

	@TagAttribute
	private String lang;

	@TagAttribute
	private String dir = "";

	@TagAttribute
	private String id = "";

	@TagAttribute
	private String headFile = null;

	@TagAttribute
	private String footerFile = null;

	@TagAttribute
	private String htmlAttrs = "";

	@TagAttribute
	private String headTitle = null;
	
	@TagAttribute(required = false)
	private String bodyClass = null;

	@TagAttribute(required = false)
	private String htmlClass = null;

	private boolean bodyEvaluated = false;

	private StringBuilder headSB = null;

	private StringBuilder footerSB = null;

	private StringBuilder specialHeadTitle = null;

	private Map<String, ImportComponentRes> components;
	private HashSet bodyClasses;


	public WebContainer() {
		super();
		this.setXmlns("http://www.w3.org/1999/xhtml");
		this.setLang("pt-br");
	}

	public static void importRes(JspContext page, String key, String version, boolean noJS, boolean noCSS, boolean jsInHead) throws JspException, IOException {
		importRes(page, key, version, noJS, noCSS, jsInHead, null, false);
	}

	public static void importRes(JspContext page, String key, String version, boolean noJS, boolean noCSS, boolean jsInHead, String moduleId, boolean pseudo) throws JspException, IOException {
		ImportComponentRes icr = new ImportComponentRes();
		icr.setKey(key);
		icr.setVersion(version);
		icr.setJsInHead(jsInHead);
		icr.setModuleId(moduleId);
		icr.setPseudo(pseudo);
		icr.setJspContext(page);
		icr.doTag();
	}

	Map<String, ImportComponentRes> getComponents() {
		if (components == null) {
			components = new LinkedHashMap<String, ImportComponentRes>();
		}
		return components;
	}

	public boolean isBodyEvaluated() {
		return bodyEvaluated;
	}

	public void addComponent(String componentUniqueKey, ImportComponentRes component) {
		if (!getComponents().containsKey(componentUniqueKey)) {
			getComponents().put(componentUniqueKey, component);
		}
	}

	@Override
	public void doTag() throws JspException, IOException {
		HttpServletRequest req = (HttpServletRequest) ((PageContext) getJspContext()).getRequest();

		threadLocal.set(this);

		StringWriter evalResult = new StringWriter();
		StringBuffer headBufferBegin = new StringBuffer();
		StringBuffer headBufferEnd = new StringBuffer();
		StringBuffer footerBuffer = new StringBuffer();

		headBufferBegin.append(The.concat("<html",id,xmlns,lang,dir," ",htmlAttrs," "));
		if (htmlClass != null && htmlClass.length() > 0) {
			headBufferBegin.append(The.concat(" class=\"", htmlClass, "\" "));
		}
		headBufferBegin.append("><head>");

		//invoca o conteúdo dentro do container
		getJspBody().invoke(evalResult);

		//Components' Resources

		for (ImportComponentRes component : getComponents().values()) {
			if (!component.isNoCSS() && !component.isPseudo()) {
				component.appendCSSto(req, headBufferEnd);
			}
			if (!component.isNoJS() && !component.isPseudo()) {
				if (component.isJsInHead()) {
					component.appendJSto(req, headBufferEnd);
				} else {
					component.appendJSto(req, footerBuffer);
				}
			}
		}

		headBufferEnd.append("</head><body ");
		if (bodyClass != null && bodyClass.length() > 0) {
			addBodyClasses(bodyClass);
		}
		if(getBodyClasses().size()>0){
			headBufferEnd.append(The.concat("class=\"", getBodyClassesStr(), "\" "));
		}

		headBufferEnd.append(" >");
		
		footerBuffer.append("</body></html>");

		getJspContext().getOut().print(headBufferBegin);

		if (headFile != null) {
			try {
				((PageContext) getJspContext()).include(headFile, false);
			} catch (ServletException ex) {
				throw new JspException("Impossível incluir arquivo HEAD '" + headFile + "'. Motivo: " + ex.getMessage(), ex);
			}
		}

		StringBuffer afterHeadFileBuffer = new StringBuffer();
		if (HeadTitleFilter.isPretty()) {
			if (!Is.empty(specialHeadTitle)) {
				afterHeadFileBuffer.append("<title>").append(specialHeadTitle).append("</title>");
			} else if (!Is.empty(this.headTitle)) {
				afterHeadFileBuffer.append("<title>").append(headTitle).append(" | ").append(HeadTitleFilter.getPrettyTitle()).append("</title>");
			} else {
				afterHeadFileBuffer.append("<title>").append(HeadTitleFilter.getPrettyTitle()).append(HeadTitleFilter.SEPARATOR).append(HeadTitleFilter.getGlobalTitle()).append("</title>");
			}
		}
		//Dynamic Head Content (from tag)
		if(headSB!=null){
			afterHeadFileBuffer
//					.append("\n<!-- [BEGIN] Dynamic Head Content  -->") //for DEBUG-MODE
					.append(headSB.toString())
//					.append("\n<!-- [END] Head Content  -->\n") //for DEBUG-MODE
			;
		}
		getJspContext().getOut().print(afterHeadFileBuffer.append(headBufferEnd).append(evalResult.getBuffer()));
		if (footerFile != null) {
			try {
				((PageContext) getJspContext()).include(footerFile, false);
			} catch (ServletException ex) {
				throw new JspException("Impossível incluir arquivo FOOTER '" + footerFile+ "'. Motivo: " + ex.getMessage(), ex);
			}
		}
		if(footerSB!=null){
			getJspContext().getOut().print(footerSB.append(footerBuffer));
		}else{
			getJspContext().getOut().print(footerBuffer);
		}

		bodyEvaluated = true;
		threadLocal.remove(); // quase equivalente a threadLocal.set(null);
	}

	public void setHeadFile(String headFile) {
		this.headFile = headFile;
	}

	public void setFooterFile(String footerFile) {
		this.footerFile = footerFile;
	}

	public void setHeadTitle(String headTitle) {
		this.headTitle = headTitle;
	}

	public void setDir(String dir) {
		this.dir = " dir=\""+dir+"\"";
	}

	public void setId(String id) {
		this.id = " id=\""+id+"\"";
	}

	public void setLang(String lang) {
		this.lang = The.concat(" xml:lang=\"",lang,"\" lang=\"",lang+"\"");
	}

	public void setXmlns(String xmlns) {
		if(!Is.empty(xmlns)){
			this.xmlns = " xmlns=\""+ xmlns+"\"";
		}else{
			this.xmlns = "";
		}
	}

	public void setHtmlAttrs(String htmlAttrs) {
		this.htmlAttrs = htmlAttrs;
	}

	public void setBodyClass(String bodyClass) {
		this.bodyClass = bodyClass;
	}

	public void setHtmlClass(String htmlClass) {
		this.htmlClass = htmlClass;
	}

	static WebContainer get() {
		return threadLocal.get();
	}

	void addHeadContent(String headContent) {
		getHeadSB().append("\n").append(headContent);
	}

	void addFooterContent(String footerContent) {
		getFooterSB().append("\n").append(footerContent);
	}

	public StringBuilder getHeadSB() {
		if(headSB==null){
			headSB = new StringBuilder();
		}
		return headSB;
	}

	public StringBuilder getFooterSB() {
		if(footerSB==null){
			footerSB = new StringBuilder();
		}
		return footerSB;
	}

	public void addSpecialHeadContent(String specialHeadContent) {
		specialHeadTitle = new StringBuilder(specialHeadContent);
	}

	public StringBuilder getSpecialHeadTitle() {
		if (specialHeadTitle == null) {
			specialHeadTitle = new StringBuilder();
		}
		return specialHeadTitle;
	}

	public void addBodyClasses(String bodyClasses) {
		String[] bodyClassesArray = bodyClasses.split(" ");
		for(String clsss : bodyClassesArray){
			getBodyClasses().add(clsss);
		}
	}

	public Set<String> getBodyClasses() {
		if(bodyClasses == null){
			bodyClasses = new HashSet();
		}
		return bodyClasses;
	}

	private String getBodyClassesStr() {
		StringBuilder sb = new StringBuilder();
		for(String clsss : getBodyClasses()){
			sb.append(clsss).append(" ");
		}
		return sb.toString();
	}

}