package webinterface;

import net.freeutils.httpserver.HTTPServer;

public class WebInterfaceHandler {
	HTTPServer httpServer;
	public WebInterfaceHandler() {
		httpServer = new HTTPServer(8080);
		// TODO
	}
}
