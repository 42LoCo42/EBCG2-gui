package ebcg2gui.tools;

import java.io.File;

import org.json.JSONArray;
import org.json.JSONObject;

public class ConfigLoader {
	public static final String configFileName = "ebcg2gui.conf";
	public static File configFile;
	
	private static JSONObject data;
	
	public static void load() {
		if(configFile == null) {
			configFile = new File(configFileName);
		}
		
		if(!configFile.exists()) {
			try {
				configFile.getAbsoluteFile().getParentFile().mkdirs();
				configFile.createNewFile();
				FileIO.writeString(configFile, "{servers: []}");
			}
			catch(Exception ex) {ex.printStackTrace();}
		}
		
		String raw = FileIO.readString(configFile);
		data = new JSONObject(raw);
	}
	
	public static void write() {
		FileIO.writeString(configFile, data.toString(4));
	}
	
	public static ServerInfo[] getServers() {
		JSONArray serverList = data.getJSONArray("servers");
		ServerInfo[] servers = new ServerInfo[serverList.length()];
		
		// parse servers
		for(int i=0; i<serverList.length(); i++) {
			JSONObject serverInfo = serverList.getJSONObject(i);
			servers[i] = new ServerInfo(serverInfo.getString("hostname"), serverInfo.getInt("port"));
		}
		
		return servers;
	}
	
	public static void addServer(ServerInfo server) {
		JSONObject serverJSON = new JSONObject();
		serverJSON.put("hostname", server.hostname);
		serverJSON.put("port", server.port);
		
		data.getJSONArray("servers").put(serverJSON);
		write();
	}
	
	public static void removeServer(int index) {
		data.getJSONArray("servers").remove(index);
		write();
	}
}
