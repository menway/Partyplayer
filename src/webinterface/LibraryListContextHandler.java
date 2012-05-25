package webinterface;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;

import player.Player;

import songstreams.SongStream;

import library.Library;
import library.exceptions.InvalidQueryException;

import net.freeutils.httpserver.HTTPServer.ContextHandler;
import net.freeutils.httpserver.HTTPServer.Request;
import net.freeutils.httpserver.HTTPServer.Response;

public class LibraryListContextHandler implements ContextHandler {
	
	private Library lib;
	private Player player;
	public LibraryListContextHandler(Library lib, Player player) {
		this.lib = lib;
		this.player = player;
	}
	@Override
	public int serve(Request req, Response resp) throws IOException {
		Map<String, String> params = req.getParams();
		if(params.containsKey("url")) {
			try {
				player.queue(lib.getSong(params.get("url")));
			} catch (InvalidQueryException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(params.containsKey("control")) {
			String playercontrol = params.get("control");
			if(playercontrol.equals("play"))
				player.play();
			else if(playercontrol.equals("pause"))
				player.pause();
			else if(playercontrol.equals("skip"))
				player.skip();
			else if(playercontrol.equals("back"))
				player.back();
		}
		StringBuilder body = new StringBuilder();
		List<SongStream> songs;
		try {
			songs = lib.getAllSongs();
			String javascriptFunctions = "<script type=\"text/javascript\">\n" +
					"	function ChangeColor(tableRow, highlight) {\n" +
					"		if(highlight) {\n" +
					"			tableRow.style.backgroundColor = '#dcfac9';\n" +
					"		}\n" +
					"		else {\n" +
					"			tableRow.style.backgroundColor = 'white';\n" +
					"		}\n" +
					"	}\n" +
					"\n" +
					"	function Queue(url) {\n" +
					"		form = document.forms['queue'];\n" +
					"		urlElement = form.elements['url'];\n" +
					"		urlElement.value = url;\n" +
					"		form.submit();\n" +
					"	}\n" +
					"	function PlayerControl(control) {\n" +
					"		form = document.forms['playerControl'];\n" +
					"		controlElement = form.elements['control'];\n" +
					"		controlElement.value = control;\n" +
					"		form.submit();\n" +
					"	}\n" +
					"</script>";	
			StringBuilder header = new StringBuilder("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\"\n");
				      header.append("\"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">");
				      header.append("<html>\n	<head>\n		<meta http-equiv=\"Content-Type\"\n");
				      header.append("				content=\"text/html; charset=UTF-8\" />\n");
				      header.append("		<title>PartyPlayer</title>\n");
				      header.append("	<LINK REL=StyleSheet HREF=\"/stylesheets/style.css\" TYPE=\"text/css\">");
				      header.append(javascriptFunctions);
				      header.append("</head>\n");
			body.append("<body>\n\n");
			body.append("<div id=\"toolbar\"><p>" + getToolbar() + "</p></div>");
			body.append("<form name=\"queue\" action=\"/\" method=\"POST\">\n");
			body.append("<input type=\"hidden\" name=\"url\" value=\"\">\n");
			body.append("</form>\n");
			body.append("<table>\n");
			for(SongStream song : songs) {
				body.append(song.getTableRow(true)).append("\n");
			}
			body.append("</table>\n\n</body>\n</html>");
			header.append(body);
			byte[] bytes = header.toString().getBytes("UTF-8");
			resp.sendHeaders(200, bytes.length, System.currentTimeMillis(), null, "text/html", null);
			resp.sendBody(new ByteArrayInputStream(bytes), bytes.length, null);
			return 200;
		} catch (Exception e) {
			return 404;
		}
	}
	public String getToolbar() {
		StringBuilder builder = new StringBuilder();
		builder.append("<form name=\"playerControl\" action=\"/\" method=\"POST\"><input type=\"hidden\" name=\"control\" value=\"\"></form><table><tr>");
		if(player.isPlaying()) {
			builder.append("<td><a href=\"#no-op\" onclick=\"PlayerControl('pause'); return false;\">Pause</a></td>");
			builder.append("<td><a href=\"#no-op\" onclick=\"PlayerControl('stop'); return false;\">Stop</a></td>");
			builder.append("<td><a href=\"#no-op\" onclick=\"PlayerControl('back'); return false;\">Back</a></td>");
			builder.append("<td><a href=\"#no-op\" onclick=\"PlayerControl('skip'); return false;\">Skip</a></td>");
			if(player.getNowPlaying() != null) {
				builder.append("</table>\n");
				builder.append("Now playing:\n");
				builder.append("<table>" + player.getNowPlaying().getTableRow(false));
			}
		}
		else
			builder.append("<td><a href=\"#no-op\" onclick=\"PlayerControl('play'); return false;\">Play</a></td></tr>");
		builder.append("</table>");
		return builder.toString();
		

	}

}
