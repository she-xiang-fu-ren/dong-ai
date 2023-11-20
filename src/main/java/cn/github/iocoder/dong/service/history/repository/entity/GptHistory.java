package cn.github.iocoder.dong.service.history.repository.entity;

import cn.github.iocoder.dong.model.api.BaseDO;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.Date;

@TableName(value = "gpt_history", autoResultMap = true) // 由于 SQL Server 的 system_user 是关键字，所以使用 system_users
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class GptHistory extends BaseDO {

private static final long serialVersionUID = -86478574331284243L;
     
    /**
    * 用户IP（没有用户id，暂时用用户IP作为id使用）
    */        private Long userId;
     
    /**
    * 客户端类型
    */        private String aiType;
     
    /**
    * 问答
    */        private String question;
     
    /**
    * 回答
    */        private String answer;
     
    /**
    * 是否删除
    */        private Integer deleted;
     
}

