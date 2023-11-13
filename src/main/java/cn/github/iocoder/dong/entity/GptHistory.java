package cn.github.iocoder.dong.entity;

import cn.github.iocoder.dong.api.BaseDO;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.util.Date;

@TableName(value = "gpt_history", autoResultMap = true) // 由于 SQL Server 的 system_user 是关键字，所以使用 system_users
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GptHistory extends BaseDO {

private static final long serialVersionUID = -86478574331284243L;
     
    /**
    * 用户IP（没有用户id，暂时用用户IP作为id使用）
    */        private String ip;
     
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
     
    /**
    * 创建时间
    */        private Date createTime;
     
    /**
    * 更新时间
    */        private Date updateTime;
     
}

