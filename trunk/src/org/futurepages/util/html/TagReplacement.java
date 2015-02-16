package org.futurepages.util.html;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static org.futurepages.util.The.*;

public class TagReplacement {

	Boolean reduce;
	String open;
	String close;
	String[] attributesToCare;

	TagReplacement(String open, String close, Boolean reduce) {
		this.open = open;
		this.close = close;
		this.reduce = reduce;
	}

	TagReplacement(String open, String close, Boolean reduce, String[] attributesToKeep) {
		this(open, close, reduce);
		this.attributesToCare = attributesToKeep;
	}

	String execute(String[] tagParts, boolean closing) {
		if (!closing) {
			return concat("<", this.open , careAttrs(tagParts[1]) , tagParts[2]);
		} else {
			return concat("</", this.close , tagParts[2]);
		}
	}

	String careAttrs(String tagSecondPart){
		if(attributesToCare==null){
			if(reduce){
				return "";
			}
		} else { //has attributes to care
			if(reduce){ //need to keep some attributes when reducing
				String attributes = "";

				for(String attr : attributesToCare){
					Matcher matcher = Pattern.compile(regexAttr(attr)).matcher(tagSecondPart);
					if(matcher.find()){
						String foundOne = tagSecondPart.substring(matcher.start(),matcher.end());
						attributes = concat(attributes,(foundOne.charAt(0)==' ')?"":" ",foundOne);
					}
				}
				tagSecondPart = attributes;
			}else{ //need to remove some attributes when keeping
				for(String attr : attributesToCare){
					tagSecondPart = tagSecondPart.replaceAll(regexAttrWithGroups(attr), "$1$2");
				}
			}
		}
		return tagSecondPart;
	}

	private String regexAttr(String attr){
		return HtmlRegex.attrPattern(attr);
	}
	private String regexAttrWithGroups(String attr){
		return HtmlRegex.attrPatternWithGroups(attr);
	}
}