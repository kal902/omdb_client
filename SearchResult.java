package omdb_sdk;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONObject;import java.io.DataInputStream;
import java.io.File;
import java.util.Random;
/**
 *
 * @author dagim
 */
public class SearchResult implements OMDBNotification{
    private JSONObject json_result;
    private String download_dir;
    private int max_download=1;
    
    public void clean(){
        download_dir=null;
        max_download = 1;
        json_result=null;
    }
    public void setMax_download(int max_download) {
        this.max_download = max_download;
    }
    public void setJson_result(JSONObject json_result) {
        this.json_result = json_result;
    }
    public void setDownload_dir(String download_dir) {
        this.download_dir = download_dir;
    }
    
    public SearchResult(){
        json_result=null;
    }
    public SearchResult(JSONObject json_obj){
        json_result = json_obj;
    }
    
    // $return value : total number of successfull downloads
    public int download_images(){
        String image_links[][] = get_images_link(); // title and url
        if(image_links==null)return 0;
        FileOutputStream fout;
        URL url;
        HttpURLConnection conn;
        int count = 0;
        Random rand = new Random();
        int total_download=image_links.length;
        for(String link[]:image_links){
            String Title = link[0];
            String img_url = link[1];
            try {
                if(img_url==null)break;
                String filename = download_dir+Title+rand.nextInt(1000)+".jpg";
                System.out.println(img_url+"\n");
                System.out.println(filename+"\n");
                BufferedInputStream in = new BufferedInputStream(new URL(img_url).openStream());
                fout = new FileOutputStream(filename);
                byte dataBuffer[] = new byte[1024];
                int bytesRead;
                while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                    fout.write(dataBuffer, 0, bytesRead);
                }
                fout.flush();
                fout.close();
                in.close();
                count++;
                notify(count+"\\"+total_download+"\n");
                if(count>=max_download)return count;
                /* or#  import org.apache.commons.io.FileUtils;
                FileUtils.copyURLToFile(
                new URL(FILE_URL), 
                new File(FILE_NAME), 
                CONNECT_TIMEOUT, 
                READ_TIMEOUT);*/
            } catch (MalformedURLException ex) {
                notify("malformed image url\n");
            } catch (IOException ex) {
                notify("IOException \n");
            }
        }
        return count;
    }
    
    public String[][] get_images_link(){
        int totalResults = get_total_results();
        if(totalResults!=0){
            JSONArray search_result = json_result.getJSONArray("Search");//get value of "Search"
            String links[][] = new String[totalResults][2];int i=0;
            while(i<totalResults){
                try{
                    JSONObject result = (JSONObject)search_result.get(i);
                    links[i][1] = result.getString("Poster");
                    links[i][0] = result.getString("Title");
                    i++;
                }catch(org.json.JSONException ex){break;}
            }
            return links;
        }
        return null;
    }
    
    public int get_total_results(){
        int count=0;
        if(json_result!=null){
            try{
            count= json_result.getInt("totalResults");
        }catch(org.json.JSONException ex){return 0;}}
        return count;
    }
}
