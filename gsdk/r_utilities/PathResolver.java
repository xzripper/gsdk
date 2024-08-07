package gsdk.r_utilities;

import java.io.File;

public class PathResolver {
    public static String resolvePath(String path) {
        if(path.contains("\\") && pathSeparator().equals("/")) {
            path = path.replace("\\", "/");
        } else if(path.contains("/") && pathSeparator().equals("\\")) {
            path = path.replace("/", "\\");
        }

        File pFile = new File(path);

        if(!pFile.getAbsoluteFile().exists()) {
            return pFile.getAbsolutePath().replace(path, String.format("src%s%s", pathSeparator(), path));
        } else {
            return pFile.toString();
        }
    }

    public static String getLastPathFile(String path) {
        String[] lFile = path.split(String.format("\\%s", pathSeparator()));

        return lFile[lFile.length - 1];
    }

    public static String pathSeparator() {
        return File.separator;
    }
}
