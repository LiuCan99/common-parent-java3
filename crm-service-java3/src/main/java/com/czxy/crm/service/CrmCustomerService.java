package com.czxy.crm.service;

import com.czxy.bos.exception.BosException;
import com.czxy.crm.dao.CrmCustomerMapper;
import com.czxy.crm.domain.Customer;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;


@Service
@Transactional
public class CrmCustomerService {
    @Resource
    private CrmCustomerMapper crmCustomerMapper;




    //查询没有关联的定区用户    客户中定区外键位null
       public List<Customer> findNoAssociationCustomers(){
           //拼凑条件   findAreaId为null
           Example example = new Example(Customer.class);
           Example.Criteria criteria = example.createCriteria();
           criteria.andIsNotNull("fixedAreaId");

           //查询
           return  crmCustomerMapper.selectByExample( example );


       }

         public List<Customer> findHasAssociationFixedAreaCustomers(String fixedAreaId){
           //创建Example
           Example example = new Example(Customer.class);
           //设置查询条件
             example.createCriteria().andEqualTo("fixedAreaId",fixedAreaId);
             List<Customer> list = crmCustomerMapper.selectByExample( example );
             return  list;

         }
         public void associationCustomersToFixedArea(String fixedAreaId,String customerIdStr){
            //将指定定区所有字段改为null（删除之前的关联）
             List<Customer> list = findHasAssociationFixedAreaCustomers((fixedAreaId));
             for (Customer customer : list){
                 customer.setFixedAreaId(null);
                 crmCustomerMapper.updateByPrimaryKey(customer);
             }
         //切割字符串 1,2,3 组成新的关联关系
             /*if (StringUtils.isBlank(customerIdStr)) {
                 return;
             }*/
             //更新本次选中的所有
             String [] customerIdArray = customerIdStr.split(",");
             for (String idStr :customerIdArray ){
                 Integer id = Integer.parseInt(idStr);
                 //查询
                 Customer customer = crmCustomerMapper.selectByPrimaryKey(id);
                 customer.setFixedAreaId(fixedAreaId);
                 crmCustomerMapper.updateByPrimaryKey(customer);

             }


         }
    public void saveCustomer(Customer customer) {
        //1 校验
        // 1.1 非空校验
        if(StringUtils.isBlank(customer.getTelephone())){
            throw new BosException("手机不能为空！");
        }
        // 1.2 手机号不能重复
        Example example = new Example(Customer.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("telephone",customer.getTelephone());
        Customer findCustomer = crmCustomerMapper.selectOneByExample(example);
        if(findCustomer != null){
            throw new BosException("手机已被占用！");
        }

        //2 添加
        crmCustomerMapper.insert(customer);

    }
    public Customer findByTelephone(String telephone) {
        Example example = new Example(Customer.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("telephone", telephone);

        return crmCustomerMapper.selectOneByExample(example);
    }


    //根据电话号码    修改用户的修改状态
    public void updateType(String telephone){
             Customer customer = findByTelephone(telephone);
             if(customer==null){
                 throw  new BosException("该操作对象已存在");
             }
             //在更新
        customer.setType(1);
             crmCustomerMapper.updateByPrimaryKey(customer);
    }
    //通过   手机号码    和密码   查询用户
    public Customer findCustomerByTelephoneAndPassword(String telephone , String password) {
       Example example = new Example(Customer.class);
       example.createCriteria().andEqualTo("telephone",telephone).andEqualTo("password",password);
       return  this.crmCustomerMapper.selectOneByExample(example);




    }

    //通过客户id和地址查询客户，所关联的定区id
    public String findFixedAreaIdByAddressAndId(String address , Integer customerId){
        //条件
        Example example = new Example(Customer.class);
        example.createCriteria().andEqualTo("id",customerId).andEqualTo("address",address);

        //查询
        Customer customer = crmCustomerMapper.selectOneByExample( example );
        if (customer == null){
            return  null;
        }
        //返回定区器
        return  customer.getFixedAreaId();
    }

}
