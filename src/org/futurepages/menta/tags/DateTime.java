package org.futurepages.menta.tags;

import org.futurepages.menta.annotations.Tag;
import org.futurepages.menta.annotations.TagAttribute;
import org.futurepages.menta.core.tags.PrintTag;
import org.futurepages.menta.core.tags.build.ContentTypeEnum;
import org.futurepages.util.DateUtil;

import javax.servlet.jsp.JspException;
import java.util.Calendar;
import java.util.Date;

/**
 * @author Leandro
 */
@Tag(bodyContent = ContentTypeEnum.EMPTY)
public class DateTime extends PrintTag{
   
	@TagAttribute(rtexprvalue = false)
	private String mask;
	
	@TagAttribute
	private Object date;
	
	public DateTime(){
		mask = "dd/MM/yyyy";
	}

	public void setMask(String mask) {
		this.mask = mask;
	}
	
    public void setDate(Object date) {
		this.date = date;
	}

	public String getStringToPrint() throws JspException {
		if(date!=null){
			if(date instanceof Date){
				return DateUtil.getInstance().format((Date) date, mask);
			}else{
				return DateUtil.getInstance().viewDateTime((Calendar) date, mask);
			}
    	}
    	return DateUtil.getInstance().viewToday(mask);
    }

}
