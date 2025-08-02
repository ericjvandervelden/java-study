package java_study.encoding;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import org.apache.commons.codec.binary.Hex;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class Base64Tester {

	@Test
	public void test() {
		assertFalse((byte) 0x7f < 0);
		assertTrue((byte) 0x80 < 0);

	}

	@Test
	@Disabled
	/*
	 * encode sessionid in base64
	 */
	public void test1() {
		byte[] bs = "c37b27a17".getBytes();
		print(bs);// 99, 51, 55, 98, 50, 55, 97, 49, 55, 
		printHex(bs);	// 63, 33, 37, 62, 32, 37, 61, 31, 37, 
		// coder=latin1, value=[99, 51, 55, 98, 50, 55, 97, 49, 55]
		// de charset in getBytes() is utf8 (default), en encodeUTF8 is called met coder, value,
		// en omdat de bytes niet <0 zijn volgt Arrays.copyOf(val, val.length);
		// doe ,
//		jshell> "c37b27a17".getBytes(StandardCharsets.ISO_8859_1)
//		$7 ==> byte[9] { 99, 51, 55, 98, 50, 55, 97, 49, 55 }
//		jshell> for (byte b: $2){System.out.print(Integer.toHexString(b)+", ");}
//		of,
//		jshell> for (byte b: $2){System.out.print(Integer.toHexString(b&0xff)+", ");}
//		63, 33, 37, 62, 32, 37, 61, 31, 37,
		
		
		// base64 is een invertable encoding, dus kunnen we niet alleen gebruiken,
		byte[] b64 = Base64.getEncoder().encode(bs);
		// .encodeToString() geeft meteen de chars, en niet de indices van de chars in de base64 table,
		// sessionId bestaat uit 0,1,...,A,...,F. Dus latin1, dus .getBytes() geeft de jvm encoding van sessionId.
		// .encodeToString() verdeelt de bytes in stukken van 6 bits, en ziet elk stuk, zeg abcdef, als de laatste 6 bits van een nieuwe byte, die dus is: 00abcdef.
		// dus in dit geval maakt van 9 bytes =72 bits 12 bytes.
		// in ons voorbeeld: 0x6333... = 0110.0011.0011.0011... dus de 1ste anderhalve byte geeft 2 bytes: 0x1833. Dit zijn 2 indexes in de base64 table op wikipedia, 
		//                               -------^^^^^^^
		// van de chars Yz of in latin1 encoding 0x597a.
		print(b64);	// 89, 122, 77, 51, 89, 106, 73, 51, 89, 84, 69, 51, 
		printHex(b64);	// 59, 7a, 4d, 33, 59, 6a, 49, 33, 59, 54, 45, 33, 
			// bytes van de base64 string, niet de indexes, ze zijn al opgezocht,
		String b64s2 = new String(b64,StandardCharsets.ISO_8859_1);
		System.out.println(b64s2); // YzM3YjI3YTE3
		// je had eerst de base64 [B en daarna de string ook in 1 keer kunnen doen,
		String b64s = Base64.getEncoder().encodeToString(bs);
		System.out.println(b64s);	// YzM3YjI3YTE3
	}
	
	@Test
	@Disabled
	/*
	 * encode hash sessionid in base64
	 */
	public void test2() throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("MD5");
		byte[] bs = "c37b27a17".getBytes();
		print(bs);	// 99, 51, 55, 98, 50, 55, 97, 49, 55, 
		printHex(bs);	// 63, 33, 37, 62, 32, 37, 61, 31, 37,
		md.update(bs);
		byte[] bs2 = md.digest();
		print(bs2);	// 16 bytes = 128 bits, MD5 is dat altijd,
			// -29, 49, -122, -78, -64, -19, 78, -51, 81, -96, -42, 53, 102, 68, 38, 22, 
		printHex(bs2);	// e3, 31, 86, b2, c0, ed, 4e, cd, 51, a0, d6, 35, 66, 44, 26, 16, 
		System.out.println(bs2); // [B@445b295b , dus niets voor in log file,
		String s = new String(bs2,StandardCharsets.UTF_8); // bs2 is geen utf8 encoding, 
			// dus we zien �1����N�Q��5fD&
		System.out.println(s);
		byte[] b64 = Base64.getEncoder().encode(bs2);
		print(b64);	// 52, 122, 71, 71, 115, 115, 68, 116, 84, 115, 49, 82, 111, 78, 89, 49, 90, 107, 81, 109, 70, 103, 61, 61, 
		printHex(b64);	// 34, 7a, 47, 47, 73, 73, 44, 74, 54, 73, 31, 52, 6f, 4e, 59, 31, 5a, 6b, 51, 6d, 46, 67, 3d, 3d, 
		String s2 = new String(b64,StandardCharsets.ISO_8859_1);
		System.out.println(s2); // 4zGGssDtTs1RoNY1ZkQmFg==
		
	}
	
	@Test
//	@Disabled
	/*
	 * encode hash sessionid in hex=base16, ipv base64
	 */
	public void test3() throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("MD5");
		byte[] bs = "c37b27a17".getBytes();
		print(bs);	// 99, 51, 55, 98, 50, 55, 97, 49, 55, 
		printHex(bs);	// 63, 33, 37, 62, 32, 37, 61, 31, 37, 
		md.update(bs);
		byte[] bs2 = md.digest();
		print(bs2);	// 16 bytes = 128 bits, MD5 is dat altijd,
				// = -29, 49, -122, -78, -64, -19, 78, -51, 81, -96, -42, 53, 102, 68, 38, 22, 
		printHex(bs2);	// e3, 31, 86, b2, c0, ed, 4e, cd, 51, a0, d6, 35, 66, 44, 26, 16, 
		System.out.println(bs2); // [B@445b295b , dus niets voor in log file,
		String s = new String(bs2,StandardCharsets.UTF_8); // bs2 is geen utf8 encoding, 
					// dus we zien �1����N�Q��5fD&
		System.out.println(s);
		char[] h = Hex.encodeHex(bs2);	
		print(h);	// e, 3, 3, 1, 8, 6, b, 2, c, 0, e, d, 4, e, c, d, 5, 1, a, 0, d, 6, 3, 5, 6, 6, 4, 4, 2, 6, 1, 6, 
		String hs = Hex.encodeHexString(bs2);	
		System.out.println(hs);	// e33186b2c0ed4ecd51a0d63566442616
						// de hex string ipv base64 string, dit is de sessionId,
		System.out.println("---");
		byte[] bs6 = "π".getBytes(StandardCharsets.UTF_8);
		printHex(bs6);	// cf, 80,
		String s6 = Base64.getEncoder().encodeToString(bs6);
		System.out.println(s6); // z4A=
		byte[] bs6_2 = Base64.getDecoder().decode(s6);
		printHex(bs6_2);	// cf, 80, 
		String s6_2 = new String(bs6_2,StandardCharsets.UTF_8);
		System.out.println(s6_2); // π
	}
	
	@Test
	public void test4() {
		
	}

	private void print(byte[] bs) {
		for (byte b : bs) {
			System.out.print(b+ ", ");
		}
		System.out.println();
	}
	private void printHex(byte[] bs) {
		for (byte b : bs) {
			String s = Integer.toHexString(b);
			System.out.print(s.substring(s.length()-2)+ ", ");
		}
		System.out.println();
	}
	private void print(char[] cs) {
		for (char c : cs) {
			System.out.print(c+ ", ");
		}
		System.out.println();
	}

}
