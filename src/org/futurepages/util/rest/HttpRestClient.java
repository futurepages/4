package org.futurepages.util.rest;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.futurepages.core.exception.AppLogger;
import org.futurepages.util.EncodingUtil;
import org.futurepages.util.ReflectionUtil;
import org.futurepages.util.rest.auth.HttpAuthentication;
import org.hibernate.cfg.NotYetImplementedException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

public abstract class HttpRestClient {

    protected HttpAuthentication authentication;

    private Gson gsonRestClient;

    public Gson getGson(){
        if(gsonRestClient == null){
            gsonRestClient = new Gson();
        }
        return gsonRestClient;
    }

    protected void setGson(Gson gson){
        this.gsonRestClient = gson;
    }

    protected String encodeUrl(String url){
        return EncodingUtil.encodeUrl(url);
    }

    public abstract String getEndpoint();


    public <T> T post(final String path, final Object object, final Class<T> type) {
        return doRequest("POST", path, object, type, "application/json");
    }


    public <T> T postWithFile(final String path, final Object object, final Class<T> type) {
        return doRequest("POST+FILE", path, object, type, "application/json");
    }

    public <T> T post(final String path, final Object object, final Class<T> type, String contentType) {
        return doRequest("POST", path, object, type, contentType);
    }

    public <T> T put(final String path, final Object object, final Class<T> type) {
        return doRequest("PUT", path, object, type, "application/json");
    }

    public <T> T get(String path, Class<T> type) {
        return doRequest("GET", path, null, type, "application/json");
    }

    public <T> T get(String path, Object obj, Class<T> type) {
        return doRequest("GET", path, obj, type, "application/json");
    }


    public <T> T delete(String path, Class<T> type) {
        return doRequest("DELETE", path, null, type, "application/json");
    }

    private <T> T doRequest(String method, final String path, final Object object, final Class<T> type, final String contentType) {
        StringBuilder responseBody = new StringBuilder();
        int responseCode = 0;
        try {
            URL url = new URL(getEndpoint() + path);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setUseCaches(false); // será se precisa mesmo?
            conn.setRequestProperty("User-Agent", "fpg/http-cli");
            conn.setRequestProperty("Cache-Control", "no-cache");
            if (authentication != null) {
                authentication.authenticate(conn);
            }
            if(method.equals("POST+FILE") && object!=null){
                if(object instanceof String){
                    // TODO: not implemented
                    throw new NotYetImplementedException("Object must not be json string when posting with file. It's Not implemented yet!");
                }

                String boundary =  "*****"+System.currentTimeMillis()+"*****",
                       crlf = "\r\n" ,
                       twoHyphens = "--"
                ;

                conn.setRequestMethod("POST");
                conn.setRequestProperty("Cache-Control","no-cache");
                conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);

                conn.setDoInput(true);    // será se precisa mesmo?
                conn.setDoOutput(true);

                OutputStream outputStream = conn.getOutputStream();
                PrintWriter writer = new PrintWriter(new OutputStreamWriter(outputStream, "UTF-8"),true);

                List<Field> fields = ReflectionUtil.getAllFields(object);
                String valor;
                File uploadFile;
                int i = 0;
                for(Field field : fields){
                    if(field.get(object)!=null){
                        if(field.getType()==File.class){
                            uploadFile = (File) field.get(object);
                            String fileName = uploadFile.getName();
                            writer.append(twoHyphens).append(boundary).append(crlf)
                                  .append("Content-Disposition: form-data; name=\"").append(field.getName())
                                 .append("\";filename=\"").append( fileName ).append("\"")
                                  .append(crlf)
                                  .append("Content-Type: ").append(URLConnection.guessContentTypeFromName(fileName))
                                  .append(crlf)
                                  .append(crlf);
                            writer.flush();

                            FileInputStream inputStream = new FileInputStream(uploadFile);
                            byte[] buffer = new byte[4096];
                            int bytesRead;
                            while ((bytesRead = inputStream.read(buffer)) != -1) {
                                outputStream.write(buffer, 0, bytesRead);
                            }
                            outputStream.flush();
                            inputStream.close();
                            writer.append(crlf);
                            writer.flush();
                        }else{
                            valor = String.valueOf(field.get(object));
                            writer.append(twoHyphens).append(boundary).append(crlf)
                                  .append("Content-Disposition: form-data; name=\"").append(field.getName()).append("\"")
                                  .append(crlf)
                                  .append(crlf)
                                  .append(valor);
                            writer.flush();
                        }
                    }
                    i++;
                    if(i<fields.size()){
                          writer.append(crlf);
                          writer.flush();
                    }
                }
                writer.append(crlf);
                writer.append(twoHyphens).append(boundary).append(twoHyphens).append(crlf).flush();
                writer.close();
            }else{
                conn.setRequestProperty("Content-type", contentType);
                conn.setRequestMethod(method.equals("POST+FILE")?"POST":method);
                if (object != null) {
                    conn.setDoOutput(true);
                    String body = getBody(object, contentType);

                    DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(wr, "UTF-8"));
                    writer.write(body);
                    writer.close();
                    wr.flush();
                    wr.close();
                }
            }
            responseCode = conn.getResponseCode();

