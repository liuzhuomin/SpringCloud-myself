package xinrui.cloud.controller;

import io.swagger.annotations.Api;
import xinrui.cloud.dto.OtherGroupDto;
import xinrui.cloud.service.CompanyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * <B>Title:</B>UserController</br>
 * <B>Description:</B>企业相关接口</br>
 * <B>Copyright: </B>Copyright (c) 2019 </br>
 *
 * @author liuliuliu
 * @version 1.0
 * 2019/6/26 20:17
 */
@Api("企业相关接口")
@RestController
@RequestMapping("company")
public class CompanyController {

    private final static Logger LOGGER = LoggerFactory.getLogger(CompanyController.class);

    @Autowired
    CompanyService companyService;

    @GetMapping("find/{name}")
    public List<OtherGroupDto> listCompanyDtoByName(@PathVariable("name") String name) {
        LOGGER.info("当前查询的企业{}", name);
        return companyService.listCompanyDtoByName(name);
    }


    @GetMapping("find/by/{id}")
    public OtherGroupDto findCompanyDtoById(@PathVariable("id") Long id) {
        LOGGER.info("当前查询的企业id为{}", id);
        return companyService.findCompanyDtoById(id);
    }

    @GetMapping("findCompanyMapById/{id}")
    public Map<String, Object> findCompanyMapById(@PathVariable("id") Long id){
      LOGGER.info("当前查询的企业id为{}", id);
      return companyService.getCompanyInfo(id);
    }
}


