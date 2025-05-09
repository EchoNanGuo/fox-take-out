package top.nanguomm.fox.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import top.nanguomm.fox.domain.address_book.PO.AddressBookPO;
import top.nanguomm.fox.domain.order_detail.OrderDetail;
import top.nanguomm.fox.domain.orders.Orders;
import top.nanguomm.fox.domain.orders.OrdersDTO;
import top.nanguomm.fox.domain.shopping_cart.ShoppingCart;
import top.nanguomm.fox.domain.user.PO.UserPO;
import top.nanguomm.fox.exception.CustomException;
import top.nanguomm.fox.mapper.OrdersMapper;
import top.nanguomm.fox.service.IAddressBookService;
import top.nanguomm.fox.service.IOrderDetailService;
import top.nanguomm.fox.service.IOrdersService;
import top.nanguomm.fox.service.IUserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * <p>
 * 订单表 服务实现类
 * </p>
 *
 * @author nanGuoMM
 * @since 2024-05-30
 */
@Service
public class OrdersServiceImpl extends ServiceImpl<OrdersMapper, Orders> implements IOrdersService {
    @Autowired
    private IOrderDetailService orderDetailService;
    @Autowired
    private IAddressBookService addressBookService;

    @Autowired
    private IUserService userService;
    @Autowired
    private HttpServletRequest request;

    @Cacheable(cacheNames = "orderCache",key = "'page:' + #page  + '_pageSize:' + #pageSize + '_beginTime:' + #beginTime + '_endTime:' + #endTime + '_number:' + #number")
    @Override
    public IPage<OrdersDTO> pageOrder(Integer page, Integer pageSize, LocalDateTime beginTime, LocalDateTime endTime, Long number) {
        Page<Orders> ordersIPage = new Page<>(page,pageSize);
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.between(beginTime != null&&endTime != null,Orders::getOrderTime,beginTime,endTime)
                .eq(number != null,Orders::getNumber,number).orderByDesc(Orders::getOrderTime);
        super.page(ordersIPage,queryWrapper);
        //查询orderDetail表，并封装List<OrdersDTO>
        List<OrdersDTO> ordersDTOList = ordersIPage.getRecords().stream().map(orders -> {
            OrdersDTO ordersDTO = new OrdersDTO();
            LambdaQueryWrapper<OrderDetail> queryWrapper1 = new LambdaQueryWrapper<>();
            queryWrapper1.eq(OrderDetail::getOrderId, orders.getNumber());
            List<OrderDetail> orderDetails = orderDetailService.list(queryWrapper1);

            ordersDTO.setOrderDetails(orderDetails);
            BeanUtils.copyProperties(orders, ordersDTO);
            return ordersDTO;
        }).collect(Collectors.toList());
        //封装page
        IPage<OrdersDTO> ordersDTOIPage = new Page<>(page,pageSize,ordersIPage.getTotal());
        ordersDTOIPage.setRecords(ordersDTOList);
        return ordersDTOIPage;
    }

    @CacheEvict(cacheNames = "orderCache", allEntries = true)
    @Override
    public void updateStatus(Orders orders) {
        super.updateById(orders);
    }

    @CacheEvict(cacheNames = "orderCache",allEntries = true)
    @Override
    public void submit(Orders orders, Long userId) {
        //查找下单地址
        Long addressBookId = orders.getAddressBookId();
        AddressBookPO addressBook = addressBookService.getById(addressBookId);
        if (addressBook == null) {
            throw new CustomException("地址信息错误");
        }
        //查询当前用户购物车信息
        HttpSession session = request.getSession();
        List<ShoppingCart> shoppingCarts = (List<ShoppingCart>) session.getAttribute("shoppingCart");
        if (shoppingCarts == null || shoppingCarts.isEmpty()) {
            throw new CustomException("购物车中无数据");
        }

        //获取user对象
        UserPO user = userService.getById(userId);

        //总金额
        AtomicInteger amount = new AtomicInteger(0);
        //订单号
        long orderId = IdWorker.getId();//订单号
        //封装order_detail
        List<OrderDetail> orderDetails = shoppingCarts.stream().map(item -> {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrderId(orderId);
            orderDetail.setNumber(item.getNumber());
            orderDetail.setDishFlavor(item.getDishFlavor());
            orderDetail.setDishId(item.getDishId());
            orderDetail.setSetmealId(item.getSetmealId());
            orderDetail.setName(item.getName());
            orderDetail.setImage(item.getImage());
            orderDetail.setAmount(item.getAmount());
            amount.addAndGet(item.getAmount().multiply(new BigDecimal(item.getNumber())).intValue());
            return orderDetail;
        }).collect(Collectors.toList());
        //给order对象赋值
        orders.setId(orderId);
        orders.setOrderTime(LocalDateTime.now());
        orders.setCheckoutTime(LocalDateTime.now());
        orders.setStatus(2);//设为派送中
        orders.setAmount(new BigDecimal(amount.get()));//总金额
        orders.setUserId(userId);
        orders.setNumber(String.valueOf(orderId));
        orders.setUserName(user.getMail());//暂时将用户名设为邮箱
        orders.setConsignee(addressBook.getConsignee());
        orders.setPhone(addressBook.getPhone());
        orders.setAddress((addressBook.getProvinceName() == null ? "" : addressBook.getProvinceName())
                + (addressBook.getCityName() == null ? "" : addressBook.getCityName())
                + (addressBook.getDistrictName() == null ? "" : addressBook.getDistrictName())
                + (addressBook.getDetail() == null ? "" : addressBook.getDetail()));
        //向订单表插入数据，一条数据
        this.save(orders);
        //向订单明细表插入数据，多条数据
        orderDetailService.saveBatch(orderDetails);
        //清空购物车
        session.setAttribute("shoppingCart", null);
    }

    @Cacheable(cacheNames = "orderCache", key = "#page + '-' + #pageSize + '-' + #userId")
    @Override
    public IPage<OrdersDTO> pageOrderFront(Integer page, Integer pageSize, Long userId) {
         //分页查询order表
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Orders::getUserId,userId).orderByDesc(Orders::getOrderTime);
        Page<Orders> ordersIPage = new Page<>(page,pageSize);
        super.page(ordersIPage,queryWrapper);
        //查询orderDetail表，并封装List<OrdersDTO>
        List<OrdersDTO> ordersDTOList = ordersIPage.getRecords().stream().map(orders -> {
            OrdersDTO ordersDTO = new OrdersDTO();
            LambdaQueryWrapper<OrderDetail> queryWrapper1 = new LambdaQueryWrapper<>();
            queryWrapper1.eq(OrderDetail::getOrderId, orders.getNumber());
            List<OrderDetail> orderDetails = orderDetailService.list(queryWrapper1);

            ordersDTO.setOrderDetails(orderDetails);
            BeanUtils.copyProperties(orders, ordersDTO);
            return ordersDTO;
        }).collect(Collectors.toList());
        //封装page
        IPage<OrdersDTO> ordersDTOIPage = new Page<>(page,pageSize,ordersIPage.getTotal());
        ordersDTOIPage.setRecords(ordersDTOList);
        return ordersDTOIPage;
    }
}
