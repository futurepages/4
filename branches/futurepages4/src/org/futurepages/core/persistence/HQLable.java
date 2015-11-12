package org.futurepages.core.persistence;

/**
 * @author leandro
 */
public interface HQLable {

    public static final String AND = " AND ";
    public static final String JOIN = " JOIN ";
    public static final String LEFT_JOIN = " LEFT JOIN ";
    public static final String WHERE = " WHERE ";
    public static final String ORDER_BY = " ORDER BY ";
    public static final String GROUP_BY = " GROUP BY ";
    public static final String HAVING = " HAVING ";
    public static final String ASC = " ASC ";
    public static final String DESC = " DESC ";
    public static final String IN = " IN ";
    public static final String IS = " IS ";
	public static final String NOT_EXISTS = " NOT EXISTS ";
    public static final String NOT_IN = " NOT IN ";
    public static final String NOT_LIKE  = " NOT LIKE ";
    public static final String AS  = " AS ";
    public static final String LIKE  = " LIKE ";
    public static final String OR  = " OR ";
    public static final String EQUALS  = " = ";
	public static final String DELETE  = " DELETE ";
	public static final String DIFFERENT  = " != ";
    public static final String GREATER  = " > ";
    public static final String GREATER_EQUALS  = " >= ";
    public static final String LOWER  = " < ";
    public static final String LOWER_EQUALS  = " <= ";
	public static final String BETWEEN  = " BETWEEN ";
}