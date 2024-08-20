package com.kukababy.service.data;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.kukababy.entity.mongo.SurveyMO;
import com.kukababy.pager.FilterEL;
import com.kukababy.pager.Page;
import com.kukababy.pager.Pager;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

@Service
public class DataService extends DataBaseService {
	private static final Logger log = LoggerFactory.getLogger(DataService.class);
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public void delSample(String anId, String surveyId) {
		SurveyMO surveyMO = this.getSurvey(surveyId);
		String tblName = this.anTblName(surveyId);
		
		Query query = new Query(Criteria.where("_id").is(anId));
		mongoTemplate.remove(query, tblName);
	}

	public HSSFWorkbook exportData01(Pager<DBObject> pager, String surveyId) {

		Map<String, Object> map = queryDataBase(surveyId);
		List<Map<String, Object>> sortQuess = (List<Map<String, Object>>) map.get("sortQuess");
		SurveyMO surveyMO = (SurveyMO) map.get("surveyMO");
		String tblName = this.anTblName(surveyId);
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet();
		int startRow = 0;
		HSSFRow row = sheet.createRow(startRow);
		int startCol = 0;
		String otherCols[] = { "startDate", "ip", "channel", "province", "city" };
		String otherHeaders[] = { "开始时间", "IP", "来源", "省份", "城市" };

		for (String header : otherHeaders) {
			HSSFCell cell = row.createCell(startCol);
			cell.setCellValue(header);
			startCol++;
		}

		for (Map<String, Object> ques : sortQuess) {
			HSSFCell cell = row.createCell(startCol);
			cell.setCellValue((String) ques.get("htmlTitle"));
			startCol++;
		}
		int currPage = 0;
		int pageSize = 1000;
		pager.setPageSize(pageSize);
		pager.setCurrPage(currPage);
		List<DBObject> list = mongoDAO.queryList(pager, DBObject.class, tblName);
		boolean hasMore = true;
		
		while (hasMore) {
			for (DBObject sample : list) {
				startRow++;
				startCol = 0;
				row = sheet.createRow(startRow);

				HSSFCell cell = row.createCell(startCol);
				cell.setCellValue(sdf.format((Date) sample.get("created")));
				startCol++;
				cell = row.createCell(startCol);
				cell.setCellValue((String) sample.get("ip"));
				startCol++;
				cell = row.createCell(startCol);
				cell.setCellValue((String) sample.get("channel"));
				startCol++;
				cell = row.createCell(startCol);
				cell.setCellValue((String) sample.get("province"));
				startCol++;
				cell = row.createCell(startCol);
				cell.setCellValue((String) sample.get("city"));
				startCol++;

				for (Map<String, Object> quesMap : sortQuess) {
					cell = row.createCell(startCol);
					cell.setCellValue(quesAn(quesMap, sample));
					startCol++;
				}
			}
			currPage++;
			if (list.size() == pageSize) {
				list = mongoDAO.queryList(pager, DBObject.class, tblName);
			} else {
				hasMore = false;
			}
		}
		return workbook;
	}

	public Page queryData01(Pager<DBObject> pager, String surveyId) {

		Map<String, Object> map = queryDataBase(surveyId);
		List<Map<String, Object>> sortQuess = (List<Map<String, Object>>) map.get("sortQuess");
		SurveyMO surveyMO = (SurveyMO) map.get("surveyMO");

		
		mongoDAO.queryPager(pager, DBObject.class, this.anTblName(surveyId));
		List<Map<String, String>> datas = new ArrayList();
		Page page = new Page(pager.getTotal(), datas);
		int quesIndex = 1;
		for (DBObject sample : pager.getRows()) {
			Map<String, String> data = new HashMap();
			if(sample.containsField("startDate")){
				data.put("startDate", sdf.format((Date) sample.get("startDate")));
			}
			if(sample.containsField("endDate")){
				data.put("endDate", sdf.format((Date) sample.get("endDate")));
			}
			data.put("province", (String) sample.get("province"));
			data.put("city", (String) sample.get("city"));
			data.put("ip", (String) sample.get("ip"));
			data.put("anId", (String) sample.get("_id"));
			data.put("channel", (String) sample.get("channel"));
			for (Map<String, Object> quesMap : sortQuess) {
				data.put("q" + quesIndex, quesAn(quesMap, sample));
				quesIndex++;
			}
			datas.add(data);
			quesIndex = 1;
		}
		return page;
	}

