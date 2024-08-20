/**
 * 
 */
package com.kukababy.common;

import com.kukababy.entity.vo.DatiDTO;
import com.kukababy.entity.vo.UserVO;

/**
 * <b>描述</b>:
 * <blockquote>
 * <pre>
 * </pre>
 * </blockquote>
 * @author caixian_wang@sina.com
 * @date 2018年1月4日 下午8:59:31
 */
public class ThreadCache {

	private static final ThreadLocal<UserVO> userThread = new ThreadLocal();
	private static final ThreadLocal<DatiDTO> datiThread = new ThreadLocal();

	
	public static void setDatiDTO(DatiDTO datiDTO) {
		datiThread.set(datiDTO);
	}
	public static DatiDTO getDatiDTO() {
		return datiThread.get();
	}
	
	public static void setUser(UserVO userVO) {
		userThread.set(userVO);
	}
	public static UserVO getUser() {
		return userThread.get();
	}

}
