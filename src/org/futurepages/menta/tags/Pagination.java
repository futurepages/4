package org.futurepages.menta.tags;

import org.futurepages.core.path.Paths;
import org.futurepages.menta.annotations.Tag;
import org.futurepages.menta.annotations.TagAttribute;
import org.futurepages.menta.core.pagination.Pageable;
import org.futurepages.menta.core.tags.PrintTag;
import org.futurepages.menta.core.tags.build.ContentTypeEnum;

import javax.servlet.jsp.JspException;

import static org.futurepages.util.The.concat;

@Tag(bodyContent = ContentTypeEnum.JSP)
public class Pagination extends PrintTag implements Pageable {

	@TagAttribute(required = true)
	private String url = null;

	private String cachedUrl;

	@TagAttribute
	private String params = "";

	@TagAttribute
	private int maxShowing = 20;

	@TagAttribute
	private String descriptor = "";

	@TagAttribute
	private boolean withoutNumbers = false; //withNumbers is default

	@TagAttribute
	private boolean justTop = false;

	@TagAttribute
	private String target = "";

	@TagAttribute
	private String cssClass = "pagination";

	@TagAttribute
	private String nextLabel = "&raquo;";

	@TagAttribute
	private String previousLabel = "&laquo;";

	@TagAttribute
	private boolean useImages = false;

	private static final String NEXT_PAGE = "nextpage";
	private static final String PREVIOUS_PAGE = "previouspage";
	private static final String IMAGE_FORMAT = "gif";
	//Não é atributo da tag
	private String symbol = "?";

	@Override
	public String getStringToPrint() throws JspException {
		Integer totalPages = (Integer) action.getOutput().getValue(_TOTAL_PAGES);
		//Se possui mais de uma página.
		if (totalPages != null) {
			String pagesLinks = allPaginationLinks(totalPages);
			if (totalPages > 1) {
				if (justTop) {
					return concat(descriptor, pagesLinks, getBodyContent().getString());
				} else {
					return concat(descriptor, pagesLinks, getBodyContent().getString(), descriptor, pagesLinks);
				}
			}
			//Se não possui mais de uma página.
		} else if (justTop) {
			return descriptor + getBodyContent().getString();
		}
		return concat(descriptor, getBodyContent().getString(), descriptor);
	}

	public void setNextLabel(String nextLabel) {
		this.nextLabel = nextLabel;
	}

	public void setPreviousLabel(String previousLabel) {
		this.previousLabel = previousLabel;
	}

	public void setWithoutNumbers(boolean withoutNumbers) {
		this.withoutNumbers = withoutNumbers;
	}

	public String getUrl() {
		if (cachedUrl == null) {
			if (url.startsWith("/")) {
				this.cachedUrl = Paths.context(req) + url;
			} else {
				this.cachedUrl = this.url;
			}
		}
		return this.cachedUrl;
	}

	public void setUrl(String url) {
		this.cachedUrl = null;
		this.url = url;
	}

	public void setParams(String params) {
		this.params = params;
	}

	public void setCssClass(String cssClass) {
		this.cssClass = cssClass;
	}

	public void setTarget(String target) {
		this.target = "target= \"" + target + "\"";
	}

	public void setJustTop(boolean justTop) {
		this.justTop = justTop;
	}

	public void setMaxShowing(int maxShowing) {
		this.maxShowing = maxShowing;
	}

	public void setUseImages(boolean useImages) {
		this.useImages = useImages;
	}

	public void setDescriptor(String descriptor) {
		this.descriptor = descriptor;
	}

