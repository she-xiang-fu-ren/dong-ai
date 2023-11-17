package cn.github.iocoder.dong.entity;

import java.util.Date;

import cn.github.iocoder.dong.api.BaseDO;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

@TableName(value = "gpt_user", autoResultMap = true) // 由于 SQL Server 的 system_user 是关键字，所以使用 system_users
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GptUser extends BaseDO {

private static final long serialVersionUID = 778562368167475185L;

    /**
    * id
    */
    @TableId(type = IdType.AUTO)
    private Long id;

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

