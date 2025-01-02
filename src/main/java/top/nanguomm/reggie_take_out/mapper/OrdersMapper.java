package top.nanguomm.reggie_take_out.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import top.nanguomm.reggie_take_out.domain.orders.Orders;

/**
 * <p>
 * 订单表 Mapper 接口
 * </p>
 *
 * @author nanGuoMM
 * @since 2024-05-30
 */
@Mapper
public interface OrdersMapper extends BaseMapper<Orders> {

}