	public Map<String, Object> getDataHeader01(String surveyId) {

		Map<String, Object> map = queryDataBase(surveyId);
		List<Map<String, Object>> sortQuess = (List<Map<String, Object>>) map.get("sortQuess");
		// SurveyMO surveyMO = (SurveyMO) map.get("surveyMO");
		// String tblName = (String) map.get("tblName");
		List<Map<String, String>> headers = new ArrayList();
		int quesIndex = 1;
		for (Map<String, Object> ques : sortQuess) {
			Map<String, String> header = new HashMap();
			header.put("label", (String) ques.get("htmlTitle"));
			header.put("field", "q" + quesIndex);
			headers.add(header);
			quesIndex++;
		}
		map.put("headers", headers);
		// log.info(JSON.toJSONString(headers));
		return map;
	}

	private String quesAn(Map<String, Object> quesMap, DBObject sample) {
		String quesType = (String) quesMap.get("quesType");
		switch (quesType) {
		case "Input":
			return InputAn(quesMap, sample);
		case "Radio":
		case "RadioVote":
			return RadioAn(quesMap, sample);
		case "Checkbox":
		case "CheckboxVote":
			return CheckboxAn(quesMap, sample);
		case "MulInput":
			return MulInputAn(quesMap, sample);
		case "MatrixRadio":
			return MatrixRadioAn(quesMap, sample);
		case "MatrixCheckbox":
			return MatrixCheckboxAn(quesMap, sample);
		case "MatrixInput":
			return MatrixInputAn(quesMap, sample);
		case "MatrixRate":
			return MatrixRateAn(quesMap, sample);
		case "MatrixScroll":
			return MatrixScrollAn(quesMap, sample);
		case "RadioRate":
			return RadioRateAn(quesMap, sample);
		case "File":
			return FileAn(quesMap, sample);
		case "InputScroll":
			return InputScrollAn(quesMap, sample);
		case "MulSort":
			return MulSortAn(quesMap, sample);
		case "MatrixSelect":
			return MatrixSelectAn(quesMap, sample);
		case "MatrixScale":
			return MatrixScaleAn(quesMap, sample);
		case "RadioSelect":
			return RadioSelectAn(quesMap, sample);
		case "RadioCascader":
			return RadioCascaderAn(quesMap, sample);
		case "MulGoods":
			return MulGoodsAn(quesMap, sample);
		case "End":
			return EndAn(quesMap, sample);
		}
		return "没有此问题类型";
	}

	/**
	 * 
	 * <b>描述:商品题</b>: <blockquote>
	 * 
	 * <pre>
	 * </pre>
	 * 
	 * </blockquote>
	 * 
	 * @param quesMap
	 * @param sample
	 */
	private String EndAn(Map<String, Object> quesMap, DBObject sample) {

		String fieldName = (String) quesMap.get("fieldName");
		String val = object2String(sample, fieldName);

		if (StringUtils.isNotBlank(val)) {// code码转文字显示
			List<Map<String, Object>> rows = (List) quesMap.get("rows");
			for (Map<String, Object> rowMap : rows) {
				String optionVal = (String) rowMap.get("val");
				if (optionVal != null && optionVal.equals(val)) {
					val = leftLable + rowMap.get("desc") + rightLable;
					break;
				}
			}
		}
		return val;
	}

	/**
	 * 
	 * <b>描述:商品题</b>: <blockquote>
	 * 
	 * <pre>
	 * </pre>
	 * 
	 * </blockquote>
	 * 
	 * @param quesMap
	 * @param sample
	 */
	private String MulGoodsAn(Map<String, Object> quesMap, DBObject sample) {

		StringBuilder sb = new StringBuilder();
		List<Map<String, Object>> rows = (List) quesMap.get("rows");
		int i = 0;
		for (Map<String, Object> rowMap : rows) {
			String fieldName = (String) rowMap.get("fieldName");
			String val = object2String(sample, fieldName);
			if (StringUtils.isNotBlank(val)) {// 选中
				if (i > 0) {
					sb.append(split2);
				}
				sb.append(leftLable + rowMap.get("htmlLabel") + rightLable);
				sb.append(split1);
				sb.append(val);
				i++;
			}
		}
		return sb.toString();
	}

