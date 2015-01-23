package org.futurepages.tags.core.webcomponent;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashSet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import org.futurepages.annotations.Tag;
import org.futurepages.annotations.TagAttribute;
import org.futurepages.core.config.Params;
import org.futurepages.core.path.Paths;
import org.futurepages.core.tags.build.ContentTypeEnum;
import org.futurepages.util.EncodingUtil;
import org.futurepages.util.Is;
import static org.futurepages.util.StringUtils.concat;

@Tag(bodyContent = ContentTypeEnum.SCRIPTLESS)
public final class ImportComponentRes extends SimpleTagSupport {

	public static final String KEY = "importComponentRes";

	public static final ThreadLocal<HashSet> asyncResources = new ThreadLocal();
	public static final ThreadLocal<Boolean> asyncResStoredInClient = new ThreadLocal();

	//chamado sempre antes da execução de uma DynAction (por conta do compartilhamento de Threads do Tomcat
	public static void destroyAsyncResources() {
		asyncResources.remove();
		asyncResStoredInClient.remove();
	}

	@TagAttribute(required = true)
	private String key;

	@TagAttribute(required = true)
	private String version;

	@TagAttribute
	private boolean noCSS = false;

	@TagAttribute
	private boolean noJS = false;

	@TagAttribute
	private boolean jsInHead = false;

	@TagAttribute
	private String moduleId = null;

	@TagAttribute
	private boolean pseudo = false;

	private WebContainer myContainer;

	@Override
	public void doTag() throws JspException, IOException {
		getMyContainer();

//		System.out.print("<#"+Thread.currentThread().getId()+"#> "); // for DEBUG-MODE
//		System.out.print(myContainer == null ? "{AJAX} " : ""); // for DEBUG-MODE
//		System.out.println(uniqueKey()); // for DEBUG-MODE
		
		if (myContainer != null) {
			StringBuffer buffer = new StringBuffer();
			if (getJspBody() != null) {
				if (!myContainer.getComponents().containsKey(uniqueKey())) {
					StringWriter out = new StringWriter();
					getJspBody().invoke(out);
					buffer.append(out.getBuffer());
				}
			}
			if (myContainer.isBodyEvaluated()) {
				throw new JspException("Componente "+uniqueKey()+" em container já avaliado e não nulo");
			}
			getJspContext().getOut().print(buffer);
//			System.out.println("---> addToContainer()"); // for DEBUG-MODE
			addToContainer();
		}		
		else
		{ //container = null (ajax request)
//			System.out.println(" ---> "+asyncResources.get()); // for DEBUG-MODE
			
			HttpServletRequest req = (HttpServletRequest) ((PageContext) getJspContext()).getRequest();
			HttpServletResponse res = (HttpServletResponse) ((PageContext) getJspContext()).getResponse();

			//if primeira vez dentro da ajax request -
			 if(asyncResources.get()==null){
				asyncResources.set(new HashSet());
				boolean cameFromClient = addComponentsPreLoaded(req,res, asyncResources.get());//adiciona no mapa os componentes que já foram requisitados na página, enviando a mensagem através do cookie
				asyncResStoredInClient.set(cameFromClient);
			 }
			 if(!asyncResources.get().contains(this.uniqueKey())){ //primeira vez do componente dentro do contexto request
				if(!noJS && !pseudo){
					String jsResStr = null;
					if(asyncResStoredInClient.get()){
						jsResStr = importJS(req, true);
					}else { //significa que não usou dynAction() no cliente, que envia o cookie.

//						System.out.println("---> needResourceJS() -- não utilizou dynAction no cliente."); // for DEBUG-MODE

						String hasModule = !Is.empty(moduleId) ? concat(",'", moduleId, "'") : "";
						jsResStr = (concat("<script type=\"text/javascript\">needResourceJS('", this.getKey(), "','", this.getVersion(), "'", hasModule, ");</script>"));
					}
					getJspContext().getOut().print(jsResStr);
				}
				if(!noCSS && !pseudo){
					getJspContext().getOut().print(importCSS(req, true));
				}
				StringWriter evalResult = new StringWriter(); //escreve o body da tag
				if(getJspBody()!=null){
					getJspBody().invoke(evalResult);
					getJspContext().getOut().print(evalResult.getBuffer());
				}
//				System.out.println("---> addToAsyncResources()"); // for DEBUG-MODE
				asyncResources.get().add(this.uniqueKey()); //adiciona para não executar mais para este componente
			 }
		}
	}

