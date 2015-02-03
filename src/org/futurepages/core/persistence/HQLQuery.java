package org.futurepages.core.persistence;

import org.futurepages.util.ReflectionUtil;
import org.futurepages.util.The;

public class HQLQuery<T> extends HQLProvider {

	private String fieldToUpdate;
	private String expression;
	private String select;
	private Class<T> entity;
	private String alias;
	private String join;
	private String where;
	private String group;
	private String having;
	private String order;

	public HQLQuery(){

	}

	public HQLQuery(String fieldToUpdate, String expression, Class<T> entity, String where) {
		this.fieldToUpdate = fieldToUpdate;
		this.expression = expression;
		this.entity = entity;
		this.where = where;
	}

	public HQLQuery(Class<T> entity) {
		this.entity = entity;
	}

	public HQLQuery(Class<T> entity, String where) {
		this.entity = entity;
		this.where = where;
	}

	public HQLQuery(Class<T> entity, String where, String order) {
		this.entity = entity;
		this.where = where;
		this.order = order;
	}

	public HQLQuery(String select ,Class<T> entity, String where) {
		this.select = select;
		this.entity = entity;
		this.where = where;
	}

	public HQLQuery(String select, Class<T> entity, String where, String order) {
		this.select = select;
		this.entity = entity;
		this.where = where;
		this.order = order;
	}

	public HQLQuery(String select, Class<T> entity, String alias, String join, String where, String order) {
		this.select = select;
		this.entity = entity;
		this.alias = alias;
		this.join = join;
		this.where = where;
		this.order = order;
	}

	public HQLQuery(String select, Class<T> entity, String alias, String join, String where, String group, String having, String order) {
		this.select = select;
		this.entity = entity;
		this.alias = alias;
		this.join = join;
		this.where = where;
		this.group = group;
		this.having = having;
		this.order = order;
	}

	@Override
	public String toString() {
		if(fieldToUpdate !=null){
			return getUpdateHQL();
		}else{
			return getSelectHQL();
		}
	}

	public String getSelectHQL(){
		return The.concat(select(select),from(entity),as(alias),join(join),where(where),groupBy(group),having(having),orderBy(order));
	}

	public String getUpdateHQL(){
		return The.concat(updateSetting(entity), fieldToUpdate, EQUALS, expression,join(join),where(where));
	}

	public String getFieldToUpdate() {
		return fieldToUpdate;
	}

	public String getExpression() {
		return expression;
	}

	public String getSelect() {
		return select;
	}

	public Class<T> getEntity() {
		return entity;
	}

	public String getAlias() {
		return alias;
	}

	public String getJoin() {
		return join;
	}

	public String getWhere() {
		return where;
	}

	public String getGroup() {
		return group;
	}

	public String getHaving() {
		return having;
	}

	public String getOrder() {
		return order;
	}

	public void setExpression(String expression) {
		this.expression = expression;
	}

	public void setSelect(String select) {
		this.select = select;
	}

	public void setEntity(Class<T> entity) {
		this.entity = entity;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public void setJoin(String join) {
		this.join = join;
	}

	public void setWhere(String where) {
		this.where = where;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public void setHaving(String having) {
		this.having = having;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public void setFieldToUpdate(String fieldToUpdate) {
		this.fieldToUpdate = fieldToUpdate;
	}
}