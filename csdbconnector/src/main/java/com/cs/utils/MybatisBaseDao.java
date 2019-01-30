package com.cs.utils;

import java.io.Serializable;
import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface MybatisBaseDao<T, PK extends Serializable, E> {
	    int countByExample(E example);

	    int deleteByExample(E example);

	    int deleteByPrimaryKey(PK id);

	    int insert(T record);

	    int insertSelective(T record);

	    List<T> selectByExample(E example);

	    T selectByPrimaryKey(PK id);

	    int updateByExampleSelective(@Param("record") T record, @Param("example") E example);

	    int updateByExample(@Param("record") T record, @Param("example") E example);

	    int updateByPrimaryKeySelective(T record);

	    int updateByPrimaryKey(T record);
	    

}