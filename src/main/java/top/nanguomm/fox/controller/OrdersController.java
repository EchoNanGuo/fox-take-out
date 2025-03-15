package top.nanguomm.fox.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.nanguomm.fox.domain.Result;
import top.nanguomm.fox.domain.orders.Orders;
import top.nanguomm.fox.domain.orders.OrdersDTO;
import top.nanguomm.fox.service.IOrdersService;
import top.nanguomm.fox.utils.BaseContext;

import java.time.LocalDateTime;

/**
 * <p>
 * 订单表 前端控制器
 * </p>
 *
 * @author nanGuoMM
 * @since 2024-05-30
 */
@RestController
@RequestMapping("/order")
@Api(tags = "订单")
public class OrdersController {

    @Autowired
    private IOrdersService ordersService;

    @ApiOperation("查询")
    @GetMapping("/page")
    public Result<IPage<OrdersDTO>> pageOrder(@ApiParam("页码") Integer page, @ApiParam("每页条数") Integer pageSize,
                                              @ApiParam("开始时间") LocalDateTime beginTime, @ApiParam("结束时间") LocalDateTime endTime,
                                              @ApiParam("订单号") Long number) {
        IPage<OrdersDTO> ordersDTOIPage = ordersService.pageOrder(page,pageSize,beginTime,endTime,number);
        return Result.success(ordersDTOIPage);
    }

    @ApiOperation("状态")
    @PutMapping
    public Result<Object> updateStatus(@ApiParam("原数据") @RequestBody Orders orders) {
        ordersService.updateStatus(orders);
        return Result.success();
    }

    @ApiOperation("用户下单")
    @PostMapping("/submit")
    public Result<Object> submit(@ApiParam("新订单") @RequestBody Orders orders) {
        //获取用户id
        Long userId = BaseContext.getCurrentId();
        ordersService.submit(orders,userId);
        return Result.success();
    }

    @ApiOperation("查询")
    @GetMapping("/userPage")
    public Result<IPage<OrdersDTO>> pageOrderFront(@ApiParam("页码") Integer page,@ApiParam("每页条数") Integer pageSize) {
        //获取当前用户数据
        Long userId = BaseContext.getCurrentId();
        IPage<OrdersDTO> ordersDTOIPage = ordersService.pageOrderFront(page,pageSize,userId);
        return Result.success(ordersDTOIPage);
    }
}
