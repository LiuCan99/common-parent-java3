package com.czxy.bos.controller.base;

import com.czxy.bos.dao.base.StandardMapper;
import com.czxy.bos.domain.base.Standard;
import com.czxy.bos.service.base.StandardService;
import com.czxy.bos.vo.EasyUIResult;
import com.github.pagehelper.PageInfo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;


@RestController     //替换2个 @Controller @ResponseBody
@RequestMapping("/standard")
public class StandardController {
    @Resource
    private StandardService standardService;
    //@RequestMapping(method=RequestMethod.GET)
    @GetMapping
    public ResponseEntity<EasyUIResult<Standard>> queryStandaredByPageTemp(Integer page , Integer rows){
        //1 查询获得分页数据
        PageInfo<Standard> pageInfo = standardService.queryStandardByPage(page ,rows);
        //2 封装数据
        EasyUIResult result = new EasyUIResult<>( pageInfo.getTotal() ,pageInfo.getList());
        //3 返回含有状态的信息
        return new ResponseEntity<>(result , HttpStatus.OK);
    }
    //添加
    @PostMapping
    public ResponseEntity<String> save(Standard standard ) {

        try {
            //保存
            int r = standardService.saveStandard(standard);
            if (r == 1) { //表示添加1条
                return new ResponseEntity<String>("创建成功", HttpStatus.OK);   //200
            }
            return new ResponseEntity<String>("创建过程出现异常", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<String>("创建失败", HttpStatus.INTERNAL_SERVER_ERROR); //500
        }
    }
    //修改
    @PutMapping
    public ResponseEntity<String> update(Standard standard){
        try {
            int r = standardService.update(standard);
            if(r==1){
                return  new ResponseEntity<String>("更新成功",HttpStatus.OK);
            }
            return  new ResponseEntity<String>("更新过程异常",HttpStatus.INTERNAL_SERVER_ERROR);

        }catch (Exception e){
            e.printStackTrace();
            return  new ResponseEntity<String>("更新失败",HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
    //删除
    @DeleteMapping
    public ResponseEntity<String>delete(String ids){
       try {
           //将拼凑好的字符串  拆分成数组
            String[] arr = ids.split(",");
            //删除
            standardService.deleteStandard(arr);
            //提示
            return  new ResponseEntity<String>("删除成功",HttpStatus.OK);
       }catch (Exception e){
            e.printStackTrace();
           return  new ResponseEntity<String>("删除失败",HttpStatus.INTERNAL_SERVER_ERROR);


        }
    }
    //查询所有快递员
    @GetMapping("/all")
    public ResponseEntity<List<Standard>>all(){
        //查询
        List<Standard> list = standardService.findAll();
        return  new ResponseEntity<>(list,HttpStatus.OK);
    }
}

















