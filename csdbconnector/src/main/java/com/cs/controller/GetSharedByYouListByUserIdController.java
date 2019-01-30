package com.cs.controller;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.ibatis.session.SqlSession;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.cs.utils.MyBatisSqlSessionFactory;
import com.wordnik.swagger.annotations.Api;

@Controller

@RequestMapping("/GetSharedByYouListByUserIdController")

@Api(basePath = "/GetSharedByYouListByUserIdController", value = "GetSharedByYouListByUserIdController", description = "Operations with Db", produces = "application/json")
@RestController
public class GetSharedByYouListByUserIdController {
	char ch = '"';
	private static final String template = "Hello, %s!";
	private final AtomicLong counter = new AtomicLong();
	
   
	@com.wordnik.swagger.annotations.ApiOperation(value = "Get Data", notes = "Get data in DB")

	@RequestMapping(method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@CrossOrigin(origins = "*")

	public @ResponseBody String getDetails(@RequestParam Long userId) {
		StringBuilder masterString = new StringBuilder();
		 
		char ch = '"';
		//System.out.println("First ");
		SqlSession sqlSession = MyBatisSqlSessionFactory.getSqlSessionFactory().openSession();
		//System.out.println("Second ");
		try {
			Connection conn = sqlSession.getConnection();
			Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			Statement stmtInner = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);	
			masterString.append("{");

            List<String> fields= Arrays.asList("userid","listid","listdesc","createdon","contacts","sharedbyuser","owner");
            String dataQryOuter ="select eul.userid,eul.listid,el.listdesc,eul.create_time,(select count(1) from journolistslinkage j where  j.listid = eul.listid and j.active='Y') contacts , eul.sharedbyuser,eul.owner from  emailuserlistlinkage eul,emaillists el where eul.listid = el.listid and  eul.sharedbyuser='"+userId+"' and eul.sharedbyuser <> eul.userid and eul.active<>'N' and el.active <>'N' ";
            //System.out.println(tableName+ " and "+columnName);
            try {

    			String header = "details";
    			ResultSet data = stmt.executeQuery(dataQryOuter);
    			masterString.append(ch + header + ch + ": [");
    			while (data.next()) {
    				int i = 1;
    				String LISTID = data.getString(2);
    				masterString.append("{");
    				for (String str : fields) {
    					masterString.append(ch + str + ch + ":" + ch + data.getString(i) + ch);
    					i++;
    					if (i <= fields.size()+1) {
    						masterString.append(",");
    					}
    				}
    				
    		            List<String> fieldsInner= Arrays.asList("journalistid","userid","firstname","designation","mediaoutletname","emailofficial","mobileprimary");
    		            String dataQryInner ="select jll.journalistid,cd.userid,ud.firstname,max(cs.designation),max(cs.mediaoutletname),cd.emailofficial,cd.mobileprimary from journolistslinkage jll,journalistdetails jld,contactdetails cd,userdetails ud,careersummary cs where "+LISTID+" = jll.listid and jll.journalistid = jld.journalistid and jld.userid = cd.userid and cd.userid = ud.userid and cd.userid = cs.userid  and jll.active ='Y' group by jll.journalistid,cd.userid,ud.firstname,cd.emailofficial,cd.mobileprimary ";
    		            System.out.println(dataQryInner);
    	    			ResultSet dataInner = stmtInner.executeQuery(dataQryInner);
    	    			masterString.append(ch + "journalistDetails" + ch + ": [");
    	    			while (dataInner.next()) {
    	    				int j = 1;
    	    				masterString.append("{");
    	    				for (String strInner : fieldsInner) {
    	    					masterString.append(ch + strInner + ch + ":" + ch + dataInner.getString(j) + ch);
    	    					j++;
    	    					if (j <= fieldsInner.size()) {
    	    						masterString.append(",");
    	    					}
    	    				}
    	    				masterString.append("}");
    	    				if (!dataInner.isLast()) {
    	    					masterString.append(",");
    	    				}
    	    			}
    	    			masterString.append("]");
    	    			
    				masterString.append("}");
    				if (!data.isLast()) {
    					masterString.append(",");
    				}
    				
    			}
    			masterString.append("]");
    			//data.close();
    		} catch (Exception e) {
    			System.out.println("Exception e ");
    			e.printStackTrace();
    			e.getStackTrace();
    		}
    
            masterString.append("}");
			
			sqlSession.commit();
			//System.out.println("Last 2");
			sqlSession.close();
			//System.out.println(masterString);

		} catch (Exception e) {
			System.out.println("Exception e ");
			e.printStackTrace();
			e.getStackTrace();
			sqlSession.rollback();

		}
		System.out.println(masterString);
		return masterString.toString();
	}

	public StringBuilder replaceString(StringBuilder sb, String toReplace, String replacement) {
		int index = -1;
		while ((index = sb.lastIndexOf(toReplace)) != -1) {
			sb.replace(index, index + toReplace.length(), replacement);
		}
		return sb;
	}

		
}
