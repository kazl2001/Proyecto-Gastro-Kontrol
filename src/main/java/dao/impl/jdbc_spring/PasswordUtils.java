package dao.impl.jdbc_spring;

import lombok.extern.log4j.Log4j2;
import org.mindrot.jbcrypt.BCrypt;
@Log4j2
public class PasswordUtils {

    // Hash the password
    public static String hashPassword(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt(12)); // 12 is the "strength" of salting
    }

    // Verify that the introduced password match with the hash
    public static boolean checkPassword(String plainPassword, String hashedPassword) {
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }
}
