package java_study.encryption;



import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.interfaces.RSAPrivateCrtKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.RSAPrivateCrtKeySpec;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.util.Set;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import org.junit.jupiter.api.Test;


public class Scratch {
	
	// voor alle keys, symm, asymm geldt dat KeyGenerator altijd werkt, geeft keys,
	// ook voor AES, maar je krijgt een SecretKeySpec, bij DES een DESKey, TODO
	// met KeyFactory kun je specs maken, en vanuit de specs ook weer de keys maken,
	// niet voor AES, TODO, maar je krijgt de spec met de KeyGenerator!, en die is ook 
	// de SecretKey,
	
	// met een PrivateKey, of SecretKey heb je .getEncoded(), getAlgorithm(), ...
	// met een RSAPrivateKeySpec kun je .getModulus(), .getPrivateComponent(), geen .getEncode(), 
	// met een RSAPrivateCrtKeySpec kun je ook .getPrimeP(), ..., maar dat kun je ook met een RSAPrivateCrtKey,
	// dus als je een PrivateKey hebt via KeyPairGenerator, dan na cast met RSAPrivateCrtKey kun je alle methods doen die RSAPrivateCrtKeySpec geeft, 
	// dus daar hoef je geen spec voor te maken via een KeyFactory, 
	// met SecretKey of SecretKeySpec kun je getEncoded(), en dat is WH voldoende voor een symm key,
	
	
	// deze test hebben we zelf verzonnen, nav KeyService en InMemoryKeyRepository
	@Test
	public void test() throws NoSuchAlgorithmException, InvalidKeySpecException {
		KeyPairGenerator pairGen = KeyPairGenerator.getInstance("RSA");
		pairGen.initialize(2028);
		KeyPair pair = pairGen.generateKeyPair();
		PrivateKey priv = pair.getPrivate();	// sun.security.rsa.RSAPrivateCrtKeyImpl
		PublicKey pub = pair.getPublic();		// sun.security.rsa.RSAPublicKeyImpl
		priv.getAlgorithm(); priv.getClass(); priv.getEncoded(); priv.getFormat();
					// dit er er alleen maar met een PrivateKey, 
		BigInteger primeExponentP = ((RSAPrivateCrtKey)priv).getPrimeExponentP();
		// dit kan dus!
		System.out.println(primeExponentP);
		
		
		KeyFactory fact = KeyFactory.getInstance("RSA");
		RSAPrivateCrtKeySpec privSpec = fact.getKeySpec(priv, RSAPrivateCrtKeySpec.class);
		RSAPrivateKeySpec privSpec2 = fact.getKeySpec(priv, RSAPrivateKeySpec.class);
		System.out.println(privSpec.getClass());	// java.security.spec.RSAPrivateCrtKeySpec
		System.out.println(privSpec2.getClass());	// java.security.spec.RSAPrivateCrtKeySpec
		RSAPrivateCrtKeySpec privSpec1=(RSAPrivateCrtKeySpec)privSpec2;
		// https://stackoverflow.com/questions/12147297/how-do-i-assert-equality-on-two-classes-without-an-equals-method
//		assertThat(privSpec1).usingRecursiveComparison().isEqualTo(privSpec); 
													// slaagt niet, TODO
		assertThat(privSpec1).isEqualToComparingFieldByFieldRecursively(privSpec); // slaagt,
		RSAPublicKeySpec pubSpec = new RSAPublicKeySpec(privSpec.getModulus(), privSpec.getPublicExponent());
		PublicKey pub1 = fact.generatePublic(pubSpec);
		assertThat(pub1).isEqualTo(pub);		// dus je hebt hem al hierboven, dus de privSpec maken en daaruit -> pub? TODO
		KeyPair pair1 = new KeyPair(pub1, priv);
//		assertThat(pair1).usingRecursiveComparison().isEqualTo(pair);		// slaagt niet,
// 		assertThat(pair1).isEqualToComparingFieldByFieldRecursively(pair);	// slaagt niet; KeyPair heeft geen getPrivateKey()
		boolean b=false;
		
		
		
	}
	
