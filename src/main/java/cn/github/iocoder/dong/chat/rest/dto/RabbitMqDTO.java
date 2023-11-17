package cn.github.iocoder.dong.chat.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class RabbitMqDTO implements Serializable {

    private String msg;

    private String session;

    private String aiType;

}
