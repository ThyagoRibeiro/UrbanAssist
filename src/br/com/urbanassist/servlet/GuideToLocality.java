package br.com.urbanassist.servlet;

import java.io.IOException;
import java.util.LinkedList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.google.gson.Gson;

import br.com.urbanassist.dao.DirectionDAO;
import br.com.urbanassist.dao.LocalityDAO;
import br.com.urbanassist.dao.ThingDAO;
import br.com.urbanassist.dao.UserDAO;
import br.com.urbanassist.model.Edge;
import br.com.urbanassist.model.Locality;
import br.com.urbanassist.model.User;
import br.com.urbanassist.util.ServletManager;

@WebServlet("/guideToLocality")
public class GuideToLocality extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public GuideToLocality() {
		super();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		JSONObject jsonObject = ServletManager.readJSON(request);
		Locality origin = new Gson().fromJson(jsonObject.get("origin").toString(), Locality.class);
		Locality destination = new Gson().fromJson(jsonObject.get("destination").toString(), Locality.class);
		User user = new Gson().fromJson(jsonObject.get("user").toString(), User.class);

		LinkedList<Edge> doorEdges = LocalityDAO.traceRoute(origin, destination, UserDAO.getRatedLocalities(user));

		LinkedList<Edge> objectEdges = new LinkedList<>();
		for (Edge doorEdge : doorEdges) {
			objectEdges.addAll(ThingDAO.traceRoute(doorEdge.getOrigin(), doorEdge.getDestination(),
					UserDAO.getRatedThings(user)));
		}

		Edge cEdge = objectEdges.removeFirst();
		DirectionDAO.insert(user, objectEdges);
		
		ServletManager.writeObject(cEdge, response);
	}

}
