package ru.otus.l161.messages;

import java.util.Map;

public class MsgCacheParametersResponse {

	private final Map<String, String> cacheParameters;

	public MsgCacheParametersResponse(Map<String, String> cacheParameters) {
		this.cacheParameters = cacheParameters;
	}

	public Map<String, String> getCacheParametes() {
		return cacheParameters;
	}
}
