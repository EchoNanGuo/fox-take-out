package top.nanguomm.fox.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.nanguomm.fox.domain.Result;
import top.nanguomm.fox.domain.dish.DTO.DishDTO;
import top.nanguomm.fox.domain.dish.PO.DishPO;
import top.nanguomm.fox.service.IDishService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 菜品管理 前端控制器
 * </p>
 *
 * @author nanGuoMM
 * @since 2024-05-23
 */
@Api(tags = "菜品")
@RestController
@RequestMapping("/dish")
public class DishController {

    @Autowired
    private IDishService dishService;

    @ApiOperation(value = "查询",notes = "分页查询")
    @GetMapping("/page")
    public Result<IPage<DishDTO>> pageDish(@ApiParam("分页参数") @RequestParam Map<String,String> params) {
        //调用service
        IPage<DishDTO> dishDTOIPage = dishService.pageDish(params.get("page"), params.get("pageSize"),params.get("name"));
        return Result.success(dishDTOIPage);
    }

    @ApiOperation(value = "添加",notes = "添加菜品")
    @PostMapping
    public Result<Object> saveDish(@ApiParam("新菜品") @RequestBody DishDTO dishDto) {
        //调用service
        dishService.saveDish(dishDto);
        return Result.success();
    }

    @ApiOperation(value = "显示",notes = "按id查询数据回显")
    @GetMapping("/{id}")
    public Result<DishDTO> getById(@ApiParam("菜品id") @PathVariable Long id) {
        DishDTO dishDTO = dishService.getDishById(id);
        return Result.success(dishDTO);
    }

    @ApiOperation(value = "修改",notes = "保存修改结果")
    @PutMapping
    public Result<Object> updateDish(@ApiParam("新菜品") @RequestBody DishDTO dishDTO) {

        dishService.updateDish(dishDTO);
        return Result.success();
    }

    @ApiOperation(value = "删除",notes = "删除菜品")
    @DeleteMapping
    public Result<Object> deleteDish(@ApiParam("菜品ids") @RequestParam List<Long> ids) {
        //调用service
        dishService.deleteDish(ids);
        return Result.success();
    }

    @ApiOperation(value = "状态",notes = "停售起售")
    @PostMapping("/status/{status}")
    public Result<Object> updateStatus(@ApiParam("状态") @PathVariable Integer status,
                                       @ApiParam("菜品ids") @RequestParam List<Long> ids) {
        dishService.updateStatus(status,ids);
        return Result.success();
    }

    @ApiOperation(value = "查询",notes = "下拉列表")
    @GetMapping("/list")
    public Result<List<DishDTO>> listDish(@ApiParam("分类id") @RequestParam Long categoryId) {
        //查询
        LambdaQueryWrapper<DishPO> queryWrapper = new LambdaQueryWrapper<DishPO>()
                .eq(DishPO::getCategoryId,categoryId);
        List<DishPO> dishPOS = dishService.list(queryWrapper);
        //转换
        List<DishDTO> dishDTOS = new ArrayList<>();
        dishPOS.forEach(dishPO -> {
            DishDTO dishDTO = new DishDTO();
            BeanUtils.copyProperties(dishPO,dishDTO);
            dishDTOS.add(dishDTO);
        });
        return Result.success(dishDTOS);
    }
    @ApiOperation(value = "查询",notes = "用户界面")
    @GetMapping("/list-front")
    public Result<List<DishDTO>> listDish(@ApiParam("分类id") Long categoryId,@ApiParam("状态") Integer status) {
        List<DishDTO> dishDTOS = dishService.listDish(categoryId, status);
        return Result.success(dishDTOS);
    }
}
