package org.futurepages.menta.util;

import org.futurepages.core.exception.AppLogger;
import org.futurepages.menta.core.control.Controller;

import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Thiago Rabelo
 */
public class BrowserDetection {
	
	private static final Pattern browserDetect;
	private static final Pattern engineDetect;
	private static final Pattern mobileDetect1;
	private static final Pattern mobileDetect2;
	private static final Pattern deviceDetect;

	static {
		browserDetect = Pattern.compile("(?i)(chrome|crios)|chromium|firefox|opera|safari|rekonq|epiphany|msie");
		engineDetect = Pattern.compile("(?i)applewebKit|gecko|presto|trident");

		mobileDetect1 = Pattern.compile("(?i)android.+mobile|avantgo|bada\\/|blackberry|blazer|compal|elaine|fennec|hiptop|iemobile|ip(hone|od)|iris|kindle|lge |maemo|midp|mmp|netfront|opera m(ob|in)i|palm( os)?|phone|p(ixi|re)\\/|plucker|pocket|psp|symbian|treo|up\\.(browser|link)|vodafone|wap|windows (ce|phone)|xda|xiino");
		mobileDetect2 = Pattern.compile("(?i)1207|6310|6590|3gso|4thp|50[1-6]i|770s|802s|a wa|abac|ac(er|oo|s\\-)|ai(ko|rn)|al(av|ca|co)|amoi|an(ex|ny|yw)|aptu|ar(ch|go)|as(te|us)|attw|au(di|\\-m|r |s )|avan|be(ck|ll|nq)|bi(lb|rd)|bl(ac|az)|br(e|v)w|bumb|bw\\-(n|u)|c55\\/|capi|ccwa|cdm\\-|cell|chtm|cldc|cmd\\-|co(mp|nd)|craw|da(it|ll|ng)|dbte|dc\\-s|devi|dica|dmob|do(c|p)o|ds(12|\\-d)|el(49|ai)|em(l2|ul)|er(ic|k0)|esl8|ez([4-7]0|os|wa|ze)|fetc|fly(\\-|_)|g1 u|g560|gene|gf\\-5|g\\-mo|go(\\.w|od)|gr(ad|un)|haie|hcit|hd\\-(m|p|t)|hei\\-|hi(pt|ta)|hp( i|ip)|hs\\-c|ht(c(\\-| |_|a|g|p|s|t)|tp)|hu(aw|tc)|i\\-(20|go|ma)|i230|iac( |\\-|\\/)|ibro|idea|ig01|ikom|im1k|inno|ipaq|iris|ja(t|v)a|jbro|jemu|jigs|kddi|keji|kgt( |\\/)|klon|kpt |kwc\\-|kyo(c|k)|le(no|xi)|lg( g|\\/(k|l|u)|50|54|e\\-|e\\/|\\-[a-w])|libw|lynx|m1\\-w|m3ga|m50\\/|ma(te|ui|xo)|mc(01|21|ca)|m\\-cr|me(di|rc|ri)|mi(o8|oa|ts)|mmef|mo(01|02|bi|de|do|t(\\-| |o|v)|zz)|mt(50|p1|v )|mwbp|mywa|n10[0-2]|n20[2-3]|n30(0|2)|n50(0|2|5)|n7(0(0|1)|10)|ne((c|m)\\-|on|tf|wf|wg|wt)|nok(6|i)|nzph|o2im|op(ti|wv)|oran|owg1|p800|pan(a|d|t)|pdxg|pg(13|\\-([1-8]|c))|phil|pire|pl(ay|uc)|pn\\-2|po(ck|rt|se)|prox|psio|pt\\-g|qa\\-a|qc(07|12|21|32|60|\\-[2-7]|i\\-)|qtek|r380|r600|raks|rim9|ro(ve|zo)|s55\\/|sa(ge|ma|mm|ms|ny|va)|sc(01|h\\-|oo|p\\-)|sdk\\/|se(c(\\-|0|1)|47|mc|nd|ri)|sgh\\-|shar|sie(\\-|m)|sk\\-0|sl(45|id)|sm(al|ar|b3|it|t5)|so(ft|ny)|sp(01|h\\-|v\\-|v )|sy(01|mb)|t2(18|50)|t6(00|10|18)|ta(gt|lk)|tcl\\-|tdg\\-|tel(i|m)|tim\\-|t\\-mo|to(pl|sh)|ts(70|m\\-|m3|m5)|tx\\-9|up(\\.b|g1|si)|utst|v400|v750|veri|vi(rg|te)|vk(40|5[0-3]|\\-v)|vm40|voda|vulc|vx(52|53|60|61|70|80|81|83|85|98)|w3c(\\-| )|webc|whit|wi(g |nc|nw)|wmlb|wonu|x700|xda(\\-|2|g)|yas\\-|your|zeto|zte\\-");

		deviceDetect = Pattern.compile("(?i)android|webos|iphone|ipad|ipod|blackberry|iemobile|opera mini");
	}
	
