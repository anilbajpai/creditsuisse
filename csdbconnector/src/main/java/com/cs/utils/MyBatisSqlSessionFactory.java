package com.cs.utils;



import java.io.IOException;
import java.io.InputStream;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyBatisSqlSessionFactory {
//	private final static Logger LOGGER = LoggerFactory.getLogger(MyBatisSqlSessionFactory.class);

	private static SqlSessionFactory sqlSessionFactory;

	public static SqlSessionFactory getSqlSessionFactory() {
		if (sqlSessionFactory == null) {
			InputStream inputStream;
			try {
				inputStream = Resources.getResourceAsStream("mybatis-config.xml");

				sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
			} catch (IOException e) {
			//	LOGGER.error("Unable to open session", e);
				throw new RuntimeException(e.getCause());

			}
		}
		return sqlSessionFactory;
	}

	public static SqlSession openSession(boolean autoCommit) {
		return getSqlSessionFactory().openSession(autoCommit);
	}
}