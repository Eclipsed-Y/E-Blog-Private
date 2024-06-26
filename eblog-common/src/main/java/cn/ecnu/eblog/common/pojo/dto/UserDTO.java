package cn.ecnu.eblog.common.pojo.dto;

import lombok.Data;

@Data
public class UserDTO {
    private String username;
    private String password;
    private String nickname;
    private String avatar;
    private String position;
    private String profile;
}
