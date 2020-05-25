package com.hang.manage.system.service.Impl;

import com.hang.manage.system.domain.CollegeMajor;
import com.hang.manage.system.mapper.CollegeMajorMapper;
import com.hang.manage.system.service.CollegeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;

@Service
public class CollegeServiceImpl implements CollegeService {

    @Autowired
    CollegeMajorMapper collegeMajorMapper;

    //根据 college id 拿到name
    public String getName(int id) {
        CollegeMajor collegeMajor = collegeMajorMapper.selectByPrimaryKey(id);
        return collegeMajor.getName();
    }

    //根据 college id 拿到 parentid=id的数据
    public List<Integer> get_id(Integer id) {
        List<Integer> list = new ArrayList<>();

        Example example = new Example(CollegeMajor.class);
        example.createCriteria().andEqualTo("parentid", id);
        List<CollegeMajor> collegeMajors = collegeMajorMapper.selectByExample(example);
        for (CollegeMajor i : collegeMajors) {
            list.add(i.getId());
        }
        return list;
    }

    //通过name拿到id
    public int selectByName(String name) {
        return collegeMajorMapper.selectByname(name);
    }
}