	/**
	 * 
	 * <b>描述:联动题</b>: <blockquote>
	 * 
	 * <pre>
	 * </pre>
	 * 
	 * </blockquote>
	 * 
	 * @param quesMap
	 * @param sample
	 */
	private String RadioCascaderAn(Map<String, Object> quesMap, DBObject sample) {
		String fieldName = (String) quesMap.get("fieldName");
		Object objVal = sample.get(fieldName);
		if (objVal != null && objVal instanceof List) {
			List<String> list = (List) objVal;
			String caderCode = "";
			for (String code : list) {
				caderCode += code;
			}
			Map<String, String> keyMap = (Map<String, String>) quesMap.get("keyMap");
			// log.info(JSON.toJSONString(keyMap));
			String caderText = keyMap.get(caderCode);
			if (caderText != null) {
				return leftLable + caderText + rightLable;
			}
		}
		return "";
	}

	/**
	 * 
	 * <b>描述:下拉题</b>: <blockquote>
	 * 
	 * <pre>
	 * </pre>
	 * 
	 * </blockquote>
	 * 
	 * @param quesMap
	 * @param sample
	 */
	private String RadioSelectAn(Map<String, Object> quesMap, DBObject sample) {
		String fieldName = (String) quesMap.get("fieldName");
		String val = object2String(sample, fieldName);

		if (StringUtils.isNotBlank(val)) {// code码转文字显示
			List<Map<String, Object>> rows = (List) quesMap.get("rows");
			for (Map<String, Object> rowMap : rows) {
				String optionVal = (String) rowMap.get("val");
				if (val.equals(optionVal)) {
					val = leftLable + rowMap.get("htmlLabel") + rightLable;
					break;
				}
			}
		}
		return val;
	}

	/**
	 * 
	 * <b>描述:比重题</b>: <blockquote>
	 * 
	 * <pre>
	 * </pre>
	 * 
	 * </blockquote>
	 * 
	 * @param quesMap
	 * @param sample
	 */
	private String MatrixScaleAn(Map<String, Object> quesMap, DBObject sample) {

		StringBuilder sb = new StringBuilder();
		List<Map<String, Object>> rows = (List) quesMap.get("rows");
		int i = 0;
		for (Map<String, Object> rowMap : rows) {
			String fieldName = (String) rowMap.get("fieldName");
			String val = object2String(sample, fieldName);
			if (StringUtils.isNotBlank(val)) {// 选中
				if (i > 0) {
					sb.append(split2);
				}
				sb.append(leftLable + rowMap.get("htmlLabel") + rightLable);
				sb.append(split1);
				sb.append(val);
				i++;
			}
		}
		return sb.toString();
	}

	/**
	 * 
	 * <b>描述:矩阵下拉题</b>: <blockquote>
	 * 
	 * <pre>
	 * </pre>
	 * 
	 * </blockquote>
	 * 
	 * @param quesMap
	 * @param sample
	 */
	private String MatrixSelectAn(Map<String, Object> quesMap, DBObject sample) {

		StringBuilder sb = new StringBuilder();
		int i = 0;
		List<Map<String, Object>> rows = (List) quesMap.get("rows");
		for (Map<String, Object> rowMap : rows) {
			String rFieldName = (String) rowMap.get("fieldName");
			List<Map<String, Object>> cols = (List) quesMap.get("cols");
			for (Map<String, Object> colMap : cols) {
				String cFieldName = (String) colMap.get("fieldName");
				String val = object2String(sample, rFieldName + cFieldName);
				if (StringUtils.isNotBlank(val)) {
					if (i > 0) {
						sb.append(split2);
					}
					List<Map<String, Object>> list = (List) quesMap.get("list");
					for (Map<String, Object> optionMap : list) {
						String optionVal = (String) optionMap.get("val");
						if (val.equals(optionVal)) {
							sb.append(leftLable + rowMap.get("htmlLabel"));
							sb.append(split3);
							sb.append(colMap.get("htmlLabel") + rightLable);
							sb.append(split1);
							sb.append(optionMap.get("htmlLabel"));
							i++;
						}
					}
				}
			}
		}
		return sb.toString();
	}

