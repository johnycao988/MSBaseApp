/**
 * 
 */
package com.cs.baseapp.repository.dao;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.cs.baseapp.errorhandling.BaseAppException;
import com.cs.baseapp.logger.LogManager;
import com.cs.cloud.message.api.MessageHeadConsumer;
import com.cs.cloud.message.api.MessageHeadService;
import com.cs.cloud.message.api.MessageRequest;
import com.cs.cloud.message.api.MessageResponse;
import com.cs.commons.jdbc.IJdbcExec;
import com.cs.log.common.logbean.LogException;
import com.cs.log.logs.LogInfoMgr;
import com.cs.log.logs.bean.Logger;

/**
 * @author Donald.Wang
 *
 */
public class MessageRepositoryDao {

	private Logger logger = LogManager.getSQLLogger();

	public void storeMessage(MessageRequest request) {
		String unitCode = request.getTransaction().getUnitCode();
		IJdbcExec exec = DSManager.getJdbcExec(unitCode);
		try {
			String sql = buildSQL(buildParaMap(request));
			exec.update(LogManager.getSQLLog(request), null);
		} catch (LogException e) {
			BaseAppException ex = new BaseAppException(e, LogInfoMgr.getErrorInfo("ERR_0038", request.getJsonString()));
			logger.write(LogManager.getServiceLogKey(request), ex);
		}
	}

	private String getTableName(String unitCode) {
		return DSManager.getSchema(unitCode) + "." + RepositoryConstant.TB_BASE_APP_MSG.getValue();
	}

	public Map<String, Integer> getColumns() throws LogException {
		IJdbcExec exec = DSManager.getDefaultJdbcExec();
		return exec.getColumns(LogManager.getSQLLog(), DSManager.getDefaultSchema(),
				RepositoryConstant.TB_BASE_APP_MSG.getValue());
	}

	private String buildSQL(Map<String, String> paraMap) {
		StringBuilder sql = new StringBuilder(
				"INSERT INTO " + getTableName(paraMap.get(RepositoryConstant.COL_UNIT_CODE.getValue())));
		Set<Entry<String, String>> paraSet = paraMap.entrySet();
		StringBuilder columns = new StringBuilder("(");
		StringBuilder values = new StringBuilder("(");
		for (Entry<String, String> entry : paraSet) {
			columns.append(entry.getKey()).append(",");
			values.append("'" + entry.getValue() + "'").append(",");
		}
		columns.append(RepositoryConstant.CREATE_TIME.getValue()).append(")");
		values.append("TO_DATE('" + getFormatDateTime() + "','yyyy-mm-dd hh24:mi:ss')").append(")");
		return sql.append(columns).append(" VALUES ").append(values).toString();
	}

	private String getFormatDateTime() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(new Date());
	}

	private Map<String, String> buildParaMap(MessageRequest request) {
		Map<String, String> paraMap = new HashMap<>();
		if (!StringUtils.isEmpty(request.getBase().getId())) {
			paraMap.put(RepositoryConstant.COL_MESSAGE_ID.getValue(), request.getBase().getId());
		}
		MessageHeadConsumer consumer = request.getConsumer();
		if (!StringUtils.isEmpty(consumer.getId())) {
			paraMap.put(RepositoryConstant.COL_SERVICE_ID.getValue(), consumer.getId());
		}
		if (!StringUtils.isEmpty(consumer.getClientId())) {
			paraMap.put(RepositoryConstant.COL_APP_ID.getValue(), consumer.getClientId());
		}
		if (!StringUtils.isEmpty(consumer.getId()) && !StringUtils.isEmpty(consumer.getClientId())) {
			paraMap.put(RepositoryConstant.COL_FROM_SERVICE.getValue(),
					consumer.getClientId() + "/" + consumer.getId());
		}
		if (!StringUtils.isEmpty(request.getTransaction().getUnitCode())) {
			paraMap.put(RepositoryConstant.COL_UNIT_CODE.getValue(), request.getTransaction().getUnitCode());
		}
		if (!StringUtils.isEmpty(request.getTransaction().getFunctionId())) {
			paraMap.put(RepositoryConstant.COL_FUNC_ID.getValue(), request.getTransaction().getFunctionId());
		}
		if (!StringUtils.isEmpty(request.getTransaction().getTransactionNo())) {
			paraMap.put(RepositoryConstant.COL_TRX_NO.getValue(), request.getTransaction().getTransactionNo());
		}
		if (!StringUtils.isEmpty(request.getTransaction().getReferenceNo())) {
			paraMap.put(RepositoryConstant.COL_REF_NO.getValue(), request.getTransaction().getReferenceNo());
		}
		if (!StringUtils.isEmpty(request.getConsumer().getUserId())) {
			paraMap.put(RepositoryConstant.COL_USER_ID.getValue(), request.getConsumer().getUserId());
		}
		MessageHeadService service = request.getServices().get(0);
		if (!StringUtils.isEmpty(service.getId()) && !StringUtils.isEmpty(service.getApplicationId())) {
			paraMap.put(RepositoryConstant.COL_TO_SERVICE.getValue(),
					service.getApplicationId() + "/" + service.getId());
		}
		paraMap.put(RepositoryConstant.COL_MESSAGE_CONTENT.getValue(), request.getJsonString());
		return paraMap;
	}

	private Map<String, String> buildParaMap(MessageResponse resp) {
		Map<String, String> paraMap = new HashMap<>();
		if (!StringUtils.isEmpty(resp.getBase().getId())) {
			paraMap.put(RepositoryConstant.COL_MESSAGE_ID.getValue(), resp.getBase().getId());
		}
		if (!StringUtils.isEmpty(resp.getFirstBodyService().getId())) {
			paraMap.put(RepositoryConstant.COL_SERVICE_ID.getValue(), resp.getFirstBodyService().getId());
		}
		if (!StringUtils.isEmpty(resp.getFirstBodyService().getAppliactionId())) {
			paraMap.put(RepositoryConstant.COL_APP_ID.getValue(), resp.getFirstBodyService().getAppliactionId());
		}
		if (!StringUtils.isEmpty(resp.getBase().getCorrelationId())) {
			paraMap.put(RepositoryConstant.COL_CORRELATION_ID.getValue(), resp.getBase().getCorrelationId());
		}
		if (!StringUtils.isEmpty(resp.getFirstBodyService().getUserId())) {
			paraMap.put(RepositoryConstant.COL_USER_ID.getValue(), resp.getFirstBodyService().getUserId());
		}
		return paraMap;
	}

}
