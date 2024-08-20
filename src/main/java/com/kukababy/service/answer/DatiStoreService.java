package com.kukababy.service.answer;

import java.util.Date;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.kukababy.dao.mongo.jpa.SurveyRepository;
import com.kukababy.entity.mongo.DatiBO;
import com.kukababy.entity.vo.DatiDTO;
import com.kukababy.entity.vo.WxUser;
import com.kukababy.exception.DatiException;
import com.kukababy.key.KeyUtils;
import com.kukababy.res.ResInfo;
import com.kukababy.service.cache.CacheAnswerService;
import com.kukababy.service.comm.BaseService;
import com.kukababy.utils.Consts;

@Service
public class DatiStoreService extends BaseService {
	private static final Logger log = LoggerFactory.getLogger(DatiStoreService.class);

	@Autowired(required = true)
	private SurveyRepository surveyRepository;
	@Autowired(required = true)
	private CacheAnswerService cacheAnswerService;

	public void datiStore(ResInfo resInfo, DatiDTO datiDTO) {
		log.info(JSON.toJSONString(datiDTO));
		if (resInfo.isHasError()) {
			return;
		}
		if(!datiDTO.getCurrFieldAnMap().containsKey("end")){
			datiDTO.getCurrFieldAnMap().put("hisAn", datiDTO.getVarAnMap());
		}
		if (StringUtils.isBlank(datiDTO.getAnId())) {// 首次提交
			buildFisrtAn(datiDTO);// 组装首次提交的答案
			mongoTemplate.save(datiDTO.getCurrFieldAnMap(), this.anTblName(datiDTO.getSurveyId()));
		} else {
			DatiBO datiBO = cacheAnswerService.getCacheAnswer(datiDTO.getAnId(), this.anTblName(datiDTO.getSurveyId()));
			if (datiBO == null) {
				resInfo.setHasError(true).setStatus(ResInfo.FAIL);
				resInfo.setMsg("警告，没有对应的答案Id，可能答题方式不对！");
				return;
			}
			log.info(JSON.toJSONString(datiDTO.getCurrFieldAnMap()));
			if (datiDTO.getCurrFieldAnMap() != null && !datiDTO.getCurrFieldAnMap().isEmpty()) {
				//回退重新答题不删除已有样本，这样的问卷基本没有逻辑关系，暂时这样处理
//				if (datiDTO.isRtn()) {// 刪除
//					Query query = new Query(Criteria.where("_id").is(datiDTO.getAnId()));
//					Update update = new Update();
//					for (Map.Entry<String, Object> entry : datiDTO.getCurrFieldAnMap().entrySet()) {
//						update.unset(entry.getKey());
//					}
//					mongoTemplate.updateFirst(query, update, this.anTblName(datiDTO.getSurveyId()));
//				} else {
					if(datiDTO.getCurrFieldAnMap().containsKey("end")){
						datiDTO.getCurrFieldAnMap().put("endDate", new Date());
					}
					mongoDAO.updateMulKeyVal(datiDTO.getAnId(), datiDTO.getCurrFieldAnMap(), this.anTblName(datiDTO.getSurveyId()));
//				}
			}

		}
	}

	/**
	 * 
	 * <b>描述:组装首次提交的答案</b>: <blockquote>
	 * 
	 * <pre>
	 * </pre>
	 * 
	 * </blockquote>
	 * 
	 * @param datiDTO
	 */
	private void buildFisrtAn(DatiDTO datiDTO) {
		String anId = KeyUtils.getInstance().getShortUUID("an");
		datiDTO.setAnId(anId);
		datiDTO.getCurrFieldAnMap().put("_id", anId);
		datiDTO.getCurrFieldAnMap().put("startDate", new Date());
		// datiDTO.getCurrFieldAnMap().put("surveyId", datiDTO.getSurveyId());
		datiDTO.getCurrFieldAnMap().put("channel", datiDTO.getAnChannel());

		if (StringUtils.isNotBlank(datiDTO.getIp())) {
			datiDTO.getCurrFieldAnMap().put("ip", datiDTO.getIp());
		}
		if (StringUtils.isNotBlank(datiDTO.getProvince())) {
			datiDTO.getCurrFieldAnMap().put("province", datiDTO.getProvince());
		}
		if (StringUtils.isNotBlank(datiDTO.getCity())) {
			datiDTO.getCurrFieldAnMap().put("city", datiDTO.getCity());
		}
		if (StringUtils.isNotBlank(datiDTO.getCookie())) {
			datiDTO.getCurrFieldAnMap().put("cookie", datiDTO.getCookie());
		}
		if (datiDTO.getWxUser() != null && !datiDTO.isWxSkip()) {
			WxUser wxUser = datiDTO.getWxUser();
			if (StringUtils.isNotBlank(wxUser.getOpenid())) {
				datiDTO.getCurrFieldAnMap().put("wxOpenid", wxUser.getOpenid());
			}
			if (StringUtils.isNotBlank(wxUser.getUnionid())) {
				datiDTO.getCurrFieldAnMap().put("wxUnionid", wxUser.getUnionid());
			}
			if (StringUtils.isNotBlank(wxUser.getCountry())) {
				datiDTO.getCurrFieldAnMap().put("wxCountry", wxUser.getCountry());
			}
			if (StringUtils.isNotBlank(wxUser.getProvince())) {
				datiDTO.getCurrFieldAnMap().put("wxProvince", wxUser.getProvince());
			}
			if (StringUtils.isNotBlank(wxUser.getCity())) {
				datiDTO.getCurrFieldAnMap().put("wxCity", wxUser.getCity());
			}
			if (StringUtils.isNotBlank(wxUser.getNickname())) {
				datiDTO.getCurrFieldAnMap().put("wxNickname", wxUser.getNickname());
			}
			if (StringUtils.isNotBlank(wxUser.getSex())) {
				datiDTO.getCurrFieldAnMap().put("wxSex", wxUser.getSex());
			}
		}
	}

}
