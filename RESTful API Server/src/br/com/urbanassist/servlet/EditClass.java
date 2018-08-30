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
import br.com.urbanassist.model.Attribute;
import br.com.urbanassist.util.ServletManager;

/**
 * Servlet implementation class EditClass
 */
@WebServlet("/EditClass")
public class EditClass extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public EditClass() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		JSONObject jsonObject = ServletManager.readJSON(request).getJSONObject("class");
		
		JSONObject jsonName = jsonObject.getJSONObject("name");
		AttrClass attrClass = new AttrClass();
		attrClass.setIdClass(Integer.parseInt(jsonObject.getString("idClass")));
		attrClass.setName(new Attribute(jsonName.getString("text"), jsonName.getString("audio"), jsonName.getString("video")));
		
		AttrClassDAO.update(attrClass);
		ServletManager.successResponse(response);
	}

}