	/**
	 * 
	 * <b>描述:排序题</b>: <blockquote>
	 * 
	 * <pre>
	 * </pre>
	 * 
	 * </blockquote>
	 * 
	 * @param quesMap
	 * @param sample
	 */
	private String MulSortAn(Map<String, Object> quesMap, DBObject sample) {

		StringBuilder sb = new StringBuilder();
		List<Map<String, Object>> rows = (List) quesMap.get("rows");
		int i = 0;
		for (Map<String, Object> rowMap : rows) {
			String fieldName = (String) rowMap.get("fieldName");
			String val = object2String(sample, fieldName);
			if (StringUtils.isNotBlank(val)) {// 选中
				if (i > 0) {
					sb.append(split2);
				}
				sb.append(leftLable + rowMap.get("htmlLabel") + rightLable);
				sb.append(split1);
				sb.append(val);
				String fieldNameT = (String) rowMap.get("fieldNameT");
				String valT = object2String(sample, fieldNameT);// 填空值
				if (StringUtils.isNotBlank(valT)) {
					sb.append(split1);
					sb.append(valT);
				}
				i++;
			}
		}
		return sb.toString();
	}

	/**
	 * 
	 * <b>描述:滑动题</b>: <blockquote>
	 * 
	 * <pre>
	 * </pre>
	 * 
	 * </blockquote>
	 * 
	 * @param quesMap
	 * @param sample
	 */
	private String InputScrollAn(Map<String, Object> quesMap, DBObject sample) {
		String fieldName = (String) quesMap.get("fieldName");
		return object2String(sample, fieldName);
	}

	/**
	 * 
	 * <b>描述:附件题</b>: <blockquote>
	 * 
	 * <pre>
	 * </pre>
	 * 
	 * </blockquote>
	 * 
	 * @param quesMap
	 * @param sample
	 */
	private String FileAn(Map<String, Object> quesMap, DBObject sample) {
		String fieldName = (String) quesMap.get("fieldName");
		return object2String(sample, fieldName);
	}

	/**
	 * 
	 * <b>描述:量表题</b>: <blockquote>
	 * 
	 * <pre>
	 * </pre>
	 * 
	 * </blockquote>
	 * 
	 * @param quesMap
	 * @param sample
	 */
	private String RadioRateAn(Map<String, Object> quesMap, DBObject sample) {
		String fieldName = (String) quesMap.get("fieldName");
		String val = object2String(sample, fieldName);

		if (StringUtils.isNotBlank(val)) {// code码转文字显示
			List<Map<String, Object>> rows = (List) quesMap.get("rows");
			for (Map<String, Object> rowMap : rows) {
				String optionVal = (String) rowMap.get("val");
				if (optionVal != null && optionVal.equals(val)) {
					val = leftLable + rowMap.get("htmlLabel") + rightLable;
					break;
				}
			}
		}
		return val;
	}

	/**
	 * 
	 * <b>描述:矩阵滑动题</b>: <blockquote>
	 * 
	 * <pre>
	 * </pre>
	 * 
	 * </blockquote>
	 * 
	 * @param quesMap
	 * @param sample
	 */
	private String MatrixScrollAn(Map<String, Object> quesMap, DBObject sample) {

		StringBuilder sb = new StringBuilder();
		List<Map<String, Object>> rows = (List) quesMap.get("rows");
		int i = 0;
		for (Map<String, Object> rowMap : rows) {
			String fieldName = (String) rowMap.get("fieldName");
			String val = object2String(sample, fieldName);
			if (StringUtils.isNotBlank(val)) {
				if (i > 0) {
					sb.append(split2);
				}
				sb.append(leftLable + rowMap.get("htmlLabel") + rightLable);
				sb.append(split1);
				sb.append(val);
				i++;
			}
		}
		return sb.toString();
	}

