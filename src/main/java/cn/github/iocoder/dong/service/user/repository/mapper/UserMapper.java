package cn.github.iocoder.dong.service.user.repository.mapper;

import cn.github.iocoder.dong.model.enums.YesOrNoEnum;
import cn.github.iocoder.dong.service.user.repository.entity.UserDO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<UserDO> {

    default UserDO getUserByName(String username){
        return selectOne(new LambdaQueryWrapper<UserDO>().
                eq(UserDO::getUsername,username).
                eq(UserDO::getDeleted, YesOrNoEnum.NO.getCode()));
    }
}
