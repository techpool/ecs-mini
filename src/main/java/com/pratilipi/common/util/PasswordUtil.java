package com.pratilipi.common.util;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import org.apache.commons.codec.binary.Base64;


// TODO: Refactor PasswordUtil
// TODO: Refactor PasswordUtil
// TODO: Refactor PasswordUtil
// TODO: Refactor PasswordUtil
// TODO: Refactor PasswordUtil
public class PasswordUtil {
	
	private static final Logger logger = 
			Logger.getLogger( PasswordUtil.class.getName() );

	
	// The higher the number of iterations the more 
    // expensive computing the hash is for us
    // and also for a brute force attack.
    private static final int iterations = 10*1024;
    private static final int saltLen = 32;
    private static final int desiredKeyLen = 256;

    /** Computes a salted PBKDF2 hash of given plaintext password
        suitable for storing in a database. 
        Empty passwords are not supported. */
    public static String getSaltedHash(String password) {
    	try {
    		byte[] salt = SecureRandom.getInstance("SHA1PRNG").generateSeed(saltLen);
    		// store the salt with the password
    		return new String(Base64.encodeBase64(salt)) + "$" + hash(password, salt);
    	} catch( NoSuchAlgorithmException | InvalidKeySpecException e ) {
    		logger.log( Level.SEVERE, "", e );
    		return password;
    	}
    }

    /** Checks whether given plaintext password corresponds 
        to a stored salted hash of the password. */
    public static boolean check(String password, String stored) {
        String[] saltAndPass = stored.split("\\$");
        if (saltAndPass.length != 2)
            return false;
        String hashOfInput;
		try {
			hashOfInput = hash(password, Base64.decodeBase64(saltAndPass[0]));
		} catch( NoSuchAlgorithmException | InvalidKeySpecException e ) {
    		logger.log( Level.SEVERE, "", e );
    		hashOfInput = password;
		}
        return hashOfInput.equals(saltAndPass[1]);
    }

    // using PBKDF2 from Sun, an alternative is https://github.com/wg/scrypt
    // cf. http://www.unlimitednovelty.com/2012/03/dont-use-bcrypt.html
    private static String hash(String password, byte[] salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
        if (password == null || password.length() == 0)
            throw new IllegalArgumentException("Empty passwords are not supported.");
        SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        SecretKey key = f.generateSecret(new PBEKeySpec(
            password.toCharArray(), salt, iterations, desiredKeyLen)
        );
        return Base64.encodeBase64String(key.getEncoded());
    }

}
