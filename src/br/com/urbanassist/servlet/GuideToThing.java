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

import br.com.urbanassist.dao.ThingDAO;
import br.com.urbanassist.dao.UserDAO;
import br.com.urbanassist.model.Edge;
import br.com.urbanassist.model.Locality;
import br.com.urbanassist.model.Thing;
import br.com.urbanassist.model.User;
import br.com.urbanassist.util.ServletManager;

/**
 * Servlet implementation class GuideToThing
 */
@WebServlet("/guideToThing")
public class GuideToThing extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public GuideToThing() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		JSONObject jsonObject = ServletManager.readJSON(request);
		Thing origin = new Gson().fromJson(jsonObject.get("origin").toString(), Thing.class);
		Thing destination = new Gson().fromJson(jsonObject.get("destination").toString(), Thing.class);
		User user = new Gson().fromJson(jsonObject.get("user").toString(), User.class);

		LinkedList<Edge> objectEdges = ThingDAO.traceRoute(origin, destination, UserDAO.getRatedThings(user));
		// fazer algo com objectEdges

	}

}