	// MÉTODOS PRIVADOS //////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////////////////
	private String pageNumLinks(int pageNum, int totalPages) {
		StringBuffer sb = new StringBuffer();

		if (!withoutNumbers) {
			//Resultado com até 20 páginas.
			if (totalPages <= maxShowing) {
				for (int i = 1; i <= totalPages; i++) {
					if (pageNum != i) {
						sb.append(pageLink(i));
					} else {
						sb.append("<span class=\"current\">" + pageNum + "</span>");
					}
				}
			} else {
				//Resultado com mais de 20 páginas.
				if (pageNum > 6) {
					for (int i = 1; i <= 3; i++) {
						sb.append(pageLink(i));
					}
					sb.append(" ... ");
					for (int i = pageNum - 3; i <= pageNum - 1; i++) {
						sb.append(pageLink(i));
					}
				} else {
					for (int i = 1; i < pageNum; i++) {
						sb.append(pageLink(i));
					}
				}

				sb.append("<strong class=\"current\">" + pageNum + "</strong>");

				if (pageNum < totalPages - 6) {
					for (int i = pageNum + 1; i <= pageNum + 3; i++) {
						sb.append(pageLink(i));
					}
					sb.append(" ... ");
					for (int i = totalPages - 2; i <= totalPages; i++) {
						sb.append(pageLink(i));
					}
				} else {
					for (int i = pageNum + 1; i <= totalPages; i++) {
						sb.append(pageLink(i));
					}
				}
			}
		} else {
			boolean prevOK = false;
			boolean nextOK = false;
			for (int i = 1; i <= totalPages; i++) {
				if (pageNum < i && !prevOK) {
					sb.append("<span class=\"disabled\"> ... </span>");
					prevOK = true;
				} else if(pageNum > i && !nextOK){
					sb.append("<span class=\"disabled\"> ... </span>");
					nextOK = true;
				} else if(pageNum == i){
					sb.append("<span class=\"current\">" + pageNum + "</span>");
				}
			}
		}
		return sb.toString();
	}

	/**
	 * Verifica se a página é a primeira para habilitar/desabilitar o botão (<)
	 */
	private String previousPageLink(int pageNum) {
		//enabled
		if (pageNum > 1) {
			return concat("<a href=\"", getUrl(), params, symbol, _PAGE_NUM, "=", (pageNum - 1), "\" ", target, ">", previousButton(true), "</a>");
		} //disabled
		else {
			return "<span class=\"disabled\">" + previousButton(false) + "</span>";
		}
	}

	/**
	 * Verifica se é a última página para habilitar/desabilitar o botão (>)
	 */
	private String nextPageLink(int pageNum, int totalPages) {
		//disabled
		if (pageNum == totalPages) {
			return "<span class=\"disabled\">" + nextButton(false) + "</span>";
		} //enabled
		else {
			return concat("<a href=\"", getUrl(), params, symbol, _PAGE_NUM, "=", (pageNum + 1), "\" ", target, ">", nextButton(true), "</a>");
		}
	}

	private String allPaginationLinks(Integer totalPages) {
		Integer pageNum = (Integer) action.getOutput().getValue(_PAGE_NUM);
		StringBuffer sb = new StringBuffer();

		if (params.contains("?")) {
			symbol = "&";
		}

		sb.append(previousPageLink(pageNum));

		sb.append(pageNumLinks(pageNum, totalPages));

		sb.append(nextPageLink(pageNum, totalPages));

		return "<div class=\"" + cssClass + "\">" + sb.toString() + "</div>";
	}

	private String pageLink(int pageNum) {
		return concat("<a href=\"", getUrl(), params, symbol, _PAGE_NUM, "=", pageNum, "\" ", target, ">", pageNum, "</a>");
	}

	private String previousButton(boolean enabled) {
		if (!useImages) {
			return previousLabel;
		} else {
			return adjImgButton(PREVIOUS_PAGE, enabled);
		}
	}

	private String nextButton(boolean enabled) {
		if (!useImages) {
			return nextLabel;
		} else {
			return adjImgButton(NEXT_PAGE, enabled);
		}
	}

	private String adjImgButton(String type, boolean enabled) {
		String enabledResult = enabled ? "" : "_disable";
		return concat("<img src=\"", Paths.theme(action.getRequest()), "/res/", cssClass, "/", type + enabledResult, ".", IMAGE_FORMAT, "\"/>");
	}
}
