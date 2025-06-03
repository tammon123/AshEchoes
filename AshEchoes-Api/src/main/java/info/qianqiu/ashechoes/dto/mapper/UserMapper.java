package info.qianqiu.ashechoes.dto.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import info.qianqiu.ashechoes.controller.vo.PoolDataUserinfoVo;
import info.qianqiu.ashechoes.dto.domain.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.ArrayList;

/**
 * 默认名称Mapper接口
 *
 * @author TianYiLuo
 * @date 2024-08-30
 */
public interface UserMapper extends BaseMapper<User> {

    @Select("select count(*) as count, type,`rank`,pool from pool_data where uid=#{uid} and pool != '海域同游' and pool != '限域巡回' and pool != '既定回响' group by type,`rank`,pool ")
    ArrayList<PoolDataUserinfoVo> groupByUserinfoData(@Param("uid") String uid);
}
