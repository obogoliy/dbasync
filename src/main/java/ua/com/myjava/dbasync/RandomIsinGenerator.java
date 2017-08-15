package ua.com.myjava.dbasync;

import static java.util.stream.Collectors.toList;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class RandomIsinGenerator {
	private static final int BASE = 1000000;
	private final Random random  = new Random();


	String getRandomIsin() {
		return "CH000" + (BASE + random.nextInt(100_000));
	}
}
