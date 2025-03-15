package top.nanguomm.fox.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.nanguomm.fox.domain.user.PO.UserPO;
import top.nanguomm.fox.mapper.UserMapper;
import top.nanguomm.fox.service.IUserService;

/**
 * <p>
 * 用户信息 服务实现类
 * </p>
 *
 * @author nanGuoMM
 * @since 2024-05-28
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, UserPO> implements IUserService {

}
