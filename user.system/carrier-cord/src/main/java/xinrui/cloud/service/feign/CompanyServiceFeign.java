package xinrui.cloud.service.feign;

import java.util.List;
import java.util.Map;
import xinrui.cloud.dto.OtherGroupDto;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import xinrui.cloud.fallback.CompanyServiceFeignFallBack;

@FeignClient(value = "authorization", path = "company", fallback = CompanyServiceFeignFallBack.class)
public interface CompanyServiceFeign  {

    @LoadBalanced
    @GetMapping("find/{name}")
    public List<OtherGroupDto> listCompanyDtoByName(@PathVariable("name") String name);


    @LoadBalanced
    @GetMapping("find/by/{id}")
    public OtherGroupDto findCompanyDtoById(@PathVariable("id") Long id) ;

    @LoadBalanced
    @GetMapping("findCompanyMapById/{id}")
    Map<String, Object> findCompanyMapById(@PathVariable("id") Long id);
}
