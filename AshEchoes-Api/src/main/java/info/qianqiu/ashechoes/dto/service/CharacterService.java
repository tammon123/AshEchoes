package info.qianqiu.ashechoes.dto.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import info.qianqiu.ashechoes.dto.domain.Character;
import info.qianqiu.ashechoes.dto.mapper.CharacterMapper;
import org.springframework.stereotype.Service;

/**
 * 角色Service业务层处理
 *
 * @author TianYiLuo
 * @date 2024-08-30
 */
@Service
public class CharacterService extends ServiceImpl<CharacterMapper, Character>
{

}
