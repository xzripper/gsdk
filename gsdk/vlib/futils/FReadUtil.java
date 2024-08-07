package gsdk.vlib.futils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import java.io.IOException;

import java.util.ArrayList;

import static java.util.regex.Pattern.compile;

import static java.util.Arrays.copyOfRange;

import static java.util.Arrays.asList;

/**
 * Utility for reading files.
 */
public class FReadUtil {
    /**
     * Read all lines in file.
     * 
     * @param file File name.
     * @param filter Lines filter (regex).
     * @param filterFlags Lines filter flags (regex flags).
     */
    public static String[] readLines(String file, String filter, int filterFlags) {
        ArrayList<String> lines = new ArrayList<>();

        String line;

        try(BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)))) {
            while((line = reader.readLine()) != null) {
                if(filter == null || compile(filter, filterFlags).matcher(line).find()) {
                    lines.add(line);
                }
            }
        } catch(IOException ioExc) {
            return null;
        }

        return lines.toArray(new String[0]);
    }

    /**
     * Read all lines in file.
     * 
     * @param file File name.
     * @param filter Lines filter (regex).
     */
    public static String[] readLines(String file, String filter) {
        return readLines(file, filter, 0);
    }

    /**
     * Read all lines in file.
     * 
     * @param file File name.
     */
    public static String[] readLines(String file) {
        return readLines(file, null);
    }

    /**
     * Read all lines in file and get only specific range.
     * 
     * @param file File name.
     * @param start Range start.
     * @param end Range end.
     * @param filter Lines filter (regex).
     * @param filterFlags Lines filter flags (regex flags).
     */
    public static String[] readLinesRange(String file, int start, int end, String filter, int filterFlags) {
        return copyOfRange(readLines(file, filter, filterFlags), start, end);
    }

    /**
     * Read all lines in file and get only specific range.
     * 
     * @param file File name.
     * @param start Range start.
     * @param end Range end.
     * @param filter Lines filter (regex).
     */
    public static String[] readLinesRange(String file, int start, int end, String filter) {
        return readLinesRange(file, start, end, filter, 0);
    }

    /**
     * Read all lines in file and get only specific range.
     * 
     * @param file File name.
     * @param start Range start.
     * @param end Range end.
     */
    public static String[] readLinesRange(String file, int start, int end) {
        return readLinesRange(file, start - 1, end, null);
    }

    /**
     * Read all lines in file and get in one string.
     * 
     * @param file File name.
     * @param filter Lines filter (regex).
     * @param filterFlags Lines filter flags (regex flags).
     */
    public static String read(String file, String filter, int filterFlags) {
        return String.join("\n", readLines(file, filter, filterFlags));
    }

    /**
     * Read all lines in file and get in one string.
     * 
     * @param file File name.
     * @param filter Lines filter (regex).
     */
    public static String read(String file, String filter) {
        return read(file, filter, 0);
    }

    /**
     * Read all lines in file and get in one string.
     * 
     * @param file File name.
     */
    public static String read(String file) {
        return read(file, null);
    }

    /**
     * Get specific line from file.
     * 
     * @param file File name.
     * @param pos Line position.
     * @param filter Line filter (regex).
     * @param filterFlags Line filter flags (regex flags).
     */
    public static String line(String file, int pos, String filter, int filterFlags) {
        return readLines(file, filter, filterFlags)[pos];
    }

    /**
     * Get specific line from file.
     * 
     * @param file File name.
     * @param pos Line position.
     * @param filter Line filter (regex).
     */
    public static String line(String file, int pos, String filter) {
        return line(file, pos, filter, 0);
    }

    /**
     * Get specific line from file.
     * 
     * @param file File name.
     * @param pos Line position.
     */
    public static String line(String file, int pos) {
        return line(file, pos, null);
    }

    /**
     * Check if file contains specific line.
     * 
     * @param file File name.
     * @param line Line.
     */
    public static boolean has(String file, String line) {
        return asList(readLines(file)).contains(line);
    }
}
