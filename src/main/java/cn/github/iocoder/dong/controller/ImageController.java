package cn.github.iocoder.dong.controller;

import cn.github.iocoder.dong.controller.vo.ImageVo;
import cn.github.iocoder.dong.model.api.ResVo;
import cn.github.iocoder.dong.model.enums.StatusEnum;
import cn.github.iocoder.dong.service.user.service.ImageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RequestMapping("image/")
@RestController
@Slf4j
public class ImageController {

    @Resource
    private ImageService imageService;

    @RequestMapping(path = "upload")
    public ResVo<ImageVo> upload(HttpServletRequest request) {
        ImageVo imageVo = new ImageVo();
        try {
            String imagePath = imageService.saveImg(request);
            imageVo.setImagePath(imagePath);
        } catch (Exception e) {
            log.error("save upload file error!", e);
            return ResVo.fail(StatusEnum.UPLOAD_PIC_FAILED);
        }
        return ResVo.ok(imageVo);
    }
}
