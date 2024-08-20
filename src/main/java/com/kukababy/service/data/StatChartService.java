package com.kukababy.service.data;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.kukababy.entity.mongo.SurveyMO;
import com.kukababy.pager.MongoWhere;
import com.kukababy.pager.Pager;
import com.mongodb.AggregationOptions;
import com.mongodb.AggregationOptions.OutputMode;
import com.mongodb.BasicDBObject;
import com.mongodb.Cursor;
import com.mongodb.DBObject;

@Service
public class StatChartService extends DataBaseService {
	private static final Logger log = LoggerFactory.getLogger(StatChartService.class);
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public void quesChart(Pager<DBObject> pager, String surveyId) {

		Map<String, Object> map = queryDataBase(surveyId);
		List<Map<String, Object>> sortQuess = (List<Map<String, Object>>) map.get("sortQuess");
		SurveyMO surveyMO = (SurveyMO) map.get("surveyMO");
		String tblName = (String) map.get("tblName");
		BasicDBObject filters = MongoWhere.getWheres(pager);

	}

	/**
	 * 
	 * <b>描述:矩阵下拉题</b>: <blockquote>
	 * 
	 * <pre>
	 *  1、Map:{"K_1fwdsK_23ss:2":20,"E_1fwdrK_w22233:3"::30}
	 *  2、返回键值对象，键是行列选项的字段名+"_"+列选项的值，值是各列的数量
	 * </pre>
	 * 
	 * </blockquote>
	 * 
	 * @param quesMap
	 * @param filters
	 * @param tblName
	 * @return
	 */
	public Map<String, Object> matrixSelectData(Map<String, Object> quesMap, BasicDBObject filters) {
		String tblName = this.anTblName((String)quesMap.get("surveyId"));
		BasicDBObject match = new BasicDBObject("$match", filters);
		Map<String, Object> resMap = new HashMap();
		List<Map<String, Object>> rows = (List) quesMap.get("rows");
		List<Map<String, Object>> cols = (List) quesMap.get("cols");
		for (Map<String, Object> rowMap : rows) {
			for (Map<String, Object> colMap : cols) {
				List<DBObject> pipe = new ArrayList<DBObject>();
				String fieldNameR = (String) rowMap.get("fieldName");
				String fieldNameC = (String) colMap.get("fieldName");
				BasicDBObject groupDetail = new BasicDBObject("_id", "$" + fieldNameR + fieldNameC);
				groupDetail.put("count", new BasicDBObject("$sum", 1));
				BasicDBObject group = new BasicDBObject("$group", groupDetail);
				pipe.add(match);
				pipe.add(group);
				Cursor cursor = mongoTemplate.getCollection(tblName).aggregate(pipe,
						AggregationOptions.builder().allowDiskUse(true).outputMode(OutputMode.CURSOR).build());
				while (cursor.hasNext()) {
					BasicDBObject dbObject = (BasicDBObject) cursor.next();
					resMap.put(fieldNameR + fieldNameC + ":" + dbObject.getString("_id"), dbObject.getInt("count"));
				}
			}
		}
		return resMap;
	}

	/**
	 * 
	 * <b>描述:排序题</b>: <blockquote>
	 * 
	 * <pre>
	 *  1、Map:{"A_1fwds:2":20,"A_1fwdr:3"::30}
	 *  2、返回键值对象，键是选项的字段名+"_"+列选项的值，值是各列的数量
	 * </pre>
	 * 
	 * </blockquote>
	 * 
	 * @param quesMap
	 * @param filters
	 * @param tblName
	 * @return
	 */
	public Map<String, Object> mulSortData(String quesKey, BasicDBObject filters) {
		Map<String, Object> quesMap = this.getQues(quesKey);
		return matrixRadioData(quesMap, filters);
	}

	public Map<String, Object> mulSortData(Map<String, Object> quesMap, BasicDBObject filters) {
		String tblName = this.anTblName((String)quesMap.get("surveyId"));
		BasicDBObject match = new BasicDBObject("$match", filters);
		Map<String, Object> resMap = new HashMap();
		List<Map<String, Object>> rows = (List) quesMap.get("rows");
		for (Map<String, Object> rowMap : rows) {
			List<DBObject> pipe = new ArrayList<DBObject>();
			String fieldName = (String) rowMap.get("fieldName");
			BasicDBObject groupDetail = new BasicDBObject("_id", "$" + fieldName);
			groupDetail.put("count", new BasicDBObject("$sum", 1));
			BasicDBObject group = new BasicDBObject("$group", groupDetail);
			pipe.add(match);
			pipe.add(group);
			Cursor cursor = mongoTemplate.getCollection(tblName).aggregate(pipe,
					AggregationOptions.builder().allowDiskUse(true).outputMode(OutputMode.CURSOR).build());
			while (cursor.hasNext()) {
				BasicDBObject dbObject = (BasicDBObject) cursor.next();
				resMap.put(fieldName + ":" + dbObject.getString("_id"), dbObject.getInt("count"));
			}
		}
		return resMap;
	}

