package xin.keep.api.service;

import org.springframework.web.bind.annotation.RestController;
import xin.keep.base.BaseApiService;
import xin.keep.base.ResponseBase;

import java.util.HashMap;
import java.util.Map;

@RestController
public class TestApiServiceImpl extends BaseApiService implements TestApiService{
    @Override
    public Map<String, Object> test(Integer id, String name) {
        Map<String,Object> result = new HashMap<>();
        result.put("rtnCode","200");
        result.put("rtnmsg","success");
        result.put("data","id="+id+",name="+name);
        return result;
    }

    @Override
    public ResponseBase testResponseBase() {

        return setResultSuccess();
    }

    @Override
    public ResponseBase settestRedis(String key, String value) {
        baseRedisService.setString(key,value,null);
        return setResultSuccess();
    }
}