	private WebContainer getMyContainer() {
		if (myContainer == null) {
//			System.out.println("new myContainer for "+this.getKey()+" in #"+Thread.currentThread().getId()); // for DEBUG-MODE
			myContainer = WebContainer.get();
		}
		return myContainer;
	}

	private void addToContainer() {
		getMyContainer().addComponent(uniqueKey(), this);
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public boolean isNoCSS() {
		return noCSS;
	}

	public void setNoCSS(boolean noCSS) {
		this.noCSS = noCSS;
	}

	public boolean isNoJS() {
		return noJS;
	}

	public void setNoJS(boolean noJS) {
		this.noJS = noJS;
	}

	public boolean isJsInHead() {
		return this.jsInHead;
	}

	public void setJsInHead(boolean jsInHead) {
		this.jsInHead = jsInHead;
	}

	public String getModuleId() {
		return moduleId;
	}

	public boolean isPseudo() {
		return this.pseudo;
	}

	public void setPseudo(boolean pseudo) {
		this.pseudo = pseudo;
	}

	public void setModuleId(String moduleId) {
		this.moduleId = moduleId;
	}

	private String uniqueKey() {
		return this.key + ">" + this.version;
	}

	public void appendJSto(HttpServletRequest req, StringBuffer buffer) {
		buffer.append(importJS(req, false));
	}

	private String importJS(HttpServletRequest req, boolean async){
//		System.out.println("[importJS"+(async?" {async}":"")+"] {"+key+">"+version+"}"); // for DEBUG-MODE
		return  concat("<script type=\"text/javascript\" src=\"" , resPath(req) , "/" , key , "/" , version , "/" , key , ".js"+Params.get("RELEASE_QUERY")+"\"></script>"
					  ,(async?"<script type=\"text/javascript\">addComponentRes('"+this.uniqueKey()+"');</script>":"")
				);
	}

	public void appendCSSto(HttpServletRequest req, StringBuffer buffer) {
		buffer.append(importCSS(req,false));
	}

	private String importCSS(HttpServletRequest req, boolean async){
		if(!async){
			return concat("<link rel=\"stylesheet\" type=\"text/css\" href=\"" , resPath(req) , "/" , key , "/" , version , "/" , key , ".css"+Params.get("RELEASE_QUERY")+"\" media=\"all\"/>");
		}else{
			String hasModule = !Is.empty(moduleId) ? concat(",'", moduleId, "'") : "";
			return concat("<script type=\"text/javascript\">needResourceCSS('"+this.getKey(),"','",this.getVersion(),"'", hasModule, ");</script>");
		}
	}

	private String resPath(HttpServletRequest req){
		return (moduleId==null)?Paths.resource(req) : Paths.resource(req,moduleId);
	}

	@Override
	public String toString() {
		getMyContainer();
		return uniqueKey()+(myContainer!=null?" in "+this.getMyContainer():" without container");
	}

	private boolean addComponentsPreLoaded(HttpServletRequest req, HttpServletResponse res, HashSet componentsSet) throws JspException {
		String componentsToString = null;
		Cookie cookieOne = null;
		Cookie[] cookies = req.getCookies();
		boolean receivedComponents = false;
		if(cookies != null){
			for(Cookie cookie : req.getCookies()){
				if(cookie.getName().equals(KEY)){
					componentsToString = EncodingUtil.decodeUrl(cookie.getValue());
					cookieOne = cookie;
					receivedComponents = true;
					break;
				}
			}
		}
		if(componentsToString==null){
			if(req.getParameterMap().get(KEY)!=null  && (req.getParameterMap().get(KEY) instanceof String[]) && ((String[])req.getParameterMap().get(KEY)).length>0){
				componentsToString = ((String[])req.getParameterMap().get(KEY))[0]; //no need to decode url. framework does it for you.
				receivedComponents = true;

			}
		}

		if(componentsToString!=null){
			String[] components = componentsToString.split("\\|");
			for(String component : components){
				componentsSet.add(component);
			}
		}
		if(cookieOne!=null){
			cookieOne.setMaxAge(0);
			res.addCookie(cookieOne);
		}
		return receivedComponents;
	}
}