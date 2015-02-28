package org.futurepages.core.persistence;

import org.futurepages.util.CalendarUtil;
import org.futurepages.util.DateUtil;
import org.futurepages.util.HQLUtil;
import org.futurepages.util.Is;
import org.futurepages.util.The;

import javax.persistence.EnumType;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * @author leandro
 */
public class HQLField implements HQLable {

	private String fieldName;

	public HQLField(String fieldName) {
		this.fieldName = fieldName;
	}


	public String between(String dateBegin, String dateEnd) {
		return concat("((", fieldName, GREATER_EQUALS, "'", escQuoteAndSlashes(dateBegin), "') ", AND, " (", fieldName, LOWER_EQUALS, "'", escQuoteAndSlashes(dateEnd), "'))");
	}

	public String between(HQLField fieldBegin, HQLField fieldEnd) {
		return concat("((", fieldName, GREATER_EQUALS, fieldBegin.fieldName, ") ", AND, " (", fieldName, LOWER_EQUALS, fieldEnd.fieldName, "))");
	}


	public String between(Calendar start, Calendar end) {
		return concat("(", fieldName, BETWEEN, "'", CalendarUtil.dbDate(start), "'", AND, "'", CalendarUtil.dbDate(end), "'", ")");
	}


	public String inDate(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return inDate(cal);
	}

	public String inDate(int year, int month, int day) {
		GregorianCalendar cal = new GregorianCalendar(year, month - 1, day);
		return inDate(cal);
	}

	public String inDate(Calendar calendar) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(calendar.getTime());

		cal.set(Calendar.HOUR_OF_DAY, cal.getActualMinimum(Calendar.HOUR_OF_DAY));
		cal.set(Calendar.MINUTE, cal.getActualMinimum(Calendar.MINUTE));
		cal.set(Calendar.SECOND, cal.getActualMinimum(Calendar.SECOND));
		String dateBegin = DateUtil.getInstance().dbDateTime(cal.getTime());

		cal.set(Calendar.HOUR_OF_DAY, cal.getActualMaximum(Calendar.HOUR_OF_DAY));
		cal.set(Calendar.MINUTE, cal.getActualMaximum(Calendar.MINUTE));
		cal.set(Calendar.SECOND, cal.getActualMaximum(Calendar.SECOND));
		String dateEnd = DateUtil.getInstance().dbDateTime(cal.getTime());

