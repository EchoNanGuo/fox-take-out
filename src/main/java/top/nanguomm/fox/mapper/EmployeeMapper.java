package top.nanguomm.fox.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import top.nanguomm.fox.domain.employee.PO.EmployeePO;

/**
 * <p>
 * 员工信息 Mapper 接口
 * </p>
 *
 * @author nanGuoMM
 * @since 2024-05-23
 */
@Mapper
public interface EmployeeMapper extends BaseMapper<EmployeePO> {

}
