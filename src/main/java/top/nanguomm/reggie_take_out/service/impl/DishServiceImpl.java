package top.nanguomm.reggie_take_out.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import top.nanguomm.reggie_take_out.domain.category.PO.CategoryPO;
import top.nanguomm.reggie_take_out.domain.dish.DTO.DishDTO;
import top.nanguomm.reggie_take_out.domain.dish.PO.DishPO;
import top.nanguomm.reggie_take_out.domain.dish_flavor.DTO.DishFlavorDTO;
import top.nanguomm.reggie_take_out.domain.dish_flavor.PO.DishFlavorPO;
import top.nanguomm.reggie_take_out.mapper.DishMapper;
import top.nanguomm.reggie_take_out.service.ICategoryService;
import top.nanguomm.reggie_take_out.service.IDishFlavorService;
import top.nanguomm.reggie_take_out.service.IDishService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 菜品管理 服务实现类
 * </p>
 *
 * @author nanGuoMM
 * @since 2024-05-23
 */
@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, DishPO> implements IDishService {

    @Autowired
    private IDishFlavorService dishFlavorService;

    @Autowired
    private ICategoryService categoryService;

    @Cacheable(cacheNames = "dishCache",key = "'page:' + #page  + '_pageSize:' + #pageSize + '_name:' + #name")
    @Override
    public IPage<DishDTO> pageDish(String page, String pageSize, String name) {
        //查询
        LambdaQueryWrapper<DishPO> queryWrapper = new LambdaQueryWrapper<DishPO>()
                .like(StringUtils.hasLength(name),DishPO::getName,name)
                .orderByAsc(DishPO::getSort).orderByDesc(DishPO::getUpdateTime);
        Page<DishPO> pagePo = new Page<>(Integer.parseInt(page), Integer.parseInt(pageSize));
        super.page(pagePo, queryWrapper);

        //将DishPO转换为DishDTO，并按id查询菜品分类
        List<DishDTO> dishDTOS = pagePo.getRecords().stream().map(po -> {
            DishDTO dto = new DishDTO();
            //调用工具类转换
            BeanUtils.copyProperties(po,dto);

            //查询菜品分类名并赋值给dto
            CategoryPO categoryPO = categoryService.getById(po.getCategoryId());
            dto.setCategoryName(categoryPO.getName());

            return dto;
        }).collect(Collectors.toList());

        //将List<DishDTO>转换为Page<DishDTO>
        Page<DishDTO> dishDTOPage = new Page<>(Integer.parseInt(page), Integer.parseInt(pageSize), pagePo.getTotal());
        dishDTOPage.setRecords(dishDTOS);
        return dishDTOPage;
    }

    @CacheEvict(cacheNames = "dishCache",allEntries = true)
    @Override
    public void saveDish(DishDTO dishDto) {

        //封装DishPO对象，并保存
        DishPO dishPO = new DishPO();
        BeanUtils.copyProperties(dishDto,dishPO);
        super.save(dishPO);

        //封装DishFlavorPo类，并添加到相关表中
        Long dishID = dishPO.getId();//菜品ID
        List<DishFlavorPO> dishFlavorPOS = dishDto.getFlavors().stream().map(dishFlavorDTO -> {
            DishFlavorPO dishFlavorPO = new DishFlavorPO();
            BeanUtils.copyProperties(dishFlavorDTO,dishFlavorPO);
            //手动设置dishID，list表单中的dishID为空
            dishFlavorPO.setDishId(dishID);
            return dishFlavorPO;
        }).collect(Collectors.toList());
        dishFlavorService.saveBatch(dishFlavorPOS);
    }

    @Override
    public DishDTO getDishById(Long id) {
        //查询dishPO
        DishPO dishPO = super.getById(id);
        //查询dishFlavorPo对象
        LambdaQueryWrapper<DishFlavorPO> queryWrapper = new LambdaQueryWrapper<DishFlavorPO>()
                .eq(DishFlavorPO::getDishId,dishPO.getId());
        List<DishFlavorPO> flavorPOList = dishFlavorService.list(queryWrapper);
        //封装dishDTO
        DishDTO dishDTO = new DishDTO();
        BeanUtils.copyProperties(dishPO,dishDTO);
        flavorPOList.forEach(dishFlavorPO -> {
            DishFlavorDTO dishFlavorDTO = new DishFlavorDTO();
            BeanUtils.copyProperties(dishFlavorPO,dishFlavorDTO);
            dishDTO.getFlavors().add(dishFlavorDTO);
        });
        //查询菜品分类名
        CategoryPO categoryPO = categoryService.getById(dishPO.getCategoryId());
        dishDTO.setCategoryName(categoryPO.getName());

        return dishDTO;
    }

    @CacheEvict(cacheNames = "dishCache",allEntries = true)
    @Override
    public void updateDish(DishDTO dishDTO) {
        //获取口味dto列表
        List<DishFlavorDTO> flavorDTOs = dishDTO.getFlavors();
        //按id修改口味
        flavorDTOs.forEach(dishFlavorDTO -> {
            DishFlavorPO dishFlavorPO = new DishFlavorPO();
            BeanUtils.copyProperties(dishFlavorDTO,dishFlavorPO);
            dishFlavorService.updateById(dishFlavorPO);
        });

        //按id修改菜品
        DishPO dishPO = new DishPO();
        BeanUtils.copyProperties(dishDTO,dishPO);
        super.updateById(dishPO);
    }

    @CacheEvict(cacheNames = "dishCache",allEntries = true)
    @Override
    public void deleteDish(List<Long> ids) {
        //按ids删除口味
        LambdaQueryWrapper<DishFlavorPO> queryWrapper = new LambdaQueryWrapper<DishFlavorPO>()
                .in(DishFlavorPO::getDishId,ids);
        dishFlavorService.remove(queryWrapper);

        //按ids删除菜品
        super.removeByIds(ids);
    }

    @CacheEvict(cacheNames = "dishCache",allEntries = true)
    @Override
    public void updateStatus(Integer status, List<Long> ids) {
        //按ids修改状态
        LambdaUpdateWrapper<DishPO> updateWrapper = new LambdaUpdateWrapper<DishPO>()
                .in(DishPO::getId,ids).set(DishPO::getStatus,status);
        super.update(updateWrapper);
    }

    @Cacheable(cacheNames = "dishCache-front",key = "#categoryId + '_' + #status")
    @Override
    public List<DishDTO> listDish(Long categoryId, Integer status) {
        //查询
        LambdaQueryWrapper<DishPO> queryWrapper = new LambdaQueryWrapper<DishPO>()
                .eq(DishPO::getCategoryId,categoryId).eq(DishPO::getStatus,status);
        List<DishPO> dishPOS = super.list(queryWrapper);
        //转换
        List<DishDTO> dishDTOS = new ArrayList<>();
        dishPOS.forEach(dishPO -> {
            DishDTO dishDTO = new DishDTO();
            BeanUtils.copyProperties(dishPO,dishDTO);
            dishDTOS.add(dishDTO);
        });
        dishDTOS.forEach(dishDTO -> {
            LambdaQueryWrapper<DishFlavorPO> queryWrapper1 = new LambdaQueryWrapper<DishFlavorPO>()
                    .eq(DishFlavorPO::getDishId,dishDTO.getId());
            List<DishFlavorPO> flavorPOS = dishFlavorService.list(queryWrapper1);

            flavorPOS.forEach(dishFlavorPO -> {
                DishFlavorDTO dishFlavorDTO = new DishFlavorDTO();
                BeanUtils.copyProperties(dishFlavorPO,dishFlavorDTO);
                dishDTO.getFlavors().add(dishFlavorDTO);
            });
        });
        return dishDTOS;
    }
}