	// rsa: PrivateKey priv= KeyPairGenerator.getInstance("RSA").generateKeyPair().getPrivate();
	// interfaces: Key > PrivateKey, RSAKey > RSAPrivateKey > RSAPrivateCrtKey
	// PrivateKey < Key, dus priv.getEncoded(), .getAlgorithm(), getFormat()
	// RSAPrivateKey < RSAKey, dus ((RSAPrivateKey)priv).getModulus(), .getParams()
	// RSAPrivateKey, dus ((RSAPrivateKey)priv).getPrivateExponent()
	// RSAPrivateCrtKey, dus ((RSAPrivateCrtKey)priv).getPublicExponent, .getPrimeP(), .getPrimeQ(), ...
	// tot zover qua programmeren (met interfaces),
	// waar valt de code in, in welke classes?
	
// package sun.security.pkcs;
// public class PKCS8Key implements PrivateKey {
//    /* The key bytes, without the algorithm information */
//    protected byte[] key;
//    /* The encoded for the key. Created on demand by encode(). */
//    protected byte[] encodedKey;
//     /**
//    * Returns the DER-encoded form of the key as a byte array,
//    * or {@code null} if an encoding error occurs.
//    */
//   public byte[] getEncoded() {			/ DER-encoded
//       byte[] b = getEncodedInternal();	/ de key moet gegenereerd worden; right klik in eclipse, References, Project om te volgen hoe het gaat,
//       return (b == null) ? null : b.clone();
//   }
	
// package sun.security.rsa;
// public final class RSAPrivateKeyImpl extends PKCS8Key implements RSAPrivateKey {
//     private final BigInteger n;         // modulus
//    private final BigInteger d;         // private exponent
	
// package sun.security.rsa;
// public final class RSAPrivateCrtKeyImpl
//    extends PKCS8Key implements RSAPrivateCrtKey {
//    	private BigInteger n;       // modulus
//    	private BigInteger e;       // public exponent
//    	private BigInteger d;       // private exponent
//    	private BigInteger p;       // prime p
//    	private BigInteger q;       // prime q
//    	private BigInteger pe;      // prime exponent p
//    	private BigInteger qe;      // prime exponent q
//    	private BigInteger coeff;   // CRT coeffcient	
	
// package java.security.spec;
// public class RSAPrivateKeySpec implements KeySpec {
//    private final BigInteger modulus;
//    private final BigInteger privateExponent;
//    private final AlgorithmParameterSpec params;
//    public BigInteger getModulus() {
//    	return this.modulus;
//	  }
//    public BigInteger getPrivateExponent() {
//        return this.privateExponent;
//    }
	
//	public class RSAPrivateCrtKeySpec extends RSAPrivateKeySpec {
//
//	    private final BigInteger publicExponent;
//	    private final BigInteger primeP;
//	    private final BigInteger primeQ;
//	    private final BigInteger primeExponentP;
//	    private final BigInteger primeExponentQ;
//	    private final BigInteger crtCoefficient;
// en getters,
	
	
	// video ch3, a simple key service
	@Test
	public void test2() throws NoSuchAlgorithmException, InvalidKeySpecException {
		KeyPair pair=KeyPairGenerator.getInstance("RSA").generateKeyPair();
		PrivateKey priv=pair.getPrivate();	// een sun.security.rsa.RSAPrivateCrtKeyImpl
		
	
//		priv	sun.security.rsa.RSAPrivateCrtKeyImpl  (id=78)	
//			algid	sun.security.x509.AlgorithmId  (id=145)	
//			coeff	java.math.BigInteger  (id=95)	
//			d	java.math.BigInteger  (id=128)	
//			e	java.math.BigInteger  (id=129)	
//			encodedKey	(id=148)	
//			key	(id=151)	
//			keyParams	null	
//			n	java.math.BigInteger  (id=99)	
//			p	java.math.BigInteger  (id=126)	
//			pe	java.math.BigInteger  (id=100)	
//			q	java.math.BigInteger  (id=127)	
//			qe	java.math.BigInteger  (id=101)	
//			type	sun.security.rsa.RSAUtil$KeyType  (id=152)	
		// maar heeft geen getters, 
		// doe een cast, deze heeft wel getters,
		RSAPrivateCrtKey privCrt = (RSAPrivateCrtKey)priv;		// heeft alle methods,
		priv.getEncoded();
		privCrt.getPrimeExponentP();
		
		// dus door te cast naar RSAPrivateCrtKey heb je ineens alle methods, is compile time type,
		
		// we hoeven alleen m,d op te slaan, en we kunnen herleiden een sun.security.rsa.RSAPrivateKeyImpl
		KeyFactory fact  = KeyFactory.getInstance("RSA");
		RSAPrivateKeySpec privSpec = fact.getKeySpec(priv, RSAPrivateKeySpec.class);
//		privSpec	java.security.spec.RSAPrivateCrtKeySpec  (id=89)	
//			crtCoefficient	java.math.BigInteger  (id=95)	
//			modulus	java.math.BigInteger  (id=99)	
//			params	null	
//			primeExponentP	java.math.BigInteger  (id=100)	
//			primeExponentQ	java.math.BigInteger  (id=101)	
//			primeP	java.math.BigInteger  (id=126)	
//			primeQ	java.math.BigInteger  (id=127)	
//			privateExponent	java.math.BigInteger  (id=128)	
//			publicExponent	java.math.BigInteger  (id=129)
		KeySpec privSpec4 = fact.getKeySpec(priv, KeySpec.class);
//		privSpec4	java.security.spec.PKCS8EncodedKeySpec  (id=122)	
//			algorithmName null
// 			encodedKey ...
		privSpec.getModulus();privSpec.getPrivateExponent(); privSpec.getParams();
		// maar privSpec is een RSAPrivateCrtKeySpec, alleen heeft de getters niet,
		RSAPrivateCrtKeySpec privSpec1 = (RSAPrivateCrtKeySpec)privSpec;
		privSpec1.getModulus();privSpec1.getPrivateExponent(); privSpec1.getParams();
		privSpec1.getCrtCoefficient();privSpec1.getPrimeExponentP();privSpec1.getPrimeExponentQ();
		privSpec1.getPrimeP();privSpec1.getPrimeQ();privSpec1.getPublicExponent();
		// privSpec1 heeft de getters wel,
		// dus WH 
		PrivateKey priv0 = fact.generatePrivate(privSpec); // inverse zie hieronder,
		boolean b = priv0.equals(priv); // true, 
		// privSpec heeft alle components, maar niet de getters, 
		// dus als je de inverse neemt krijg je runtime priv weer terug,
		// alleen als je met de uitgebreide spec wilt werken kun je
		// 1. cast doen zoals hierboven bij privSpec1, 
		// 2. met andere spec class opvragen, zoals bij privCrtSpec hieronder 
 		System.out.println(privSpec.getClass());// java.security.spec.RSAPrivateCrtKeySpec
		// maar de compile time type is RSAPrivateKeySpec, en geen RSAPrivateCrtKeySpec
		BigInteger m = privSpec.getModulus();
		BigInteger d = privSpec.getPrivateExponent();
		AlgorithmParameterSpec pars = privSpec.getParams(); // er is ook een <init> met m,d,pars
		
		RSAPrivateKeySpec privSpec2 = new RSAPrivateKeySpec(m, d);
		// we maken zelf een spec,
		// dit is runtime echt een kleinere spec dan privSpec,
		
		PrivateKey priv1 = fact.generatePrivate(privSpec2);	
			// sun.security.rsa.RSAPrivateKeyImpl, heeft alleen .getEncoded() der encoding, getAlgorithm(), ...
			// kun je niet cast naar RSAPrivateCrtKey,
		System.out.println(priv1.getClass());	// sun.security.rsa.RSAPrivateKeyImpl

// priv1 is runtime een kleinere class dan priv==priv0
// hieronder maken zelf een grotere spec privCrtSpec2, en die geeft wel een RSAPrivateCrtKeyImpl,
//		priv1	sun.security.rsa.RSAPrivateKeyImpl  (id=100)	
//			algid	sun.security.x509.AlgorithmId  (id=178)	
//			d	java.math.BigInteger  (id=98)	
//			encodedKey	(id=179)	
//			key	(id=180)	
//			keyParams	null	
//			n	java.math.BigInteger  (id=94)	
//			type	sun.security.rsa.RSAUtil$KeyType  (id=130)	
		
// hierboven hadden privSpec opgevraagd met RSAPrivateKeySpec.class als 2de arg, en 
		// de runtime grote RSAPrivateCrtKeyImpl priv als 1ste arg, 
// compile time krijg je een kleinere spec RSAPrivateKeySpec, 
// maar runtime is het de grote RSAPrivateCrtKeySpec
// en daarom krijg je met de inverse fact.generatePrivate 
// toch de grote key RSAPrivateCrtKeyImpl 
// maar als je met de hand new RSAPrivateKeySpec(m, d) maakt, 
// maak je ook runtime een kleinere RSAPrivateKeySpec
// en krijg je met de inverse fact.generatePrivate een kleinere RSAPrivateKeyImpl

		boolean b0 = priv0.equals(priv1); // false, 
			// priv0	sun.security.rsa.RSAPrivateCrtKeyImpl  (id=76)	
			// priv1	sun.security.rsa.RSAPrivateKeyImpl  (id=100)	
		
		// we hoeven alleen m,e,d,p,q,ep,eq,c op te slaan, en we kunnen herleiden een sun.security.rsa.RSAPrivateCrtKeyImpl
		// we maken privSpec hierboven zelf,
		RSAPrivateCrtKeySpec privCrtSpec = fact.getKeySpec(priv, RSAPrivateCrtKeySpec.class);
		boolean b1 = privSpec1.equals(privCrtSpec);	// false, 
		// ze zijn gelijk, maar spec classes hebben zelf geen equals,
		// en Object.equals is ==
						
		BigInteger m_ = privCrtSpec.getModulus();
		BigInteger e = privCrtSpec.getPublicExponent();
		BigInteger d_ = privCrtSpec.getPrivateExponent();
		BigInteger p = privCrtSpec.getPrimeP();
		BigInteger q = privCrtSpec.getPrimeQ();
		BigInteger ep = privCrtSpec.getPrimeExponentP();
		BigInteger eq = privCrtSpec.getPrimeExponentQ();
		BigInteger c = privCrtSpec.getCrtCoefficient();
		
		RSAPrivateCrtKeySpec privCrtSpec2 = new RSAPrivateCrtKeySpec(m_,e,d_,p,q,ep,eq,c);
		PrivateKey priv2 = fact.generatePrivate(privCrtSpec2);
		System.out.println(priv2.getClass());	// sun.security.rsa.RSAPrivateCrtKeyImpl
		// priv2.get...() // getEncoded(), getFormat(), getParams(), getAlgorithm(), 
		// ((RSAPrivateKey)priv2).get...()   // 4 hierboven,  getModulus(), getPrivateExponent() 
		// ((RSAPrivateCrtKey)priv2).get...() // 6 hierboven, getCrtCoefficient(), getPrimeExponentP(), getPrimeExponentQ(), getPrimeP(), getPrimeQ(), getPublicExponent()

		// inverse,
		// factory		.getKeySpec()			.generatePrivate 			zijn elkaars invers,
		// 	key	, Spec.class -> 	spec 			-> 			priv
	
		// key's hebben wel een equals, spec's niet,
		
		boolean b2 = priv0.equals(priv2);		// true
		System.out.println();
		
	}
	
