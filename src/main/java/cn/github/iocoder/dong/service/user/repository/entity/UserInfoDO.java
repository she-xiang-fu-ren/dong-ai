package cn.github.iocoder.dong.service.user.repository.entity;

import cn.github.iocoder.dong.model.api.BaseDO;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import lombok.experimental.Accessors;

@TableName(value = "gpt_user_info", autoResultMap = true) // 由于 SQL Server 的 system_user 是关键字，所以使用 system_users
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class UserInfoDO extends BaseDO {

private static final long serialVersionUID = 778562368167475145L;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 照片地址
     */
    private String photo;
     
    /**
    * 职位
    */        
    private String position;
     
    /**
    * 公司
    */        
    private String company;

    /**
     * 个人介绍
     */
    private String profile;

    /**
     * 是否删除
     */
    private Integer deleted;

}

