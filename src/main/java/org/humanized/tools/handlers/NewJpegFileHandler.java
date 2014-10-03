package org.humanized.tools.handlers;

import org.humanized.tools.importers.JpegImporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class NewJpegFileHandler implements FileOperationHandler {

    private static final Logger mLogger = LoggerFactory.getLogger(NewJpegFileHandler.class);
    private final Path mBasePath;
    private final JpegImporter mJpegImporter = new JpegImporter();


    public NewJpegFileHandler(final Path basePath) {
        mBasePath = basePath;
    }


    @Override
    public List<String> getExtensionFilter() {
        final List<String> extensions = new ArrayList<>();
        extensions.add("jpg");
        extensions.add("jpeg");
        extensions.add("JPG");
        extensions.add("JPEG");
        return extensions;
    }


    @Override
    public void processFile(Path path) {

        File basePath = new File(mBasePath.toString());
        File file = new File(basePath, path.toString());

        if (!file.exists()) {
            throw new IllegalStateException("Creation event for non-existing file" + file.toString());
        }

        mJpegImporter.handleNewJpeg(file);
    }
}
