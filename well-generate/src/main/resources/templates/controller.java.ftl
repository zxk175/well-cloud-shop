package ${package.Controller};

<#assign comment = table.comment?replace("表", "")>
<#assign pojo = entity?substring(0,1)?lower_case + entity?substring(1)>
import com.zxk175.well.base.http.Response;
import ${package.Entity}.${entity};
import ${package.Service}.${table.serviceName};
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
<#if restControllerStyle>
import org.springframework.web.bind.annotation.RestController;
<#else>
import org.springframework.stereotype.Controller;
</#if>
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
<#if superControllerClassPackage??>
import ${superControllerClassPackage};
</#if>

/**
 * <p>
 * ${table.comment!} 前端控制器
 * </p>
 *
 * @author ${author}
 * @since ${cfg.since}
 */
<#if restControllerStyle>
@RestController
<#else>
@Controller
</#if>
@AllArgsConstructor
@RequestMapping("/<#if controllerMappingHyphenStyle??>${controllerMappingHyphen}<#else>${table.entityPath}</#if>")
@Api(tags = "${entity!}", description = "${comment}V1")
<#if kotlin>
class ${table.controllerName}<#if superControllerClass??> : ${superControllerClass}()</#if>
<#else>
<#if superControllerClass??>
public class ${table.controllerName} extends ${superControllerClass} {
<#else>
public class ${table.controllerName} {
</#if>

    private ${table.serviceName} ${pojo}Service;


    @ResponseBody
    @PostMapping(value = "/save/v1")
    @ApiOperation(value = "添加${comment}", notes = "添加${comment}")
    public Response save(@Validated @RequestBody ${entity} ${pojo}) {
        boolean flag = ${pojo}Service.save(${pojo});
        return saveReturn(flag);
    }
}
</#if>
