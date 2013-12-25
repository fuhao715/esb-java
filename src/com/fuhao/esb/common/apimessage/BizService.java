package com.fuhao.esb.common.apimessage;

import java.util.Map;



public class BizService extends Service {
	private static final long serialVersionUID = 2759662466047955907L;

	public BizService(Service service)  {
		
		this.body = service.getBody();
		this.head = service.getHead();
	}

	private Map<String, Object> bizObject;

	public Map<String, Object> getBizObject() {
		return bizObject;
	}

	public void setBizObject(Map<String, Object> bizObject) {
		this.bizObject = bizObject;
	}
}
