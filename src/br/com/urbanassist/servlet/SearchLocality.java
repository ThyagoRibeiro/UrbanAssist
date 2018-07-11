package br.com.urbanassist.servlet;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import br.com.urbanassist.dao.LocalityDAO;
import br.com.urbanassist.model.Locality;
import br.com.urbanassist.util.ServletManager;

@WebServlet("/searchLocality")
public class SearchLocality extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public SearchLocality() {
		super();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		JSONObject jsonObject = ServletManager.readJSON(request);
		String keyword = jsonObject.getString("keyword");
		
		ArrayList<Locality> localityList = LocalityDAO.selectByKeyword(keyword);
	
		ServletManager.writeJSON(localityList, response);

	}

}
