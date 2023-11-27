package cn.github.iocoder.dong.model.convert;

import cn.github.iocoder.dong.model.context.dto.UserDTO;
import cn.github.iocoder.dong.service.user.repository.entity.UserDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserConvert {
    UserConvert INSTANCE = Mappers.getMapper(UserConvert.class);

    UserDTO convert(UserDO userDO);
}
