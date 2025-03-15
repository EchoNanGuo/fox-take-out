package top.nanguomm.fox.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import top.nanguomm.fox.domain.orders.Orders;

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
