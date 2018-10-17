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
import com.cs.commons.jdbc.DSManager;
import com.cs.commons.jdbc.IJdbcExec;
import com.cs.commons.jdbc.JdbcStatement;
import com.cs.log.common.logbean.LogException;
import com.cs.log.logs.LogInfoMgr;
import com.cs.log.logs.bean.Logger;

/**
 * @author Donald.Wang
 *
 */
public class MessageRepositoryDao {

	private Logger logger = LogManager.getSQLLogger();

	public void storeMessage(MessageRequest request, MessageResponse response) {
		try {
			String unitCode = request.getTransaction().getUnitCode();
			IJdbcExec exec = DSManager.getJdbcExec(unitCode);
			String sql;
			if (response != null) {
				sql = buildSQL(buildParaMap(request, response));
			} else {
				sql = buildSQL(buildParaMap(request));
			}
			JdbcStatement stat = new JdbcStatement(sql);
			exec.update(LogManager.getSQLLog(request), stat);
		} catch (LogException e) {
			BaseAppException ex = new BaseAppException(e, LogInfoMgr.getErrorInfo("ERR_0038", request.getJsonString()));
			logger.write(LogManager.getServiceLogKey(request), ex);
		}
	}

	private String getTableName(String unitCode) {
		return DSManager.getSchema(DSManager.getDs(unitCode)) + "." + RepositoryConstant.TB_BASE_APP_MSG.getValue();
	}

	public Map<String, Integer> getColumns(String unitCode) throws LogException {
		IJdbcExec exec = DSManager.getDefaultJdbcExec();
		return exec.getColumns(LogManager.getSQLLog(), DSManager.getSchema(DSManager.getDs(unitCode)),
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

	private Map<String, String> buildParaMap(MessageRequest request, MessageResponse response) {
		Map<String, String> paraMap = new HashMap<>();
		if (!StringUtils.isEmpty(response.getBase().getId())) {
			paraMap.put(RepositoryConstant.COL_MESSAGE_ID.getValue(), response.getBase().getId());
		}
		if (!StringUtils.isEmpty(response.getFirstBodyService().getId())) {
			paraMap.put(RepositoryConstant.COL_SERVICE_ID.getValue(), response.getFirstBodyService().getId());
		}
		if (!StringUtils.isEmpty(response.getFirstBodyService().getAppliactionId())) {
			paraMap.put(RepositoryConstant.COL_APP_ID.getValue(), response.getFirstBodyService().getAppliactionId());
		}
		if (!StringUtils.isEmpty(response.getBase().getCorrelationId())) {
			paraMap.put(RepositoryConstant.COL_CORRELATION_ID.getValue(), response.getBase().getCorrelationId());
		}
		if (!StringUtils.isEmpty(response.getFirstBodyService().getUserId())) {
			paraMap.put(RepositoryConstant.COL_USER_ID.getValue(), response.getFirstBodyService().getUserId());
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
		if (!StringUtils.isEmpty(response.getFirstBodyService().getAppliactionId())
				&& !StringUtils.isEmpty(response.getFirstBodyService().getId())) {
			paraMap.put(RepositoryConstant.COL_FROM_SERVICE.getValue(),
					response.getFirstBodyService().getAppliactionId() + "/" + response.getFirstBodyService().getId());
		}
		if (!StringUtils.isEmpty(request.getConsumer().getClientId())
				&& !StringUtils.isEmpty(request.getConsumer().getId())) {
			paraMap.put(RepositoryConstant.COL_TO_SERVICE.getValue(),
					request.getConsumer().getClientId() + "/" + request.getConsumer().getId());
		}
		paraMap.put(RepositoryConstant.COL_MESSAGE_CONTENT.getValue(), response.getJsonString());
		return paraMap;
	}

}
