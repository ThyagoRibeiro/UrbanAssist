package br.com.urbanassist.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.google.gson.Gson;

import br.com.urbanassist.dao.ThingDAO;
import br.com.urbanassist.model.Locality;
import br.com.urbanassist.model.WifiData;
import br.com.urbanassist.util.ServletManager;
import br.com.urbanassist.util.WifiPositioningSystem;

@WebServlet("/DiscoverLocality")
public class DiscoverLocality extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public DiscoverLocality() {
		super();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		JSONObject jsonObject = ServletManager.readJSON(request);
		String method = jsonObject.getString("method");

		Locality locality = null;

		switch (method) {

		case "qrCode":

			int id = jsonObject.getInt("data");
			locality = ThingDAO.select(id).getLocality();
			break;

		case "wifiData":

			WifiData wifiData = new Gson().fromJson(jsonObject.get("data").toString(), WifiData.class);
			locality = WifiPositioningSystem.discoverThing(wifiData).getLocality();
			break;
		}

		ServletManager.writeObject(locality, response);
	}

}
