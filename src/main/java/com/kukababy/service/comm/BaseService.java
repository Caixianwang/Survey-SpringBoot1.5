package com.kukababy.service.comm;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSON;
import com.kukababy.dao.mongo.MongoDAO;
import com.kukababy.dao.mongo.jpa.OtherRepository;
import com.kukababy.dao.mongo.jpa.SurveyRepository;
import com.kukababy.dao.mongo.jpa.UserRepository;
import com.kukababy.dao.redis.RedisDAO;
import com.kukababy.entity.mongo.SurveyMO;
import com.kukababy.entity.mongo.bo.PageBO;
import com.kukababy.entity.mongo.bo.QuotaDetailBO;
import com.kukababy.entity.mongo.bo.QuotaGroupBO;
import com.kukababy.entity.vo.BaseItemVO;
import com.kukababy.entity.vo.BaseQuesVO;
import com.kukababy.entity.vo.DatiDTO;
import com.kukababy.entity.vo.publish.QuotaDetail;
import com.kukababy.entity.vo.publish.QuotaGroup;
import com.kukababy.key.KeyUtils;
import com.kukababy.pager.FilterEL;
import com.kukababy.pager.MongoWhere;
import com.kukababy.pager.Pager;
import com.kukababy.utils.Consts;
import com.kukababy.utils.DateUtil;
import com.kukababy.utils.RedisConsts;
import com.kukababy.utils.Utils;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class BaseService {
	private static final Logger log = LoggerFactory.getLogger(BaseService.class);

	@Autowired(required = true)
	protected MongoTemplate mongoTemplate;
	@Autowired(required = true)
	protected MongoDAO mongoDAO;
	@Autowired
	protected RedisDAO redisDAO;
	@Autowired
	protected StringRedisTemplate stringRedisTemplate;

	@Autowired(required = true)
	protected SurveyRepository surveyRepository;

	@Autowired(required = true)
	protected OtherRepository otherRepository;

	@Autowired(required = true)
	protected UserRepository userRepository;

	@Autowired
	protected RestTemplate restTemplate;

	/**
	 * 
	 * <b>描述:答题问题排序</b>: <blockquote>
	 * 
	 * <pre>
	 * </pre>
	 * 
	 * </blockquote>
	 * 
	 * @param surveyMO
	 * @param quess
	 * @return
	 */
	public List<Map<String, Object>> sortQuess(SurveyMO surveyMO, List<Map<String, Object>> quess) {
		List<Map<String, Object>> sortQuess = new ArrayList();// 问题排序
		for (PageBO pageBO : surveyMO.getPageBOs()) {
			for (String quesId : pageBO.getQuesIds()) {
				for (Map<String, Object> ques : quess) {
					if (ques.get("quesId") != null && quesId.equals((String) ques.get("quesId"))) {
						if (!"Desc".equals(ques.get("quesType"))) {// 去掉描述题
							sortQuess.add(ques);
							break;
						}
					}
				}
			}
		}
		for (Map<String, Object> ques : quess) {
			if ("End".equals(ques.get("quesType"))) {// 结束题
				sortQuess.add(ques);
				break;
			}
		}
		return sortQuess;
	}

	public void html2TextQuess(List<Map<String, Object>> quess) {
		for (Map<String, Object> quesMap : quess) {
			String htmlTitle = (String) quesMap.get("htmlTitle");
			String text = Jsoup.parse(htmlTitle).text();
			quesMap.put("htmlTitle", text);
			if (quesMap.get("rows") != null) {
				List<Map<String, Object>> rows = (List) quesMap.get("rows");
				for (Map<String, Object> rowMap : rows) {
					String htmlLabel = (String) rowMap.get("htmlLabel");
					if (htmlLabel != null) {
						String label = Jsoup.parse(htmlLabel).text();
						rowMap.put("htmlLabel", label);
					}
				}
			}
			if (quesMap.get("cols") != null) {
				List<Map<String, Object>> cols = (List) quesMap.get("cols");
				for (Map<String, Object> colMap : cols) {
					String htmlLabel = (String) colMap.get("htmlLabel");
					if (htmlLabel != null) {
						String label = Jsoup.parse(htmlLabel).text();
						colMap.put("htmlLabel", label);
					}
				}
			}
		}
	}

	/**
	 * 
	 * <b>描述:数据分析时使用的问卷的问题</b>: <blockquote>
	 * 
	 * <pre>
	 * 1、来自缓层或数据库
	 * </pre>
	 * 
	 * </blockquote>
	 * 
	 * @param surveyId
	 * @return
	 */
	public List<Map<String, Object>> getDataQuess(String surveyId) {
		String redisKey = surveyId;
		
		String quessRedisKey = RedisConsts.quessPublishPrefix + redisKey;
		BoundHashOperations<String, Object, Object> cacheQuessMap = this.stringRedisTemplate.boundHashOps(quessRedisKey);
		List<Map<String, Object>> quess = (List) JSON.parseArray((String) cacheQuessMap.get("quess"));
		if (quess == null) {
			return this.queryQues(surveyId);
		}
		return quess;
	}

	/**
	 * 
	 * <b>描述:数据分析时使用的问卷</b>: <blockquote>
	 * 
	 * <pre>
	 * 1、来自缓层或数据库
	 * </pre>
	 * 
	 * </blockquote>
	 * 
	 * @param surveyId
	 * @return
	 */
	public SurveyMO getDataSurvey(String surveyId) {
		String redisKey = surveyId;
		
		String surveyRedisKey = RedisConsts.surveyPublishPrefix + redisKey;
		BoundHashOperations<String, Object, Object> cacheMap = this.stringRedisTemplate.boundHashOps(surveyRedisKey);
		SurveyMO survey = JSON.parseObject((String) cacheMap.get("survey"), SurveyMO.class);
		if (survey == null) {
			return this.getSurvey(surveyId);
		}
		return survey;
	}

	/**
	 * 
	 * <b>描述:统计答案里指定字段的数量，目前用于ip数和cookie数</b>: <blockquote>
	 * 
	 * <pre>
	 * </pre>
	 * 
	 * </blockquote>
	 * 
	 * @param answerVO
	 * @param fieldName
	 * @param fieldVal
	 * @return
	 */
	public long countFieldSize(DatiDTO datiDTO, String fieldName, String fieldVal) {
		Criteria criteria = Criteria.where("fieldName").is(fieldVal);
		Query query = new Query(criteria);
		long count = mongoTemplate.count(query, this.anTblName(datiDTO.getSurveyId()));
		return count;
	}

	/**
	 * 
	 * <b>描述:统计答案表里的微信用户数，不同模式对应不同时间段</b>: <blockquote>
	 * 
	 * <pre>
	 * </pre>
	 * 
	 * </blockquote>
	 * 
	 * @param answerVO
	 * @param ipVal
	 * @return
	 */
	public long countWxAnSize(DatiDTO datiDTO) {
		Criteria criteria = Criteria.where("wxOpenid").is(datiDTO.getWxUser().getOpenid());
		Date currDate = new Date();
		if (datiDTO.getSurvey().getWxCfg().getWxCtl().equals(Consts.wxCtl1)) {
			criteria.and("created").gte(DateUtil.todayStart(currDate)).lt(DateUtil.todayEnd(currDate));
			Query query = new Query(criteria);
			return mongoTemplate.count(query, this.anTblName(datiDTO.getSurveyId()));
		}
		return 0;
	}

	/**
	 * 
	 * <b>描述:统计答案表里的ip数，不同模式对应不同时间段</b>: <blockquote>
	 * 
	 * <pre>
	 * </pre>
	 * 
	 * </blockquote>
	 * 
	 * @param answerVO
	 * @param ipVal
	 * @return
	 */
	public long countIpSize(DatiDTO datiDTO) {
		Criteria criteria = Criteria.where("ip").is(datiDTO.getIp());
		Date currDate = new Date();
		if (datiDTO.getSurvey().getIpCtl().equals(Consts.ipCtl1)) {
			criteria.and("created").gte(DateUtil.todayStart(currDate)).lt(DateUtil.todayEnd(currDate));
		} else if (datiDTO.getSurvey().getIpCtl().equals(Consts.ipCtl2)) {
			criteria.and("created").gte(DateUtil.hourStart(currDate)).lt(DateUtil.hourEnd(currDate));
		} else {
			return 0;
		}
		Query query = new Query(criteria);
		return mongoTemplate.count(query, this.anTblName(datiDTO.getSurveyId()));
	}

	/**
	 * 
	 * <b>描述:统计答案表里的cookie数，不同模式对应不同时间段</b>: <blockquote>
	 * 
	 * <pre>
	 * </pre>
	 * 
	 * </blockquote>
	 * 
	 * @param answerVO
	 * @param cookieVal
	 * @return
	 */
	public long countCookieSize(DatiDTO datiDTO) {
		Criteria criteria = Criteria.where("cookie").is(datiDTO.getCookie());
		Date currDate = new Date();
		if (datiDTO.getSurvey().getCookieCtl().equals(Consts.cookieCtl1)) {
			criteria.and("created").gte(DateUtil.todayStart(currDate)).lt(DateUtil.todayEnd(currDate));
		} else if (datiDTO.getSurvey().getCookieCtl().equals(Consts.cookieCtl1)) {
			criteria.and("created").gte(DateUtil.hourStart(currDate)).lt(DateUtil.hourEnd(currDate));
		} else {
			return 0;
		}
		Query query = new Query(criteria);
		//log.info(query.toString());
		return mongoTemplate.count(query, this.anTblName(datiDTO.getSurveyId()));
	}

	public void updatePublishState(String surveyId, Date publishTime) {
		Map<String, Object> updateMap = new HashMap();
		
		updateMap.put("publishState", Consts.YES);
		updateMap.put("publishTime", publishTime);
		
		mongoDAO.updateMulKeyVal(surveyId, updateMap, SurveyMO.TABLE_NAME);
	}

	public void stopSurveyState(String surveyId) {
		Map<String, Object> updateMap = new HashMap();
		updateMap.put("publishState", Consts.NO);
		updateMap.put("publishTime", new Date());
		
		mongoDAO.updateMulKeyVal(surveyId, updateMap, SurveyMO.TABLE_NAME);
	}

	/**
	 * 
	 * <b>描述:统计问卷答题数</b>: <blockquote>
	 * 
	 * <pre>
	 * </pre>
	 * 
	 * </blockquote>
	 * 
	 * @param surveyId
	 * @param anTblName
	 * @return
	 */
	public long countAnSize(String surveyId) {
		Query query = new Query();
		long count = mongoTemplate.count(query, this.anTblName(surveyId));
		return count;
	}

	/**
	 * 
	 * <b>描述:每次发布时，需要重新计算配额缓层时，需要调用</b>: <blockquote>
	 * 
	 * <pre>
	 * 1、得到目前对应的问卷的配额情况
	 * </pre>
	 * 
	 * </blockquote>
	 * 
	 * @param surveyId
	 * @param anTblName
	 * @param cfgQuotaGroups
	 * @return
	 */
	public List<QuotaGroup> cacheQuotaGroup(String surveyId, Map<String, String> var2FieldMap, List<QuotaGroupBO> cfgQuotaGroups) {

		List<QuotaGroup> realQuotaGroups = new ArrayList();
		for (QuotaGroupBO quotaGroupBO : cfgQuotaGroups) {
			QuotaGroup quotaGroup = new QuotaGroup();
			quotaGroup.setName(quotaGroupBO.getName());
			for (QuotaDetailBO quotaDetailBO : quotaGroupBO.getDetails()) {
				// 缓层需要的配额明细结构
				QuotaDetail quotaDetail = getVar2FieldQuotaDetail(surveyId,quotaDetailBO, var2FieldMap);
				if (quotaDetail != null) {
					quotaGroup.getDetails().add(quotaDetail);
				}
			}
			if (!quotaGroup.getDetails().isEmpty()) {
				realQuotaGroups.add(quotaGroup);
			}
		}
		return realQuotaGroups;
	}

	/**
	 * 
	 * <b>描述:转换配置表的配额，得到每个变量配额对应的明细，</b>: <blockquote>
	 * 
	 * <pre>
	 * </pre>
	 * 
	 * </blockquote>
	 * 
	 * @param surveyId
	 * @param anTblName
	 * @param quotaDetailBO
	 * @param var2FieldMap
	 * @return
	 */
	private QuotaDetail getVar2FieldQuotaDetail(String surveyId, QuotaDetailBO quotaDetailBO, Map<String, String> var2FieldMap) {

		QuotaDetail quotaDetail = null;
		List<String> contents = quotaDetailBO.getContents();
		// 第一位是quesId,第二位是varName#@#varVal
		if (!contents.isEmpty() && contents.size() == 2) {
			String split = contents.get(1);
			String dar[] = split.split(Consts.stringSplit);
			if (dar.length == 2) {
				String fieldName = var2FieldMap.get(dar[0]);// 得到字段名
				String fieldVal = dar[1];// 对应字段的值
				if (StringUtils.isNoneBlank(fieldName) && StringUtils.isNoneBlank(fieldVal)) {
					Criteria criteria = Criteria.where(fieldName).is(fieldVal);
					Query query = new Query(criteria);
					long realSize = mongoTemplate.count(query, this.anTblName(surveyId));
					quotaDetail = new QuotaDetail();
					quotaDetail.setFieldName(fieldName);
					quotaDetail.setFieldVal(fieldVal);
					quotaDetail.setMaxSize(quotaDetailBO.getMaxSize());
					quotaDetail.setRealSize(realSize);
					return quotaDetail;
				}
			}
		}
		return null;
	}

	public Map<String, String> var2FieldMap(List<Map<String, Object>> quessMap) {
		List<BaseQuesVO> quess = JSON.parseArray(JSON.toJSONString(quessMap), BaseQuesVO.class);
		Map<String, String> var2FieldMap = new HashMap();
		for (BaseQuesVO ques : quess) {
			handleQuesVarName2FieldName(ques, var2FieldMap);
			if (!ques.getRows().isEmpty() && ques.getCols().isEmpty()) {// 行可能有变量
				handleQuesRowsVarName2FieldName(ques.getRows(), var2FieldMap);
			}
			if (!ques.getRows().isEmpty() && !ques.getCols().isEmpty()) {// 行和列可能有变量
				handleQuesRowsColsVarName2FieldName(ques.getRows(), ques.getCols(), var2FieldMap);
			}
		}
		log.info("varName2FieldName=" + JSON.toJSONString(var2FieldMap));
		return var2FieldMap;
	}

	/**
	 * 
	 * <b>描述:行和列可能都有变量的处理</b>: <blockquote>
	 * 
	 * <pre>
	 * </pre>
	 * 
	 * </blockquote>
	 * 
	 * @param rows
	 * @param cols
	 * @param var2FieldMap
	 */
	private void handleQuesRowsColsVarName2FieldName(List<BaseItemVO> rows, List<BaseItemVO> cols, Map<String, String> var2FieldMap) {
		boolean hasCol = false;
		for (BaseItemVO row : rows) {
			String rvarName = row.getVarName();
			String rvarNameT = row.getVarNameT();
			String rfieldName = row.getFieldName();
			String rfieldNameT = row.getFieldNameT();
			for (BaseItemVO col : cols) {
				String cvarName = col.getVarName();
				String cvarNameT = col.getVarNameT();
				String cfieldName = col.getFieldName();
				String cfieldNameT = col.getFieldNameT();
				if (StringUtils.isNotBlank(cvarName)) {// 行和列组成的变量
					hasCol = true;
					if (StringUtils.isNotBlank(rvarName) && StringUtils.isNotBlank(cvarName)) {
						if (StringUtils.isNotBlank(rfieldName) && StringUtils.isNotBlank(cfieldName)) {
							var2FieldMap.put(rvarName + cvarName, rfieldName + cfieldName);// 变量名2字段名映射
						}
					}
					if (StringUtils.isNotBlank(rvarNameT) && StringUtils.isNotBlank(cvarNameT)) {
						if (StringUtils.isNotBlank(rfieldNameT) && StringUtils.isNotBlank(cfieldNameT)) {
							var2FieldMap.put(rvarNameT + cvarNameT, rfieldNameT + cfieldNameT);// 变量名2字段名映射
						}
					}
				}
			}
		}

		if (!hasCol) {// 使用行作为变量，主要是矩阵题型，如矩阵单选
			for (BaseItemVO row : rows) {
				String varName = row.getVarName();
				String fieldName = row.getFieldName();
				var2FieldMap.put(varName, fieldName);//
			}
		}
	}

	/**
	 * 
	 * <b>描述:处理行选项的变量名映射</b>: <blockquote>
	 * 
	 * <pre>
	 * </pre>
	 * 
	 * </blockquote>
	 * 
	 * @param items
	 * @param var2FieldMap
	 */
	private void handleQuesRowsVarName2FieldName(List<BaseItemVO> rows, Map<String, String> var2FieldMap) {
		for (BaseItemVO row : rows) {
			if (StringUtils.isNotBlank(row.getVarName()) && StringUtils.isNotBlank(row.getFieldName())) {
				var2FieldMap.put(row.getVarName(), row.getFieldName());// 变量名2字段名映射
			}
			if (StringUtils.isNotBlank(row.getVarNameT()) && StringUtils.isNotBlank(row.getFieldNameT())) {
				var2FieldMap.put(row.getVarNameT(), row.getFieldNameT());// 变量名2字段名映射
			}
		}
	}

	/**
	 * 
	 * <b>描述:处理题型一级的变量名2字段名映射</b>: <blockquote>
	 * 
	 * <pre>
	 * </pre>
	 * 
	 * </blockquote>
	 * 
	 * @param ques
	 * @param var2FieldMap
	 */
	private void handleQuesVarName2FieldName(BaseQuesVO ques, Map<String, String> var2FieldMap) {
		if (StringUtils.isNotBlank(ques.getVarName()) && StringUtils.isNotBlank(ques.getFieldName())) {
			var2FieldMap.put(ques.getVarName(), ques.getFieldName());// 变量名2字段名映射
		}
		if (StringUtils.isNotBlank(ques.getVarNameT()) && StringUtils.isNotBlank(ques.getFieldNameT())) {
			var2FieldMap.put(ques.getVarNameT(), ques.getFieldNameT());// 变量名2字段名映射
		}
	}

	public List<Map<String, Object>> queryQues(String surveyId) {
		Pager<Map<String, Object>> pager = new Pager();
		pager.setPageSize(500);
		pager.getFilters().add(new FilterEL("surveyId", surveyId));
		mongoDAO.queryPager(pager, Map.class, "m_ques");
		//for (Map<String, Object> map : pager.getRows()) {
			//map.put("quesKey", map.get("_id"));
			//map.remove("_id");
		//}
		return pager.getRows();
	}

	public List<Map> getQuess(String surveyId) {
		Query query = new Query(Criteria.where("surveyId").is(surveyId));
		List<Map> quess = mongoTemplate.find(query, Map.class, "m_ques");
		return quess;
	}
	/**
	 * 
	 * <b>描述:返回问题的部分字段</b>:
	 * <blockquote>
	 * <pre>
	 * </pre>
	 * </blockquote>
	 * @param surveyId
	 * @param fields
	 * @return
	 */
	public List<Map> getFieldsQuess(String surveyId,String...fields) {
		
		BasicDBObject filters = new BasicDBObject("surveyId", surveyId);
		//QueryBuilder queryBuilder = new QueryBuilder();   
		//queryBuilder.or(new BasicDBObject("", "002"), new BasicDBObject("cname","zcy1"));   
		Query query=new BasicQuery(filters);  
		if(fields!=null){
			BasicDBObject fieldsObject=new BasicDBObject(); 
			for(String field:fields){
				fieldsObject.put(field, 1); 
			}
			query=new BasicQuery(filters,fieldsObject);  
		}
		List<Map> quess = mongoTemplate.find(query, Map.class, "m_ques");
		return quess;
	}
	
	public void delQuess(String surveyId,Set<String> quesKeys) {
		Query query = new Query(Criteria.where("surveyId").is(surveyId).and("_id").in(quesKeys));
		mongoTemplate.remove(query, "m_ques");
	}

	/**
	 * 
	 * <b>描述:请求参数对应的值，赋给目标对象</b>: <blockquote>
	 * 
	 * <pre>
	 * 1、
	 * </pre>
	 * 
	 * </blockquote>
	 * 
	 * @param params
	 * @param target
	 */
	protected void copyProperties(Map<String, Object> params, Object target) {

		Class<? extends Object> cls = target.getClass();
		Set<String> fields = Utils.getAllFields(target);
		// Object source =
		// JSON.parseObject(JSON.toJSONString(params,SerializerFeature.WriteMapNullValue),
		// cls);
		Object source = JSON.parseObject(JSON.toJSONString(params), cls);
		for (String key : params.keySet()) {
			if (fields.contains(key)) {
				try {
					PropertyDescriptor pd = new PropertyDescriptor(key, cls);
					Method getMethod = pd.getReadMethod();
					Method setMethod = pd.getWriteMethod();
					Object getVal = getMethod.invoke(source);
					setMethod.invoke(target, new Object[] { getVal });
				} catch (Exception ex) {
					log.error("属性值复制错误");
					throw new RuntimeException(ex);
				}
			}
		}
	}

	public Map<String, Object> getQues(String quesKey) {
		DBObject dbObject = mongoTemplate.findById(quesKey, DBObject.class, "m_ques");
		return dbObject.toMap();
	}
	/**
	 * 
	 * <b>描述:复制问题</b>:
	 * <blockquote>
	 * <pre>
	 * </pre>
	 * </blockquote>
	 * @param quesKey
	 * @return
	 */
	public Map<String, Object> getCopyQues(String quesKey) {
		DBObject ques = mongoTemplate.findById(quesKey, DBObject.class, "m_ques");
		modifyQuesVarName(ques);
		return ques.toMap();
	}


	/**
	 * 
	 * <b>描述:</b>: <blockquote>
	 * 
	 * <pre>
	 * </pre>
	 * 
	 * </blockquote>
	 * 
	 * @param quesKey
	 * @param ref
	 *            Y:从题库插入到问卷，需要修改变量名
	 * @return
	 */
	public Map<String, Object> getQuesLib(String quesKey, String ref) {
		DBObject ques = mongoTemplate.findById(quesKey, DBObject.class, "m_queslib");
		if (ref.equals("Y")) {
			modifyQuesVarName(ques);
		}
		//ques.put("quesKey", ques.get("_id"));
		//ques.removeField("_id");
		return ques.toMap();
	}
	
	private void modifyQuesVarName(DBObject ques) {
		modifyVarName(ques);
		if (ques.get("rows") != null) {
			List<DBObject> rows = (List) ques.get("rows");
			for (DBObject row : rows) {
				modifyVarName(row);
			}
		}
		if (ques.get("cols") != null) {
			List<DBObject> cols = (List) ques.get("cols");
			for (DBObject col : cols) {
				modifyVarName(col);
			}
		}
		
	}

	/**
	 * 
	 * <b>描述:修改变量名</b>: <blockquote>
	 * 
	 * <pre>
	 * </pre>
	 * 
	 * </blockquote>
	 * 
	 * @param ques
	 */
	private void modifyVarName(DBObject dbObject) {
		String varName = (String) dbObject.get("varName");
		if (StringUtils.isNotBlank(varName)) {
			String pre = varName.substring(0, 2);
			dbObject.put("varName", pre + KeyUtils.getInstance().getShortUUID("v"));
		}
		String varNameT = (String) dbObject.get("varNameT");
		if (StringUtils.isNotBlank(varNameT)) {
			String pre = varName.substring(0, 3);
			dbObject.put("varNameT", pre + KeyUtils.getInstance().getShortUUID("v"));
		}
		String fieldName = (String) dbObject.get("fieldName");
		if (StringUtils.isNotBlank(fieldName)) {
			String pre = fieldName.substring(0, 2);
			dbObject.put("fieldName", pre + KeyUtils.getInstance().getShortUUID("f"));
		}
		String fieldNameT = (String) dbObject.get("fieldNameT");
		if (StringUtils.isNotBlank(fieldNameT)) {
			String pre = fieldNameT.substring(0, 3);
			dbObject.put("fieldNameT", pre + KeyUtils.getInstance().getShortUUID("f"));
		}
	}

	public SurveyMO getSurvey(String surveyId) {
		return surveyRepository.findOne(surveyId);
	}

	public String anTblName(String surveyId) {
		return "an_" + surveyId;
	}

	public Map<String, Object> getDataQues(String surveyId, String quesKey) {
		String redisKey = surveyId;
		
		String quessRedisKey = RedisConsts.quessPublishPrefix + redisKey;
		BoundHashOperations<String, Object, Object> cacheQuessMap = this.stringRedisTemplate.boundHashOps(quessRedisKey);
		List<Map<String, Object>> quess = (List) JSON.parseArray((String) cacheQuessMap.get("quess"));
		if (quess == null) {
			return this.getQues(quesKey);
		} else {
			for (Map<String, Object> ques : quess) {
				if (quesKey.equals((String) ques.get("_id"))) {
					return ques;
				}
			}
		}
		return this.getQues(quesKey);
	}

	public long getCount(Pager pager, String surveyId) {
		BasicDBObject filters = MongoWhere.getWheres(pager);
		long count = mongoTemplate.getCollection(this.anTblName(surveyId)).count((DBObject) filters);
		return count;
	}

	/**
	 * 
	 * <b>描述:</b>: <blockquote>
	 * 
	 * <pre>
	 * </pre>
	 * 
	 * </blockquote>
	 * 
	 * @param primaryId
	 * @return
	 */
	public int getSeq(String primaryId) {
		Query query = new Query(Criteria.where("_id").is(primaryId));
		Update update = new Update().inc("seq", 1);
		Map<String, Object> map = mongoTemplate.findAndModify(query, update, Map.class, "m_seq");
		return 1 + (int) map.get("seq");
	}

}
