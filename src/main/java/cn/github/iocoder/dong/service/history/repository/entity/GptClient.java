package cn.github.iocoder.dong.service.history.repository.entity;

import cn.github.iocoder.dong.model.api.BaseDO;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.Date;

@TableName(value = "gpt_client", autoResultMap = true) // 由于 SQL Server 的 system_user 是关键字，所以使用 system_users
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
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
    * 是否删除
    */        private Integer deleted;
     
}

