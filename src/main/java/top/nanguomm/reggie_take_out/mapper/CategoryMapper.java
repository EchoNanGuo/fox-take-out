package top.nanguomm.reggie_take_out.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import top.nanguomm.reggie_take_out.domain.category.PO.CategoryPO;

/**
 * <p>
 * 菜品及套餐分类 Mapper 接口
 * </p>
 *
 * @author nanGuoMM
 * @since 2024-05-23
 */
@Mapper
public interface CategoryMapper extends BaseMapper<CategoryPO> {

}