	/**
	 * 
	 * <b>描述:滑动题</b>: <blockquote>
	 * 
	 * <pre>
	 *  1、Map:{"1":200,"2":10}
	 *  2、返回键值对象，键是选项值，值是此选项的选择数量
	 * </pre>
	 * 
	 * </blockquote>
	 * 
	 * @param quesMap
	 * @param filters
	 * @param tblName
	 * @return
	 */

	public Map<String, Object> inputScrollData(String quesKey, BasicDBObject filters) {
		Map<String, Object> quesMap = this.getQues(quesKey);
		return inputScrollData(quesMap, filters);
	}

	public Map<String, Object> inputScrollData(Map<String, Object> quesMap, BasicDBObject filters) {
		String tblName = this.anTblName((String)quesMap.get("surveyId"));
		String fieldName = (String) quesMap.get("fieldName");
		List<DBObject> pipe = new ArrayList<DBObject>();
		BasicDBObject match = new BasicDBObject("$match", filters);

		BasicDBObject groupDetail = new BasicDBObject("_id", "$" + fieldName);
		groupDetail.put("count", new BasicDBObject("$sum", 1));
		BasicDBObject group = new BasicDBObject("$group", groupDetail);

		pipe.add(match);
		pipe.add(group);
		// log.info(pipe.toString());
		Cursor cursor = mongoTemplate.getCollection(tblName).aggregate(pipe,
				AggregationOptions.builder().allowDiskUse(true).outputMode(OutputMode.CURSOR).build());
		Map<String, Object> res = new HashMap();
		while (cursor.hasNext()) {
			BasicDBObject dbObject = (BasicDBObject) cursor.next();
			if (dbObject.get("_id") != null) {
				res.put(dbObject.getString("_id"), dbObject.getInt("count"));
			}
		}
		// log.info(JSON.toJSONString(res));
		return res;
	}

	/**
	 * 
	 * <b>描述:矩阵多选</b>: <blockquote>
	 * 
	 * <pre>
	 *  1、Map:{"E_1fwdsE_1fwdd":200,"E_1fwdrE_1fwde":10}
	 *  2、返回键值对象，键是选项行列字段名，值是单元格的数量
	 * </pre>
	 * 
	 * </blockquote>
	 * 
	 * @param quesMap
	 * @param filters
	 * @param tblName
	 * @return
	 */
	public Map<String, Object> matrixCheckboxData(String quesKey, BasicDBObject filters) {
		Map<String, Object> quesMap = this.getQues(quesKey);
		return matrixCheckboxData(quesMap, filters);
	}

	public Map<String, Object> matrixCheckboxData(Map<String, Object> quesMap, BasicDBObject filters) {
		String tblName = this.anTblName((String)quesMap.get("surveyId"));
		List<DBObject> pipe = new ArrayList<DBObject>();
		BasicDBObject match = new BasicDBObject("$match", filters);

		BasicDBObject groupDetail = new BasicDBObject("_id", null);
		List<Map<String, Object>> rows = (List) quesMap.get("rows");
		List<Map<String, Object>> cols = (List) quesMap.get("cols");
		for (Map<String, Object> rowMap : rows) {
			for (Map<String, Object> colMap : cols) {
				String fieldNameR = (String) rowMap.get("fieldName");
				String fieldNameC = (String) colMap.get("fieldName");
				groupDetail.put(fieldNameR + fieldNameC, new BasicDBObject("$sum", "$" + fieldNameR + fieldNameC));
			}
		}
		BasicDBObject group = new BasicDBObject("$group", groupDetail);
		pipe.add(match);
		pipe.add(group);

		Cursor cursor = mongoTemplate.getCollection(tblName).aggregate(pipe,
				AggregationOptions.builder().allowDiskUse(true).outputMode(OutputMode.CURSOR).build());
		BasicDBObject dbObject = new BasicDBObject();
		if (cursor.hasNext()) {
			dbObject = (BasicDBObject) cursor.next();
			dbObject.remove("_id");
		}
		return (Map<String, Object>) dbObject;
	}

