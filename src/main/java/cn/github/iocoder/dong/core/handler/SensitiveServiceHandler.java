package cn.github.iocoder.dong.core.handler;

import cn.github.iocoder.dong.core.config.properties.SensitiveProperty;
import com.github.houbb.sensitive.word.api.IWordAllow;
import com.github.houbb.sensitive.word.api.IWordDeny;
import com.github.houbb.sensitive.word.bs.SensitiveWordBs;
import com.github.houbb.sensitive.word.support.allow.WordAllowSystem;
import com.github.houbb.sensitive.word.support.deny.WordDenySystem;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class SensitiveServiceHandler {

    private volatile SensitiveWordBs sensitiveWordBs;
    @Autowired
    private SensitiveProperty sensitiveConfig;

    @PostConstruct
    public void refresh() {
        IWordDeny deny = () -> {
            List<String> sub = WordDenySystem.getInstance().deny();
            sub.addAll(sensitiveConfig.getDeny());
            return sub;
        };

        IWordAllow allow = () -> {
            List<String> sub = WordAllowSystem.getInstance().allow();
            sub.addAll(sensitiveConfig.getAllow());
            return sub;
        };
        sensitiveWordBs = SensitiveWordBs.newInstance()
                .wordDeny(deny)
                .wordAllow(allow)
                .init();
        log.info("敏感词初始化完成!");
    }

    /**
     * 判断是否包含敏感词,返回
     *
     * @param txt
     * @return
     */
    public List<String> contains(String txt) {
        if (!BooleanUtils.isTrue(sensitiveConfig.getEnable())) {
            return Collections.emptyList();
        }
        List<String> ans = sensitiveWordBs.findAll(txt);

        return ans;
    }

}
