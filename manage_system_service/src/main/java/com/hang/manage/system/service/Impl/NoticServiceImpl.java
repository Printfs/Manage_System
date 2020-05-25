package com.hang.manage.system.service.Impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hang.manage.system.common.ServletResponse;
import com.hang.manage.system.domain.Notic;
import com.hang.manage.system.mapper.NoticMapper;
import com.hang.manage.system.redis.RedisService;
import com.hang.manage.system.service.NoticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class NoticServiceImpl implements NoticService {

    @Autowired
    NoticMapper noticMapper;

    @Autowired
    RedisService redisService;

    //发布公告
    public int publishNotic(Notic notic) {
        return noticMapper.insertNotic(notic);
    }

    //公告列表
    public List<Notic> List_Notic(String publisher) {

        return noticMapper.AdminListNotic(publisher);
    }

    //公告模糊查找
    public List<Notic> FuzzySearchNotic(String publisher, String input) {
        return noticMapper.fuzzySearchnotic(publisher, input);
    }

    //根据id拿到公告
    public Notic UpdateSelectById(int id){

        return noticMapper.UpdateSelectById(id);
    }

    //修改公告
    public int UpdateNotic(Notic notic){
        return noticMapper.UpdateNotic(notic);
    }

    //公告删除
    public int deleteNotic(int id) {
        return noticMapper.deleteNotic(id);
    }

    //首页显示公告
    public ServletResponse listNotic(int pageNum, int pageSize) {

        Example example=new Example(Notic.class);
        example.setOrderByClause("publishtime DESC");
        PageHelper.startPage(pageNum, pageSize);
        List<Notic> notics = noticMapper.selectByExample(example);
        PageInfo<Notic> pageInfo = new PageInfo(notics);
        return ServletResponse.creatBySuccess(pageInfo);
    }

    //显示新闻
    public ServletResponse ViewNotic(int id) {
        Example example = new Example(Notic.class);
        example.createCriteria().andEqualTo("id", id);
        Notic notic = noticMapper.selectOneByExample(example);
        return ServletResponse.creatBySuccess(notic);
    }
}
