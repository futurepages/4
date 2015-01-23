
package org.futurepages.filters;

import javax.servlet.http.HttpServletRequest;
import org.futurepages.core.action.Action;
import org.futurepages.core.control.InvocationChain;
import org.futurepages.core.filter.Filter;
import org.futurepages.util.Is;
import org.futurepages.util.NumberUtil;

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

	public AutoRedirectDomainFilter(String baseURL) {
		int posiProtocol = baseURL.indexOf("://");
		if(posiProtocol>-1){
			mainProtocol = baseURL.substring(0, posiProtocol);
			mainDomain = baseURL.substring(posiProtocol+3);
		}else{
			this.mainDomain = baseURL;
			this.mainProtocol = null;
		}
//		System.out.println(mainProtocol);
//		System.out.println(mainDomain);
		String[] domainParts = this.mainDomain.split("\\:");
		if(!Is.validStringKey(domainParts[0],3,100,true) || this.mainDomain.endsWith(":") || (domainParts.length==2 && NumberUtil.strToLong(domainParts[1])==null) ){
			throw new RuntimeException("Erro ao inicializar o filtro AutoRedirectDomainFilter. Especifique um domínio válido em app-params.xml[param=AUTO_REDIRECT_DOMAIN]");
		}
	}

	@Override
	public String filter(InvocationChain chain) throws Exception {
		HttpServletRequest req = chain.getAction().getRequest();
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
		return chain.invoke();
	}

	/**
	 *
	 * @param request
	 * @return
	 */
	private String changeDomain(HttpServletRequest request){
		StringBuffer newUrl = new StringBuffer();
		newUrl.append(request.getScheme()).append("://").append(this.mainDomain);
		//nao descomentar, erroneamente estava encaminhando a porta para a nova url, acontece que a porta deve vim no parametro da url nova.
		//newUrl.append(request.getLocalPort()!=80 && request.getLocalPort()!=443 ? ":"+request.getLocalPort() : "" );
		newUrl.append(request.getRequestURI());
		if(request.getQueryString()!=null){
			newUrl.append("?").append(request.getQueryString());
		}
		return newUrl.toString();
	}

	private String changeDomainAndProtocol(HttpServletRequest request){
		StringBuffer newUrl = new StringBuffer();
		newUrl.append(this.mainProtocol).append("://").append(this.mainDomain);
		//nao descomentar, erroneamente estava encaminhando a porta para a nova url, acontece que a porta deve vim no parametro da url nova.
		//newUrl.append(request.getLocalPort()!=80 && request.getLocalPort()!=443 ? ":"+request.getLocalPort() : "" );
		newUrl.append(request.getRequestURI());
		if(request.getQueryString()!=null){
			newUrl.append("?").append(request.getQueryString());
		}
		return newUrl.toString();
	}

	@Override
	public void destroy() {	}

}