	// bij aes geven ze de result key meteen in een spec af, 
	// bij des ook in een DESKey(Impl), en in een spec,
	// bij rsa ook in een PKCS8Key, maar ook in een spec,
	// aes en des:
	// SecretKey key = KeyGenerator.getInstance("AES/DES").generateKey();
	// interface SecretKey < Key, die getEncoded(), getAlgorithm(), getFormat() heeft,
	// aes: runtime = class SecretKeySpec, die met getEncoded() de byte[]key geeft (key material)
	// des: runtime = class DESKeySpec, die met getEncoded() de byte[]key geeft (key material)
	// verschil des, aes: er is ook een DESKey class, die ook byte[] key heeft, met getEncoded()
	
	// video ch3, key generators
	@Test
	public void test3() throws NoSuchAlgorithmException {
		Set<String> algorithms = Security.getAlgorithms("KeyGenerator");
		System.out.println(algorithms);
		// [RC2, SUNTLSKEYMATERIAL, HMACSHA384, DESEDE, BLOWFISH, ARCFOUR, HMACSHA512/256, HMACSHA256, HMACSHA224, HMACMD5, HMACSHA512/224, AES, HMACSHA3-384, CHACHA20, SUNTLSPRF, HMACSHA512, SUNTLSRSAPREMASTERSECRET, DES, HMACSHA3-256, HMACSHA3-224, HMACSHA3-512, SUNTLSMASTERSECRET, HMACSHA1, SUNTLS12PRF]
		KeyGenerator aesGen = KeyGenerator.getInstance("AES");
		SecretKey key = aesGen.generateKey(); // javax.crypto.spec.SecretKeySpec
		
		System.out.println(key.getClass());// javax.crypto.spec.SecretKeySpec
			// je hebt geen KeyFactory nodig, die is er ook niet voor AES
			// bij DES krijg je hier een DESKey, dus geen spec, en daar is wel een KeyFactory,
		byte[] encoded = key.getEncoded(); // key material,  = [89, -91, -104, -58, -40, -1, 92, -95, -68, 104, -34, 14, 89, -80, -21, 111]
		String algorithm = key.getAlgorithm();	// "AES"
		// dit is net als bij de private key in test2(), maar daar is key geen spec, TODO
		int len=key.getEncoded().length*8;	// 128
		System.out.println(len);// 128
		
		// KeyGenerator.getInstance("AES").generateKey() = runtime een class SecretKeySpec,
		// compile time = SecretKey interface, met een getEncoded() bijv, 
		// daarmee kun je dus programeren,
		// runtime valt hij in de SecretKeySpec.getEncoded(),
		// SecretKeySpec heeft een byte[]key en getEncoded() die key geeft,
		// SecretKey < Key is een tag interface, 
		// Key is een interface met getEncoded(), getAlgorithm(), getFormat()
//		key	javax.crypto.spec.SecretKeySpec  (id=86)	
//			algorithm	"AES" (id=79)	
//			key	(id=93)	
//				[0]	-65	
//				[1]	-124	
//				...
		
// package javax.crypto.spec;
// public class SecretKeySpec implements KeySpec, SecretKey {	/ KeyGenerator.generateKey() maakt deze,
//     private byte[] key;
//     public byte[] getEncoded() {			/ interface SecretKey method,
//        return this.key.clone();
//    }	
		
// package java.security.spec;
// public interface KeySpec { }		/ grouping interface,

// package javax.crypto;
// public interface SecretKey extends
//	    java.security.Key, javax.security.auth.Destroyable {
// }
// package java.security;
// public interface Key extends java.io.Serializable {
//     public String getAlgorithm();
//     public String getFormat();
//     public byte[] getEncoded();
// }
		
		
	}
	
