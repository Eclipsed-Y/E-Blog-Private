package cn.ecnu.eblog.user.controller;

import cn.ecnu.eblog.common.annotation.RunningTime;
import cn.ecnu.eblog.common.context.BaseContext;
import cn.ecnu.eblog.common.pojo.dto.PasswordDTO;
import cn.ecnu.eblog.common.pojo.dto.UserDTO;
import cn.ecnu.eblog.common.pojo.entity.user.UserDO;
import cn.ecnu.eblog.common.pojo.entity.user.UserInfoDO;
import cn.ecnu.eblog.common.pojo.result.Result;
import cn.ecnu.eblog.user.service.UserInfoService;
import cn.ecnu.eblog.user.service.UserService;
import cn.ecnu.eblog.user.utils.PasswordUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.LoginException;


@RequestMapping("/user")
@RestController
public class LoginController {
    @Autowired
    private UserService userService;
    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private PasswordUtil passwordUtil;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private TransactionTemplate transactionTemplate;
    @PostMapping("/signup")
    @RunningTime
    public Result<?> signup(@RequestBody UserDTO user){
        UserDO userDO = new UserDO();
        UserInfoDO userInfoDO = new UserInfoDO();
        BeanUtils.copyProperties(user, userDO);
        BeanUtils.copyProperties(user, userInfoDO);
        userDO.setPassword(passwordUtil.encPwd(userDO.getPassword()));  // 加盐后md5加密

        transactionTemplate.execute(new TransactionCallback<Object>() {
            @Override
            public Object doInTransaction(TransactionStatus transactionStatus) {
                userService.save(userDO);
                userInfoDO.setUserId(userDO.getId());
                userInfoService.save(userInfoDO);
                return null;
            }
        });

        return Result.success();
    }

    @PostMapping("/login")
    @RunningTime
    public Result<?> login(@RequestBody UserDTO user) {
        return Result.success(userService.login(user));
    }

    @PostMapping("/logout")
    @RunningTime
    public Result<?> logout(){
        redisTemplate.delete("token:" + BaseContext.getCurrentId());
        return Result.success();
    }

    @PutMapping("/password")
    @RunningTime
    public Result<?> changePassword(@RequestBody PasswordDTO passwordDTO){
        userService.changePassword(passwordDTO.getOldPassword(), passwordDTO.getNewPassword());
        return logout();
    }
}
