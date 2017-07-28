package ua.com.myjava.dbasync.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class Derivative {
	private final String derivativeIsin;
	private final String productName;
}