	// video ch3, key factories
	@Test
	public void test4() throws NoSuchAlgorithmException, InvalidKeySpecException {
		Set<String> algorithms = Security.getAlgorithms("SecretKeyFactory");
		System.out.println(algorithms);
		// [PBEWITHHMACSHA384ANDAES_128, PBEWITHSHA1ANDRC4_40, PBEWITHHMACSHA512ANDAES_256, DESEDE, PBEWITHHMACSHA512ANDAES_128, PBKDF2WITHHMACSHA1, PBKDF2WITHHMACSHA384, PBEWITHHMACSHA224ANDAES_256, DES, PBEWITHHMACSHA256ANDAES_128, PBEWITHMD5ANDDES, PBEWITHHMACSHA256ANDAES_256, PBKDF2WITHHMACSHA224, PBEWITHHMACSHA1ANDAES_128, PBEWITHSHA1ANDRC4_128, PBEWITHSHA1ANDDESEDE, PBKDF2WITHHMACSHA512, PBEWITHSHA1ANDRC2_128, PBEWITHSHA1ANDRC2_40, PBEWITHHMACSHA384ANDAES_256, PBEWITHHMACSHA1ANDAES_256, PBEWITHMD5ANDTRIPLEDES, PBEWITHHMACSHA224ANDAES_128, PBKDF2WITHHMACSHA256]
		
		KeyGenerator desGen = KeyGenerator.getInstance("DES");	// AES kan ook, zie test3(),
		// desGen	javax.crypto.KeyGenerator  (id=87)	
		SecretKey desKey = desGen.generateKey();
		System.out.println(desKey.getClass());// com.sun.crypto.provider.DESKey
			// bij AES was dit een SecretKeySpec, TODO
		desKey.getAlgorithm();
		desKey.getEncoded();
		SecretKeyFactory desFact = SecretKeyFactory.getInstance("DES");// AES kan niet,
		KeySpec desKeySpec = desFact.getKeySpec(desKey, DESKeySpec.class);
		System.out.println(desKeySpec);// javax.crypto.spec.DESKeySpec@41e1e210
		SecretKey desKey2 = desFact.generateSecret(desKeySpec);
		assertThat(desKey2).isEqualTo(desKey);
		
		// factory		.getKeySpec()			.generateSecret 			zijn elkaars invers,
		// 	key	, Spec.class -> 	spec 			-> 			key
		
		// we zien geen verschil tussen desKey en desKeySpec,
//		desKey	com.sun.crypto.provider.DESKey  (id=84)	
//			key	(id=94)	
//				[0]	-70	
//				[1]	52	
//				[2]	2	
//				[3]	13	
//				[4]	50	
//				[5]	19	
//				[6]	-116	
//				[7]	-92	
//		desKeySpec	javax.crypto.spec.DESKeySpec  (id=91)	
//			key	(id=117)	
//				[0]	-70	
//				[1]	52	
//				[2]	2	
//				[3]	13	
//				[4]	50	
//				[5]	19	
//				[6]	-116	
//				[7]	-92	

		desKey.getEncoded(); //= key hierboven
		DESKeySpec desKeySpec2 = (DESKeySpec)desKeySpec;
		byte[] keyBytes = desKeySpec2.getKey();
		
		
		// bij RSAPrivateKeySpec had je extra getters, zoals getModulus(), getPrivateExponent(),
		// en nog meer als je RSAPrivateCrtKeySpec
		// maar met DESKey kun je getEncoded(), net als bij een RSAPrivateKey 
		// een DESKey van maken mbv. een SecretKeyFactory
		// aesKey is een SecretKeySpec < <SecretKey < Key, dus heeft getEncoded(), 
	
		// KeyGenerator.getInstance("DES").generateKey() = runtime een DESKey
		// compile time = SecretKey < Key interface, met een getEncoded() bijv, 
		// en als je daarmee programeert valt hij in de DESKey.getEncoded(),
		
// aes werkt niet met zoiets als DESKey, alleen met SecretKeySpec ipv DESKeySpec,
// je ziet ook dat DESKey helemaal niet nodig is, want DESKeySpec heeft ook de key 
// en die krijg je via getEncoded().
		
// KeyGenerator.generateKey maakt de key. KeyFactory maakt een (algemeen) interface voor deze key (spec), 
// en kan ook vanuit de spec een key maken, maar het blijft een (algemeen) interface, 
		
// SecretKey is het (java) interface voor symm. keys, heeft de getEncoded() method, 
// die = DER format van de key
		
// package com.sun.crypto.provider;
// final class DESKey implements SecretKey { / KeyGenerator.generateKey() maakt deze,
//    private byte[] key;
//    public byte[] getEncoded() {						/ interface SecretKey method,
//        // Return a copy of the key, rather than a reference,
//        // so that the key data cannot be modified from outside
//
//        // The key is zeroized by finalize()
//        // The reachability fence ensures finalize() isn't called early
//        byte[] result = key.clone();
//        Reference.reachabilityFence(this);
//        return result;
//    }
// package javax.crypto;
// public interface SecretKey extends
//	    java.security.Key, javax.security.auth.Destroyable {
// }
// package java.security;
// public interface Key extends java.io.Serializable {
//    public String getAlgorithm();
//     public String getFormat();
//	    public byte[] getEncoded();
// }
//
// package javax.crypto.spec;
// public class DESKeySpec implements java.security.spec.KeySpec {
//     private byte[] key;		 / krijgt op een of andere manier uit DESKey,
//     /**
//	     * Returns the DES key material.
//	     *
//	     * @return the DES key material. Returns a new array
//	     * each time this method is called.
//	     */
//     public byte[] getKey() {
//        return this.key.clone();
//    }
	
// package java.security.spec;
// public interface KeySpec { }
		
// waar gevonden?		
		// JRE System Library [ JavaSE-17]
		// 	java.base
		// 		com.sun.crypto.provider
		//			DESKey.class
	
		// JRE System Library [ JavaSE-17]
		// 	java.base
		// 		javax.crypto.spec
		// 			DESKeySpec

	
	}

}


