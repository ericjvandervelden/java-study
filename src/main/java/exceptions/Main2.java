package exceptions;

/*
 * catch een runtime exception, 
 * f hoeft hem niet te throw,
 */
public class Main2 {
	public static void main(String[] args) {
		X x = new X();
		try {
			int f = x.f(-1);
			System.out.println(f);
		}catch(IllegalArgumentException e) {
			System.out.println(e.getMessage());
		}
	}
	private static class X{
		int f(int i) {
			if (i<0) {
				throw new IllegalArgumentException("i<0");
			}
			return i^2;
		}
	}
}
