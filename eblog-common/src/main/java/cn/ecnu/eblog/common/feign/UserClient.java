package cn.ecnu.eblog.common.feign;

import cn.ecnu.eblog.common.pojo.result.Result;
import cn.ecnu.eblog.common.pojo.vo.UserInfoVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("user-service")
public interface UserClient {
    @GetMapping(value = "/user/{id}", headers = {"userId={id}"})
    Result<UserInfoVO> getUserInfoById(@PathVariable("id") Long id);

}