	/**
	 * 
	 * <b>描述:矩阵量表题</b>: <blockquote>
	 * 
	 * <pre>
	 * </pre>
	 * 
	 * </blockquote>
	 * 
	 * @param quesMap
	 * @param sample
	 */
	private String MatrixRateAn(Map<String, Object> quesMap, DBObject sample) {

		StringBuilder sb = new StringBuilder();
		int i = 0;
		List<Map<String, Object>> rows = (List) quesMap.get("rows");
		for (Map<String, Object> rowMap : rows) {
			String fieldName = (String) rowMap.get("fieldName");
			String val = object2String(sample, fieldName);
			if (StringUtils.isNotBlank(val)) {// code码转文字显示
				List<Map<String, Object>> cols = (List) quesMap.get("cols");
				for (Map<String, Object> colMap : cols) {
					String optionVal = (String) colMap.get("val");
					if (val.equals(optionVal)) {
						if (i > 0) {
							sb.append(split2);
						}
						sb.append(leftLable + rowMap.get("htmlLabel"));
						sb.append(split3);
						sb.append(colMap.get("htmlLabel") + rightLable);
						i++;
						break;
					}
				}
			}
		}
		return sb.toString();
	}

	/**
	 * 
	 * <b>描述:矩阵填空题</b>: <blockquote>
	 * 
	 * <pre>
	 * </pre>
	 * 
	 * </blockquote>
	 * 
	 * @param quesMap
	 * @param sample
	 */
	private String MatrixInputAn(Map<String, Object> quesMap, DBObject sample) {

		StringBuilder sb = new StringBuilder();
		int i = 0;
		List<Map<String, Object>> rows = (List) quesMap.get("rows");
		for (Map<String, Object> rowMap : rows) {
			String rFieldName = (String) rowMap.get("fieldName");
			List<Map<String, Object>> cols = (List) quesMap.get("cols");
			for (Map<String, Object> colMap : cols) {
				String cFieldName = (String) colMap.get("fieldName");
				String val = object2String(sample, rFieldName + cFieldName);
				if (StringUtils.isNotBlank(val)) {
					if (i > 0) {
						sb.append(split2);
					}
					sb.append(leftLable + rowMap.get("htmlLabel"));
					sb.append(split3);
					sb.append(colMap.get("htmlLabel") + rightLable);
					sb.append(split1);
					sb.append(val);
					i++;
				}
			}
		}
		return sb.toString();
	}

	/**
	 * 
	 * <b>描述:矩阵多选题</b>: <blockquote>
	 * 
	 * <pre>
	 * </pre>
	 * 
	 * </blockquote>
	 * 
	 * @param quesMap
	 * @param sample
	 */
	private String MatrixCheckboxAn(Map<String, Object> quesMap, DBObject sample) {

		StringBuilder sb = new StringBuilder();
		int i = 0;
		List<Map<String, Object>> rows = (List) quesMap.get("rows");
		for (Map<String, Object> rowMap : rows) {
			String rFieldName = (String) rowMap.get("fieldName");
			List<Map<String, Object>> cols = (List) quesMap.get("cols");
			for (Map<String, Object> colMap : cols) {
				String cFieldName = (String) colMap.get("fieldName");
				String val = object2String(sample, rFieldName + cFieldName);
				if (StringUtils.isNotBlank(val) && "1".equals(val)) {
					if (i > 0) {
						sb.append(split2);
					}
					sb.append(leftLable + rowMap.get("htmlLabel"));
					sb.append(split3);
					sb.append(colMap.get("htmlLabel") + rightLable);
					i++;
				}
			}
		}
		return sb.toString();
	}

	/**
	 * 
	 * <b>描述:矩阵单选题</b>: <blockquote>
	 * 
	 * <pre>
	 * </pre>
	 * 
	 * </blockquote>
	 * 
	 * @param quesMap
	 * @param sample
	 */
	private String MatrixRadioAn(Map<String, Object> quesMap, DBObject sample) {

		StringBuilder sb = new StringBuilder();
		int i = 0;
		List<Map<String, Object>> rows = (List) quesMap.get("rows");
		for (Map<String, Object> rowMap : rows) {
			String fieldName = (String) rowMap.get("fieldName");
			String val = object2String(sample, fieldName);
			if (StringUtils.isNotBlank(val)) {// code码转文字显示
				List<Map<String, Object>> cols = (List) quesMap.get("cols");
				for (Map<String, Object> colMap : cols) {
					String optionVal = (String) colMap.get("val");
					if (val.equals(optionVal)) {
						if (i > 0) {
							sb.append(split2);
						}
						sb.append(leftLable + rowMap.get("htmlLabel"));
						sb.append(split3);
						sb.append(colMap.get("htmlLabel") + rightLable);
						i++;
						break;
					}
				}
			}
		}
		return sb.toString();
	}

