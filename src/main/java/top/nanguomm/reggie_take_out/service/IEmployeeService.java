package top.nanguomm.reggie_take_out.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import top.nanguomm.reggie_take_out.domain.employee.DTO.EmployeeDTO;
import top.nanguomm.reggie_take_out.domain.employee.PO.EmployeePO;

/**
 * <p>
 * 员工信息 服务类
 * </p>
 *
 * @author nanGuoMM
 * @since 2024-05-23
 */
public interface IEmployeeService extends IService<EmployeePO> {

    IPage<EmployeeDTO> pageEmployee(String page, String pageSize, String name);
}
