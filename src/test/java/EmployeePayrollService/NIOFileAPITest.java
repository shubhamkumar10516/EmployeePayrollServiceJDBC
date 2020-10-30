package EmployeePayrollService;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.IntStream;
import org.junit.Test;

public class NIOFileAPITest {
	private static String HOME = System.getProperty("user.home");
	private static String PLAY_WITH_NIO = "TempPlayGround";

	@Test
	public void checkPathTest() throws IOException {
		Path homePath = Paths.get(HOME);
		assertTrue(Files.exists(homePath));
		//Delete File
		Path playPath = Paths.get(HOME+"/"+PLAY_WITH_NIO);
		if(Files.exists(playPath))
			FileUtils.deleteFiles(playPath.toFile());
		assertTrue(Files.notExists(playPath));
		// Create Directory
		Files.createDirectory(playPath);
		assertTrue(Files.exists(playPath));
		// Create File
		IntStream.range(1,10).forEach(cntr ->{
			Path tempFile = Paths.get(playPath+"/temp"+cntr);
			assertTrue(Files.notExists(tempFile));
			try {
				Files.createFile(tempFile);
			}catch(IOException e) {
				assertTrue(Files.exists(tempFile));
			}
		});
		// List Files, Directories
		Files.list(playPath).filter(Files::isRegularFile).forEach(System.out::println);
		Files.newDirectoryStream(playPath).forEach(System.out::println);
	}
	
	@Test
	public void testWhenWatchListAllActivities() throws IOException {
		Path playPath = Paths.get(HOME+"/"+PLAY_WITH_NIO);
		Files.list(playPath).filter(Files:: isRegularFile).forEach(System.out::println);
		new Java8WatchServiceExample(playPath).processEvents();
	}
	
	
}
