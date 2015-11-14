package org.futurepages.menta.core.pagination;

import java.util.List;

@Deprecated
public class OldPaginationSlice<T> {

	/** Total de registros não paginados*/
    private Long totalSize;
    
    /** Tamanho da página*/
    private Integer pageSize;
    
    /** número total de páginas*/
    private int totalPages;
    
    /** número da página*/
    private int pageNumber;

	/** o quanto deslocou */
    private int pagesOffset;
    
    /** lista referente à página*/
    private List<T> list;

    public OldPaginationSlice(Long totalSize, Integer pageSize, int pagesOffset, int numPages, int page, List<T> list) {
        this.totalSize = totalSize;
        this.pageSize = pageSize;
        this.totalPages = numPages;
        this.list = list;
        this.pageNumber = page;
        this.pagesOffset = pagesOffset;
    }

    public void setTotalPages(int total) {
        this.totalPages = total;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public Long getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(Long totalSize) {
        this.totalSize = totalSize;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(int page) {
		this.pageNumber = page;
	}

	public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

	public int getPagesOffset() {
		return pagesOffset;
	}

	public void setPagesOffset(int pagesOffset) {
		this.pagesOffset = pagesOffset;
	}
}