package top.nanguomm.reggie_take_out.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.transaction.annotation.Transactional;
import top.nanguomm.reggie_take_out.domain.dish.DTO.DishDTO;
import top.nanguomm.reggie_take_out.domain.dish.PO.DishPO;

import java.util.List;

/**
 * <p>
 * 菜品管理 服务类
 * </p>
 *
 * @author nanGuoMM
 * @since 2024-05-23
 */
@Transactional
public interface IDishService extends IService<DishPO> {
    IPage<DishDTO> pageDish(String page, String PageSize, String name);

    void saveDish(DishDTO dishDto);

    DishDTO getDishById(Long id);

    void updateDish(DishDTO dishDTO);

    void deleteDish(List<Long> ids);

    void updateStatus(Integer status, List<Long> ids);

    List<DishDTO> listDish(Long categoryId, Integer status);
}
