package com.cs.baseapp.api.messagebroker.event;

import com.cs.baseapp.errorhandling.BaseAppException;

public interface EventHandler {

	public void onEvent(EventMessage eventMsg) throws BaseAppException;

}
