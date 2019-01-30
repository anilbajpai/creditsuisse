package com.cs.utils;



import java.io.Serializable;
import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;

import com.cs.entities.BaseEntity;
import com.cs.entities.BaseEntityExample;




public class MybatisAbstractDao<T, PK extends Serializable, E> implements MybatisBaseDao<T, PK, E> {

	private final static String NAMESPACE_PREFIX = "com.cs.dao.";

	BaseEntity entities;
	SqlSession sqlSession;

	public SqlSession getSqlSession() {
		return sqlSession;
	}

	public void setSqlSession(SqlSession sqlSession) {
		this.sqlSession = sqlSession;
	}

	public BaseEntity getEntities() {
		return entities;
	}

	public void setEntities(BaseEntity entities) {
		this.entities = entities;
	}

	public int countByExample(E example) {
		// TODO Auto-generated method stub
		return 0;
	}

	public int deleteByExample(T record,E example) {
		SqlSession sqlSession1=MyBatisSqlSessionFactory.getSqlSessionFactory().openSession();
		int updatedRows = 0;
		try{
		
	
			String stmt=NAMESPACE_PREFIX + record.getClass().getSimpleName() + "Mapper.deleteByExample";
			updatedRows = sqlSession1.update(stmt,
					example);
			sqlSession1.commit();	
		}
		
		catch(Exception e)
		{
			e.printStackTrace();
			
			sqlSession1.rollback();
		}
		
			return updatedRows;
			

	}
		

	public int deleteByPrimaryKey(PK id) {
		// TODO Auto-generated method stub
		return 0;
	}

	public int insert(T record) {
		SqlSession sqlSession1=MyBatisSqlSessionFactory.getSqlSessionFactory().openSession();
		int insertedRows = 0;
		try{
		
		System.out.println("In abstractdao insert with record class" + record.getClass().getSimpleName());
		
		String stmt=NAMESPACE_PREFIX + record.getClass().getSimpleName() + "Mapper.insert";
		System.out.println(stmt);
		System.out.println(record);
		insertedRows = sqlSession1.insert(stmt,
				record);
		System.out.println("Successfully inserted");
		sqlSession1.commit();
		sqlSession1.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			
			sqlSession1.rollback();
		}
		return insertedRows;
	}

	public int insertSelective(T record) {
		System.out.println("In abstractdao insert with record class" + record.getClass().getSimpleName());

		int insertedRows = this.sqlSession
				.insert(NAMESPACE_PREFIX + record.getClass().getSimpleName() + "Mapper.insertSelective", record);
		return insertedRows;
	}

	public List<T> selectByExample(E example) {
		SqlSession sqlSession1=MyBatisSqlSessionFactory.getSqlSessionFactory().openSession();
		
		
		
		//System.out.println("In abstractdao select by example  with record class "
			//	+ example.getClass().getSimpleName().replace("Example", ""));
		BaseEntityExample ss = (BaseEntityExample) example;
	List<T> response=sqlSession1.selectList(
			NAMESPACE_PREFIX + example.getClass().getSimpleName().replace("Example", "") + "Mapper.selectByExample",
			example);
	sqlSession1.close();
		return response;

	}
	
	public List<T> selectByExample(E example,int  resultLimit) {
		
		RowBounds rowbounds = new RowBounds(0, resultLimit);
		
		SqlSession sqlSession1=MyBatisSqlSessionFactory.getSqlSessionFactory().openSession();
		
		
		
		//System.out.println("In abstractdao select by example  with record class "
			//	+ example.getClass().getSimpleName().replace("Example", ""));
		BaseEntityExample ss = (BaseEntityExample) example;
	List<T> response=sqlSession1.selectList(
			NAMESPACE_PREFIX + example.getClass().getSimpleName().replace("Example", "") + "Mapper.selectByExample",
			example,rowbounds);
	sqlSession1.close();
		return response;

	}


	public T selectByPrimaryKey(PK id) {
		SqlSession sqlSession1=MyBatisSqlSessionFactory.getSqlSessionFactory().openSession();
		T entities = null;
		entities = sqlSession1.selectOne(
				NAMESPACE_PREFIX + this.entities.getClass().getSimpleName() + "Mapper.selectByPrimaryKey", id);

		return entities;
	}

	public int updateByExampleSelective(T record, E example) {
		// TODO Auto-generated method stub
		return 0;
	}

	public int updateByExample(T record, E example) {
		// TODO Auto-generated method stub
		SqlSession sqlSession1=MyBatisSqlSessionFactory.getSqlSessionFactory().openSession();
		int updatedRows = 0;
		try{
		
		System.out.println("In abstractdao update by example with record class" + record.getClass().getSimpleName());
		
		String stmt=NAMESPACE_PREFIX + record.getClass().getSimpleName() + "Mapper.updateByExample";
		updatedRows = sqlSession1.update(stmt,
				example);
		System.out.println("Successfully updated");
		sqlSession1.commit();
		sqlSession1.close();
		}
		catch(Exception e)
		{
			sqlSession1.rollback();
		}
		return updatedRows;
	}

	public int updateByPrimaryKeySelective(T record) {
		// TODO Auto-generated method stub
		return 0;
	}

	public int updateByPrimaryKey(T record) {
		
	SqlSession sqlSession1=MyBatisSqlSessionFactory.getSqlSessionFactory().openSession();
	int updatedRows = 0;
	try{
	
	System.out.println("In abstractdao insert with record class" + record.getClass().getSimpleName());
	
	String stmt=NAMESPACE_PREFIX + record.getClass().getSimpleName() + "Mapper.updateByPrimaryKey";
	updatedRows = sqlSession1.update(stmt,
			record);
	System.out.println("Statement "+stmt);
	System.out.println("record "+record);
	System.out.println("Successfully updated "+updatedRows+" Rows ");
	sqlSession1.commit();
	sqlSession1.close();
	}
	catch(Exception e)
	{
		System.out.println("Exception is "+e);
		sqlSession1.rollback();
	}
	return updatedRows;
}

	public int updateByPrimaryKeyNew(T record) {
		
		SqlSession sqlSession1=MyBatisSqlSessionFactory.getSqlSessionFactory().openSession();
		int updatedRows = 0;
		try{
		
		System.out.println("In abstractdao insert with record class" + record.getClass().getSimpleName());
		
		String stmt=NAMESPACE_PREFIX + record.getClass().getSimpleName() + "Mapper.updateByPrimaryKey";
		updatedRows = sqlSession1.update(stmt,
				record);
		System.out.println("Successfully updated "+updatedRows+" Rows ");
		sqlSession1.commit();
		sqlSession1.close();
		}
		catch(Exception e)
		{
			sqlSession1.rollback();
		}
		return updatedRows;
	}
	public List<T> findRecords(String sql, T record) {
		return this.sqlSession.selectList(NAMESPACE_PREFIX + record.getClass().getSimpleName() + "Mapper.findRecords",
				sql);

	}

	@Override
	public int deleteByExample(E example) {
		// TODO Auto-generated method stub
		return 0;
	}
}
