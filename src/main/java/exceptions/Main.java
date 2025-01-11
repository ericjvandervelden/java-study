package exceptions;

/*
 * als main geen exc mag gooien, dan moet je een checked exc afhandelen, 
 * een runtime exc niet, 
 * dus dit compiles niet,
 * 	public static void main(String[] args)  {
		throw new Exception();
	}
 * dit compiles wel,
 * public static void main(String[] args) throws Exception  {
		throw new Exception();
	}
 * lees,
 * https://stackoverflow.com/questions/6115896/understanding-checked-vs-unchecked-exceptions-in-java
 */
public class Main {
	public static void main(String[] args)  {
		throw new RuntimeException();
	}

}
