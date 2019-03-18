package com.czxy.bos.service.transit;

import com.czxy.bos.dao.transit.InOutStorageInfoMapper;
import com.czxy.bos.dao.transit.TransitInfoMapper;
import com.czxy.bos.domain.transit.InOutStorageInfo;
import com.czxy.bos.domain.transit.TransitInfo;
import com.czxy.bos.exception.BosException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * Created by lenovo on 2018/10/11.
 */
@Service
@Transactional
public class InOutStorageInfoService {

    @Resource
    private InOutStorageInfoMapper inOutStorageInfoMapper;

    @Resource
    private TransitInfoMapper transitInfoMapper;

    public void save(InOutStorageInfo inOutStorageInfo){
        //1 查询“运输配送信息对象”
        System.out.println("99999"+inOutStorageInfo);
        TransitInfo transitInfo =transitInfoMapper.selectByPrimaryKey(inOutStorageInfo.getTransitInfoId()             );

        //2 服务器端校验：如果状态是“出入库中转”
        if(transitInfo==null){
            throw  new BosException("操作对象不存在");
        }
        if(! "出入库中转".equals(transitInfo.getStatus())){
            throw  new BosException("当前运输配送信息对象的状态不是‘出入库中转’");
        }

        //3 保存出入库信息
        inOutStorageInfoMapper.insert(inOutStorageInfo);

        //4  如果是“到达网点”，需将“运输配送信息对象”的状态修改成“到达网点”，同时记录网点地址值
        if("到达网点".equals(inOutStorageInfo.getOperation())){
            transitInfo.setStatus("到达网点");
            transitInfo.setOutletAddress(inOutStorageInfo.getAddress());
            transitInfoMapper.updateByPrimaryKey(transitInfo);

        }





    }
}
