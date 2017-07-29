package ua.com.myjava.dbasync;

import static java.util.stream.Collectors.toList;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class RandomIsinService {
	private List<String> isins;
	private Iterator<String> iterator;
	private static final String FILE_NAME = "isins.txt";

	public RandomIsinService() {
		loadIsins();
		if (isins.isEmpty()) {
			throw new IllegalStateException("No isins present in isins.txt");
		}
		iterator = isins.listIterator();
	}

	private void loadIsins() {
		try (Stream<String> stream = Files.lines(Paths.get(FILE_NAME))) {
			isins = stream.collect(toList());
		}
		catch (IOException e) {
			log.error("Failed to load isins", e);
		}
	}

	public String getRandomIsin() {
		if (!iterator.hasNext()) {
			iterator = isins.iterator();
		}
		return iterator.next();
	}
}
