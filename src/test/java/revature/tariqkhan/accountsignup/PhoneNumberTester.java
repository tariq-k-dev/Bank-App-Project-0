package revature.tariqkhan.accountsignup;

import static org.junit.jupiter.api.Assertions.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PhoneNumberTester {

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
		Pattern pattern = Pattern.compile("^((\\(\\d{3}\\))|\\d{3})[- .]?\\d{3}[- .]?\\d{4}$");
	    Matcher matcher1 = pattern.matcher("(202)-555-0125");
	    Matcher matcher2 = pattern.matcher("202-555-0125");
	    assertTrue(matcher1.matches());
	    assertTrue(matcher2.matches());
	}

}
