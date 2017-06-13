package org.futurepages.core.persistence;

/**
 * @author leandro
 */
public interface HQLable {

    String AND = " AND ";
    String JOIN = " JOIN ";
    String LEFT_JOIN = " LEFT JOIN ";
    String RIGHT_JOIN = " RIGHT JOIN ";
    String WHERE = " WHERE ";
    String ORDER_BY = " ORDER BY ";
    String GROUP_BY = " GROUP BY ";
    String HAVING = " HAVING ";
    String ASC = " ASC ";
    String DESC = " DESC ";
    String IN = " IN ";
    String IS = " IS ";
	String NOT_EXISTS = " NOT EXISTS ";
    String NOT_IN = " NOT IN ";
    String NOT_LIKE  = " NOT LIKE ";
    String AS  = " AS ";
    String LIKE  = " LIKE ";
    String OR  = " OR ";
    String EQUALS  = " = ";
	String DELETE  = " DELETE ";
	String DIFFERENT  = " != ";
    String GREATER  = " > ";
    String GREATER_EQUALS  = " >= ";
    String LOWER  = " < ";
    String LOWER_EQUALS  = " <= ";
	String BETWEEN  = " BETWEEN ";
}