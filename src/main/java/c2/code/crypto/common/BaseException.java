package c2.code.crypto.common;

import lombok.Data;

@Data
public class BaseException extends Exception{

    private final ResCode resCode;

    public BaseException(ResCode resCode) {
        this.resCode = resCode;
    }

    public ResCode getResCode() {
        return this.resCode;
    }
}
