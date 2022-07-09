package test;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;



/**
 * 用于生成密码单向HASH值的工具
 * 
 * 
 * 参考资料:
 * <li>Apache Shiro 1.3.2  /org.apache.shiro.crypto.hash.SimpleHash</li>
 * <li>https://www.owasp.org/index.php/Hashing_Java</li>
 * <li>https://www.owasp.org/index.php/Password_Storage_Cheat_Sheet</li>

 *
 */
public final class PasswordHashUtil  {
	
	private static final Charset DEFAULT_CHARSET=Charset.forName("UTF-8");
	
	private static final String ALGORITHM_NAME="SHA-256";
	/**
     * Used to build output as Hex
     */
    private static final char[] DIGITS = {
            '0', '1', '2', '3', '4', '5', '6', '7',
            '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'
    };
	
    /**
     * 对源数据求Hash值
     * 
     * @param source Hash源
     * @param salt 随机数，每个用户应该有自己的salt, 可以通过generateSaltString()生成
     * @param hashIterations hash迭代次数，例如64/256/1024
     * @return
     * @throws NoSuchAlgorithmException
     */
	public static String hash(String source, byte[] salt, int hashIterations) throws NoSuchAlgorithmException {
        MessageDigest digest = getDigest(ALGORITHM_NAME);
        if (salt != null) {
            digest.reset();
            digest.update(salt);
        }
        byte[] hashed = digest.digest(source.getBytes(DEFAULT_CHARSET));
        int iterations = hashIterations - 1; //already hashed once above
        //iterate remaining number:
        for (int i = 0; i < iterations; i++) {
            digest.reset();
            hashed = digest.digest(hashed);
        }
        return toHexString(hashed);
    }
	private static MessageDigest getDigest(String algorithmName) throws NoSuchAlgorithmException {
	    return MessageDigest.getInstance(algorithmName);
	}
	
    public static String toHexString(byte[] bytes) {
        char[] encodedChars = encode(bytes);
        return new String(encodedChars);
    }
    
    public static byte[] generateSalt() {
        SecureRandom random = new SecureRandom();
        byte bytes[] = new byte[32];
        random.nextBytes(bytes);
        return bytes;
    }
    
    public static String generateSaltString() {
        SecureRandom random = new SecureRandom();
        byte bytes[] = new byte[32];
        random.nextBytes(bytes);
        return toHexString(bytes);
    }

    public static byte[] decodeHexString(String hexString){
    	return decode(hexString.toCharArray());
    }
    private static char[] encode(byte[] data) {
        int l = data.length;
        char[] out = new char[l << 1];
        // two characters form the hex value.
        for (int i = 0, j = 0; i < l; i++) {
            out[j++] = DIGITS[(0xF0 & data[i]) >>> 4];
            out[j++] = DIGITS[0x0F & data[i]];
        }
        return out;
    }
    
    private static byte[] decode(char[] data) throws IllegalArgumentException {
        int len = data.length;
        if ((len & 0x01) != 0) {
            throw new IllegalArgumentException("Odd number of characters.");
        }
        byte[] out = new byte[len >> 1];
        for (int i = 0, j = 0; j < len; i++) {
            int f = toDigit(data[j], j) << 4;
            j++;
            f = f | toDigit(data[j], j);
            j++;
            out[i] = (byte) (f & 0xFF);
        }

        return out;
    }
    private static int toDigit(char ch, int index) throws IllegalArgumentException {
        int digit = Character.digit(ch, 16);
        if (digit == -1) {
            throw new IllegalArgumentException("Illegal hexadecimal charcter " + ch + " at index " + index);
        }
        return digit;
    }
    
    public static void main(String[] args) {
		try {
			System.out.println(hash("hello world", generateSalt(), 1024));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}

}
