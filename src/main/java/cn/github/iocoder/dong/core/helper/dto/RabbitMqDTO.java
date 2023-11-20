package cn.github.iocoder.dong.core.helper.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RabbitMqDTO implements Serializable {

    private String msg;

    private String session;

    private String aiType;

}
