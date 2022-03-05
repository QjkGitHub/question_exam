package com.qjk.dao;

import com.qjk.pojo.Parameter;

public interface ParameterDao {
    Parameter getParamByStateId(int stateId);
}
