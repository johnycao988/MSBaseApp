/**
 * 
 */
package com.cs.baseapp.api.messagebroker.event;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.cs.baseapp.api.app.MSBaseApplication;
import com.cs.baseapp.errorhandling.BaseAppException;
import com.cs.commons.jdbc.DSManager;
import com.cs.commons.jdbc.IJdbcExec;

/**
 * @author Donald.Wang
 *
 */
public class EventDao {

	public void addEvent(EventMessage eventMessage) throws BaseAppException {
		IJdbcExec exec = DSManager.getJdbcExec(MSBaseApplication.getBaseInfo().getSystemDataSource());
		StringBuilder sql = new StringBuilder("INSERT INTO ").append(EventDaoConst.EVENT_TB_BASE_APP_EVENT);

	}

	private Map<String, String> buildParaMap(EventMessage eventMessage) {
		Map<String, String> paraMap = new HashMap<>();
		if (!StringUtils.isEmpty(eventMessage.getEventName())) {
			paraMap.put(EventDaoConst.EVENT_CLOUMN_EVENT_NAME, eventMessage.getEventName());
		}
		if (!StringUtils.isEmpty(eventMessage.getEventId())) {
			paraMap.put(EventDaoConst.EVENT_CLOUMN_EVENT_ID, eventMessage.getEventId());
		}
		if (!StringUtils.isEmpty(eventMessage.getTrxId())) {
			paraMap.put(EventDaoConst.EVENT_CLOUMN_TRX_ID, eventMessage.getTrxId());
		}
		if (!StringUtils.isEmpty(eventMessage.getTrxBusinessDesc())) {
			paraMap.put(EventDaoConst.EVENT_CLOUMN_TRX_BUS_DESC, eventMessage.getTrxBusinessDesc());
		}
		if (!StringUtils.isEmpty(eventMessage.getCreator())) {
			paraMap.put(EventDaoConst.EVENT_CLOUMN_CREATOR, eventMessage.getCreator());
		}
		if (eventMessage.getEventData() != null) {
			paraMap.put(EventDaoConst.EVENT_CLOUMN_EVENT_DATA, eventMessage.getEventData().toString());
		}
		paraMap.put(EventDaoConst.EVENT_CLOUMN_CREATE_TIME,
				new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		return paraMap;
	}

}
