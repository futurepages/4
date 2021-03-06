package org.futurepages.jersey.filters;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.io.IOUtils;
import org.futurepages.core.config.Apps;
import org.futurepages.core.exception.AppLogger;
import org.futurepages.util.gson.GsonWithCalendarDateTime;
import org.futurepages.util.gson.GsonWithDecimals;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

@Provider
@Consumes({MediaType.APPLICATION_JSON, "text/json"})
@Produces({MediaType.APPLICATION_JSON, "text/json"})
public class JsonWithGsonMapper implements MessageBodyReader<Object>, MessageBodyWriter<Object> {
  private static final String UTF_8 = "UTF-8";

	public static final Gson GSON = buildGsonAdapter(false);
	public static final Gson GSON_DEF_FLOAT = buildGsonAdapter(true);

	private static Gson buildGsonAdapter(boolean defaultFloat) {
		if(Apps.isTrue("NEW_JERSEY_MODE")){
			GsonBuilder gb = new GsonBuilder();
			GsonWithCalendarDateTime.registerFor(gb);
			GsonWithDecimals.registerFor(gb,defaultFloat);
			if(Apps.devMode()){
				gb.setPrettyPrinting();
			}
			return gb.create();
		}
		return new Gson();
	}

    @Override
    public boolean isReadable(Class<?> type, Type genericType,
            java.lang.annotation.Annotation[] annotations, MediaType mediaType) {
        return true;
    }

    @Override
    public Object readFrom(Class<Object> type, Type genericType,
            Annotation[] annotations, MediaType mediaType,
            MultivaluedMap<String, String> httpHeaders, InputStream entityStream)
            throws IOException {
	        InputStreamReader streamReader = new InputStreamReader(entityStream, UTF_8);
	    String json = null;
	    try {
		    json = IOUtils.toString(streamReader);
		    if(json.startsWith("defFloat:{")){
		    	json = json.replaceFirst("^defFloat\\:\\{","{"); // notação utilizada para converter floats a partir do formato numerico com ponto para decimais.
			    return GSON_DEF_FLOAT.fromJson(json, genericType);
		    }else{
			    return GSON.fromJson(json, genericType);
		    }
	    } catch (com.google.gson.JsonSyntaxException e) {
		    AppLogger.getInstance().silent(e,json);
	    } finally {
		    streamReader.close();
	    }
	    return null;
    }

    @Override
    public boolean isWriteable(Class<?> type, Type genericType,
            Annotation[] annotations, MediaType mediaType) {
        return true;
    }

    @Override
    public long getSize(Object object, Class<?> type, Type genericType,
            Annotation[] annotations, MediaType mediaType) {
        return -1;
    }

    @Override
    public void writeTo(Object object, Class<?> type, Type genericType,
                        Annotation[] annotations, MediaType mediaType,
                        MultivaluedMap<String, Object> httpHeaders,
                        OutputStream entityStream) throws IOException, WebApplicationException {

	    OutputStreamWriter writer = new OutputStreamWriter(entityStream, UTF_8);
		try {
		    GSON.toJson(object, genericType, writer);
	    } finally {
		    writer.close();
	    }
    }
}