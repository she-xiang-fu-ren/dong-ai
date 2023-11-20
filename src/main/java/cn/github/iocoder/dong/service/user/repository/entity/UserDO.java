package cn.github.iocoder.dong.service.user.repository.entity;

import cn.github.iocoder.dong.model.api.BaseDO;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import lombok.experimental.Accessors;

@TableName(value = "gpt_user", autoResultMap = true) // 由于 SQL Server 的 system_user 是关键字，所以使用 system_users
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class UserDO extends BaseDO {

private static final long serialVersionUID = 778562368167475185L;


    /**
     * 账号
     */
    private String username;
     
    /**
    * 手机号
    */        
    private String phone;


    /**
     * 照片地址
     */
    private String photoUrl;
     
    /**
    * 密码
    */        
    private String password;
     
    /**
    * 是否删除
    */        
    private Integer deleted;
     
}