	/**
	 * 
	 * <b>描述:矩阵单选</b>: <blockquote>
	 * 
	 * <pre>
	 *  1、Map:{"A_1fwds:2":20,"A_1fwdr:3"::30}
	 *  2、返回键值对象，键是选项的字段名+"_"+列选项的值，值是各列的数量
	 * </pre>
	 * 
	 * </blockquote>
	 * 
	 * @param quesMap
	 * @param filters
	 * @param tblName
	 * @return
	 */
	public Map<String, Object> matrixRadioData(String quesKey, BasicDBObject filters) {
		Map<String, Object> quesMap = this.getQues(quesKey);
		return matrixRadioData(quesMap, filters);
	}

	public Map<String, Object> matrixRadioData(Map<String, Object> quesMap, BasicDBObject filters) {
		String tblName = this.anTblName((String)quesMap.get("surveyId"));
		BasicDBObject match = new BasicDBObject("$match", filters);
		Map<String, Object> resMap = new HashMap();
		List<Map<String, Object>> rows = (List) quesMap.get("rows");
		for (Map<String, Object> rowMap : rows) {
			List<DBObject> pipe = new ArrayList<DBObject>();
			String fieldName = (String) rowMap.get("fieldName");
			BasicDBObject groupDetail = new BasicDBObject("_id", "$" + fieldName);
			groupDetail.put("count", new BasicDBObject("$sum", 1));
			BasicDBObject group = new BasicDBObject("$group", groupDetail);
			pipe.add(match);
			pipe.add(group);
			Cursor cursor = mongoTemplate.getCollection(tblName).aggregate(pipe,
					AggregationOptions.builder().allowDiskUse(true).outputMode(OutputMode.CURSOR).build());
			while (cursor.hasNext()) {
				BasicDBObject dbObject = (BasicDBObject) cursor.next();
				resMap.put(fieldName + ":" + dbObject.getString("_id"), dbObject.getInt("count"));
			}
		}
		return resMap;
	}

	/**
	 * 
	 * <b>描述:商品题</b>: <blockquote>
	 * 
	 * <pre>
	 *  1、Map:{"A_1fwds":200,"A_1fwdr":10}
	 *  2、返回键值对象，键是选项的字段名，值是此选项的选择数量
	 * </pre>
	 * 
	 * </blockquote>
	 * 
	 * @param quesMap
	 * @param filters
	 * @param tblName
	 * @return
	 */
	public Map<String, Object> mulGoodsData(String quesKey, BasicDBObject filters) {
		Map<String, Object> quesMap = this.getQues(quesKey);
		return mulGoodsData(quesMap, filters);
	}

	public Map<String, Object> mulGoodsData(Map<String, Object> quesMap, BasicDBObject filters) {
		String tblName = this.anTblName((String)quesMap.get("surveyId"));
		List<DBObject> pipe = new ArrayList<DBObject>();
		BasicDBObject match = new BasicDBObject("$match", filters);

		BasicDBObject groupDetail = new BasicDBObject("_id", null);
		List<Map<String, Object>> rows = (List) quesMap.get("rows");
		for (Map<String, Object> rowMap : rows) {
			String fieldName = (String) rowMap.get("fieldName");
			groupDetail.put(fieldName, new BasicDBObject("$sum", "$" + fieldName));
		}
		BasicDBObject group = new BasicDBObject("$group", groupDetail);
		pipe.add(match);
		pipe.add(group);

		Cursor cursor = mongoTemplate.getCollection(tblName).aggregate(pipe,
				AggregationOptions.builder().allowDiskUse(true).outputMode(OutputMode.CURSOR).build());
		BasicDBObject dbObject = new BasicDBObject();
		if (cursor.hasNext()) {
			dbObject = (BasicDBObject) cursor.next();
			dbObject.remove("_id");
		}
		return (Map<String, Object>) dbObject;
	}

	/**
	 * 
	 * <b>描述:多选题</b>: <blockquote>
	 * 
	 * <pre>
	 *  1、Map:{"A_1fwds":200,"A_1fwdr":10}
	 *  2、返回键值对象，键是选项的字段名，值是此选项的选择数量
	 * </pre>
	 * 
	 * </blockquote>
	 * 
	 * @param quesMap
	 * @param filters
	 * @param tblName
	 * @return
	 */
	public Map<String, Object> checkboxData(String quesKey, BasicDBObject filters) {
		Map<String, Object> quesMap = this.getQues(quesKey);
		return checkboxData(quesMap, filters);
	}

