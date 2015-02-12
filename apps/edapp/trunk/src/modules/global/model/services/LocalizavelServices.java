package modules.global.model.services;

import org.futurepages.util.Is;

public class LocalizavelServices {

	/*
	 * Verifica se o inicío da URL do google maps é válida
	 *   - No caso do google em inglês ".com"
	 *   - Ou do google em português ".com.br"
	 */
	// TODO: pensar em um nome melhor para este método
	public static boolean ehInicioValidoGoogleMapsUrl(String googleMapsURL) {

		String mapsURL = googleMapsURL.toLowerCase();

		if ((mapsURL.startsWith("http://maps.google.com/") && mapsURL.length() > "http://maps.google.com/".length())
				|| (mapsURL.startsWith("http://maps.google.com.br/") && mapsURL.length() >  "http://maps.google.com.br/".length()  ) ) {
			return true;
		}
		return false;
	}

	/**
	 * Se é vazia, altera para altera a url  para null,
	 * senão valida url do google mapGrouped passada e seta ela 
	 */
	public static String corrigeUrlGoogleMapsDoLocal(String googleMapUrl) {

		if (Is.empty(googleMapUrl)) {
			return null;
		} else {
			if (googleMapUrl.startsWith("<iframe")) {
				int inicio = googleMapUrl.indexOf("src=\"") + 5;
				int fim = googleMapUrl.indexOf("&amp;output=embed\"></iframe>");

				if (inicio == -1 || fim == -1) {
					return "http://maps.google.com/";
				}
				return googleMapUrl.substring(inicio, fim);

			} else {
				if(googleMapUrl.startsWith("https://")){
					googleMapUrl = googleMapUrl.replace("https://", "http://");
				}
				if (googleMapUrl.startsWith("http://") && !googleMapUrl.contains("&amp;")) {
					return googleMapUrl.replaceAll("&", "&amp;");
				} else {
					return googleMapUrl;
				}
			}
		}
	}
}
