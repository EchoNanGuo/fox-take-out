package top.nanguomm.fox.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import top.nanguomm.fox.domain.order_detail.OrderDetail;

/**
 * <p>
 * 订单明细表 Mapper 接口
 * </p>
 *
 * @author nanGuoMM
 * @since 2024-05-30
 */
@Mapper
public interface OrderDetailMapper extends BaseMapper<OrderDetail> {

}
