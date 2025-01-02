package top.nanguomm.reggie_take_out.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import top.nanguomm.reggie_take_out.domain.dish_flavor.PO.DishFlavorPO;

/**
 * <p>
 * 菜品口味关系表 Mapper 接口
 * </p>
 *
 * @author nanGuoMM
 * @since 2024-05-24
 */
@Mapper
public interface DishFlavorMapper extends BaseMapper<DishFlavorPO> {

}
