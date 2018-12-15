package ru.otus.l151.app.messages;

import java.util.Map;

import javax.servlet.AsyncContext;

import ru.otus.l151.app.MsgToUI;
import ru.otus.l151.messagesystem.Address;
import ru.otus.l151.uiservice.UIService;

public class MsgCacheParametersResponse extends MsgToUI {
	private final Map<String, String> cacheParameters;
	private final AsyncContext asyncContext;
	
	public MsgCacheParametersResponse(Address from, Address to, AsyncContext asyncContext, Map<String, String> cacheParameters) {
		super(from, to);
		this.cacheParameters = cacheParameters;
		this.asyncContext = asyncContext;
	}

	@Override
	public void exec(UIService uiService) {
		uiService.handleCacheResponse(asyncContext, cacheParameters);
	}

}
