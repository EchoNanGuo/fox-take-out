package top.nanguomm.reggie_take_out.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import top.nanguomm.reggie_take_out.domain.dish.PO.DishPO;

/**
 * <p>
 * 菜品管理 Mapper 接口
 * </p>
 *
 * @author nanGuoMM
 * @since 2024-05-23
 */
@Mapper
public interface DishMapper extends BaseMapper<DishPO> {

}