	public Map<String, Object> checkboxData(Map<String, Object> quesMap, BasicDBObject filters) {
		String tblName = this.anTblName((String)quesMap.get("surveyId"));
		List<DBObject> pipe = new ArrayList<DBObject>();
		BasicDBObject match = new BasicDBObject("$match", filters);

		BasicDBObject groupDetail = new BasicDBObject("_id", null);
		List<Map<String, Object>> rows = (List) quesMap.get("rows");
		for (Map<String, Object> rowMap : rows) {
			String fieldName = (String) rowMap.get("fieldName");
			groupDetail.put(fieldName, new BasicDBObject("$sum", "$" + fieldName));
		}
		BasicDBObject group = new BasicDBObject("$group", groupDetail);
		pipe.add(match);
		pipe.add(group);

		Cursor cursor = mongoTemplate.getCollection(tblName).aggregate(pipe,
				AggregationOptions.builder().allowDiskUse(true).outputMode(OutputMode.CURSOR).build());
		BasicDBObject dbObject = new BasicDBObject();
		if (cursor.hasNext()) {
			dbObject = (BasicDBObject) cursor.next();
			dbObject.remove("_id");
		}
		return (Map<String, Object>) dbObject;
	}

	/**
	 * 
	 * <b>描述:级联题</b>: <blockquote>
	 * 
	 * <pre>
	 *  1、list:[{ "_id" : [ "1", "1", "1" ], "count" : 2 }]
	 *  2、返回list对象，键是选项值，值是此选项的选择数量
	 * </pre>
	 * 
	 * </blockquote>
	 * 
	 * @param quesMap
	 * @param filters
	 * @param tblName
	 * @return
	 */

	public List<DBObject> radioCascaderData(String quesKey, BasicDBObject filters) {
		Map<String, Object> quesMap = this.getQues(quesKey);
		return radioCascaderData(quesMap, filters);
	}

	public List<DBObject> radioCascaderData(Map<String, Object> quesMap, BasicDBObject filters) {
		String tblName = this.anTblName((String)quesMap.get("surveyId"));
		String fieldName = (String) quesMap.get("fieldName");
		List<DBObject> pipe = new ArrayList<DBObject>();
		BasicDBObject match = new BasicDBObject("$match", filters);

		BasicDBObject groupDetail = new BasicDBObject("_id", "$" + fieldName);
		groupDetail.put("count", new BasicDBObject("$sum", 1));
		BasicDBObject group = new BasicDBObject("$group", groupDetail);

		pipe.add(match);
		pipe.add(group);
		// log.info(pipe.toString());
		Cursor cursor = mongoTemplate.getCollection(tblName).aggregate(pipe,
				AggregationOptions.builder().allowDiskUse(true).outputMode(OutputMode.CURSOR).build());
		List<DBObject> list = new ArrayList();
		while (cursor.hasNext()) {
			BasicDBObject dbObject = (BasicDBObject) cursor.next();
			if (dbObject.get("_id") != null) {
				list.add(dbObject);
			}			
		}
		// log.info(JSON.toJSONString(res));
		return list;
	}

	/**
	 * 
	 * <b>描述:结束题</b>: <blockquote>
	 * 
	 * <pre>
	 *  1、Map:{"1":200,"2":10}
	 *  2、返回键值对象，键是选项值，值是此选项的选择数量
	 * </pre>
	 * 
	 * </blockquote>
	 * 
	 * @param quesMap
	 * @param filters
	 * @param tblName
	 * @return
	 */

	public Map<String, Object> endData(String quesKey, BasicDBObject filters) {
		Map<String, Object> quesMap = this.getQues(quesKey);
		return endData(quesMap, filters);
	}

