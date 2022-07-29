package c2.code.crypto.restcontroller;

import c2.code.crypto.common.BaseException;
import c2.code.crypto.common.BaseRequest;
import c2.code.crypto.common.BaseResponse;
import c2.code.crypto.dto.UserDto;
import c2.code.crypto.service.RequestService;
import c2.code.crypto.utils.RequestUtils;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
public class DemoController {

    @Autowired
    private RequestService requestService;

    private static final Logger logger = LoggerFactory.getLogger(DemoController.class);

    @PostMapping("/test")
    public BaseResponse test(@RequestBody BaseRequest request) {
        try {
            logger.info("Received encrypted request: {}", request);
            String user = requestService.parseRequest(request);
            logger.info("Raw user info: {}", user);
            /*
             * Handle logic...
             * */
            return BaseResponse.success(user);
        } catch (BaseException ex) {
            logger.error("Invalid request, error: {}", ex.getResCode());
            return BaseResponse.fail(ex);
        } catch (Exception ex) {
            logger.error("Internal error, error: {}", ex.toString());
        }
        return BaseResponse.internalError();
    }

    /*
     * Run this method to fake request with data
     * */
    public static void main(String[] args) throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException, BaseException {
        Gson gson = new Gson();
        String aesKey = "UkXp2s5v8x/A?D(G+KbPeShVmYq3t6w9";
        String hmacKey = "E(H+KbPeShVmYq3t6w9z$C&F)J@NcQfTjWnZr4u7x!A%D*G-KaPdSgUkXp2s5v8y";
        UserDto userDto = new UserDto();
        userDto.setUsername("cuongnt");
        userDto.setPassword(UUID.randomUUID().toString());
        String user = gson.toJson(userDto);
        logger.info("User info (rawData): {}", user);
        BaseRequest requestEncrypted = RequestUtils.newRequest(user, aesKey, hmacKey);
        logger.info("Generated request: {}", requestEncrypted);
        /*
        * Example:
        * Generated request:
        * BaseRequest(encryptedData=iOtC3LGOrhfcub4Dw9D8L5k5t0J+sNGahaF+BM8PLfCPrgKFUxZh/+7JTWlbAUz8ley91CjY+zvDHjiUau345ztp1TLerwmZdgqE9FGFAo+P5fHsMJgkfA==,
        * mac=FChDI49xT99299t+M+MXKDaJi/Afy5M8EITHXQe1NcMa9yFpKe96/Ya2ql5hL6YRH2I/OS4cbWEeOnFo1Ss3kA==)
        * */
        // Copy this to postman call api test
        String rawData = RequestUtils.parseRequest(requestEncrypted.getEncryptedData(), requestEncrypted.getMac(), aesKey, hmacKey);
        logger.info("Raw data after decrypt: {}", rawData);

    }
}
