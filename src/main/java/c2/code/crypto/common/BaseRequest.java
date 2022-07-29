package c2.code.crypto.common;

import lombok.Data;

@Data
public class BaseRequest {

    private String encryptedData;
    private String mac;
}
