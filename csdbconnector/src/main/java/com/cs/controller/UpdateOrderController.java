package com.cs.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.cs.entities.BaseEntityExample;
import com.cs.entities.OrderBook;
import com.cs.entities.OrderBookExample;
import com.cs.utils.MyBatisSqlSessionFactory;
import com.cs.utils.MybatisAbstractDao;
import com.wordnik.swagger.annotations.Api;

@Controller

@RequestMapping("/UpdateOrder")

@Api(basePath = "/UpdateOrder", value = "UpdateOrder", description = "Operations with Db", produces = "application/json")
@RestController
public class UpdateOrderController {

	@com.wordnik.swagger.annotations.ApiOperation(value = "Insert Data", notes = "Insert data in DB")

	@RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.ALL_VALUE)
	@CrossOrigin(origins = "*")
	public @ResponseBody String saveChangesPost(
			@RequestBody(required = true) OrderBook insertRequest) {
		SqlSession sqlSession = MyBatisSqlSessionFactory.getSqlSessionFactory().openSession();
		List<OrderBook> orderBookList = new ArrayList<>();
		try {
			MybatisAbstractDao<OrderBook, Serializable, BaseEntityExample> dao = new MybatisAbstractDao<OrderBook, Serializable, BaseEntityExample>();
			OrderBookExample obe = new OrderBookExample();
			obe.createCriteria().andOrderIdEqualTo(insertRequest.getOrderId()).andClientIdEqualTo(insertRequest.getClientId());
			orderBookList = dao.selectByExample(obe);
			if(orderBookList.isEmpty()){
				return "Order Does Not Exist";
			}else{
				for(OrderBook ob:orderBookList){
					if(ob.getIsOrderOpen().equals("Y")){
						if(ob.getQuantity()>insertRequest.getQuantity()){
							return "You can not decrease Order";
						}else{
							dao.updateByPrimaryKey(insertRequest);
						}
					}else{
						return "Sorry, Order Closed";
					}
						
				}
			}
			//dao.insert(insertRequest);
			sqlSession.commit();
			sqlSession.close();
			return "SUCCESS_POST";
		} catch (Exception e) {
			e.printStackTrace();
			e.getStackTrace();
			sqlSession.rollback();
			return "ERROR_POST";

		}
	}
}