// Overzicht keys,

//package java.security;
//public interface Key extends java.io.Serializable {
//	public String getAlgorithm();
//	public String getFormat();
//	public byte[] getEncoded();
//	>
//	package java.security;
//	public interface PrivateKey extends Key, javax.security.auth.Destroyable {	/ grouping interface,
//		>
//		package sun.security.pkcs;
//		public class PKCS8Key implements PrivateKey {
//			protected AlgorithmId algid;
//			/* The key bytes, without the algorithm information */
//    			protected byte[] key;
//			/* The encoded for the key. Created on demand by encode(). */
//    			protected byte[] encodedKey;
//		>
//		package java.security.interfaces;
//		public interface RSAPrivateKey extends java.security.PrivateKey, RSAKey
//			public BigInteger getPrivateExponent();
//			>
//			package sun.security.rsa;
//			public final class RSAPrivateKeyImpl extends PKCS8Key implements RSAPrivateKey {
//				private final BigInteger n;         // modulus
//    				private final BigInteger d;         // private exponent
//				public BigInteger getModulus()
//				public BigInteger getPrivateExponent() {
//			>
//			package java.security.interfaces;
//			public interface RSAPrivateCrtKey extends RSAPrivateKey {
//				private final BigInteger n;         // modulus
//    				private final BigInteger d;         // private exponent
//				...
//				public BigInteger getPublicExponent();
//				public BigInteger getPrimeP();
//				public BigInteger getPrimeQ();
//				public BigInteger getPrimeExponentP();
//				public BigInteger getPrimeExponentQ();
//				public BigInteger getCrtCoefficient();				
//				>
//				package sun.security.rsa;
//				public final class RSAPrivateCrtKeyImpl extends PKCS8Key implements RSAPrivateCrtKey {
//					/ overrides niet RSAPrivateKeyImpl, heeft ook getModulus(), getPrivateExponent()
//package java.security;
//public interface Key extends java.io.Serializable {
//	public String getAlgorithm();
//	public String getFormat();
//	public byte[] getEncoded();
//	>
//	package javax.crypto;
//	public interface SecretKey extends java.security.Key, javax.security.auth.Destroyable {		/ grouping interface
//		>
//		package com.sun.crypto.provider;
//		final class DESKey implements SecretKey {
//			private byte[] key;
//			public byte[] getEncoded() {
//			...
//		>
// 		package javax.crypto.spec;
//		public class SecretKeySpec implements KeySpec, SecretKey {
//			private byte[] key;
//			public byte[] getEncoded() {
//			...

