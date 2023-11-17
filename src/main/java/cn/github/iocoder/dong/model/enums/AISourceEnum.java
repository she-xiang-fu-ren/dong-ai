package cn.github.iocoder.dong.model.enums;

import lombok.Getter;

/**
 * @author dong
 * @date 2023/11/16
 */
@Getter
public enum AISourceEnum {
    /**
     * 阿里巴巴
     */
    ALIBABA_QIAN_WEN(0, "阿里巴巴通义千问大模型"),
    /**
     * 阿里巴巴
     */
    ALIBABA_WAN_XIANG(1, "阿里巴巴通义万象大模型"),
    /**
     * 百度千帆大模型
     */
    BAI_DU_AI(2, "百度千帆大模型"),
    /**
     * 讯飞
     */
    XUN_FEI_AI_1_5(3,"讯飞火星大模型1.5") {
        @Override
        public boolean syncSupport() {
            return false;
        }
    },
    /**
     * 讯飞
     */
    XUN_FEI_AI_2_0(4,"讯飞火星大模型2.0") {
        @Override
        public boolean syncSupport() {
            return false;
        }
    },
    /**
     * 讯飞
     */
    XUN_FEI_AI_3_0(5,"讯飞火星大模型3.0") {
        @Override
        public boolean syncSupport() {
            return false;
        }
    },
    ;

    private String name;
    private Integer code;

    AISourceEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    /**
     * 是否支持同步
     *
     * @return
     */
    public boolean syncSupport() {
        return true;
    }

    /**
     * 是否支持异步
     *
     * @return
     */
    public boolean asyncSupport() {
        return true;
    }
}
