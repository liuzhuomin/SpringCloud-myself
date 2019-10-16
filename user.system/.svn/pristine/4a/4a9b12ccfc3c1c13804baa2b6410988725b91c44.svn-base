package xinrui.cloud.service.feign;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;
import xinrui.cloud.domain.vto.OtherGroupVto;
import xinrui.cloud.dto.OtherGroupDto;
import xinrui.cloud.dto.ResultDto;
import xinrui.cloud.fallback.BankGroupServiceFallBack;
import xinrui.cloud.fallback.OrganizationServiceFallBack;

@FeignClient(value = "authorization", path = "bank", fallback = BankGroupServiceFallBack.class)
public interface BankGroupServiceFeign {

    @LoadBalanced
    @GetMapping("find/{name}")
    public ResultDto<OtherGroupDto> findUserByUserName(@PathVariable("name") String name) ;

    @LoadBalanced
    @GetMapping("find/by/{id}")
    public ResultDto<OtherGroupDto> findUserByUserId(@PathVariable("id") Long id);

    @LoadBalanced
    @PostMapping
    public ResultDto<OtherGroupDto> create(@RequestBody OtherGroupVto vto);

    @LoadBalanced
    @DeleteMapping("{id}")
    public ResultDto deleteById(@PathVariable("id") Long id);

    @LoadBalanced
    @GetMapping("/list/{type}")
    public ResultDto listByType(@PathVariable("type") String type);
}
