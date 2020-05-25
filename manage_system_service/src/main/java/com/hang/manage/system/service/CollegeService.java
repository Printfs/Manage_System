package com.hang.manage.system.service;

import java.util.List;

public interface CollegeService {
     String getName(int id);


     //根据 college id 拿到 parentid=id的数据
     public List<Integer> get_id(Integer id);

     //通过name拿到id
      int selectByName(String name);
}
