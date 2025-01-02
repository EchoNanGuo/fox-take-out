package top.nanguomm.reggie_take_out.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.transaction.annotation.Transactional;
import top.nanguomm.reggie_take_out.domain.orders.Orders;
import top.nanguomm.reggie_take_out.domain.orders.OrdersDTO;

import java.time.LocalDateTime;

/**
 * <p>
 * 订单表 服务类
 * </p>
 *
 * @author nanGuoMM
 * @since 2024-05-30
 */
@Transactional
public interface IOrdersService extends IService<Orders> {

    IPage<OrdersDTO> pageOrder(Integer page, Integer pageSize, LocalDateTime beginTime, LocalDateTime endTime, Long number);

    void updateStatus(Orders orders);

    void submit(Orders orders, Long userId);

    IPage<OrdersDTO> pageOrderFront(Integer page, Integer pageSize, Long userId);
}
