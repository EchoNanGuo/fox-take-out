package top.nanguomm.fox.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import top.nanguomm.fox.domain.address_book.PO.AddressBookPO;

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
