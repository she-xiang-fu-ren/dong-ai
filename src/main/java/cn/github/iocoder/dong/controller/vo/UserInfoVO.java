package cn.github.iocoder.dong.controller.vo;

import cn.github.iocoder.dong.service.user.repository.entity.UserInfoDO;
import lombok.Data;

@Data
public class UserInfoVO extends UserInfoDO {
    /**
     * 身份信息完整度百分比
     */
    private Integer infoPercent;

    /**
     * 加入天数
     */
    private Integer joinDayCount;

    /**
     * 用户登录区域
     */
    private String region;
}
