package com.cs.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import javax.ws.rs.Consumes;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.cs.entities.BaseEntityExample;
import com.cs.entities.InstrumentMst;
import com.cs.entities.OrderBook;
import com.cs.contracts.InsertResponse;
import com.cs.contracts.InstrumentPojo;
import com.cs.utils.MyBatisSqlSessionFactory;
import com.cs.utils.MybatisAbstractDao;

import org.apache.ibatis.session.SqlSession;
import org.springframework.http.MediaType;
import com.google.gson.Gson;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiResponses;

@Controller

@RequestMapping("/CreateOrder")

@Api(basePath = "/CreateOrder", value = "CreateOrder", description = "Operations with Db", produces = "application/json")
@RestController
public class CreateOrderController {

	@com.wordnik.swagger.annotations.ApiOperation(value = "Insert Data", notes = "Insert data in DB")

	@RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.ALL_VALUE)
	@CrossOrigin(origins = "*")
	public @ResponseBody String saveChangesPost(
			@RequestBody(required = true) OrderBook insertRequest) {
		SqlSession sqlSession = MyBatisSqlSessionFactory.getSqlSessionFactory().openSession();
		try {
			MybatisAbstractDao<OrderBook, Serializable, BaseEntityExample> dao = new MybatisAbstractDao<OrderBook, Serializable, BaseEntityExample>();
			dao.insert(insertRequest);
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
