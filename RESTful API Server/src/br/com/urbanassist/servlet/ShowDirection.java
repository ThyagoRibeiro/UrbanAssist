package br.com.urbanassist.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.google.gson.Gson;

import br.com.urbanassist.dao.DirectionDAO;
import br.com.urbanassist.dao.ThingDAO;
import br.com.urbanassist.model.Edge;
import br.com.urbanassist.model.Thing;
import br.com.urbanassist.model.User;
import br.com.urbanassist.model.WifiData;
import br.com.urbanassist.util.ServletManager;
import br.com.urbanassist.util.WifiPositioningSystem;

@WebServlet("/showDirection")
public class ShowDirection extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public ShowDirection() {
		super();
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		JSONObject jsonObject = ServletManager.readJSON(request);
		User user = new Gson().fromJson(jsonObject.get("user").toString(), User.class);
		String method = jsonObject.getString("method");

		Thing thingOrigin = null;

		switch (method) {
		case "qrCode":

			int id = jsonObject.getInt("data");
			thingOrigin = ThingDAO.select(id);
			break;

		case "wifiData":

			WifiData wifiData = new Gson().fromJson(jsonObject.get("data").toString(), WifiData.class);
			thingOrigin = WifiPositioningSystem.discoverThing(wifiData);
			break;
		}

		Edge cEdge = DirectionDAO.removeEdge(user, thingOrigin);
		
		if (cEdge != null) {
			
			if (cEdge.getOrigin().getID() == cEdge.getDestination().getID())
				ServletManager.successResponse(response);
			else
				ServletManager.writeObject(cEdge, response);
		} else {
			DirectionDAO.removeRoute(user);
			ServletManager.failureResponse("Left the route", response);
		}
	}
}
