package info.qianqiu.ashechoes.dto.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import info.qianqiu.ashechoes.dto.domain.Memory;
import info.qianqiu.ashechoes.dto.mapper.MemoryMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 默认名称Service业务层处理
 *
 * @author TianYiLuo
 * @date 2024-08-30
 */
@Service
public class MemoryService extends ServiceImpl<MemoryMapper, Memory> {

}
