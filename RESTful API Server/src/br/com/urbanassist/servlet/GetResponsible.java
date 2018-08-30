package br.com.urbanassist.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import br.com.urbanassist.dao.ResponsibleDAO;
import br.com.urbanassist.model.Responsible;
import br.com.urbanassist.util.ServletManager;

/**
 * Servlet implementation class GetResponsible
 */
@WebServlet("/GetResponsible")
public class GetResponsible extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public GetResponsible() {
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

		JSONObject jsonResponsible = ServletManager.readJSON(request).getJSONObject("responsible");
		
		Responsible responsible = ResponsibleDAO.select(Integer.parseInt(jsonResponsible.getString("idResponsible")));
		ServletManager.writeObject(responsible, response);
	}

}
