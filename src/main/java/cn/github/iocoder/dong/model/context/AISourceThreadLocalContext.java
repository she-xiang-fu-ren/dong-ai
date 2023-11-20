package cn.github.iocoder.dong.model.context;

import cn.github.iocoder.dong.model.enums.AISourceEnum;
import com.alibaba.ttl.TransmittableThreadLocal;
import org.springframework.util.Assert;

public class AISourceThreadLocalContext {

    private static final ThreadLocal<AISourceEnum> SOURCE_ENUM_THREAD_LOCAL = new TransmittableThreadLocal<AISourceEnum>();


    public static AISourceEnum getContext(){
        return SOURCE_ENUM_THREAD_LOCAL.get();
    }

    public static void setContext(AISourceEnum aiSourceEnum){
        Assert.notNull(aiSourceEnum, "Only non-null SecurityContext instances are permitted");
        SOURCE_ENUM_THREAD_LOCAL.set(aiSourceEnum);
    }

    public static  void cloneContext(){
        SOURCE_ENUM_THREAD_LOCAL.remove();
    }

}
