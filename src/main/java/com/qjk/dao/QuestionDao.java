package com.qjk.dao;


import com.qjk.pojo.Question;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface QuestionDao {
    //获取题表中的所有阶段id
    List<Integer> getStateIdList();

    //获取题表中的所有难度id
    List<Integer> getLevelIdList();

    //参数1：阶段
    //参数2：难度系数
    public List<Question> findQuestionByState(@Param("stateId") int stateId, @Param("levelId") int levelId);


}