// Overzicht specs,

//package java.security.spec;
//public interface KeySpec { }	/ grouping interface
//	>
//	package javax.crypto.spec;
//	public class DESKeySpec implements java.security.spec.KeySpec {
//		private byte[] key;
//		public byte[] getKey() {		/ heeft geen getEncoded() zoals SecretKeySpec,
//	>
//	package java.security.spec;
//	public class RSAPrivateKeySpec implements KeySpec {
//		private final BigInteger modulus;
//		private final BigInteger privateExponent;
//		public BigInteger getModulus() {
//		public BigInteger getPrivateExponent() {
//	>
//	package java.security.spec;
//	public class RSAPublicKeySpec implements KeySpec {
//		private final BigInteger modulus;
//		private final BigInteger publicExponent;
//		public BigInteger getModulus() {
//		public BigInteger getPublicExponent() {
//	>
//	package javax.crypto.spec;
//	public class SecretKeySpec implements KeySpec, SecretKey {		/ heeft geen getKey() zoals DESKeySpec
//		private byte[] key;
//		public String getAlgorithm() {
//		public String getFormat() {
//		public byte[] getEncoded() {

// key -> spec -> key

//KeyPairGenerator pairGen = KeyPairGenerator.getInstance("RSA");		
//KeyPair pair = pairGen.generateKeyPair();
//PrivateKey priv = pair.getPrivate();	/ runtime sun.security.rsa.RSAPrivateCrtKeyImpl
//RSAPrivateCrtKey privCrt = (RSAPrivateCrtKey)priv;	/ OK, 
//KeyFactory fact  = KeyFactory.getInstance("RSA");
//
//RSAPrivateKeySpec privSpec = fact.getKeySpec(priv, RSAPrivateKeySpec.class); / runtime RSAPrivateCrtKeySpec
//RSAPrivateCrtKeySpec privSpec1 = (RSAPrivateCrtKeySpec)privSpec; / OK,
//
//KeySpec privSpec4 = fact.getKeySpec(priv, KeySpec.class);	/ runtime PKCS8EncodedKeySpec
//
//PrivateKey priv0 = fact.generatePrivate(privSpec); // inverse zie hieronder,
//boolean b = priv0.equals(priv); / true,
// TODO afmaken


