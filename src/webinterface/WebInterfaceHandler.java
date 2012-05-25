package webinterface;

import java.io.File;
import java.io.IOException;

import player.Player;

import library.Library;
import net.freeutils.httpserver.HTTPServer;
import net.freeutils.httpserver.HTTPServer.VirtualHost;

public class WebInterfaceHandler {
	HTTPServer httpServer;
	private Library lib;
	
	public WebInterfaceHandler(Library lib) {
		this.lib = lib;
		httpServer = new HTTPServer(8080);
		VirtualHost host = httpServer.getVirtualHost(null);
		host.addContext("/", new LibraryListContextHandler(lib, new Player()));
		
		host.addContext("/stylesheets/", new CSSHandler(new File(getClass().getResource("/resources/webinterface/style.css").getFile())));
		
	}
	public void startServer() throws IOException {
		httpServer.start();
	}
	
	public static void main(String[] args) {
		Library lib;
		try {
			lib = new Library(new File(System.getProperty("user.dir") + "/music"));
			WebInterfaceHandler wih = new WebInterfaceHandler(lib);
			wih.startServer();
			while(true)
				Thread.sleep(100);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
