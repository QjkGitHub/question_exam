package com.qjk.service;

import com.qjk.pojo.Question;

import java.util.List;

public interface IQuestionService {

    String saveQuestionInRedis();

    List<Question> createExam(int stateId);
}
