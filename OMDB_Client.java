/*
 * SDK for OMDB API
 */
package omdb_sdk;
import java.io.BufferedReader;
import java.net.Socket;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.UnknownHostException;
import org.json.JSONArray;
import org.json.JSONObject;
import java.net.HttpURLConnection;
/**
 *
 * @author kaleab
 */
public class OMDB_Client implements OMDBNotification{
    
    private String api_url = "http://www.omdbapi.com/?apikey=b5797cc3&s=NAME";
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private String movie_name;
    private int IO_Exceptions = 0;
    private JSONObject json_result = null;
    public static final int TYPE_SERIES = 1;
    public static final int TYPE_MOVIE = 0;
    
    public JSONObject search_movie(String mov_name) {
        try{
            // construct the url
            movie_name = mov_name;
            String full_url = api_url.replace("NAME", mov_name);
            
            URL url = new URL(full_url);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            
            int responsecode = conn.getResponseCode();
            //System.out.println(responsecode);
            if(responsecode==200){
                in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder json_string = new StringBuilder();
                
                String temp;
                while((temp=in.readLine())!=null){
                    json_string.append(temp);
                }
                in.close();
                conn.disconnect();
                //System.out.println(json_string.toString());
                return new JSONObject(json_string.toString());
                
            }else{
                notify("error code: "+responsecode+"\n");
            }
            
        }catch(UnknownHostException ex){
            notify("Unknown HOST Exception\n");
        }catch(IOException ex){
            search_movie(movie_name);
        }
        return null;
    }
    
    public boolean setType(int type){
        switch(type){
            case TYPE_MOVIE:
                api_url = api_url+"&type=movie";
                break;
            case TYPE_SERIES:
                api_url = api_url+"&type=series";
                break;
            default:
                return false;
        }
        return true;
    }
    
}
