package java_study.records;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import basic.Person;
import basic.Person2;
import basic.Person3;

public class RecordTest {
	
	@Test
	public void test() {
		Person p = new Person();
		assertNotNull(p);
	}
	@Test
	public void test2() {
		Person2 p2 = new Person2("foo","bar 13");
		assertNotNull(p2);
		Person2 p2a = new Person2("foo","bar 13");
		assertTrue(p2.equals(p2a));	}
	@Test
	public void test3() {
		Person2 p2 = new Person2("foo","bar 13");
		assertNotNull(p2);
		System.out.println(p2);
	}
	@Test
	public void test4() {
		Person2 p2 = new Person2("foo",null);
		assertEquals("foo", p2.name());
	}
	@Test
	public void test5() {
		Assertions.assertThrows(NullPointerException.class,()->{
			Person3 p3 = new Person3("foo",null);
		});
	}

}
