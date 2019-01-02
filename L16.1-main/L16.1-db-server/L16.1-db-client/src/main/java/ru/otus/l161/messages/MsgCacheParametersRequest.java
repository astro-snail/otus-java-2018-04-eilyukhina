package ru.otus.l161.messages;

import ru.otus.l161.handler.RequestHandler;

public class MsgCacheParametersRequest extends Request {

	public MsgCacheParametersRequest() {

	}

	@Override
	public Response execute(RequestHandler handler) {
		return new MsgCacheParametersResponse(handler.getCacheParameters());
	}

	@Override
	public Class<? extends Response> getResponseType() {
		return MsgCacheParametersResponse.class;
	}
}
