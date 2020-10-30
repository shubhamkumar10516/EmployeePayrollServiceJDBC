package EmployeePayrollService;

import static java.nio.file.StandardWatchEventKinds.*;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.Map;

public class Java8WatchServiceExample {

	private final WatchService watcher;
	private final Map<WatchKey, Path> keys;
    public Java8WatchServiceExample(Path dir) throws IOException {
		this.watcher = FileSystems.getDefault().newWatchService();
		this.keys = new HashMap<WatchKey, Path>();
		walkAndRegisterDirectories(dir);
	}
    private void registerDirectory(Path dir) throws IOException {
		WatchKey key = dir.register(watcher, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);
		keys.put(key, dir);
	}

	private void walkAndRegisterDirectories(final Path start) throws IOException {
		Files.walkFileTree(start, new SimpleFileVisitor<Path>() {
			@Override
			public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
				registerDirectory(dir);
				return FileVisitResult.CONTINUE;
			}
		});
	}

	void processEvents() {
		while (true) {
			WatchKey key;
			try {
				key = watcher.take();
			} catch (InterruptedException x) {
				return;
			}

			Path dir = keys.get(key);
			if (dir == null) {
				System.err.println("WatchKey not recognized!!");
				continue;
			}
			for (WatchEvent<?> event : key.pollEvents()) {
				@SuppressWarnings("rawtypes")
				WatchEvent.Kind kind = event.kind();
				@SuppressWarnings("unchecked")
				Path name = ((WatchEvent<Path>) event).context();
				Path child = dir.resolve(name);
				System.out.format("%s: %s\n", event.kind().name(), child);
				if (kind == ENTRY_CREATE) {
					try {
						if (Files.isDirectory(child)) {
							walkAndRegisterDirectories(child);
						}
					} catch (IOException x) {
						// do something useful
					}
				}
			}
			boolean valid = key.reset();
			if (!valid) {
				keys.remove(key);
				if (keys.isEmpty()) {
					break;
				}
			}
		}
	}
}
