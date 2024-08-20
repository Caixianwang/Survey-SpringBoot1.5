package com.kukababy.service.design;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.kukababy.common.ThreadCache;
import com.kukababy.entity.mongo.SurveyMO;
import com.kukababy.entity.vo.UserVO;
import com.kukababy.key.KeyUtils;
import com.kukababy.pager.FilterEL;
import com.kukababy.pager.MongoWhere;
import com.kukababy.pager.Pager;
import com.kukababy.service.comm.BaseService;
import com.kukababy.utils.Utils;

@Service
public class QuesDesignService extends BaseService {
	private static final Logger log = LoggerFactory.getLogger(QuesDesignService.class);
	
	public void delQuesLib(String quesKey) {
		
		Query query = new Query(Criteria.where("_id").is(quesKey));
		mongoTemplate.remove(query, "m_queslib");
	}

	public void quesLibList(Pager pager,String share) {
		UserVO userVO = ThreadCache.getUser();
		
		if(share.equals("0")){
			pager.getFilters().add(new FilterEL("share", "Y"));
		}else if(share.equals("1")){
			pager.getSysFilters().add(new FilterEL("userId", userVO.getUserId()));
		}else{
			pager.setSysLogic(false);
			pager.getSysFilters().add(new FilterEL("share", "Y"));
			pager.getSysFilters().add(new FilterEL("userId", userVO.getUserId()));
		}

		mongoDAO.queryPager(pager, Map.class, "m_queslib");
	}

	public void editQues(Map<String, Object> quesMap, String tblName) {
		String quesKey = (String) quesMap.get("_id");
		if (StringUtils.isBlank(quesKey)) {
			quesKey = KeyUtils.getInstance().getShortUUID("qk");
			quesMap.put("_id", quesKey);
			String quesId = KeyUtils.getInstance().getShortUUID("qu");
			quesMap.put("quesId", quesId);
			if (tblName.equals("m_queslib")) {
				UserVO userVO = ThreadCache.getUser();
				quesMap.put("userId", userVO.getUserId());
				if (userVO.getUserId().equals("001")) {
					quesMap.put("share", "Y");//管理员同时最为分享平台
				} 
			}else{
				quesMap.remove("userId");
				quesMap.remove("share");
				quesMap.remove("updateTime");
			}
		}
		if (tblName.equals("m_queslib")) {
			String text = Jsoup.parse((String) quesMap.get("htmlTitle")).text();
			quesMap.put("title", text);
			quesMap.put("updateTime", new Date());
		}
		//quesMap.put("_id", quesKey);
		//quesMap.remove("quesKey");
		validFieldNames(quesMap);// 替换系统唯一fieldId

		mongoTemplate.save(quesMap, tblName);
		log.info(JSON.toJSONString(quesMap));

	}

	private void validFieldNames(Map<String, Object> quesMap) {
		validFieldName(quesMap);
		if (quesMap.get("rows") != null) {
			List<Map<String, Object>> rows = (List) quesMap.get("rows");
			for (Map<String, Object> map : rows) {
				validFieldName(map);
			}
			if (((String) quesMap.get("quesType")).equals("RadioCascader")) {
				handerCascaderVarVal(rows);// 联动题特殊处理
			} else {
				handerVarVal(rows);
			}

		}
		if (quesMap.get("cols") != null) {
			List<Map<String, Object>> cols = (List) quesMap.get("cols");
			for (Map<String, Object> map : cols) {
				validFieldName(map);
			}
			handerVarVal(cols);
		}
		if (quesMap.get("list") != null) {
			List<Map<String, Object>> list = (List) quesMap.get("list");
			// for (Map<String, Object> map : list) {
			// validFieldName(map);
			// }
			handerVarVal(list);
		}

	}

	private void validFieldName(Map<String, Object> map) {
		int pos = -1;
		String varName = (String) map.get("varName");
		if (StringUtils.isNotBlank(varName)) {
			pos = varName.indexOf("CLIenT");
			String pre = "";
			if (pos >= 0) {
				if (pos > 0) {
					pre = varName.substring(0, pos);
				}
				map.put("varName", pre + KeyUtils.getInstance().getShortUUID("v"));
			}

		}
		String varNameT = (String) map.get("varNameT");
		if (StringUtils.isNotBlank(varNameT)) {
			pos = varNameT.indexOf("CLIenT");
			String pre = "";
			if (pos >= 0) {
				if (pos > 0) {
					pre = varNameT.substring(0, pos);
				}
				map.put("varNameT", pre + KeyUtils.getInstance().getShortUUID("v"));
			}
		}

		String fieldName = (String) map.get("fieldName");
		if (StringUtils.isNotBlank(fieldName)) {
			pos = fieldName.indexOf("CLIenT");
			String pre = "";
			if (pos >= 0) {
				if (pos > 0) {
					pre = fieldName.substring(0, pos);
				}
				map.put("fieldName", pre + KeyUtils.getInstance().getShortUUID("f"));
			}
		}
		String fieldNameT = (String) map.get("fieldNameT");
		if (StringUtils.isNotBlank(fieldNameT)) {
			pos = fieldNameT.indexOf("CLIenT");
			String pre = "";
			if (pos >= 0) {
				if (pos > 0) {
					pre = fieldNameT.substring(0, pos);
				}
				map.put("fieldNameT", pre + KeyUtils.getInstance().getShortUUID("f"));
			}
		}
	}

	/**
	 * 
	 * <b>描述:处理变量名的值，如单选题的radio的值</b>: <blockquote>
	 * 
	 * <pre>
	 * </pre>
	 * 
	 * </blockquote>
	 * 
	 * @param map
	 */
	private void handerVarVal(List<Map<String, Object>> rowOrCols) {

		int maxVal = 0;// 找到目前最大值
		for (Map<String, Object> map : rowOrCols) {
			String val = (String) map.get("val");
			if (StringUtils.isNotBlank(val)) {
				if (Utils.isPInt(val)) {
					int currVal = Integer.parseInt(val);
					maxVal = maxVal >= currVal ? maxVal : currVal;
				}
			}
		}
		maxVal++;
		for (Map<String, Object> map : rowOrCols) {
			String val = (String) map.get("val");
			if (StringUtils.isNotBlank(val)) {
				int pos = val.indexOf("CLIenT");
				if (pos == 0) {
					map.put("val", "" + maxVal);
					maxVal++;
				}

			}
		}
	}

	/**
	 * 
	 * <b>描述:联动题的变量值特殊处理</b>: <blockquote>
	 * 
	 * <pre>
	 * </pre>
	 * 
	 * </blockquote>
	 * 
	 * @param rowOrCols
	 */
	private void handerCascaderVarVal(List<Map<String, Object>> rowOrCols) {

		handerVarVal(rowOrCols);

		for (Map<String, Object> map : rowOrCols) {
			Object chileRowsObj = map.get("rows");
			if (chileRowsObj != null) {// 继续处理下一级
				List<Map<String, Object>> chileRows = (List<Map<String, Object>>) chileRowsObj;
				handerCascaderVarVal(chileRows);
			}
		}

	}

}
