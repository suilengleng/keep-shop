package xin.keep.api.service;

import org.springframework.web.bind.annotation.RequestMapping;
import xin.keep.base.ResponseBase;

import java.util.Map;

@RequestMapping("/member")
public interface TestApiService {
    @RequestMapping("/test")
    public Map<String,Object> test(Integer id,String name);
    @RequestMapping("/testResponseBase")
    public ResponseBase testResponseBase();
    @RequestMapping("/testRedis")
    public ResponseBase settestRedis(String key,String value);
}
