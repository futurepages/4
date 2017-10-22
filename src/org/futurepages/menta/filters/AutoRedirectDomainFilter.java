
package org.futurepages.menta.filters;

import org.futurepages.core.config.Apps;
import org.futurepages.menta.core.action.Action;
import org.futurepages.menta.core.control.InvocationChain;
import org.futurepages.menta.core.filter.Filter;
import org.futurepages.util.Is;
import org.futurepages.util.brazil.NumberUtil;

import javax.servlet.http.HttpServletRequest;

/**
 * Filtro responsável pela modificação do domínio da url para um domínio padronizado. Isto é útil para
 * SEO. Os indexadores de busca não penalizarão o sistema por conta de urls redundantes e ainda ajudará
 * na comunicação do sistema com outros através de callback urls padronizadas.
 * 
 * @author leandro
 */
public class AutoRedirectDomainFilter implements Filter {

	private String mainDomain;
	private String mainProtocol;
	private String protocolPlusDomain;

	public AutoRedirectDomainFilter(String baseURL) {
		int posiProtocol = baseURL.indexOf("://");
		if(posiProtocol>-1){
			mainProtocol = baseURL.substring(0, posiProtocol);
			mainDomain = baseURL.substring(posiProtocol+3);
			protocolPlusDomain = mainProtocol+"://"+mainDomain;
		}else{
			this.mainProtocol = null;
			this.mainDomain = baseURL;
		}
//		System.out.println(mainProtocol);
//		System.out.println(mainDomain);
		String[] domainParts = this.mainDomain.split("\\:");
		if(!Is.validStringKey(domainParts[0].replaceAll("/", "_"), 3, 1000, true) || this.mainDomain.endsWith(":") || (domainParts.length==2 && NumberUtil.strToLong(domainParts[1])==null) ){
			throw new RuntimeException("Erro ao inicializar o filtro AutoRedirectDomainFilter. Especifique um domínio válido em app-params.xml[param=AUTO_REDIRECT_DOMAIN]");
		}
	}

	@Override
	public String filter(InvocationChain chain) throws Exception {
		HttpServletRequest req = chain.getAction().getRequest();
		if(!req.getHeader("Host").equals("localhost")){
			if(mainProtocol==null){
				if(!req.getHeader("Host").equals(mainDomain)){
					chain.getAction().getOutput().setValue(Action.REDIR_URL, changeDomain(chain.getAction().getRequest()));
					return REDIR;
				}
			}else{
				if(!req.getHeader("Host").equals(mainDomain)
				|| !req.getScheme().equals(mainProtocol)
				  ) {
					chain.getAction().getOutput().setValue(Action.REDIR_URL, changeDomainAndProtocol(chain.getAction().getRequest()));
					return REDIR;
				}
			}
		}
		return chain.invoke();
	}

	/**
	 *
	 * @param request
	 * @return
	 */
	private String changeDomain(HttpServletRequest request){
		StringBuilder newUrl = new StringBuilder();
		newUrl.append(request.getScheme()).append("://").append(this.mainDomain);
		//nao descomentar, erroneamente estava encaminhando a porta para a nova url, acontece que a porta deve vim no parametro da url nova.
		//newUrl.append(request.getLocalPort()!=80 && request.getLocalPort()!=443 ? ":"+request.getLocalPort() : "" );
		if(!request.getRequestURI().equals("/"+ Apps.get("START_PAGE_NAME"))){
			newUrl.append(request.getRequestURI());
		}else{
			newUrl.append("/");
		}
		if(request.getQueryString()!=null){
			newUrl.append("?").append(request.getQueryString());
		}
		return newUrl.toString();
	}

	private String changeDomainAndProtocol(HttpServletRequest request){
		StringBuilder newUrl = new StringBuilder();
		newUrl.append(this.protocolPlusDomain);
		//nao descomentar, erroneamente estava encaminhando a porta para a nova url, acontece que a porta deve vim no parametro da url nova.
		//newUrl.append(request.getLocalPort()!=80 && request.getLocalPort()!=443 ? ":"+request.getLocalPort() : "" );
		if(!request.getRequestURI().equals("/"+Apps.get("START_PAGE_NAME"))) {
			newUrl.append(request.getRequestURI());
		}else{
			newUrl.append("/");
		}
		if(request.getQueryString()!=null){
			newUrl.append("?").append(request.getQueryString());
		}
		return newUrl.toString();
	}

	@Override
	public void destroy() {	}

//	public static void main(String[] args) throws Exception {
//		AutoRedirectDomainFilter f = new AutoRedirectDomainFilter("https://www.tjpi.jus.br/intranet/");
//		f.filter(new InvocationChain("", new AbstractAction() {	}));
//	}

}