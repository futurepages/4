package org.futurepages.util;
import org.futurepages.util.html.HtmlRegex;
import org.futurepages.util.iterator.string.IterableString;
import org.futurepages.util.iterator.string.MatchedToken;

import java.util.regex.Pattern;

public class EmojiUtil {

	private static Pattern EMOJI_CHAR_PATTERN;

	private static Pattern getEmojiCharPattern() {
		if(EMOJI_CHAR_PATTERN == null){
			EMOJI_CHAR_PATTERN = Pattern.compile("#<\\{\\d+\\}>");
		}
		return EMOJI_CHAR_PATTERN;
	}

	private static String decodeChar(String encodedEmoji) {
		int codePoint = Integer.parseInt(encodedEmoji.substring(3, encodedEmoji.length()-2));
		int numChars = Character.charCount(codePoint);

		if(numChars>1){
			char[] chars = Character.toChars(codePoint);
			StringBuilder sb = new StringBuilder();
			for(int i = 0; i<numChars; i++){
				sb.append(chars[i]);
			}
			return sb.toString();
		}else{
			return ((char) codePoint)+"";
		}
	}

	public static String encodeChar(String ch){
		int chCode = Character.codePointAt(ch,0);
		if(chCode > 255){
			return "#<{"+chCode+"}>";
		}else{
			return ch;
		}
	}
	public static String decodeAll(String txt){
		if(Is.empty(txt)){
			return txt;
		}
		StringBuilder sb = new StringBuilder();
		IterableString iter = new IterableString(getEmojiCharPattern(), txt);
		String end = txt;
		for (MatchedToken token : iter) {
			sb.append(token.getBefore());
			String encodedEmoji = token.getMatched();
			sb.append(decodeChar(encodedEmoji));
			end = token.getAfter();
		}
		sb.append(end);
		return sb.toString();
	}

	public static String encodeAll(String txt){
		if(Is.empty(txt)){
			return txt;
		}
		StringBuilder sb = new StringBuilder();
		IterableString iter = new IterableString(HtmlRegex.getAnyCharPattern(), txt);
		String end = txt;
		for (MatchedToken token : iter) {
			sb.append(token.getBefore());
			String emoji = token.getMatched();
			sb.append(encodeChar(emoji));
			end = token.getAfter();
		}
		sb.append(end);
		return sb.toString();
	}

	public static void main(String[] args) {
		String x = ("♟ ♾ ♂ ♀ ✈ a 😂 b 😍 c 🎉 d 👍 e>>>>:>>>>leão!@#$%¨¨¨ 👁🗣⛑🕶🕷🕸🕊🌪🌥🌦🌧⛈🌩⛸⛷🎖🏵🎗🎟🏎🏍🛩🛥🛳⛱🏖🏝🏜⛰🏔🏘🏚🏗⛩🛤🛣🏞🖥🖨🖱🖲🕹🗜📽🎞🎙🎚🎛⏱⏲🕯🗑🛢⚒🛠⛏⛓🗡🛡🕳🌡🛎🗝🛋🛏🖼 🛍🏷🗒 🗓🗃🗳🗄🗂🗞🖇🖊🖋🖌🖍⏸⏯⏹⏺⏭👁‍🗨♟♾");
		//♟ ♾ ♂ ♀ ✈ a 😂 b 😍 c 🎉 d 👍 e>>>>:>>>>leão!@#$%¨¨¨ 👁🗣⛑🕶🕷🕸🕊🌪🌥🌦🌧⛈🌩⛸⛷🎖🏵🎗🎟🏎🏍🛩🛥🛳⛱🏖🏝🏜⛰🏔🏘🏚🏗⛩🛤🛣🏞🖥🖨🖱🖲🕹🗜📽🎞🎙🎚🎛⏱⏲🕯🗑🛢⚒🛠⛏⛓🗡🛡🕳🌡🛎🗝🛋🛏🖼 🛍🏷🗒 🗓🗃🗳🗄🗂🗞🖇🖊🖋🖌🖍⏸⏯⏹⏺⏭👁‍🗨♟♾
		System.out.println(EmojiUtil.encodeAll(x));
		System.out.println(EmojiUtil.decodeAll(EmojiUtil.encodeAll(x)));
	}
}
