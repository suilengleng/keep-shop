package xin.keep.base;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseBase {
    private Integer rtnCode;
    private String msg;
    private Object data;
    public ResponseBase(){

    }
    public ResponseBase(Integer rtnCode, String msg, Object oj) {
        this.rtnCode = rtnCode;
        this.msg = msg;
        this.data = oj;
    }


}
