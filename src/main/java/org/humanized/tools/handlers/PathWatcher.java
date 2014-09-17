package org.humanized.tools.handlers;

import com.sun.nio.file.SensitivityWatchEventModifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

import static java.nio.file.StandardWatchEventKinds.*;

public class PathWatcher {

    private static final Logger mLogger = LoggerFactory.getLogger(PathWatcher.class);

    private PathEventProcessor mFileProcessor = new PathEventProcessor();
    private List<Thread> mThreadList = new ArrayList<Thread>();


    public void addWatch(final String directoryToWatch) throws IOException, InterruptedException {

        final Path path = Paths.get(directoryToWatch);
        if (path == null) {
            throw new IllegalArgumentException("Directory " + directoryToWatch + " not found");
        }

        // Handlers
        mFileProcessor.addHandler(PathEventProcessor.PathEvent.EVENT_CREATE, new NewJpegFileHandler(path));

        final WatchService watcher = path.getFileSystem().newWatchService();
        final QueueReader queueReader = new QueueReader(watcher);
        final Thread thread = new Thread(queueReader, "FileWatcher" + (int) (Math.random() * 10000));

        // Register operations
        path.register(watcher, new WatchEvent.Kind[]{StandardWatchEventKinds.ENTRY_CREATE}, SensitivityWatchEventModifier.HIGH);
        mThreadList.add(thread);
    }


    public void startWatching() throws InterruptedException {
        mLogger.debug("Starting watchers...");
        for (final Thread thread : mThreadList) {
            thread.start();
        }

        mLogger.debug("Ready!");
        for (final Thread thread : mThreadList) {
            thread.join();
        }
    }


    private class QueueReader implements Runnable {

        private final WatchService mWatchService;


        public QueueReader(WatchService watchService) {
            mWatchService = watchService;
        }


        @Override
        public void run() {
            try {
                WatchKey key = mWatchService.take();
                mLogger.debug("Watch active");

                while (key != null) {
                    for (final WatchEvent event : key.pollEvents()) {
                        final Path path = (Path) event.context();

                        mLogger.trace("EVENT {} for {}", event.kind(), event.context());

                        if (event.kind() == ENTRY_CREATE) {
                            mFileProcessor.handleFile(PathEventProcessor.PathEvent.EVENT_CREATE, path);
                        } else if (event.kind() == ENTRY_DELETE) {
                            mFileProcessor.handleFile(PathEventProcessor.PathEvent.EVENT_DELETE, path);
                        } else if (event.kind() == ENTRY_MODIFY) {
                            mFileProcessor.handleFile(PathEventProcessor.PathEvent.EVENT_MODIFY, path);
                        }
                    }

                    // if the watched directed gets deleted, get out of run method
                    if (!key.reset()) {
                        mLogger.debug("Watcher no longer valid");
                        key.cancel();
                        mWatchService.close();
                        break;
                    }
                    key = mWatchService.take();
                }
            } catch (InterruptedException e) {
                mLogger.error("Interrupted: {}", e.getMessage());
            } catch (IOException e) {
                mLogger.error("I/O Exception: {}", e.getMessage());
            }
            mLogger.info("Stopping thread");
        }
    }
}
