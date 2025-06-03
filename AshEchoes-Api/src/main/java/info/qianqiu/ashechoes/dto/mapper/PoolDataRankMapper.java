package info.qianqiu.ashechoes.dto.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import info.qianqiu.ashechoes.controller.vo.PoolDataVo;
import info.qianqiu.ashechoes.dto.domain.PoolDataRank;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Date;
import java.util.List;

/**
 * 默认名称Mapper接口
 *
 * @author TianYiLuo
 * @date 2024-08-30
 */
public interface PoolDataRankMapper extends BaseMapper<PoolDataRank> {

    @Select("SELECT " +
            "CASE " +
            "WHEN DATE(last_time) = DATE(#{datee}) " +
            "THEN 1 " +
            "ELSE 0 " +
            "END " +
            "FROM pool_data_rank where uid=#{uid} limit 1 ")
    public Integer queryTodayUpdated(@Param("datee") Date date, @Param("uid") String uid);

    @Select("<script> "+
            "SELECT " +
            "    COUNT(*) " +
            "FROM pool_data_rank " +
            "WHERE  pool_type=${type} and allow=1 and t_count &gt; ${ignum} and ${beh} " +
            "<if test=\" sort == 0 \"> "+
            " &gt; " +
            "</if>" +
            "<if test=\" sort == 1 \"> "+
            " &lt; " +
            "</if>" +
            "(SELECT ${beh} FROM pool_data_rank WHERE uid = #{uid} and t_count &gt; ${ignum} and pool_type=${type} and allow=1) "+
            "</script> ")
    Integer getUserRank(@Param("type") int type, @Param("sort") int sort, @Param("beh") String beh,
                     @Param("uid") String uid,@Param("ignum") int ignum);

    @Select("select name, count(*) as count from pool_data where uid = #{uid} and type = 1 and `rank` in (2,3) group by name")
    List<PoolDataVo> getMemoryGroupCountByUid(@Param("uid") String uid);
}
