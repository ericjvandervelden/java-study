package java_study.encoding;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class StringTester {
	public static void main(String[] args) {
		new StringTester();
	}

	public StringTester() {
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
//		md5.update("abcdefgh".);
//		byte[] bytes = "abcdefgh".getBytes();
		byte[] bs = "ë".getBytes(); // coder=0=latin1, -21=0xeb is latin1 encoding in jvm van ë,
		// -61, -85=0xc3ab, is utf8 encoding van ë ;
		// dus encoding in jvm = latin1,
		print(bs); // maar gevraagde getBytes() is default utf8 encoding,
					// dus hij zet latin1 om in utf8
		byte[] bs2 = "ë".getBytes(StandardCharsets.ISO_8859_1); // -21=0xeb is latin1 encoding in jvm van ë,
		// want coder=0=latin1,
		print(bs2); // dus encoding in jvm = gevraagde encoding,
		byte[] bs3 = "ë".getBytes(StandardCharsets.US_ASCII); // 63=0x3f=? ,
		print(bs3);
//		byte[] bytes = "\u03c0".getBytes();
		byte[] bs4 = "π".getBytes(); // 3,-64= 0x03c0 is utf16 encoding van π in jvm,
										// coder=1=utf16, en van utf16 naar utf8 gaat okay,
										// -49,-128=0xcf80 is utf8 encoding van π
		print(bs4);
		byte[] bs5 = "π".getBytes(StandardCharsets.ISO_8859_1); // 63=? , latin
		print(bs5); // coder=1=utf16, en van utf16 naar latin1 gaat err,

		String pi = new String(new byte[] { -49, -128 }, StandardCharsets.UTF_8); // π
		System.out.println(pi);
		String pi2 = new String(new byte[] { (byte) 0xcf, (byte) 0x80 }, StandardCharsets.UTF_8); // π
		System.out.println(pi2);
//		byte[] bytes = string.getBytes();

		String pi3 = new String(new byte[] { -49, -128 }, StandardCharsets.UTF_8);
		System.out.println(pi3); // π , je geeft de utf8 encoding op, dus dit is decoding,
		String e = new String(new byte[] { -21 }, StandardCharsets.ISO_8859_1);
		System.out.println(e); // ë , je geeft de latin1 encoding op, dus dit is decoding,
		
		// apart geval,
		String pi4 = new String(new byte[] { -49, -128 }, StandardCharsets.ISO_8859_1);
		System.out.println(pi4);	// we zien Ï, dus hij heeft de 1ste byte -49 latin1 decode okay,
		// als je ? wilt, dan moet je bytes opgeven die de encoding niet aankan,
		String e2 = new String(new byte[] { -21 }, StandardCharsets.US_ASCII);
		System.out.println(e2); // we zien ? , je geeft de ascii encoding op, en die kan deze byte niet decode,

	}

	private void print(byte[] bs) {
		for (byte b : bs) {
			System.out.print(b + ", ");
		}
		System.out.println();
	}
	private void printHex(byte[] bs) {
		for (byte b : bs) {
			System.out.print(Integer.toHexString(b) + ", ");
		}
		System.out.println();
	}

}