	private String userAgent;
	
	private String browser = null;
	//private String version;
	private String engine  = null;
	private Boolean mobile  = null;
	private Boolean device = null;
	
	public BrowserDetection(String userAgent) {
		this.userAgent = userAgent;
	}

	/**
	 * Só vale para quem está usando Chain com Actions do Futurepages ativamente
	 */
	public static BrowserDetection getInstance(HttpServletRequest req) {
			BrowserDetection bd = null;
			bd = (BrowserDetection) req.getSession().getAttribute("_browserDetectionInstance");
			if(bd==null){
				bd = new BrowserDetection(req.getHeader("user-agent"));
				req.getSession().setAttribute("_browserDetectionInstance", bd);
			}
			return bd;
	}

	/**
	 * Só vale para quem está usando Chain com Actions do Futurepages ativamente
	 */
	public static BrowserDetection getInstance(){
		Controller controller = Controller.getInstance();
		if(controller !=null && controller.getChain()!=null){
			return getInstance(controller.getChain().getAction().getRequest());
		}else{
			AppLogger.getInstance().execute(new IllegalStateException("Browser couldn't be detected using getInstance() without a chain. Use getIntance(request)!"));
			return new BrowserDetection(""); //just to not break the page
		}
	}
	
	protected void detectBrowser() {
		Matcher bd = browserDetect.matcher(userAgent);
		HashSet<String> bdMatches = new HashSet<String>();

		while (bd.find()) {
			bdMatches.add(bd.group());
		}

		if (bdMatches.size() > 0) {
			if (bdMatches.contains("Firefox")) {
				browser = "firefox";
			} else if (bdMatches.contains("Opera")) {
				browser = "Opera";
			} else if (bdMatches.contains("MSIE")) {
				browser = "ie";
			} else if (bdMatches.contains("Safari")) {
				if ((bdMatches.contains("Chrome") || bdMatches.contains("CriOS")) && !bdMatches.contains("Epiphany")) {
					browser = "chrome";
				} else if (bdMatches.contains("Chromium") && !bdMatches.contains("Epiphany")) {
					browser = "chromium";
				} else if (bdMatches.contains("rekonq")) {
					browser = "rekonq";
				} else if (bdMatches.contains("Epiphany")) {
						browser = "epiphany";
				} else {
					browser = "safari";					
				}
			} else {
				browser = "unknow_browser";
			}
		} else {
			browser = "unknow_browser";
		}
	}

	protected void detectEngine() {
		Matcher ed = engineDetect.matcher(userAgent);
		HashSet<String> edMatches = new HashSet<String>();
		
		while (ed.find()) {
			edMatches.add(ed.group());
		}
	
		if (edMatches.size() > 0) {
			if (edMatches.contains("AppleWebKit")) {
				engine = "webkit";
			} else if (edMatches.contains("Gecko") && !edMatches.contains("AppleWebKit")) {
				engine = "gecko";
			} else if (edMatches.contains("Presto")) {
				engine = "presto";
			} else if (edMatches.contains("Trident")) {
				engine = "trident";
			} else {
				engine = "unknow_engine";
			}
		} else {
			engine = "unknow_engine";
		}
	}

	protected void detectMobile() {
		mobile = mobileDetect1.matcher(this.userAgent).find() || mobileDetect2.matcher(this.userAgent.substring(0, 4)).find();
	}
	
	protected void detectDevice() {
		device = deviceDetect.matcher(this.userAgent).find();
	}
	
	public String getBrowser() {
		if (browser == null) {
			detectBrowser();
		}

		return browser;
	}
	
	public String getEngine() {
		if (engine == null) {
			detectEngine();
		}

		return engine;
	}
	
	public boolean getMobile() {
		if (mobile == null) {
			detectMobile();
		}
		return mobile.booleanValue();
	}
	
	public boolean getDevice() {
		if (device == null) {
			detectDevice();
		}
		
		System.out.println(device);

		return device.booleanValue();
	}
}
