package top.nanguomm.fox.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import top.nanguomm.fox.domain.dish_flavor.PO.DishFlavorPO;

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
