package com.qjk.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.qjk.dao.ParameterDao;
import com.qjk.dao.QuestionDao;
import com.qjk.pojo.Parameter;
import com.qjk.pojo.Question;
import com.qjk.util.JedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionServiceImpl implements IQuestionService {
    @Autowired
    private QuestionDao questionDao;

    @Autowired
    private ParameterDao parameterDao;


    @Override
    public String saveQuestionInRedis() {
        List<Integer> stateIdList = questionDao.getStateIdList();
        List<Integer> levelIdList = questionDao.getLevelIdList();
        for (int i = 0; i < stateIdList.size(); i++) {
            for (int j = 0; j < levelIdList.size(); j++) {
                List<Question> questionList = questionDao.findQuestionByState(stateIdList.get(i), levelIdList.get(j));
                changeJsonToRedis(questionList, "stateId" + stateIdList.get(i) + "levelId" + levelIdList.get(j));
            }
        }
        return null;
    }

    public String changeJsonToRedis(List<Question> questionList, String key) {
        Jedis jedis = JedisUtil.getJedis();
        jedis.select(0);
        String json = JSON.toJSONString(questionList);
        String result = jedis.set(key, json);
        return result;
    }

    @Override
    public List<Question> createExam(int stateId) {
        List<Integer> levelIdList = questionDao.getLevelIdList();
        //从难度系数表中读出题的比例
        Parameter parameter = parameterDao.getParamByStateId(stateId);
        String scoreInfo = parameter.getScoreInfo();
        String[] questionNum = scoreInfo.split(":");
        ArrayList<Question> examList = new ArrayList<>();
        //从redis中获取试题
        Jedis jedis = JedisUtil.getJedis();
        for (int i = 0; i < levelIdList.size(); i++) {
            String json = jedis.get("stateId" + stateId + "levelId" + levelIdList.get(i));
            List<Question> questionList = JSONArray.parseArray(json, Question.class);
            randomQuestion(examList, questionList, Integer.parseInt(questionNum[i]));
        }
        return examList;
    }

    public void randomQuestion(List<Question> examList, List<Question> questionList, int count) {
        int temp = (int) ((questionList.size() / count) * Math.random() + 1);
        for (int i = temp; count > 0; i += temp) {
            examList.add(questionList.get(i-1));
            count--;
        }

    }
}
