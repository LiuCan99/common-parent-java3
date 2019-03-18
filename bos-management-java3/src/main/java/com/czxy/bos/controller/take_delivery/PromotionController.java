package com.czxy.bos.controller.take_delivery;

import com.czxy.bos.domain.take_delivery.Promotion;
import com.czxy.bos.service.take_delivery.PromotionService;
import com.czxy.bos.vo.EasyUIResult;
import com.github.pagehelper.PageInfo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/promotion")
public class PromotionController {
    @Resource
    private PromotionService promotionService;

    @Resource
    private HttpServletRequest request;

    @Resource
    private HttpServletResponse response;

    @PostMapping
    public ResponseEntity<Void> add(Promotion promotion , MultipartFile titleImgFile) throws IOException {
        //TODO 上传(服务器保存，设置给titleImg)
        //确定上传文件硬盘中的存放路径
        String savePath = request.getServletContext().getRealPath("/upload/");

        //确定数据库中的图片路径 （项目名 + "/upload/" + 文件名）
        String saveUrl = request.getContextPath() + "/upload/";

        //随机生成文件夹，扩展名不变
        String uuidStr = UUID.randomUUID().toString().replace("-","");
        String fileName = titleImgFile.getOriginalFilename();
        String ext = fileName.substring( fileName.lastIndexOf("."));

        String randomFileName =uuidStr + ext;


        //将文件保存服务器
        File file = new File(savePath  , randomFileName);  // c:/xxx/upload/xxxx.jpg
        System.out.println("上传图片位置：" + file.getAbsolutePath());
        if(!file.getParentFile().exists()){
            file.getParentFile().mkdirs();
        }
        titleImgFile.transferTo( file );

        //将图片相对的路径设置到promotion中
        promotion.setTitleImg(saveUrl + randomFileName);    //数据库：  /bos/upload/xxx.jpg



        //保存
        promotionService.save( promotion );

        //保存成功后，重定向查询所有
        response.sendRedirect("/pages/take_delivery/promotion");

        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<EasyUIResult<Promotion>> queryStandardByPage(@RequestParam("page")Integer page,
                                                                       @RequestParam("rows")Integer rows){
        // 查询用户
        PageInfo<Promotion> pageInfo = this.promotionService.queryPromotionListByPage(page, rows);
        // 封装datagrid需要的格式
        EasyUIResult<Promotion> result = new EasyUIResult<>( pageInfo.getTotal() , pageInfo.getList() );
        // 封装还有状态码
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
      //前段分页
    @GetMapping("/findAll")
    public ResponseEntity<List<Promotion>>findAll(){
        //1.查询所有
        List<Promotion> list = this.promotionService.findAll();
        //2.封装状态码
        return  new ResponseEntity<List<Promotion>>(list,HttpStatus.OK);
    }


}






