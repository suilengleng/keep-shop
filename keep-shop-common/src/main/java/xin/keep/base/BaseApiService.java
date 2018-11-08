package xin.keep.base;

import org.springframework.beans.factory.annotation.Autowired;
import xin.keep.constants.Constants;

public class BaseApiService {
    @Autowired
    protected BaseRedisService baseRedisService;
    public ResponseBase setResultSuccess(Object data){
        return setResult(Constants.HTTP_RES_CODE_200,Constants.HTTP_RES_CODE_200_VALUE,data);
    }
    public ResponseBase setResultSuccess(){
        return setResult(Constants.HTTP_RES_CODE_200,Constants.HTTP_RES_CODE_200_VALUE,null);
    }
    public ResponseBase setResultError(String msg){
        return setResult(Constants.HTTP_RES_CODE_500,msg,null);
    }

    public ResponseBase setResult(Integer code,String msg,Object data){
        return new ResponseBase(code,msg,data);
    }
    public ResponseBase setResultSuccess(String msg){
        return setResult(Constants.HTTP_RES_CODE_200,msg,null);
    }
}
