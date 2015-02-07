package br.com.betterplace.security;

import org.apache.shiro.codec.Base64;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.Sha512Hash;
import org.apache.shiro.util.ByteSource;

public class ShiroUtils {

    public static int HASH_ITERACTIONS = 1024;

    public static void hashPassword(AuthenticationInfo info, String plainTextPassword) {
        if (plainTextPassword == null) {
            throw new SecurityException(new IllegalArgumentException("password must not be null"));
        }
        ByteSource salt = getSalt();
        String hashedPasswordBase64 = hashAndSaltPassword(plainTextPassword, salt);
        info.setHashedPassword(hashedPasswordBase64);
        info.setPasswordSalt(salt.toBase64());
    }

    public static boolean checkPassword(AuthenticationInfo info, String plainTextPassword) {
        if (info == null || info.getPasswordSalt() == null || info.getHashedPassword() == null || plainTextPassword == null) {
            throw new SecurityException(new IllegalArgumentException("password and user's authentication information must not be null"));
        }
        ByteSource salt = ByteSource.Util.bytes(Base64.decode(info.getPasswordSalt()));
        String hashedPassword = hashAndSaltPassword(plainTextPassword, salt);
        return hashedPassword.equals(info.getHashedPassword());
    }

    private static String hashAndSaltPassword(String plainTextPassword, ByteSource salt) {
        return new Sha512Hash(plainTextPassword, salt, HASH_ITERACTIONS).toBase64();
    }

    private static ByteSource getSalt() {
        return new SecureRandomNumberGenerator().nextBytes();
    }
}