package org.humanized.tools.handlers;

import java.nio.file.Path;
import java.util.List;

public interface FileOperationHandler {

    /**
     * If set, list of file extensions (without leading '.') to handle. If empty or null, all files are handled
     *
     * @return list of file extensions to handle or null
     */
    List<String> getExtensionFilter();


    /**
     * Handle the given file action
     *
     * @param path path to the file
     */
    void processFile(final Path path);
}
