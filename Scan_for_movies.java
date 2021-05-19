package omdb_sdk;
import java.io.File;
import java.io.FileFilter;
public class Scan_for_movies {
    private static char badchars[]={';',':','-','_','\\','/','@','#','(',')'};
    private String base_dir;
    private boolean include_folders = true;
    private String supported_ext[] = {"mkv","mp4","avi","3gp"};
    public void setBase_dir(String base_dir) {
        this.base_dir = base_dir;
    }
    public File[] scandir(String path){
        File dir = new File(path);
        if(!dir.isDirectory()) return null;
        base_dir = dir.getAbsolutePath();
        FileFilter known_extension = new FileFilter(){
            @Override
            public boolean accept(File file) {
                String filename = file.getName();
                if(filename.endsWith(supported_ext[0])&& 
                        filename.endsWith(supported_ext[1])&&
                        filename.endsWith(supported_ext[2])&&
                        filename.endsWith(supported_ext[3]))return true;
                if(file.isDirectory()&&include_folders) return true;
                return false;
            }
            
        };
        File files[] = dir.listFiles(known_extension);
        return files;
    }
    public String remove_unwanted_chars(String file_name){
        String sanitized;
        String name = file_name;
        String[] name_no_2 = name.split("2");
        String[] name_no_3 = name_no_2[0].split("1");
        String badcharremoved = "";
        for (int j = 0; j < badchars.length; j++) {
            badcharremoved = name_no_3[0].replace(badchars[j], ' ');
        }
        badcharremoved = badcharremoved.trim();
        badcharremoved = badcharremoved.replace(' ', '_');
        badcharremoved = badcharremoved.replace('.', ' ');
        String removed[] = badcharremoved.split(" ");
        sanitized = removed[0].trim();
        return sanitized;
    }
    
    public boolean checkExtension(String file_name){
        
        for(String Ext:supported_ext){
            int index = file_name.lastIndexOf(".");
            if(index>0){
                String ext = file_name.substring(index+1);
                if(ext.toLowerCase().contains(Ext))return true;
            }
        }
        // check if the path is a directory
        if(include_folders){ // default=true
            if(base_dir!=null){
                String full_path = base_dir+file_name;
                File file = new File(full_path);
                return file.isDirectory();
            }
        }
        return false;
    }
}
