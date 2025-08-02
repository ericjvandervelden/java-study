package java_study.encryption;

import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.interfaces.RSAPrivateCrtKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPrivateCrtKeySpec;
import java.security.spec.RSAPrivateKeySpec;

import org.junit.jupiter.api.Test;


public class KeyTester {
	
	

	@Test
	public void test() throws NoSuchAlgorithmException, InvalidKeySpecException {
		
		KeyPair pair = KeyPairGenerator.getInstance("RSA").generateKeyPair();
//		pair	KeyPair  (id=80)	
//			privateKey	sun.security.rsa.RSAPrivateCrtKeyImpl  (id=83)	
//			publicKey	sun.security.rsa.RSAPublicKeyImpl  (id=92)
// public final class RSAPrivateCrtKeyImpl
// 		extends PKCS8Key implements RSAPrivateCrtKey {
// en PKCS8Key heeft wel een equals()
		PrivateKey private1 = pair.getPrivate();// d,e, n, p, pe, q, qe, ..., maar kun je niet opvragen,
		Class<? extends PrivateKey> class1	 = private1.getClass();
		byte[] encoded1 = private1.getEncoded(); // ook: .getFormat(), .getAlgorithm(), en meer niet,
		
		KeyFactory rsaFactory = KeyFactory.getInstance("RSA");
		RSAPrivateKeySpec keySpec = rsaFactory.getKeySpec(private1,RSAPrivateKeySpec.class );
		RSAPrivateKeySpec keySpec2 = rsaFactory.getKeySpec(private1,RSAPrivateCrtKeySpec.class );
		RSAPrivateCrtKeySpec crtKeySpec = rsaFactory.getKeySpec(private1,RSAPrivateCrtKeySpec.class );
		// keySpec. keySpec2, crtKeySpec zijn gelijk = 'n RSAPrivateCrtKeySpec
		// deze heeft getModulus(), getPrimeP(), getPrimeExponentP(), ... methods,
		
		boolean b = keySpec.equals(keySpec2);	// false	, klopt: RSAPrivate(Crt)KeySpec heeft zelf geen equals(),
							// dus equals is die van Object, en dat is ==
		boolean b2 = keySpec.equals(crtKeySpec);	// false
		boolean b3 = keySpec2.equals(crtKeySpec);	// false
		// TODO
		// equals is ==
		// keySpec en crtKeySpec zijn java.security.spec.RSAPrivateCrtKeySpec
		// Objects.equals(keySpec, keySpec2); // false,  is return (a == b) || (a != null && a.equals(b));
		BigInteger m = keySpec.getModulus();
		BigInteger d = keySpec.getPrivateExponent();
		PrivateKey private2 = rsaFactory.generatePrivate(keySpec);
		PrivateKey crlPrivate2 = rsaFactory.generatePrivate(crtKeySpec);
		// private2 en crlPrivate2 zijn RSAPrivateCrtKeyImpl
		// private2.equals(crlPrivate2), beide RSAPrivateCrtKeyImpl
		RSAPrivateCrtKey private3=(RSAPrivateCrtKey)private2;
		RSAPrivateCrtKey crlPrivate3=(RSAPrivateCrtKey)crlPrivate2;
		
		private2.equals(private3);	// true, klopt, 
			// RSAPrivateCrtKeyImpl extends PKCS8Key heeft wel een equals()
		PrivateKey private2a = rsaFactory.generatePrivate(new RSAPrivateKeySpec(m,d));
		// private2a is een RSAPrivateKeyImpl
		// !private2.equals(private2a), private2a is RSAPrivateKeyImpl
		// met new RSAPrivateCrtKeySpec(...) krijg je WH een RSAPrivateCrtKeyImpl
		RSAPrivateKey private3a=(RSAPrivateKey)private2a;
		private2.getClass(); // class sun.security.rsa.RSAPrivateCrtKeyImpl
		private2.getClass().getInterfaces();	// [interface java.security.interfaces.RSAPrivateCrtKey]
		private2a.getClass();// class sun.security.rsa.RSAPrivateKeyImpl
		private2a.getClass().getInterfaces(); // [interface java.security.interfaces.RSAPrivateKey]
		
//		RSAPrivateCrtKey private3=rsaFactory.generatePrivate(keySpec);

		
		
	}

}
