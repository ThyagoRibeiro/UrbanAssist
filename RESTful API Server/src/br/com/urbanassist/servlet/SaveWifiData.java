package br.com.urbanassist.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.google.gson.Gson;

import br.com.urbanassist.dao.WifiDataDAO;
import br.com.urbanassist.model.WifiData;
import br.com.urbanassist.util.ServletManager;

/**
 * Servlet implementation class SaveWifiData
 */
@WebServlet("/saveWifiData")
public class SaveWifiData extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public SaveWifiData() {
    }

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		JSONObject jsonObject = ServletManager.readJSON(request);
		
		WifiData wifiData = new Gson().fromJson(jsonObject.get("wifiData").toString(), WifiData.class);
		WifiDataDAO.insert(wifiData);
		
		ServletManager.successResponse(response);
	}

}
