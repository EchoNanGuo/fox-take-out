package top.nanguomm.fox.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.nanguomm.fox.domain.address_book.PO.AddressBookPO;
import top.nanguomm.fox.mapper.AddressBookMapper;
import top.nanguomm.fox.service.IAddressBookService;

/**
 * <p>
 * 地址管理 服务实现类
 * </p>
 *
 * @author nanGuoMM
 * @since 2024-05-29
 */
@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBookPO> implements IAddressBookService {

}
