package top.nanguomm.fox.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import top.nanguomm.fox.domain.user.PO.UserPO;

/**
 * <p>
 * 用户信息 Mapper 接口
 * </p>
 *
 * @author nanGuoMM
 * @since 2024-05-28
 */
@Mapper
public interface UserMapper extends BaseMapper<UserPO> {

}
