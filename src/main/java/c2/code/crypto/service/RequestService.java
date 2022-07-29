package c2.code.crypto.service;

import c2.code.crypto.common.BaseException;
import c2.code.crypto.common.BaseRequest;
import c2.code.crypto.utils.RequestUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Service
public class RequestService {

    @Value("${key.aes}")
    private String aesKey;

    @Value("${key.hmac}")
    private String hmacKey;

    /* Generate request from raw data*/
    public BaseRequest newRequest(String data) throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        return RequestUtils.newRequest(data, aesKey, hmacKey);
    }

    /* Parse request with encrypted data -> raw data to handle*/
    public String parseRequest(BaseRequest request) throws BaseException, InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        return RequestUtils.parseRequest(request.getEncryptedData(), request.getMac(), aesKey, hmacKey);
    }
}