	public Map<String, Object> endData(Map<String, Object> quesMap, BasicDBObject filters) {
		String tblName = this.anTblName((String)quesMap.get("surveyId"));
		String fieldName = (String) quesMap.get("fieldName");
		List<DBObject> pipe = new ArrayList<DBObject>();
		BasicDBObject match = new BasicDBObject("$match", filters);

		BasicDBObject groupDetail = new BasicDBObject("_id", "$" + fieldName);
		groupDetail.put("count", new BasicDBObject("$sum", 1));
		BasicDBObject group = new BasicDBObject("$group", groupDetail);

		pipe.add(match);
		pipe.add(group);
		// log.info(pipe.toString());
		Cursor cursor = mongoTemplate.getCollection(tblName).aggregate(pipe,
				AggregationOptions.builder().allowDiskUse(true).outputMode(OutputMode.CURSOR).build());
		Map<String, Object> res = new HashMap();
		while (cursor.hasNext()) {
			BasicDBObject dbObject = (BasicDBObject) cursor.next();
			if (dbObject.get("_id") != null) {
				res.put(dbObject.getString("_id"), dbObject.getInt("count"));
			}
		}
		// log.info(JSON.toJSONString(res));
		return res;
	}

	/**
	 * 
	 * <b>描述:</b>: <blockquote>
	 * 
	 * <pre>
	 *  1、Map:{"1":200,"2":10}
	 *  2、返回键值对象，键是选项值，值是此选项的选择数量
	 * </pre>
	 * 
	 * </blockquote>
	 * 
	 * @param quesMap
	 * @param filters
	 * @param tblName
	 * @return
	 */

	public Map<String, Object> radioData(String quesKey, BasicDBObject filters) {
		Map<String, Object> quesMap = this.getQues(quesKey);
		return radioData(quesMap, filters);
	}

	public Map<String, Object> radioData(Map<String, Object> quesMap, BasicDBObject filters) {
		String tblName = this.anTblName((String)quesMap.get("surveyId"));
		String fieldName = (String) quesMap.get("fieldName");
		List<DBObject> pipe = new ArrayList<DBObject>();
		BasicDBObject match = new BasicDBObject("$match", filters);

		BasicDBObject groupDetail = new BasicDBObject("_id", "$" + fieldName);
		groupDetail.put("count", new BasicDBObject("$sum", 1));
		BasicDBObject group = new BasicDBObject("$group", groupDetail);

		pipe.add(match);
		pipe.add(group);
		// log.info(pipe.toString());
		Cursor cursor = mongoTemplate.getCollection(tblName).aggregate(pipe,
				AggregationOptions.builder().allowDiskUse(true).outputMode(OutputMode.CURSOR).build());
		Map<String, Object> res = new HashMap();
		while (cursor.hasNext()) {
			BasicDBObject dbObject = (BasicDBObject) cursor.next();
			if (dbObject.get("_id") != null) {
				res.put(dbObject.getString("_id"), dbObject.getInt("count"));
			}
		}
		// log.info(JSON.toJSONString(res));
		return res;
	}

	/**
	 * 
	 * <b>描述:量表题</b>: <blockquote>
	 * 
	 * <pre>
	 *  1、Map:{"1":200,"2":10}
	 *  2、返回键值对象，键是选项值，值是此选项的选择数量
	 * </pre>
	 * 
	 * </blockquote>
	 * 
	 * @param quesMap
	 * @param filters
	 * @param tblName
	 * @return
	 */

	public Map<String, Object> radioRateData(String quesId, BasicDBObject filters) {
		Map<String, Object> quesMap = this.getQues(quesId);
		return radioRateData(quesMap, filters);
	}

	public Map<String, Object> radioRateData(Map<String, Object> quesMap, BasicDBObject filters) {
		String tblName = this.anTblName((String)quesMap.get("surveyId"));
		String fieldName = (String) quesMap.get("fieldName");
		List<DBObject> pipe = new ArrayList<DBObject>();
		BasicDBObject match = new BasicDBObject("$match", filters);

		BasicDBObject groupDetail = new BasicDBObject("_id", "$" + fieldName);
		groupDetail.put("count", new BasicDBObject("$sum", 1));
		BasicDBObject group = new BasicDBObject("$group", groupDetail);

		pipe.add(match);
		pipe.add(group);
		// log.info(pipe.toString());
		Cursor cursor = mongoTemplate.getCollection(tblName).aggregate(pipe,
				AggregationOptions.builder().allowDiskUse(true).outputMode(OutputMode.CURSOR).build());
		Map<String, Object> res = new HashMap();
		while (cursor.hasNext()) {
			BasicDBObject dbObject = (BasicDBObject) cursor.next();
			if (dbObject.get("_id") != null) {
				res.put(dbObject.getString("_id"), dbObject.getInt("count"));
			}
		}
		// log.info(JSON.toJSONString(res));
		return res;
	}

}
