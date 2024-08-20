package com.kukababy.service.data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.kukababy.entity.mongo.SurveyMO;
import com.kukababy.service.comm.BaseService;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

@Service
public class DataBaseService extends BaseService {
	private static final Logger log = LoggerFactory.getLogger(DataBaseService.class);

	protected static final String split1 = "=";//〓
	protected static final String split2 = "✚";//✚
	protected static final String split3 = "_";//▂

	protected static final String leftLable = "[";//【
	protected static final String rightLable = "]";//】

	protected Map<String, Object> exportDataBase(String surveyId) {
		SurveyMO surveyMO = this.getDataSurvey(surveyId);
		List<Map<String, Object>> quess = this.getDataQuess(surveyId);
		List<Map<String, Object>> sortQuess = this.sortQuess(surveyMO, quess);// 问题排序
		this.html2TextQuess(sortQuess);// html2text转换
		this.code2LabelMap(sortQuess);//code码和标签映射

		DBObject query = new BasicDBObject(); // setup the query criteria 设置查询条件
		query.put("surveyId", surveyId);

		// DBObject fields = new BasicDBObject(); //only get the needed fields.
		// 设置需要获取哪些域
		// fields.put("_id", 0);
		String tblName = "an_" + surveyMO.getUserId();
		
		DBCursor dbCursor = mongoTemplate.getCollection(tblName).find(query);
		Map<String, Object> map = new HashMap();
		map.put("surveyMO", surveyMO);
		map.put("sortQuess", sortQuess);
		map.put("dbCursor", dbCursor);
		return map;
	}
	
	public Map<String, Object> getSurveyAndQuess(String surveyId) {
		SurveyMO surveyMO = this.getDataSurvey(surveyId);
		List<Map<String, Object>> quess = this.getDataQuess(surveyId);
		List<Map<String, Object>> sortQuess = this.sortQuess(surveyMO, quess);// 问题排序
		this.html2TextQuess(sortQuess);// html2text转换
		this.code2LabelMap(sortQuess);//code码和标签映射

		Map<String, Object> map = new HashMap();
		map.put("survey", surveyMO);
		map.put("quess", sortQuess);

		return map;
	}
	
	protected Map<String, Object> queryDataBase(String surveyId) {
		SurveyMO surveyMO = this.getDataSurvey(surveyId);
		List<Map<String, Object>> quess = this.getDataQuess(surveyId);
		List<Map<String, Object>> sortQuess = this.sortQuess(surveyMO, quess);// 问题排序
		this.html2TextQuess(sortQuess);// html2text转换
		this.code2LabelMap(sortQuess);//code码和标签映射

		// DBObject fields = new BasicDBObject(); //only get the needed fields.
		// 设置需要获取哪些域
		// fields.put("_id", 0);
		
		Map<String, Object> map = new HashMap();
		map.put("surveyMO", surveyMO);
		map.put("sortQuess", sortQuess);
		return map;
	}


	/**
	 * 
	 * <b>描述:增加问题上的code码l对应名称映射，便于数据导出时直接通过键值快速获取</b>: <blockquote>
	 * 
	 * <pre>
	 * </pre>
	 * 
	 * </blockquote>
	 * 
	 * @param sortQuess
	 */
	protected void code2LabelMap(List<Map<String, Object>> sortQuess) {

		for (Map<String, Object> quesMap : sortQuess) {
			String quesType = (String) quesMap.get("quesType");
			switch (quesType) {
			case "RadioCascader":
				radioCascaderVal2LabelMap(quesMap);
				break;
			}
		}
	}

	protected void radioCascaderVal2LabelMap(Map<String, Object> quesMap) {
			Map<String,String> keyMap = new HashMap();
			List<Map<String, Object>> rows = (List) quesMap.get("rows");
			for (Map<String, Object> rowMap : rows) {
				String currKey = (String) rowMap.get("val");
				String htmlLabel = (String) rowMap.get("htmlLabel");
				keyMap.put(currKey, htmlLabel);
				recursionRadioCascader(rowMap,keyMap,currKey);
			}
			quesMap.put("keyMap", keyMap);
	}
	
	private void recursionRadioCascader(Map<String, Object> rowMap,Map<String,String> keyMap,String currKey){
		List<Map<String, Object>> rows = (List) rowMap.get("rows");
		if(rows!=null&&!rows.isEmpty()){
			for (Map<String, Object> rowChildMap : rows) {
				String childKey = (String) rowChildMap.get("val");
				String htmlLabel = (String) rowChildMap.get("htmlLabel");
				String currLabel = keyMap.get(currKey);
				keyMap.put(currKey+childKey, currLabel+split3+htmlLabel);
				recursionRadioCascader(rowChildMap,keyMap,currKey+childKey);
			}
			keyMap.remove(currKey);//删除多余的键
		}
	}

}
