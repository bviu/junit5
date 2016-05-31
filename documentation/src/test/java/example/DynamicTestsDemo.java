/*
 * Copyright 2015-2016 the original author or authors.
 *
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution and is available at
 *
 * http://www.eclipse.org/legal/epl-v10.html
 */

package example;

// tag::user_guide[]
import static org.junit.gen5.api.Assertions.assertFalse;
import static org.junit.gen5.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

import org.junit.gen5.api.DynamicTest;
import org.junit.gen5.api.Tag;
import org.junit.gen5.api.TestFactory;

@Tag("exclude")
class DynamicTestsDemo {

	@TestFactory
	List<String> dynamicTestsWithWrongReturnType() {
		List<String> tests = new ArrayList<>();
		tests.add("Hello");
		return tests;
	}

	@TestFactory
	List<DynamicTest> dynamicTestsFromList() {
		List<DynamicTest> tests = new ArrayList<>();

		tests.add(new DynamicTest("succeedingTest", () -> assertTrue(true, "succeeding")));
		tests.add(new DynamicTest("failingTest", () -> assertTrue(false, "failing")));

		return tests;
	}

	@TestFactory
	Stream<DynamicTest> dynamicTestsFromStream() {
		String[] testNames = new String[] { "test1", "test2" };
		return Arrays.stream(testNames).map(name -> new DynamicTest(name, () -> {
		}));
	}

	@TestFactory
	Iterator<DynamicTest> dynamicTestStreamFromIterator() {
		List<DynamicTest> tests = new ArrayList<>();
		tests.add(new DynamicTest("succeedingTest", () -> assertTrue(true, "succeeding")));
		tests.add(new DynamicTest("failingTest", () -> assertTrue(false, "failing")));
		return tests.iterator();
	}

	@TestFactory
	Iterable<DynamicTest> dynamicTestStreamFromIterable() {
		List<DynamicTest> tests = new ArrayList<>();
		tests.add(new DynamicTest("succeedingTest", () -> assertTrue(true, "succeeding")));
		tests.add(new DynamicTest("failingTest", () -> assertTrue(false, "failing")));
		return tests;
	}

	@TestFactory
	Iterator<DynamicTest> generatedTestsFromGeneratorFunction() {
		Iterator<DynamicTest> generator = new Iterator<DynamicTest>() {

			int counter = 0;

			@Override
			public boolean hasNext() {
				return counter < 100;
			}

			@Override
			public DynamicTest next() {
				int index = counter++;
				return new DynamicTest("test" + index, () -> assertTrue(index % 11 != 0));
			}
		};
		return generator;
	}

	@TestFactory
	Stream<DynamicTest> generatedRandomNumberOfTests() {
		final int AVERAGE = 49;

		Iterator<Integer> generator = new Iterator<Integer>() {

			int last = -1;
			Random random = new Random();

			@Override
			public boolean hasNext() {
				return last % AVERAGE != 0;
			}

			@Override
			public Integer next() {
				last = random.nextInt();
				return last;
			}
		};
		return DynamicTest.stream(generator, index -> "test" + index, index -> assertFalse(index % AVERAGE == 0));
	}

}
// end::user_guide[]
