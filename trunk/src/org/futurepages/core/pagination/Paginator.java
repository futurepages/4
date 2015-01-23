package org.futurepages.core.pagination;

import java.io.Serializable;
import java.util.List;

import org.futurepages.core.input.Input;
import org.futurepages.core.output.Output;
import org.futurepages.core.persistence.Dao;
import org.futurepages.exceptions.InputException;
import org.futurepages.util.Is;

/**
 * Operações de action para provimento de paginação.
 * @author Danilo
 */
public class Paginator implements Pageable {

	private Output output;
	private Input input;

	private int defaultPageSize = 10;

	public Paginator(Output output, Input input) {
		super();
		this.output = output;
		this.input = input;
	}

	/**
	 * Paginação de elementos
	 *
	 * Depreciado por ser uma má pratica de programação (mistura controle e modelo)
	 *
	 * @deprecated Utilize setOutputPaginationSlice (verificar em site2 e scrummer o uso)
	 */
	public <T extends Serializable> List<T> paginateList(int pageSize, Class<T> entityClass, String where, String order) {
		List<T> list = Dao.listPage(getPageNum(), pageSize, 0, " FROM " + entityClass.getName() +  (Is.empty(where) ? "" :" WHERE " + where) + " ORDER BY " + order);
		long totalSize = Dao.numRows(entityClass, where);
		double total = totalSize;
		int totalPages = (int) Math.ceil(total / pageSize);
		setOutputPaginationValues(pageSize, 0, totalSize, totalPages, getPageNum());
		return list;
	}
	/**
	 * Paginação de elementos de um report
	 *
	 * Depreciado por ser uma má pratica de programação (mistura controle e modelo)
	 *
	 * @deprecated Utilize setOutputPaginationSlice (verificar em site2 e scrummer o uso)
	 */
	public <T extends Serializable> List<T> paginateReport(int pageSize, Class<T> entityClass, Class resultClass, String fields, String where, String group, String... order) {
		List<T> list = Dao.reportPage(getPageNum(), pageSize, 0, entityClass, resultClass, fields, where , group, order);
		long totalSize = Dao.numRows(entityClass, where);
		double total = totalSize;
		int totalPages = (int) Math.ceil(total / pageSize);
		setOutputPaginationValues(pageSize, 0, totalSize, totalPages, getPageNum());
		return list;
	}

	public <T extends Serializable> void setOutputPaginationSlice(String listKey, PaginationSlice<T> slice) {
		setOutputPaginationValues(slice.getPageSize(),slice.getPagesOffset(), slice.getTotalSize(), slice.getTotalPages(), slice.getPageNumber());
		output.setValue(listKey, slice.getList());
	}

	public void setOutputPaginationValues(int pageSize, int pagesOffset, long totalSize, int totalPages, int pageNum) {
		output.setValue(_TOTAL_SIZE, (int) totalSize);
		output.setValue(_TOTAL_PAGES, totalPages);
		output.setValue(_PAGE_NUM, pageNum);
		output.setValue(_PAGE_SIZE, pageSize);
		output.setValue(_PAGES_OFFSET, pagesOffset);
	}

	/**
	 * @return Pega o numero da página corrente em uso
	 */
	public int getPageNum() {
		int pageNum = 1;
		if (input.getValue(_PAGE_NUM) != null) {
			pageNum = input.getIntValue(_PAGE_NUM);
		}
		return pageNum;
	}

	/**
	 * Extrai o tamanho da página definido no input.
	 * Se nenhum tamanho for definido, o valor retornado será o 
	 * @param defaultPageSize
	 * @return
	 */
	public int getPageSize(int defaultPageSize) {
		int pageSize; 
		try {
			pageSize = input.getIntValue(_PAGE_SIZE);
			if (!Is.selected(pageSize)) {
				pageSize = defaultPageSize;
			}
		} catch (InputException e) {
			pageSize = defaultPageSize;
		}
		return pageSize;
	}

	/*
	 * Primeiro parâmetro é o default e é o mínimo também.
	 * O segundo parâmetro é o máximo permitido.
	 */
	public int getPageSize(int defaultPageSize, int maxPageSize) {
		int pageSize;
		try {
			pageSize = input.getIntValue(_PAGE_SIZE);
			if (!Is.selected(pageSize)) {
				pageSize = defaultPageSize;
			}else{
				if(pageSize > maxPageSize){
					pageSize = maxPageSize;
				}else if(pageSize < defaultPageSize){
					pageSize = defaultPageSize;
				}
			}
		} catch (InputException e) {
			pageSize = defaultPageSize;
		}
		return pageSize;
	}

	public void setDefaultPageSize(int defPageSize){
		this.defaultPageSize = defPageSize;
	}

	public int getPageSize(){
		return this.getPageSize(this.defaultPageSize);
	}

	/**
	 * Offset é a quantidade de elementos que as páginas que estão sendo exibidas assincronamente
	 * mudou desde a última vez que a página foi carregada.
	 * @return
	 */
	public int getPagesOffset() {
		int offsetPages = 0;
		if (input.getValue(_PAGES_OFFSET) != null) {
			offsetPages = input.getIntValue(_PAGES_OFFSET);
		}
		return offsetPages;
	}

}