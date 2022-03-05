package com.qjk.controller;

import com.qjk.pojo.Question;
import com.qjk.service.IQuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@CrossOrigin
@RestController
@RequestMapping("/question")
public class QuestionController {
    @Autowired
    private IQuestionService questionService;

    //缓存预热
    @GetMapping("/saveInRedis")
    public String SaveInRedis(){
        String s = questionService.saveQuestionInRedis();
        return "成功";
    }

    //随机生成试卷
    @GetMapping("/createExam")
    public List<Question> createExam(int stateId){
        List<Question> questionList = questionService.createExam(stateId);
        return questionList;
    }


}
