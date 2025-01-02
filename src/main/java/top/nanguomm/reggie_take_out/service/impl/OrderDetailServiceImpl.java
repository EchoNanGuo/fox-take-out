package top.nanguomm.reggie_take_out.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.nanguomm.reggie_take_out.mapper.OrderDetailMapper;
import top.nanguomm.reggie_take_out.domain.order_detail.OrderDetail;
import top.nanguomm.reggie_take_out.service.IOrderDetailService;

/**
 * <p>
 * 订单明细表 服务实现类
 * </p>
 *
 * @author nanGuoMM
 * @since 2024-05-30
 */
@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements IOrderDetailService {

}
