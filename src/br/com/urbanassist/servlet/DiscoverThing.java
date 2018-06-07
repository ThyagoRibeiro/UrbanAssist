package br.com.urbanassist.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.google.gson.Gson;

import br.com.urbanassist.util.ClassificationAlgorithm;
import br.com.urbanassist.util.ServletManager;
import br.com.urbanassist.dao.ThingDAO;
import br.com.urbanassist.model.Thing;
import br.com.urbanassist.model.WifiData;

/**
 * Servlet implementation class DiscoverThing
 */
@WebServlet("/discoverThing")
public class DiscoverThing extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public DiscoverThing() {
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
		String method = jsonObject.getString("method");

		Thing thing = null;

		switch (method) {
		case "qrCode":

			// converte conteudo para id
			int id = jsonObject.getInt("idThing");

			thing = ThingDAO.select(id);
			// devolve object

			break;

		case "wifiData":

			jsonObject = jsonObject.getJSONObject("data");
			ClassificationAlgorithm algorithm = ClassificationAlgorithm.valueOf(jsonObject.getString("algorithm"));
			WifiData wifiData = new Gson().fromJson(jsonObject.get("wifiData").toString(), WifiData.class);

			thing = ThingDAO.discoverThing(wifiData, algorithm);
			break;
		}

		ServletManager.writeObject(thing, response);
	}

}