		return this.between(dateBegin, dateEnd);
	}


	/**
	 * Cuidado: só é dado supor para aquelas enums anotadas com @Enumerated(EnumType.STRING)
	 *
	 * @param enumeration
	 * @return true if equals
	 */
	public String equalsTo(Enum<?> enumeration) {
		if (Is.empty(enumeration)) {
			return "";
		}
		return concat(fieldName, " = '", escQuoteAndSlashes(enumeration.name()), "'");
	}

	public String equalsTo(String value) {
		if (Is.empty(value)) {
			return "";
		}
		return concat(fieldName, " = '", escQuoteAndSlashes(value), "'");
	}

	public String equalsTo(long value) {
		return fieldName + " = " + value;
	}

	public String equalsTo(double value) {
		return fieldName + " = " + value;
	}

	public String equalsTo(Integer value) {
		if (value == null) {
			return "";
		}
		return fieldName + " = " + value;
	}

	public String equalsTo(Long value) {
		if (value == null) {
			return "";
		}
		return fieldName + " = " + value;
	}

	public String equalsTo(BigDecimal value) {
		if (value == null) {
			return "";
		}
		return fieldName + " = " + value;
	}

	//Este foge do padrão dos demais. Portanto, não modificá-lo para padronizar, para não causar transtorno em sistemas legados.
	public String equalsTo(Boolean value) {
		if (value == null) {
			return isNull();
		} else if (!value) {
			return isFalse();
		}
		return isTrue();
	}

	public String equalsTo(HQLField field) {
		return compareTo(EQUALS, field);
	}

	public String equalsTo(Calendar cal) {
		return timeExpression(cal, EQUALS);
	}


	public String matches(String value, boolean bringAll, boolean findSmaller) {
		if (Is.empty(value)) {
			return "";
		}
		return HQLUtil.matches(fieldName, value, bringAll, findSmaller);
	}

	public String matches(String value) {
		if (Is.empty(value)) {
			return "";
		}
		return HQLUtil.matches(fieldName, value);
	}


	public String is(Boolean bool) {
		if (Is.empty(bool)) {
			return "";
		}
		return concat(fieldName, IS, String.valueOf(bool));
	}

	public String is(HQLField field) {
		return compareTo(IS, field);
	}


	public String differentFrom(String value) {
		if (Is.empty(value)) {
			return "";
		}
		return concat(fieldName, DIFFERENT, "'", escQuoteAndSlashes(value), "'");
	}

	public String differentFrom(HQLField field) {
		return compareTo(DIFFERENT, field);
	}

	public String differentFrom(Enum<?> enumeration, EnumType type) {
		String value = escQuoteAndSlashes(enumeration.name());
		if (type.equals(EnumType.ORDINAL)) {
			value = enumeration.ordinal() + "";
		}
		return differentFrom(value);
	}

	public String differentFrom(Enum<?> enumeration) {
		return differentFrom(enumeration, EnumType.STRING);
	}

	public String differentFrom(int value) {
		return fieldName + DIFFERENT + value;
	}

	public String differentFrom(long value) {
		return differentFrom(new Long(value));
	}

	public String differentFrom(Integer value) {
		return differentFrom(value.intValue());
	}

	public String differentFrom(Long value) {
		return differentFrom(value.intValue());
	}


	public String greaterThen(String value) {
		return concat(fieldName, GREATER, "'", escQuoteAndSlashes(value), "'");
	}

	public String greaterThen(long value) {
		return fieldName + GREATER + value;
	}

	public String greaterThen(double value) {
		return fieldName + GREATER + value;
	}

	public String greaterThen(Calendar cal) {
		return timeExpression(cal, GREATER);
	}

	public String greaterThen(HQLField field) {
		return compareTo(GREATER, field);
	}


	public String greaterEqualsThen(Calendar cal) {
		return timeExpression(cal, GREATER_EQUALS);
	}

	public String greaterEqualsThen(String value) {
		return concat(fieldName, GREATER_EQUALS, "'", escQuoteAndSlashes(value), "'");
	}

	public String greaterEqualsThen(long value) {
		return fieldName + GREATER_EQUALS + value;
	}

	public String greaterEqualsThen(double value) {
		return fieldName + GREATER_EQUALS + value;
	}

	public String greaterEqualsThen(HQLField field) {
		return compareTo(GREATER_EQUALS, field);
	}


	public String lowerThen(String value) {
		return concat(fieldName, LOWER, "'", escQuoteAndSlashes(value), "'");
	}

	public String lowerThen(long value) {
		return fieldName + LOWER + value;
	}

	public String lowerThen(double value) {
		return fieldName + LOWER + value;
	}

	public String lowerThen(Calendar cal) {
		return timeExpression(cal, LOWER);
	}

	public String lowerThen(HQLField field) {
		return compareTo(LOWER, field);
	}


	public String lowerEqualsThen(String value) {
		return concat(fieldName, LOWER_EQUALS, "'", escQuoteAndSlashes(value), "'");
	}

	public String lowerEqualsThen(long value) {
		return fieldName + LOWER_EQUALS + value;
	}

	public String lowerEqualsThen(double value) {
		return fieldName + LOWER_EQUALS + value;
	}

	public String lowerEqualsThen(Calendar cal) {
		return timeExpression(cal, LOWER_EQUALS);
	}

	public String lowerEqualsThen(HQLField field) {
		return compareTo(LOWER_EQUALS, field);
	}


	public String compareTo(String signal, HQLField field) {
		return concat(fieldName, signal, field.fieldName);
	}


	public String hasAllWordsInSequence(String... words) {
		return hasAllWordsInSameSequence(words);
	}

	public String hasAllWordsInSameSequence(String[] words) {
		if (words == null) {
			return "";
		}
		return HQLUtil.fieldHasAllWordsInSameSequence(fieldName, words);
	}

	public String hasAllWordsInSameSequence(String words) {
		if (words == null) {
			return "";
		}
		return HQLUtil.fieldHasAllWordsInSameSequence(fieldName, words);
	}

	public String hasAllWords(String[] tokens) {
		if (tokens == null) {
			return "";
		}
		return HQLUtil.fieldHasWords(fieldName, tokens, AND);
	}

	public String hasAllWords(String tokens) {
		if (Is.empty(tokens)) {
			return "";
		}
		return HQLUtil.fieldHasWords(fieldName, The.explodedToArray(tokens, " "), AND);
	}

	public String hasAnyOfWords(String[] tokens) {
		if (tokens == null) {
			return "";
		}
		return HQLUtil.fieldHasWords(fieldName, tokens, OR);
	}

	/*
	 * Separa a String em um array de Strings usando espaços
	 * ou tabulações como ponto de "quebra". Em seguida monta
	 * a parte da HQL usando este array de Strings.
	 */
	public String hasAnyOfWords(String tokens) {
		String[] arrayTokens;

		if (Is.empty(tokens)) {
			return "";
		}

		arrayTokens = tokens.split("\\s+");

		return HQLUtil.fieldHasWords(fieldName, arrayTokens, OR);
	}

	public String isTrue() {
		return fieldName + " = true";
	}

	public String isFalse() {
		return fieldName + " = false";
	}

	public String isNull() {
		return fieldName + " = null";
	}

	public String isNotNull() {
		return fieldName + " != null";
	}

	private String buildlStringExpression(String logicConector, String... tokens) {
		if (tokens == null || tokens.length == 0) {
			return "";
		}
		return concat(fieldName, logicConector, "(", HQLUtil.imploded(tokens), ")");
	}

	private String buildlLongExpression(String logicConector, long... tokens) {
		if (tokens == null || tokens.length == 0) {
			return "";
		}
		return concat(fieldName, logicConector, "(", HQLUtil.imploded(tokens), ")");
	}

	public String in(String... tokens) {
		return buildlStringExpression(IN, tokens);
	}

	public String inList(Collection list) {
		if (list == null || list.size() == 0) {
			return "";
		}
		return concat(fieldName, IN, "(", HQLUtil.imploded(list), ")");
	}

	public String notInList(List list) {
		if (list == null || list.size() == 0) {
			return "";
		}
		return concat(fieldName, NOT_IN, "(", HQLUtil.imploded(list), ")");
	}

	public String inSubQuery(String subQuery) {
		if (subQuery == null || subQuery.length() == 0) {
			return "";
		}
		return concat(fieldName, IN, "(", subQuery, ")");
	}

	public String notInSubQuery(String subQuery) {
		if (subQuery == null || subQuery.length() == 0) {
			return "";
		}
		return concat(fieldName, NOT_IN, "(", subQuery, ")");
	}

	public String in(long... tokens) {
		return buildlLongExpression(IN, tokens);
	}

	public String notIn(String... tokens) {
		return buildlStringExpression(NOT_IN, tokens);
	}

	public String notIn(List<String> elements) {
		if (elements != null && elements.size() > 0) {
			return concat(fieldName, NOT_IN + "(", HQLUtil.imploded(elements), ")");
		} else {
			return "";
		}
	}

	public String notIn(String tokensStr) {
		if (tokensStr != null && tokensStr.length() > 0) {
			return concat(fieldName, NOT_IN + "(", HQLUtil.imploded(tokensStr), ")");
		} else {
			return "";
		}
	}

	public String notIn(long... tokens) {
		return buildlLongExpression(NOT_IN, tokens);
	}

	public String startsWith(String value) {
		return likeExpression("'", value, "%'");
	}

	public String startsWith(Enum<?> value) {
		return likeExpression("'", value.name(), "%'");
	}

	public String notStartsWith(String value) {
		return notLikeExpression("'", value, "%'");
	}

	public String notStartsWith(Enum<?> value) {
		return notLikeExpression("'", value.name(), "%'");
	}

	public String endsWith(String value) {
		return likeExpression("'%", value, "'");
	}

	public String contains(String value) {
		return likeExpression("'%", value, "%'");
	}

	private String likeExpression(String prefix, String value, String sufix) {
		if (Is.empty(value)) {
			return "";
		}
		return concat(fieldName, LIKE, prefix, escLike(value), sufix);
	}

	private String notLikeExpression(String prefix, String value, String sufix) {
		if (Is.empty(value)) {
			return "";
		}
		return concat(fieldName, NOT_LIKE, prefix, escLike(value), sufix);
	}

	public String as(String alias) {
		return fieldName + AS + alias;
	}

	@Override
	public String toString() {
		return this.fieldName;
	}

	private String escLike(String hql) {
		return HQLUtil.escLike(hql);
	}

	private String escQuoteAndSlashes(String hql) {
		return HQLUtil.escQuotesAndSlashes(hql);
	}

	/**
	 * utilize o #equalsTo()
	 */
	@Override
	@Deprecated
	public boolean equals(Object obj) {
		return false;
	}

	private String timeExpression(Calendar cal, String comparator) {
		if (cal == null) {
			return "";
		}
		return concat(fieldName, comparator, "'", escQuoteAndSlashes(DateUtil.getInstance().dbDateTime(cal.getTime())), "'");
	}

	private String concat(String... str) {
		return The.concat(str);
	}
}