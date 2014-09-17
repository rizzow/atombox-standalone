package org.humanized.tools.helpers;

public class FileUtils {

    /**
     * Remove an extension from a filename, if present
     *
     * @param filename name of file to process
     * @return filename without extension
     */
    public static String removeExtension(final String filename) {

        final String separator = System.getProperty("file.separator");
        String name;

        int lastSeparatorIndex = filename.lastIndexOf(separator);
        if (lastSeparatorIndex == -1) {
            name = filename;
        } else {
            name = filename.substring(lastSeparatorIndex + 1);
        }

        int extensionIndex = name.lastIndexOf(".");
        if (extensionIndex == -1) {
            return name;
        }

        return name.substring(0, extensionIndex);
    }
}
