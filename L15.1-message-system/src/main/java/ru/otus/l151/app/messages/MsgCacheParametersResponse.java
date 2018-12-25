package ru.otus.l151.app.messages;

import java.util.Map;

import ru.otus.l151.app.MsgToUI;
import ru.otus.l151.messagesystem.Address;
import ru.otus.l151.messagesystem.MessageContext;
import ru.otus.l151.uiservice.UIService;

public class MsgCacheParametersResponse extends MsgToUI {

	private final Map<String, String> cacheParameters;

	public MsgCacheParametersResponse(Address from, Address to, MessageContext context,
			Map<String, String> cacheParameters) {
		super(from, to, context);
		this.cacheParameters = cacheParameters;
	}

	@Override
	public void exec(UIService uiService) {
		uiService.handleCacheResponse(getContext(), cacheParameters);
	}

}
