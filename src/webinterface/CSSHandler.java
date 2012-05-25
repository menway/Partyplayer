package webinterface;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import net.freeutils.httpserver.HTTPServer;
import net.freeutils.httpserver.HTTPServer.ContextHandler;
import net.freeutils.httpserver.HTTPServer.Headers;
import net.freeutils.httpserver.HTTPServer.Request;
import net.freeutils.httpserver.HTTPServer.Response;

public class CSSHandler implements ContextHandler {

	private File cssFile;
	public CSSHandler(File cssFile) {
		this.cssFile = cssFile;
	}
	@Override
	public int serve(Request req, Response resp) throws IOException {
		if(!cssFile.exists() || cssFile.isHidden())
			return 404;
		resp.sendHeaders(200, cssFile.length(), cssFile.lastModified(), null, "text/css", null);
		resp.sendBody(new FileInputStream(cssFile), cssFile.length(), null);
		return 0;
	}

}
