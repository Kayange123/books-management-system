package dev.coder.booker.constants;

import java.security.SecureRandom;

public abstract class Utils {
    public static String generateActivationOtp(Integer otpLength){
        String digits = "0123456789";
        StringBuilder sb = new StringBuilder();
        SecureRandom secureRandom = new SecureRandom();
        for(int i=0; i<otpLength; i++){
            int index=  secureRandom.nextInt(digits.length());
            sb.append(digits.charAt(index));
        }

        return sb.toString();
    }
}
