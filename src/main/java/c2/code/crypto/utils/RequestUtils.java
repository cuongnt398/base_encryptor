package c2.code.crypto.utils;

import c2.code.crypto.common.BaseException;
import c2.code.crypto.common.ResCode;
import c2.code.crypto.crypto.AES;
import c2.code.crypto.crypto.HmacSHA512;
import c2.code.crypto.common.BaseRequest;
import c2.code.crypto.dto.UserDto;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.UUID;

public class RequestUtils {

    private static final Logger logger = LoggerFactory.getLogger(RequestUtils.class);

    private RequestUtils() {

    }

    public static String parseRequest(String data, String mac, String aesKey, String hmacKey) throws BaseException {
        try {
            byte[] encryptedDataWithIV = Base64.getDecoder().decode(data);
            byte[] iv = new byte[AES.IV_LENGTH];
            byte[] encryptedData = new byte[encryptedDataWithIV.length - iv.length];
            System.arraycopy(encryptedDataWithIV, 0, iv, 0, iv.length);
            System.arraycopy(encryptedDataWithIV, iv.length, encryptedData, 0, encryptedData.length);

            String serverMac = HmacSHA512.generateMac(encryptedDataWithIV, hmacKey);
            if (!serverMac.equals(mac)) {
                logger.error("Invalid mac serverMac: {}, clientMac: {}", serverMac, mac);
                throw new BaseException(ResCode.INVALID_MAC);
            }
            return AES.decrypt(encryptedData, aesKey, iv);
        } catch (BaseException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new BaseException(ResCode.INVALID_ENCRYPTED_DATA);
        }
    }

    public static BaseRequest newRequest(String data, String aesKey, String hmacKey) throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        byte[] iv = AES.generateIV();
        byte[] encryptedData = AES.encrypt(data, aesKey, iv);
        byte[] encryptedDataWithIv = new byte[encryptedData.length + iv.length];
        System.arraycopy(iv, 0, encryptedDataWithIv, 0, iv.length);
        System.arraycopy(encryptedData, 0, encryptedDataWithIv, iv.length, encryptedData.length);
        BaseRequest baseRequest = new BaseRequest();
        baseRequest.setEncryptedData(Base64.getEncoder().encodeToString(encryptedDataWithIv));
        baseRequest.setMac(HmacSHA512.generateMac(encryptedDataWithIv, hmacKey));
        return baseRequest;
    }

    /* Run this method to see how can generate request with data encrypted && how parse request to see raw data */
    public static void main(String[] args) throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException, BaseException {
        String aesKey = "UkXp2s5v8x/A?D(G+KbPeShVmYq3t6w9";
        String hmacKey = "E(H+KbPeShVmYq3t6w9z$C&F)J@NcQfTjWnZr4u7x!A%D*G-KaPdSgUkXp2s5v8y";
        Gson gson = new Gson();
        UserDto userDto = new UserDto();
        userDto.setUsername("cuongnt");
        userDto.setPassword(UUID.randomUUID().toString());
        String user = gson.toJson(userDto);
        logger.info("User info (rawData): {}", user);
        BaseRequest requestEncrypted = newRequest(user, aesKey, hmacKey);
        logger.info("Generated request: {}", requestEncrypted);
        String rawData = parseRequest(requestEncrypted.getEncryptedData(), requestEncrypted.getMac(), aesKey, hmacKey);
        logger.info("Raw data after decrypt: {}", rawData);
    }
}
