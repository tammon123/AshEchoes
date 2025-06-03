package info.qianqiu.ashechoes.dto.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import info.qianqiu.ashechoes.dto.domain.Story;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.ArrayList;
import java.util.List;

/**
 * 伤害乘区Mapper接口
 *
 * @author TianYiLuo
 * @date 2024-08-30
 */
public interface StoryMapper extends BaseMapper<Story> {

    @Select({
            "<script>",
            "SELECT * FROM story WHERE sort IN(",
            "(SELECT MAX(sort) FROM story WHERE sort &lt; ${sort} and type in ",
            "<foreach item='item' index='index' collection='list' open='(' separator=',' close=')'>",
            "#{item}",
            "</foreach>) ,",
            "${sort} ,",
            "(SELECT MIN(sort) FROM story WHERE sort &gt; ${sort} and type in ",
            "<foreach item='item' index='index' collection='list' open='(' separator=',' close=')'>",
            "#{item}",
            "</foreach>)) order by sort asc ",
            "</script>"
    })
    ArrayList<Story> getStoryFilterSort(@Param("sort") Long sort, @Param("list") List<String> list);
}
