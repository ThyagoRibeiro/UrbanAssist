package br.com.urbanassist.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import br.com.urbanassist.dao.ThingDAO;
import br.com.urbanassist.util.ServletManager;

/**
 * Servlet implementation class AddNeighbour
 */
@WebServlet("/AddNeighbour")
public class AddNeighbour extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public AddNeighbour() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		JSONObject jsonObject = ServletManager.readJSON(request).getJSONObject("neighbour");
		
		System.out.println(jsonObject.toString());
		
		int idOrigin = jsonObject.getInt("idOrigin");
		int idDestination = ThingDAO.selectByKeyword(jsonObject.getString("destination")).get(0).getID();
		double distance = Double.parseDouble(jsonObject.getString("distance"));
		int degree = Integer.parseInt(jsonObject.getString("degree"));

		ThingDAO.addNeighbour(idOrigin, idDestination, distance, degree);
		ServletManager.successResponse(response);
	}

}
