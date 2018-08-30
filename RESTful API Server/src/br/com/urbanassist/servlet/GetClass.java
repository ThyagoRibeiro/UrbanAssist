package br.com.urbanassist.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import br.com.urbanassist.dao.AttrClassDAO;
import br.com.urbanassist.model.AttrClass;
import br.com.urbanassist.util.ServletManager;

/**
 * Servlet implementation class GetClass
 */
@WebServlet("/GetClass")
public class GetClass extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetClass() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		JSONObject jsonClass = ServletManager.readJSON(request).getJSONObject("class");
		
		AttrClass attrClass = AttrClassDAO.select(Integer.parseInt(jsonClass.getString("idClass")));
		ServletManager.writeObject(attrClass, response);
	}

}
