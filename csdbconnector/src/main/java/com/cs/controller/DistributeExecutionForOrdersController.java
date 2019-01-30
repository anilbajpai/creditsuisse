package com.cs.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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
import com.cs.entities.ExecutionMst;
import com.cs.entities.ExecutionMstExample;
import com.cs.entities.ExecutionOrderTxn;
import com.cs.entities.InstrumentMst;
import com.cs.entities.OrderBook;
import com.cs.entities.OrderBookExample;
import com.cs.utils.MyBatisSqlSessionFactory;
import com.cs.utils.MybatisAbstractDao;
import com.wordnik.swagger.annotations.Api;

@Controller

@RequestMapping("/DistributeExecutionForOrders")

@Api(basePath = "/DistributeExecutionForOrders", value = "DistributeExecutionForOrders", description = "Operations with Db", produces = "application/json")
@RestController
public class DistributeExecutionForOrdersController {

	@com.wordnik.swagger.annotations.ApiOperation(value = "Insert Data", notes = "Insert data in DB")

	@RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.ALL_VALUE)
	@CrossOrigin(origins = "*")
	public @ResponseBody String saveChangesPost(
			@RequestBody(required = true) InstrumentMst insertRequest) {
		SqlSession sqlSession = MyBatisSqlSessionFactory.getSqlSessionFactory().openSession();
		List<OrderBook> validOrders,marketOrders,updatedValidOrders = new ArrayList<>();
		int sumOfQuantityInValidOrders =0;
		List<OrderBook> inValidOrders;
		List<ExecutionOrderTxn> settledOrderByExecution = new ArrayList<>();
		ExecutionMst finalExecution = new ExecutionMst();
		
		try {
			//Collecting Total Orders
			MybatisAbstractDao<OrderBook, Serializable, BaseEntityExample> daoOrders = new MybatisAbstractDao<OrderBook, Serializable, BaseEntityExample>();
			List<OrderBook> orderBookList;
			OrderBookExample orderBookExample = new OrderBookExample();
			orderBookExample.createCriteria().andInstrumentIdEqualTo(insertRequest.getInstrumentId()).andIsOrderOpenEqualTo("N");//To Select Only Closed Order for Distribution
			orderBookList = daoOrders.selectByExample(orderBookExample);
			
			//Collecting Execution
			MybatisAbstractDao<ExecutionMst, Serializable, BaseEntityExample> daoExecution = new MybatisAbstractDao<ExecutionMst, Serializable, BaseEntityExample>();
			List<ExecutionMst> executionMstList;
			ExecutionMstExample executionMstExample = new ExecutionMstExample();
			executionMstExample.createCriteria().andInstrumentIdEqualTo(insertRequest.getInstrumentId()).andIsExecutionAvailableEqualTo("Y");
			executionMstList = daoExecution.selectByExample(executionMstExample);
			
			if(executionMstList.size()==0){
				return "No Execution Available for this InstrumentId";
			}else{ // Collecting Invalid and Valid Orders in seperate Lists
				finalExecution = executionMstList.get(0);
				validOrders = orderBookList.stream()
					    .filter((p) -> (p.getPrice() <= executionMstList.get(0).getPrice() || p.getOrderType().equals("M"))).collect(Collectors.toList());
				marketOrders = orderBookList.stream()
					    .filter(p -> p.getOrderType().equals("M")).collect(Collectors.toList());
				inValidOrders = orderBookList.stream()
					    .filter(p -> p.getPrice() > executionMstList.get(0).getPrice()).collect(Collectors.toList());
			}
			

			
			for(OrderBook ob: validOrders){
				sumOfQuantityInValidOrders = sumOfQuantityInValidOrders + ob.getQuantity();
			}
			if (finalExecution.getQuantity() >= sumOfQuantityInValidOrders){
				//Distribute Equally
				for(OrderBook ob: validOrders){
					ExecutionOrderTxn eotObj = new ExecutionOrderTxn();
					eotObj.setExecutionId(executionMstList.get(0).getExecutionId());
					eotObj.setOrderId(ob.getOrderId());
					eotObj.setDistributedOrder(ob.getQuantity().floatValue());
					eotObj.setExecutionDate(new Date());
					settledOrderByExecution.add(eotObj);
					//Updating Execution with Remaining Value
					finalExecution.setQuantity(finalExecution.getQuantity() - ob.getQuantity().floatValue());
					//Updating OrderBook
					ob.setQuantity(0);
					ob.setIsOrderOpen("D");
					updatedValidOrders.add(ob);
				}
			}else{
				for(OrderBook ob: validOrders){
					ExecutionOrderTxn eotObj = new ExecutionOrderTxn();
					eotObj.setExecutionId(executionMstList.get(0).getExecutionId());
					eotObj.setOrderId(ob.getOrderId());
					eotObj.setDistributedOrder((ob.getQuantity()*executionMstList.get(0).getQuantity())/sumOfQuantityInValidOrders);
					eotObj.setExecutionDate(new Date());
					settledOrderByExecution.add(eotObj);
					//Updating Execution with Remaining Value
					finalExecution.setQuantity(finalExecution.getQuantity() - eotObj.getDistributedOrder());
					//Updating OrderBook
					ob.setQuantity(ob.getQuantity() - eotObj.getDistributedOrder().intValue());
					if(ob.getQuantity()<=0){
						ob.setIsOrderOpen("D");
					}
					updatedValidOrders.add(ob);
				}
			}
			
			updateExecutionMaster(finalExecution);
			
			updateOrders(updatedValidOrders);
			
			
			
			MybatisAbstractDao<ExecutionOrderTxn, Serializable, BaseEntityExample> daoExecutionOrderTxn = new MybatisAbstractDao<ExecutionOrderTxn, Serializable, BaseEntityExample>();
			for(ExecutionOrderTxn eot:settledOrderByExecution){
				daoExecutionOrderTxn.insert(eot);
			}
			
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

	private void updateOrders(List<OrderBook> updatedValidOrders) {
		SqlSession sqlSession = MyBatisSqlSessionFactory.getSqlSessionFactory().openSession();
		List<OrderBook> orderBookList = new ArrayList<>();
		try {
			for(OrderBook ob: updatedValidOrders){
				MybatisAbstractDao<OrderBook, Serializable, BaseEntityExample> dao = new MybatisAbstractDao<OrderBook, Serializable, BaseEntityExample>();
				OrderBookExample orderBookExample = new OrderBookExample();
				orderBookExample.createCriteria().andOrderIdEqualTo(ob.getOrderId());
				orderBookList = dao.selectByExample(orderBookExample);
					for(OrderBook obi:orderBookList){
						dao.updateByPrimaryKey(ob);
					}
			}
			sqlSession.commit();
			sqlSession.close();
		} catch (Exception e) {
			e.printStackTrace();
			e.getStackTrace();
			sqlSession.rollback();
		}
	}

	private void updateExecutionMaster(ExecutionMst finalExecution) {
		SqlSession sqlSession = MyBatisSqlSessionFactory.getSqlSessionFactory().openSession();
		List<ExecutionMst> executionMstList = new ArrayList<>();
		try {
			MybatisAbstractDao<ExecutionMst, Serializable, BaseEntityExample> dao = new MybatisAbstractDao<ExecutionMst, Serializable, BaseEntityExample>();
			ExecutionMstExample exMstExample = new ExecutionMstExample();
			exMstExample.createCriteria().andExecutionIdEqualTo(finalExecution.getExecutionId());
			executionMstList = dao.selectByExample(exMstExample);
				for(ExecutionMst ex:executionMstList){
							dao.updateByPrimaryKey(finalExecution);
						}						
			sqlSession.commit();
			sqlSession.close();
		} catch (Exception e) {
			e.printStackTrace();
			e.getStackTrace();
			sqlSession.rollback();
		}
		
	}
}
