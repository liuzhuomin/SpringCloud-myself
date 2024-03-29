package xinrui.cloud.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import xinrui.cloud.BootException;
import xinrui.cloud.compoment.Application;
import xinrui.cloud.domain.IdEntity;
import xinrui.cloud.domain.TechnologyFinancialFile;
import xinrui.cloud.dto.ResponseDto;
import xinrui.cloud.dto.ResultDto;
import xinrui.cloud.interfaces.FileExtensionFilter;
import xinrui.cloud.service.TechnologyFinancialFileService;
import xinrui.cloud.util.FileUtil;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

/**
 * @author liuliuliu
 * @version 1.0
 *  2019/8/9 9:38
 */
@RestController
@RequestMapping("file")
@Api("文件上传接口")
public class FileController {


    @Autowired
    TechnologyFinancialFileService technologyFinancialFileService;

    @ApiOperation(position = 8, value = "上传图片", notes = "上传图片格式的文件，可支持的文件类型有(\"BMP\", \"JPG\", \"JPEG\",\n" +
            "                \"PNG\", \"GIF\", \"PCX\", \"TIFF\", \"TGA\", \"EXIF\",\n" +
            "                \"FPX\", \"SVG\", \"PSD\")", tags = "文件上传接口", httpMethod = "POST", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @PostMapping(value = "/upload", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResultDto uploadFile(@RequestPart String description, @RequestPart MultipartFile file) throws IOException {

        String fullPath = getFullPath();
        File fileObject = FileUtil.saveFile(fullPath, file, new FileExtensionFilter() {
            @Override
            public boolean canContinue(MultipartFile file) {
                String extension = FilenameUtils.getExtension(file.getOriginalFilename());
                String[] suffixArray = new String[]{"BMP", "JPG", "JPEG",
                        "PNG", "GIF", "PCX", "TIFF", "TGA", "EXIF",
                        "FPX", "SVG", "PSD"};
                if (StringUtils.isNotBlank(extension)) {
                    for (int i = 0; i < suffixArray.length; i++) {
                        String suffix = suffixArray[i];
                        if (extension.equalsIgnoreCase(suffix)) {
                            return true;
                        }
                    }
                }
                return false;
            }
        });

        String baseServicePath = Application.getBaseServicePath() + "file/download";

        TechnologyFinancialFile technologyFinancialFile = new TechnologyFinancialFile();
        technologyFinancialFile.setUserId(Application.getCurrentUser().getId());
        technologyFinancialFile.setServerUri(baseServicePath);
        technologyFinancialFile.setMenu(getMenu());
        technologyFinancialFile.setDescription(description);
        technologyFinancialFile.setName(fileObject.getName());
        technologyFinancialFile.setFileDownLoadPath(technologyFinancialFile.getMenu() + "/" + fileObject.getName());
        technologyFinancialFile.setFileSavePath(fullPath + File.separator + fileObject.getName());

        baseServicePath = technologyFinancialFile.getServerUri() + "?filePath" + technologyFinancialFile.getFileDownLoadPath();
        technologyFinancialFile.setFullPath(baseServicePath);
        technologyFinancialFileService.persist(technologyFinancialFile);

        return ResponseDto.success(baseServicePath);
    }

    /**
     * 获取存储物理路径
     *
     * @return
     */
    private String getFullPath() {
        String property = System.getProperty("user.dir");
        String menu = getMenu();
        return property + File.separator + menu;
    }

    /**
     * 获取存储目录
     *
     * @return
     */
    private String getMenu() {
        String menu = this.getClass().getAnnotation(RequestMapping.class).value()[0];
        if (StringUtils.isNotBlank(menu)) {
            menu.replace("/", "");
        } else {
            menu = Application.getCurrentUser().getUsername();
        }
        return menu;
    }

    @ApiOperation(position = 9, value = "下载文件", notes = "根据filePath路径下载文件", tags = "文件上传接口", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiImplicitParam(name = "filePath", value = "下载的路径", required = true, paramType = "query", dataType = "String")
    @GetMapping("download")
    public void download(@RequestParam(value = "filePath") String filePath, HttpServletResponse response) throws IOException {

        //截取filePath后的文件名，并且利用URLEncoder编码
        String substring = filePath.substring(filePath.lastIndexOf('/') + 1);
        filePath = filePath.replace(substring, URLEncoder.encode(substring, StandardCharsets.UTF_8.name()));

        //查找该文件对象是否存在,禁止胡乱下载
        TechnologyFinancialFile byPath = technologyFinancialFileService.findByPath(filePath);
        if (byPath == null) {
            throw new BootException("找不到该文件!");
        }

        FileSystemResource resource = new FileSystemResource(byPath.getFileSavePath());
        String fileName = resource.getFilename();
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
        response.setContentType(Files.probeContentType(resource.getFile().toPath()));

        ServletOutputStream outputStream = response.getOutputStream();
        InputStream inputStream = resource.getInputStream();
        IOUtils.copy(inputStream, outputStream);

        response.flushBuffer();
        if (inputStream != null) {
            inputStream.close();
        }
        if (outputStream != null) {
            outputStream.close();
        }

    }
}
