package top.nanguomm.fox.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.nanguomm.fox.mapper.OrderDetailMapper;
import top.nanguomm.fox.domain.order_detail.OrderDetail;
import top.nanguomm.fox.service.IOrderDetailService;

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
