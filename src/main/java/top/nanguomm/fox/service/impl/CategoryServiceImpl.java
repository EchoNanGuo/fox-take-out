package top.nanguomm.fox.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import top.nanguomm.fox.domain.category.DTO.CategoryDTO;
import top.nanguomm.fox.domain.category.PO.CategoryPO;
import top.nanguomm.fox.domain.dish.PO.DishPO;
import top.nanguomm.fox.domain.setmeal.PO.SetmealPO;
import top.nanguomm.fox.exception.CustomException;
import top.nanguomm.fox.mapper.CategoryMapper;
import top.nanguomm.fox.service.ICategoryService;
import top.nanguomm.fox.service.IDishService;
import top.nanguomm.fox.service.ISetmealService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 菜品及套餐分类 服务实现类
 * </p>
 *
 * @author nanGuoMM
 * @since 2024-05-23
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, CategoryPO> implements ICategoryService {

    @Autowired
    private IDishService dishService;
    @Autowired
    private ISetmealService setmealService;

    @Cacheable(cacheNames = "categoryCache",key = "'page:' + #page + '_' + 'pageSize:' + #pageSize")
    @Override
    public IPage<CategoryDTO> pageCategory(Integer page, Integer pageSize) {
        //查询数据
        Page<CategoryPO> pagePo = new Page<>(page,pageSize);
        //条件构造器
        LambdaQueryWrapper<CategoryPO> queryWrapper = new LambdaQueryWrapper<CategoryPO>()
                .orderByAsc(CategoryPO::getSort).orderByDesc(CategoryPO::getUpdateTime);
        super.page(pagePo,queryWrapper);

        //将CategoryPO象转换为CategoryDTO对象
        List<CategoryDTO> categoryDTOList = pagePo.getRecords().stream()
                .map(categoryPO -> {
                    //封装对象
                    CategoryDTO dto = new CategoryDTO();

                    BeanUtils.copyProperties(categoryPO,dto);

                    return dto;
                }).collect(Collectors.toList());

        // 将转换后的数据封装到Page<EmployeePageVO>对象中
        Page<CategoryDTO> categoryDTOPage = new Page<>(page,pageSize,pagePo.getTotal());
        categoryDTOPage.setRecords(categoryDTOList);
        return categoryDTOPage;

    }

    @CacheEvict(cacheNames = "categoryCache", allEntries = true)
    @Override
    public void removeCategory(Long id) {
        //查询该分类dish数
        LambdaQueryWrapper<DishPO> queryWrapperDish = new LambdaQueryWrapper<DishPO>()
                .eq(DishPO::getCategoryId,id);

        long dishCount = dishService.count(queryWrapperDish);
        if (dishCount > 0) {
            //抛出业务异常
            throw new CustomException("关联了菜品，不能删除");
        }

        //查询该分类meal数
        LambdaQueryWrapper<SetmealPO> queryWrapperSetMeal = new LambdaQueryWrapper<SetmealPO>()
                .eq(SetmealPO::getCategoryId,id);

        long setMealcount = setmealService.count(queryWrapperSetMeal);
        if(setMealcount > 0) {
            //抛出业务异常
            throw new CustomException("关联了套餐，不能删除");
        }

        //删除
        LambdaQueryWrapper<CategoryPO> queryWrapper = new LambdaQueryWrapper<CategoryPO>()
                .eq(CategoryPO::getId,id);
        super.remove(queryWrapper);
    }

    @CacheEvict(cacheNames = "categoryCache", allEntries = true)
    @Override
    public void saveCagory(CategoryDTO categorySaveFormDTO) {
        //封装CategoryPo对象，调用service
        CategoryPO categoryPO = new CategoryPO();
        BeanUtils.copyProperties(categorySaveFormDTO,categoryPO);

        super.save(categoryPO);
    }

    @CacheEvict(cacheNames = "categoryCache", allEntries = true)
    @Override
    public void updateCategory(CategoryDTO categoryDTO) {
        //封装PO
        CategoryPO categoryPO = new CategoryPO();
        BeanUtils.copyProperties(categoryDTO,categoryPO);
        //调用service
        super.updateById(categoryPO);
    }

    @Cacheable(cacheNames = "categoryCache",key = "'category_type_' + #type")
    @Override
    public List<CategoryDTO> listCategory(Integer type) {
        //查询
        LambdaQueryWrapper<CategoryPO> queryWrapper = new LambdaQueryWrapper<CategoryPO>()
                .eq(type != null,CategoryPO::getType,type).orderByAsc(CategoryPO::getSort).orderByDesc(CategoryPO::getUpdateTime);
        List<CategoryPO> listPO = super.list(queryWrapper);
        //封装dto数据
        List<CategoryDTO> listDTO = new ArrayList<>();
        listPO.forEach(categoryPO -> {
            CategoryDTO categoryDTO = new CategoryDTO();
            BeanUtils.copyProperties(categoryPO,categoryDTO);
            listDTO.add(categoryDTO);
        });

        return listDTO;
    }
}