	/**
	 * 
	 * <b>描述:多行填空題</b>: <blockquote>
	 * 
	 * <pre>
	 * </pre>
	 * 
	 * </blockquote>
	 * 
	 * @param quesMap
	 * @param sample
	 */
	private String MulInputAn(Map<String, Object> quesMap, DBObject sample) {

		StringBuilder sb = new StringBuilder();
		List<Map<String, Object>> rows = (List) quesMap.get("rows");
		int i = 0;
		for (Map<String, Object> rowMap : rows) {
			String fieldName = (String) rowMap.get("fieldName");
			String val = object2String(sample, fieldName);
			if (StringUtils.isNotBlank(val)) {
				if (i > 0) {
					sb.append(split2);
				}
				sb.append(leftLable + rowMap.get("htmlLabel") + rightLable);
				sb.append(split1);
				sb.append(val);
				i++;
			}
		}
		return sb.toString();
	}

	/**
	 * 
	 * <b>描述:多选题</b>: <blockquote>
	 * 
	 * <pre>
	 * </pre>
	 * 
	 * </blockquote>
	 * 
	 * @param quesMap
	 * @param sample
	 */
	private String CheckboxAn(Map<String, Object> quesMap, DBObject sample) {

		StringBuilder sb = new StringBuilder();
		List<Map<String, Object>> rows = (List) quesMap.get("rows");
		int i = 0;
		for (Map<String, Object> rowMap : rows) {
			String fieldName = (String) rowMap.get("fieldName");
			String val = object2String(sample, fieldName);
			if (StringUtils.isNotBlank(val) && val.equals("1")) {// 选中
				if (i > 0) {
					sb.append(split2);
				}
				sb.append(leftLable + rowMap.get("htmlLabel") + rightLable);
				String fieldNameT = (String) rowMap.get("fieldNameT");
				String valT = object2String(sample, fieldNameT);// 填空值
				if (StringUtils.isNotBlank(valT)) {
					sb.append(split1);
					sb.append(valT);
				}
				i++;
			}
		}
		return sb.toString();
	}

	/**
	 * 
	 * <b>描述:单选题</b>: <blockquote>
	 * 
	 * <pre>
	 * </pre>
	 * 
	 * </blockquote>
	 * 
	 * @param quesMap
	 * @param sample
	 */
	private String RadioAn(Map<String, Object> quesMap, DBObject sample) {
		String fieldName = (String) quesMap.get("fieldName");
		String fieldNameT = (String) quesMap.get("fieldNameT");

		String val = object2String(sample, fieldName);
		String valT = object2String(sample, fieldNameT);// 填空值

		if (StringUtils.isNotBlank(val)) {// code码转文字显示
			List<Map<String, Object>> rows = (List) quesMap.get("rows");
			for (Map<String, Object> rowMap : rows) {
				String optionVal = (String) rowMap.get("val");
				if (optionVal != null && optionVal.equals(val)) {
					val = leftLable + rowMap.get("htmlLabel") + rightLable;
					break;
				}
			}
		}
		if (StringUtils.isNotBlank(valT)) {
			val += split1 + valT;
		}
		return val;
	}

	/**
	 * 
	 * <b>描述:开放题</b>: <blockquote>
	 * 
	 * <pre>
	 * </pre>
	 * 
	 * </blockquote>
	 * 
	 * @param quesMap
	 * @param sample
	 * @return
	 */
	private String InputAn(Map<String, Object> quesMap, DBObject sample) {
		String fieldName = (String) quesMap.get("fieldName");
		return object2String(sample, fieldName);
	}

	private static String object2String(DBObject sample, String key) {
		Object obj = sample.get(key);
		if (obj != null) {
			String val = null;
			if (obj instanceof String) {
				val = (String) obj;
			} else {
				val = String.valueOf(obj);
			}
			return val.replaceAll(",", "，");
		}
		return "";
	}

}
