package org.futurepages.core.pagination;

/**
 * Quando uma classe é paginável (pageable) possui constantes como número da página,
 * total de páginas, total de registros, etc.
 * 
 * @author leandro
 */
public interface Pageable {
    
     public static final String _PAGE_NUM    = "pNum";          		// Número da página
     public static final String _TOTAL_SIZE  = "totalSize";   			// Total de registros
     public static final String _TOTAL_PAGES = "totalPages"; 			// Total de Páginas
     public static final String _PAGE_SIZE   = "pageSize";     			// Número de registros da página
     public static final String _PAGES_OFFSET= "pagesOffset";     			// Número de registros da página

     public static final String _HAS_NEXT_PAGE     = "hasNextPage";     // possui página posterior
     public static final String _HAS_PREVIOUS_PAGE = "hasPreviousPage"; // possui página anterior
     
}