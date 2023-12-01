package cn.github.iocoder.dong.service.user.repository.mapper;

import cn.github.iocoder.dong.model.enums.YesOrNoEnum;
import cn.github.iocoder.dong.service.user.repository.entity.UserDO;
import cn.github.iocoder.dong.service.user.repository.entity.UserInfoDO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserInfoMapper extends BaseMapper<UserInfoDO> {

    default UserInfoDO getUserId(Long userId){
        return selectOne(new LambdaQueryWrapper<UserInfoDO>().
                eq(UserInfoDO::getUserId,userId).
                eq(UserInfoDO::getDeleted, YesOrNoEnum.NO.getCode()));
    }
}
