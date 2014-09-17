package org.humanized.tools.handlers;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PathEventProcessor {

    public enum PathEvent {
        EVENT_CREATE, EVENT_MODIFY, EVENT_DELETE
    }

    private final Map<PathEvent, List<FileOperationHandler>> mHandlerMap = new HashMap<PathEvent, List<FileOperationHandler>>();


    public PathEventProcessor() {
        mHandlerMap.put(PathEvent.EVENT_CREATE, new ArrayList<FileOperationHandler>());
        mHandlerMap.put(PathEvent.EVENT_MODIFY, new ArrayList<FileOperationHandler>());
        mHandlerMap.put(PathEvent.EVENT_DELETE, new ArrayList<FileOperationHandler>());
    }


    public void addHandler(final PathEvent event, final FileOperationHandler handler) {
        mHandlerMap.get(event).add(handler);
    }


    public void handleFile(final PathEvent event, final Path path) {
        List<FileOperationHandler> handlers = mHandlerMap.get(event);
        assert handlers != null;

        for (FileOperationHandler handler : handlers) {
            if (shouldProcessPath(path, handler)) {
                handler.processFile(path);
            }
        }
    }


    private boolean shouldProcessPath(Path path, FileOperationHandler handler) {
        boolean shouldHandle = true;

        final List<String> extensions = handler.getExtensionFilter();
        if (extensions != null && extensions.size() > 0) {
            shouldHandle = false;
            for (String ext : extensions) {
                if (path.toString().endsWith('.' + ext)) {
                    shouldHandle = true;
                    break;
                }
            }
        }

        return shouldHandle;
    }
}
