package basic;

import java.util.Objects;

public record Person3(String name, String address) {
	public Person3 {
		Objects.requireNonNull(name);
		Objects.requireNonNull(address);
	}
	public Person3(String name) {
		this(name,"Unknown");
	}
}
