package info.qianqiu.ashechoes.dto.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import info.qianqiu.ashechoes.dto.domain.Cards;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 默认名称Mapper接口
 *
 * @author TianYiLuo
 * @date 2024-08-30
 */
public interface CardsMapper extends BaseMapper<Cards> {

    @Select("select * from cards where char_names like concat(#{cards.keyWord}, '%') " +
            " and del=0 and card_id in (select cards_id as card_id from cards_user where del=0 and uid=#{cards.uid} order by create_time desc ) order by create_time desc limit ${page}, ${size}")
    List<Cards> pageList(@Param("page") int page, @Param("size") int size, @Param("cards") Cards cards);
}
