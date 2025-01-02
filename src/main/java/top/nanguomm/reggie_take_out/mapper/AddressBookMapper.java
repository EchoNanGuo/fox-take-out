package top.nanguomm.reggie_take_out.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import top.nanguomm.reggie_take_out.domain.address_book.PO.AddressBookPO;

/**
 * <p>
 * 地址管理 Mapper 接口
 * </p>
 *
 * @author nanGuoMM
 * @since 2024-05-29
 */
@Mapper
public interface AddressBookMapper extends BaseMapper<AddressBookPO> {

}
