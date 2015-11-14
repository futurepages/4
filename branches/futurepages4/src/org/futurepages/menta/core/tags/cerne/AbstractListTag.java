package org.futurepages.menta.core.tags.cerne;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;

import org.futurepages.menta.annotations.SuperTag;
import org.futurepages.menta.annotations.TagAttribute;
import org.futurepages.menta.core.tags.PrintTag;

/**
 * @author Sergio Oliveira
 */
@SuperTag
public abstract class AbstractListTag<T extends Object> extends AbstractListContext<T> {

	@TagAttribute(required = false)  //@TODO - não ficar aqui, deveria estar na tag List
	private String value;

	@TagAttribute
	private String orderBy = null;

	@TagAttribute
	private boolean mapKeys = false;

	@TagAttribute
	private boolean desc = false;

	public void setValue(String value) {
		this.value = value;
	}

	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	public void setDesc(boolean decreasing) {
		this.desc = decreasing;
	}

	@Override
	protected String getName() {
		return value;
	}

	@Override
	public List<T> getList() throws JspException {
		Tag parent = findAncestorWithClass(this, Context.class);

		if (parent != null) {
			Context ctx = (Context) parent;
			Object obj = ctx.getObject();
			if (obj != null) {
				Object object = PrintTag.getValue(obj, value, false);
				if (object instanceof List) {

					if (orderBy != null) {
						return (List<T>) ListSorter.sort((List<Object>) (List<T>) object, orderBy, desc);
					}else if(desc){
						Collections.reverse((List<T>)object);
					}

					return (List<T>) object;

				} else if (object instanceof Object[]) {

					if (orderBy != null) {
						return (List<T>) ListSorter.sort((List<Object>) Arrays.asList((T[]) object), orderBy, desc);
					}else if(desc){
						Collections.reverse((List<Object>) Arrays.asList((T[]) object));
					}

					return Arrays.asList((T[]) object);

				} else if (object instanceof Set) {

					// TODO:
					// this is not good, but for now let's do it to support sets...
					// A ListWrapper for a Set would be better to avoid copying...

					Set set = (Set) object;

					List<T> list = new ArrayList<T>(set);

					if (orderBy != null) {
						return (List<T>) ListSorter.sort((List<Object>) list,orderBy,desc);
					}else if(desc){
						Collections.reverse(list);
					}

					return list;

				} else if (object instanceof Collection) {

					// TODO:
					// this is not good, but for now let's do it to support sets...
					// A CollectionWrapper for a Collection would be better to avoid
					// copying...

					Collection coll = (Collection) object;

					List<T> list = new ArrayList<T>(coll);

					if (orderBy != null) {
						return (List<T>) ListSorter.sort((List<Object>) list,orderBy, desc);
					}else if(desc){
						Collections.reverse(list);
					}

					return list;

				} else if (object instanceof Map) {


					Collection coll = ((Map) object).values();

					List<T> list = new ArrayList<T>(coll);

					if (orderBy != null) {
						return (List<T>) ListSorter.sort((List<Object>) list,orderBy, desc);
					}else if(desc){
						Collections.reverse(list);
					}

					return list;
				}
			}
		}

		/*
		 * if (action != null) { Output output = action.getOutput(); Object obj =
		 * output.getValue(value); if (obj instanceof List) { return (List) obj; }
		 * else if (obj instanceof Object[]) { return Arrays.asList((Object[])
		 * obj); } }
		 */

		Object obj = PrintTag.getValue(value, pageContext, false);

		if (obj == null) {
			return null;
		}

		if (obj instanceof List) {

			if (orderBy != null) {
				return (List<T>) ListSorter.sort((List<Object>) (List<T>) obj, orderBy, desc);
			}else if(desc){
				Collections.reverse((List<T>)obj);
			}

			return (List<T>) obj;

		} else if (obj instanceof Object[]) {

			if (orderBy != null) {
				return (List<T>) ListSorter.sort((List<Object>) Arrays.asList((T[]) obj), orderBy, desc);
			}else if(desc){
				Collections.reverse((List<Object>) Arrays.asList((T[]) obj));
			}

			return Arrays.asList((T[]) obj);

		} else if (obj instanceof Set) {

			// TODO:
			// this is not good, but for now let's do it to support sets...
			// A ListWrapper for a Set would be better to avoid copying...

			Set set = (Set) obj;

			List<T> list = new ArrayList<T>(set);

			if (orderBy != null) {
				return (List<T>) ListSorter.sort((List<Object>) list,orderBy, desc);
			}else if(desc){
				Collections.reverse(list);
			}

			return list;

		} else if (obj instanceof Collection) {

			// TODO:
			// this is not good, but for now let's do it to support collection...
			// A CollectionWrapper for a Collection would be better to avoid
			// copying...

			Collection coll = (Collection) obj;

			List<T> list = new ArrayList<T>(coll);

			if (orderBy != null) {
				return (List<T>) ListSorter.sort((List<Object>) list,orderBy, desc);
			}else if(desc){
				Collections.reverse(list);
			}

			return list;

		} else if (obj instanceof Map) {
			if(mapKeys){
				Set<T> set = ((Map) obj).keySet();
				List<T> list = new ArrayList<T>(set);

				if (orderBy != null) {
					return (List<T>) ListSorter.sort((List<Object>) list,orderBy, desc);
				}else if(desc){
					Collections.reverse(list);
				}
				return list;
			}else{
				Collection coll = ((Map) obj).values();

				List<T> list = new ArrayList<T>(coll);

				if (orderBy != null) {
					return (List<T>) ListSorter.sort((List<Object>) list,orderBy, desc);
				}else if(desc){
					Collections.reverse(list);
				}

				return list;
			
			}

		}
		throw new JspException("Tag List: Value " + value + " (" + obj.getClass().getName() + ") is not an instance of List or Object[], List or Set!");
	}

	public void setMapKeys(boolean mapKeys) {
		this.mapKeys = mapKeys;
	}
}