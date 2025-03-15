package top.nanguomm.fox.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import top.nanguomm.fox.domain.setmeal_dish.PO.SetmealDishPO;

/**
 * <p>
 * 套餐菜品关系 Mapper 接口
 * </p>
 *
 * @author nanGuoMM
 * @since 2024-05-26
 */
@Mapper
public interface SetmealDishMapper extends BaseMapper<SetmealDishPO> {

}
