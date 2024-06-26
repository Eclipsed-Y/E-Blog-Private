package cn.ecnu.eblog.common.pojo.dto;

import lombok.Data;

@Data
public class UserInfoDTO {
    private Long userId;
    private String nickname;
    private String avatar;
    private String position;
    private String profile;
}
