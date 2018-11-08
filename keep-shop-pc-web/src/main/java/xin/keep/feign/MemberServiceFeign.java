package xin.keep.feign;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.stereotype.Component;
import xin.keep.api.service.MemberService;
@FeignClient("member")
@Component
public interface MemberServiceFeign extends MemberService {
}
