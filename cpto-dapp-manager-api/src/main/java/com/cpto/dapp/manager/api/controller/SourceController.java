package com.cpto.dapp.manager.api.controller;

import com.cpto.dapp.auth.AuthManager;
import com.cpto.dapp.auth.annotation.CurrentAdmin;
import com.cpto.dapp.auth.annotation.ManagerAuth;
import com.cpto.dapp.common.simple.SimpleResponseEntity;
import com.cpto.dapp.domain.ManagerAdmin;
import com.cpto.dapp.pojo.dto.SourceDTO;
import com.cpto.dapp.pojo.vo.SourceVO;
import com.cpto.dapp.service.SourceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 资源Controller
 *
 * @author sunli
 * @date 2019/01/31
 */
@Api(tags = "资源")
@RequestMapping("/sources")
@RestController
@Validated
public class SourceController extends BaseController {

    @Autowired
    private SourceService sourceService;

    @ApiOperation(value = "取得资源", notes = "取得图片、影音等文件资源。")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "relationType", value = "关联类型（1：提币申请，2：订单，3：收益记录，4：项目，5：公告）", dataType = "int", paramType = "query", required = true),
            @ApiImplicitParam(name = "relationId", value = "关联id", dataType = "long", paramType = "query", required = true)
    })
    @GetMapping
    @ManagerAuth
    public ResponseEntity<List<SourceVO>> findSourceList(@RequestParam @NotNull(message = "关联类型不能为空") Integer relationType,
                                                         @RequestParam @NotNull(message = "关联id不能为空") Long relationId) {
        return SimpleResponseEntity.get(sourceService.findSourceList(relationType, relationId));
    }

    @ApiOperation(value = "新增资源", notes = "新增图片、影音等文件资源。")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "addSourceDTO", value = "新增资源信息", dataType = "SourceDTO", paramType = "body", required = true)
    })
    @PostMapping
    @ManagerAuth(AuthManager.WRITE)
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<SourceVO> addSource(@ApiIgnore @CurrentAdmin ManagerAdmin admin,
                                              @RequestBody @Validated SourceDTO addSourceDTO) {
        sourceService.addSource(admin, addSourceDTO);
        return SimpleResponseEntity.post();
    }

    @ApiOperation(value = "删除资源", notes = "逻辑删除图片、影音等文件资源。")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sourceId", value = "资源id", dataType = "long", paramType = "path", required = true)
    })
    @DeleteMapping("/{sourceId}")
    @ManagerAuth(AuthManager.WRITE)
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity removeSource(@ApiIgnore @CurrentAdmin ManagerAdmin admin,
                                       @PathVariable @NotNull(message = "资源id不能为空") Long sourceId) {
        sourceService.removeSource(admin, sourceId);
        return SimpleResponseEntity.delete();
    }


//    @RequestMapping("/pics")
//    @ResponseBody
//    @ManagerAuth(AuthManager.WRITE)
//    public ResponseEntity upload(@RequestParam("file") MultipartFile file) throws IOException {
//
//
//        // Endpoint以杭州为例，其它Region请按实际情况填写。
//        String endpoint = "http://oss-us-west-1.aliyuncs.com";
//// 阿里云主账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM账号进行API访问或日常运维，请登录 https://ram.console.aliyun.com 创建RAM账号。
//        String accessKeyId = "LTAIl5j3LOSHtUpk";
//        String accessKeySecret = "4K62q8giHVHxnYfKrf87baE9T596Hx";
//        String bucketName = "cpto-dapp-pic";
//        String objectName = file.getResource().getFilename();
//
//// 创建OSSClient实例。
//        OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
//
//// 上传内容到指定的存储空间（bucketName）并保存为指定的文件名称（objectName）。
//        PutObjectResult putObjectResult = ossClient.putObject(bucketName, file.getResource().getFilename(), new ByteArrayInputStream(file.getBytes()));
//
//// 关闭OSSClient。
//        ossClient.shutdown();
//
//        return SimpleResponseEntity.get().body(putObjectResult.getETag());
//    }

}