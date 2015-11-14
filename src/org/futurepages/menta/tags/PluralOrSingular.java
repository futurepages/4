package org.futurepages.menta.tags;

import org.futurepages.menta.annotations.Tag;
import org.futurepages.menta.annotations.TagAttribute;
import org.futurepages.menta.core.tags.PrintTag;
import org.futurepages.menta.core.tags.build.ContentTypeEnum;

import javax.servlet.jsp.JspException;

/**
 * @author Leandro
 */
@Tag(bodyContent = ContentTypeEnum.EMPTY)
public class PluralOrSingular extends PrintTag{
   
	@TagAttribute(required = true)
	private long value;
	
	@TagAttribute
    private boolean valueBefore;
	
	@TagAttribute(required = true)
	private String singular;
	
	@TagAttribute(required = true)
    private String plural;

	@TagAttribute
	private String zero;
	    
	public String getStringToPrint() throws JspException {
		String before = valueBefore ? value+" " : "" ;
        if(value==1){
            return before+singular;
        }
        if((value == 0) && (zero!=null)){
            return zero;
        }
        return before+plural;
    }

	public void setValue(long value) {
		this.value = value;
	}

    public void setSingular(String singular) {
        this.singular = singular;
    }

	public void setValueBefore(boolean valueBefore) {
		this.valueBefore = valueBefore;
	}

	 public void setPlural(String plural) {
        this.plural = plural;
    }

    public void setZero(String zero) {
        this.zero = zero;
    }
}