            responseBody = readBody(conn.getResponseCode()==200? conn.getInputStream():conn.getErrorStream());
            if(type != String.class){
                return getGson().fromJson(responseBody.toString(), type);
            }else{
	            //noinspection SingleStatementInBlock,unchecked
	            return (T) responseBody.toString();
            }
        }catch (Exception ex){
            if(responseCode!=502 && responseCode!=503 && responseCode!=204){
                AppLogger.getInstance().execute(ex,
                                                (getEndpoint()+path),
                                                String.valueOf(responseCode),
                                                responseBody.toString(),
                                                authentication!=null?authentication.getToken():"[null]"
                );
            }
        }
        return null;
    }

    @SuppressWarnings("Duplicates")
    private StringBuilder readBody(final InputStream inputStream) throws IOException {
        StringBuilder body = new StringBuilder();
        BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            body.append(inputLine);
        }
        in.close();
        return body;
    }

    private String getBody(Object object, String contentType) {
        String data = object instanceof  String ?  (String) object   :   getGson().toJson(object);

        if (contentType.equals("application/x-www-form-urlencoded")) {
            return jsonToUrlEncodedString((JsonObject) new JsonParser().parse(data),"");
        }
        return data;
    }

    @SuppressWarnings("Duplicates")
    private static String jsonToUrlEncodedString(JsonObject jsonObject, String prefix) {
        StringBuilder url = new StringBuilder("");
        Boolean firstEntry = true;

        try {
            for (Map.Entry<String, JsonElement> item : jsonObject.entrySet()) {
                if (!firstEntry) {
                    url.append("&");
                }

                if (item.getValue() != null && item.getValue().isJsonObject()) {
                    url.append(jsonToUrlEncodedString(
                            item.getValue().getAsJsonObject(),
                            prefix.isEmpty() ? item.getKey() : prefix + "[" + item.getKey() + "]"
                            )
                    );
                } else {
                    url.append(prefix.isEmpty() ?
                            item.getKey() + "=" + URLEncoder.encode(item.getValue().getAsString(), "UTF-8") :
                            prefix + "[" + item.getKey() + "]=" + URLEncoder.encode(item.getValue().getAsString(), "UTF-8")
                    );
                }

                firstEntry = false;
            }

        } catch(UnsupportedEncodingException e) {
            AppLogger.getInstance().execute(e);
        }
        return url.toString();
    }



    public String postWithFormURLEncoded(String path, String formUrlEncoded) {
        StringBuilder responseBody = new StringBuilder();
        int responseCode = 0;
        try {
            URL url = new URL(getEndpoint() + path);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setUseCaches(false); // será se precisa mesmo?
            conn.setRequestProperty("User-Agent", "fpg/http-cli");
            conn.setRequestProperty("Cache-Control", "no-cache");
            if (authentication != null) {
                authentication.authenticate(conn);
            }

            conn.setRequestProperty("Content-type", "application/x-www-form-urlencoded");
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            String body = formUrlEncoded;

            DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(wr, "UTF-8"));
            writer.write(body);
            writer.close();
            wr.flush();
            wr.close();
            responseCode = conn.getResponseCode();

            responseBody = readBody(conn.getResponseCode()==200? conn.getInputStream():conn.getErrorStream());
            return responseBody.toString();
        }catch (Exception ex){
            if(responseCode!=502 && responseCode!=503){
                AppLogger.getInstance().execute(ex,
                        (getEndpoint()+path),
                        String.valueOf(responseCode),
                        responseBody.toString(),
                        authentication!=null?authentication.getToken():"[null]"
                );
            }
        }
        return null;
    }
}