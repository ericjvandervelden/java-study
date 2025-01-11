package java_study.encoding;

import java.nio.charset.StandardCharsets;

/* 
 * de jvm heeft latin-1 of utf-16 encoding van strings,
 */

/*
 * ga naar https://unicode.org/charts/
 * Find chart by hex code: geef de unicode, bijv (U)03c0
 * -> https://unicode.org/cgi-bin/Code2Chart
 * download https://www.unicode.org/charts/PDF/U0370.pdf, 
 * scoll naar beneden, je zien π, dat is dus het char bij U03c0
 * ga terug naar ga naar https://unicode.org/charts/
 * zoek op Uc003, 
 * download https://www.unicode.org/charts/PDF/UAC00.pdf
 * scroll down een aantal tabellen totdat je bent bij tabel
 * C000 Hangul Syllables C0FF
 * we zien het char bij Uc003: 쀃
 */

public class JvmEncodeTester {
        public static void main(String[]args){
                new JvmEncodeTester();
        }
        public JvmEncodeTester(){
        	{
                byte[] pi_e = "π".getBytes(StandardCharsets.UTF_16BE);	// 3,c0
                String pi_d = new String(pi_e,StandardCharsets.UTF_16BE);	// π
                printHex(pi_e);
                System.out.println(pi_d);
        	}
        	{
                byte[] pi_e = "π".getBytes(StandardCharsets.UTF_16BE);	// 3,c0
                String pi_d = new String(pi_e,StandardCharsets.UTF_16LE);	// 쀃
                	// want le ziet [3,c0] als Uc003
                printHex(pi_e);
                System.out.println(pi_d);       		
        	}
        	{
                byte[] pi_e = "π".getBytes(StandardCharsets.UTF_16LE);	// c0, 3
                String pi_d = new String(pi_e,StandardCharsets.UTF_16LE);	// π
                printHex(pi_e);
                System.out.println(pi_d);       		
        	}
        	{
                byte[] pi_e = "π".getBytes(StandardCharsets.UTF_16LE);	// c0, 3
                String pi_d = new String(pi_e,StandardCharsets.UTF_16BE);	// 쀃
                		// want be ziet [c0,03] als Uc003
                printHex(pi_e);
                System.out.println(pi_d);       		
        	}
        	{
                byte[] pi_e = "π".getBytes(StandardCharsets.UTF_16);	// fe, ff, 3, c0, 
                		// lees https://docs.oracle.com/javase/8/docs/api/java/nio/charset/Charset.html
                		// 0xfeff is een byte order mark
                String pi_d = new String(pi_e,StandardCharsets.UTF_16);	// π
                printHex(pi_e);
                System.out.println(pi_d);             		
        	}
        	{
                byte[] pi_e = "π".getBytes(StandardCharsets.UTF_16);	// fe, ff, 3, c0, 
                	// want we zijn op een big endian machine, dus Ufeff wordt [fe,ff]
                	// Ufeff is een byte order mark en is [fe,ff] op be en [ff,fe] op le machine,
                	// lees https://docs.oracle.com/javase/8/docs/api/java/nio/charset/Charset.html
                	// lees https://en.wikipedia.org/wiki/Byte_order_mark
                	// StandardCharsets.UTF_16BE kijkt niet naar de bom (byte order mark), 
                	// hij ziet [03,c0] en dat is voor hem U03c0
                String pi_d = new String(pi_e,StandardCharsets.UTF_16BE);	// π
                		
                printHex(pi_e);
                System.out.println(pi_d);             		
        	}
        	{
                byte[] pi_e = "π".getBytes(StandardCharsets.UTF_16);	// fe, ff, 3, c0, 
                		// [fe,ff] omdat windows big endian is; anders hadden we ff, fe gezien,
                		// lees https://docs.oracle.com/javase/8/docs/api/java/nio/charset/Charset.html
                		// 0xfeff is een byte order mark
                		
                String pi_d = new String(pi_e,StandardCharsets.UTF_16LE);	// 쀃
             // StandardCharsets.UTF_16LE kijkt niet naar de bom (byte order mark), 
                // hij ziet [03,c0] en dat is voor hem Uc003
                printHex(pi_e);
                System.out.println(pi_d);             		
        	}
                
        }
    	private void print(byte[] bs) {
    		for (byte b : bs) {
    			System.out.print(b + ", ");
    		}
    		System.out.println();
    	}
    	private void printHex(byte[] bs) {
    		for (byte b : bs) {
    			System.out.print(Integer.toHexString(b&0xff) + ", ");
    		}
    		System.out.println();
    	}
}