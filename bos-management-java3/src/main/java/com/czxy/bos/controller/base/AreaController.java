package com.czxy.bos.controller.base;

import com.czxy.bos.domain.base.Area;
import com.czxy.bos.service.base.AreaService;
import com.czxy.bos.util.PinYin4jUtils;
import com.czxy.bos.vo.EasyUIResult;
import com.github.pagehelper.PageInfo;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/area")
public class AreaController {

    @Resource
    private AreaService areaService;


    @PostMapping("/batchImport")
    public ResponseEntity<String> batchImport(MultipartFile file) throws IOException {
        //1 处理excel文件 （注意：必须上传提供模板）
        //1.1 excel文件保存到服务器
        File newFile = new File("C:\\upload", UUID.randomUUID() + file.getOriginalFilename());
        file.transferTo(newFile);
        //1.2 解析excel文件
        // 1) 加载 xls 文件 获得工作簿（Workbook ，Ctrl + H 查看接口的实现类，从而编写出HSSFWorkBook类）
        Workbook workbook = new HSSFWorkbook(new FileInputStream(newFile));
        // 2) 获得第一张表
        Sheet sheet = workbook.getSheetAt(0);
        // 3) 获得所有的行
        int lastRow = sheet.getLastRowNum();
        // 存放所有解析后的area对象的
        List<Area> areaList = new ArrayList<>();

        for (int i = 1; i <= lastRow; i++) {
            // 4) 获得对应单元格
            Row row = sheet.getRow(i);

            String id = row.getCell(0).getStringCellValue();
            String city = row.getCell(1).getStringCellValue();
            String district = row.getCell(3).getStringCellValue();
            String postcode = row.getCell(4).getStringCellValue();
            String province = row.getCell(5).getStringCellValue();
            String shortcode = row.getCell(6).getStringCellValue();

            //将数据封装到area对象
            Area area = new Area();
            area.setId(id);
            area.setProvince(province);
            area.setCity(city);
            area.setDistrict(district);
            area.setPostcode(postcode);

            // citycode , shortchode 程序自动生成的
            // * 去除 省市县 3个汉字
            province = province.substring(0, province.length() - 1);
            city = city.substring(0, city.length() - 1);
            district = district.substring(0, district.length() - 1);

            // * 简化，省名 + 市名 + 县名 首字
            String[] arr = PinYin4jUtils.getHeadByString(province + city + district);
            StringBuilder sb = new StringBuilder();
            for (String a : arr) {
                sb.append(a);
            }
            area.setShortcode(sb.toString());

            // * 城市码
            String cityCode = PinYin4jUtils.hanziToPinyin(city, "");
            area.setCitycode(cityCode);

            //添加数据到集合
            areaList.add(area);

        }

        //2 保存所有数据
        int count = areaService.saveAreas(areaList);


        //删除和关闭
        workbook.close();
        newFile.delete();

        return new ResponseEntity<String>("本次上传成功" + count + "条", HttpStatus.OK);
    }

    //分页   查询
    @GetMapping
    public ResponseEntity<EasyUIResult<Area>> queryAreaListByPage(Area area,
                                                                  @RequestParam(name = "page", defaultValue = "1") Integer page,
                                                                  @RequestParam(name = "rows", defaultValue = "30") Integer rows) {
        //查询分页数据
        PageInfo<Area> pageInfo = areaService.queryAreaListByPage(area, page, rows);
        //分组成datagrid需要的格式
        EasyUIResult<Area> result = new EasyUIResult<>(pageInfo.getTotal(), pageInfo.getList());

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    //增加  区块
    @PostMapping
    public ResponseEntity<String> save(Area area) {
        area.setId(UUID.randomUUID().toString());
        int count = areaService.save(area);
        if (count == 1) {
            return new ResponseEntity<String>("添加成功", HttpStatus.OK);//200
        }
        return new ResponseEntity<String>("添加失败", HttpStatus.INTERNAL_SERVER_ERROR);//500
    }

    //删除
    @DeleteMapping
    public ResponseEntity<String> delete(@RequestParam(name = "ids[]") String[] ids) {
        int count = areaService.delectArea(ids);
        return new ResponseEntity<String>("本对此作废" + count + "条", HttpStatus.OK);//通用删除
    }

    //修改
    @PutMapping
    public ResponseEntity<String> update(Area area) {

        int r = areaService.update(area);
        if (r == 1) {
            return new ResponseEntity<String>("更新成功", HttpStatus.OK);
        }

        return new ResponseEntity<String>("更新失败", HttpStatus.INTERNAL_SERVER_ERROR);
    }


}