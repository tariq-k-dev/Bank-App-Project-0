package revature.tariqkhan.accountsignup;

import static org.junit.jupiter.api.Assertions.*;



import java.time.format.DateTimeFormatter;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import revature.tariqkhan.datevalidator.DateValidator;
import revature.tariqkhan.datevalidator.DateValidatorUsingLocalDate;

class DateFormatterTest {

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		System.out.println("Set Up Before Class - @BeforeAll");
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
		System.out.println("Tear Down After Class - @AfterAll");
	}

	@BeforeEach
	void setUp() throws Exception {
		System.out.println("Set Up @BeforeEach");
	}

	@AfterEach
	void tearDown() throws Exception {
		System.out.println("Tear Down @AfterEach");
	}

	@Test
	void test() {
		DateTimeFormatter dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE;
		DateValidator validator = new DateValidatorUsingLocalDate(dateFormatter);
		
		assertTrue(validator.isValid("2019-02-28"));
		assertFalse(validator.isValid("2019-02-30"));
		assertTrue(validator.isValid("2011-03-21"));
	}

}
