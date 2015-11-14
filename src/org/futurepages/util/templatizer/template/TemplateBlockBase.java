package org.futurepages.util.templatizer.template;

import java.util.HashMap;
import java.util.Map;

import org.futurepages.core.config.Apps;
import org.futurepages.util.templatizer.util.ContextTemplateTag;

/**
 *
 * @author thiago
 */
public class TemplateBlockBase extends AbstractTemplateBlock {

	private int initialBufferSize;

	public TemplateBlockBase() {
		initialBufferSize = 128;
	}

	public TemplateBlockBase(int initialBufferSize) {
		this.initialBufferSize = initialBufferSize;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		toString(sb);

		return sb.toString();
	}

	@Override
	public void toString(StringBuilder sb) {
		if (getNextInner() != null) {
			getNextInner().toString(sb);

			if (getNextInnerElse() != null) {
				getNextInnerElse().toString(sb);
			}
		}

		if (getNext() != null) {
			getNextInner().toString(sb);
		}
	}

	public String eval(Map<String, Object> params) {
		TemplateWriter sb = new TemplateWriter(initialBufferSize);

		if (params instanceof ContextTemplateTag) {
			eval((ContextTemplateTag)params, sb);
		} else {
			eval(params, sb);
		}

		return sb.toString();
	}

	public void eval(Map<String, Object> params, TemplateWriter sb) {
		HashMap<String, Object> auxMap = new HashMap<String,Object>();
		Apps.getInstance().getParamsMap();
		for(String key : Apps.getInstance().getParamsMap().keySet()){
			auxMap.put(key, Apps.get(key));
		}
		for(String key : params.keySet()){
			auxMap.put(key, params.get(key));
		}
		ContextTemplateTag context = new ContextTemplateTag(auxMap);
		eval(context, sb);
	}

	@Override
	public void eval(ContextTemplateTag context, TemplateWriter sb) {
		getNextInner().eval(context, sb);
	}
}
