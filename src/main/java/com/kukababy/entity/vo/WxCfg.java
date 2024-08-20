/**
 * 
 */
package com.kukababy.entity.vo;

import java.util.List;
import java.util.Map;

import com.kukababy.entity.mongo.SurveyMO;
import com.kukababy.entity.vo.publish.QuotaGroup;
import com.kukababy.utils.Consts;

/**
 * <b>描述：微信答题要求</b>: <blockquote>
 * 
 * <pre>
 * </pre>
 * 
 * </blockquote>
 * 
 * @author caixian.wang@ezcharting.cn
 * @date 2017年7月15日 下午6:55:50
 */

public class WxCfg {

	/**
	 * 需要先判断是否在微信环境了，否则即使设置也不生效 <br/>
	 * 获取微信用户的昵称、性别等（需登录）,此项为true，才有重定向页面
	 */
	private boolean useUser = false;

	/**
	 * true必须登录才能填写,false提示用户登录或不登录也可以填写
	 */
	private boolean login = true;
	/**
	 * 可以修改答案，通过微信账户识别
	 */
	private boolean update = false;
	/**
	 * 答题次数控制，0：答卷期间，1：每天
	 */
	private String wxCtl = Consts.wxCtl0;
	/**
	 * 如果为0，不限制数量
	 */
	private long wxSize = 0;
	/**
	 * 是否允许分享朋友圈
	 */
	boolean shareTimeLine = true ;
	
	/**
	 * 是否允许分享给朋友
	 */
	boolean shareAppMessage = true ;

	/**
	 * @return the useUser
	 */
	public boolean isUseUser() {
		return useUser;
	}

	/**
	 * @param useUser the useUser to set
	 */
	public void setUseUser(boolean useUser) {
		this.useUser = useUser;
	}

	/**
	 * @return the wxCtl
	 */
	public String getWxCtl() {
		return wxCtl;
	}

	/**
	 * @param wxCtl the wxCtl to set
	 */
	public void setWxCtl(String wxCtl) {
		this.wxCtl = wxCtl;
	}

	/**
	 * @return the wxSize
	 */
	public long getWxSize() {
		return wxSize;
	}

	/**
	 * @param wxSize the wxSize to set
	 */
	public void setWxSize(long wxSize) {
		this.wxSize = wxSize;
	}

	/**
	 * @return the login
	 */
	public boolean isLogin() {
		return login;
	}

	/**
	 * @param login the login to set
	 */
	public void setLogin(boolean login) {
		this.login = login;
	}

	/**
	 * @return the shareTimeLine
	 */
	public boolean isShareTimeLine() {
		return shareTimeLine;
	}

	/**
	 * @param shareTimeLine the shareTimeLine to set
	 */
	public void setShareTimeLine(boolean shareTimeLine) {
		this.shareTimeLine = shareTimeLine;
	}

	/**
	 * @return the shareAppMessage
	 */
	public boolean isShareAppMessage() {
		return shareAppMessage;
	}

	/**
	 * @param shareAppMessage the shareAppMessage to set
	 */
	public void setShareAppMessage(boolean shareAppMessage) {
		this.shareAppMessage = shareAppMessage;
	}

	/**
	 * @return the update
	 */
	public boolean isUpdate() {
		return update;
	}

	/**
	 * @param update the update to set
	 */
	public void setUpdate(boolean update) {
		this.update = update;
	}
	
	
}
