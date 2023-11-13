package cn.github.iocoder.dong.entity;

import cn.github.iocoder.dong.api.BaseDO;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.util.Date;

@TableName(value = "gpt_client", autoResultMap = true) // 由于 SQL Server 的 system_user 是关键字，所以使用 system_users
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GptClient extends BaseDO {

private static final long serialVersionUID = 747214347680712999L;

    /**
    * 应用名
    */        private String name;

    /**
     * 客户端id
     */       private String appId;
     
    /**
    * 客户端key
    */        private String clientKey;
     
    /**
    * 客户端secret
    */        private String clientSecret;
     
    /**
    * 状态
    */        private Integer status;
     
    /**
    * 创建时间
    */        private Date createTime;
     
    /**
    * 修改时间
    */        private Date updateTime;
     
    /**
    * 是否删除
    */        private Integer deleted;
     
}

