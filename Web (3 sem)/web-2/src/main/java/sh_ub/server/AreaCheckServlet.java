package sh_ub.server;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "AreaCheckServlet", urlPatterns = {"/area-check"})
public class AreaCheckServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        processRequest(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        processRequest(request, response);
    }
    
    private void processRequest(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        try {
            double x = Double.parseDouble(request.getParameter("x"));
            double y = Double.parseDouble(request.getParameter("y"));
            double r = Double.parseDouble(request.getParameter("r"));
            
            Coordinates point = new Coordinates(x, y, r);
            
            if (!point.isValidCoordinates()) {
                request.setAttribute("error", "Некорректные координаты или радиус");
                request.getRequestDispatcher("/WEB-INF/views/result.jsp").forward(request, response);
                return;
            }
            
            HttpSession session = request.getSession();
            @SuppressWarnings("unchecked")
            List<Coordinates> results = (List<Coordinates>) session.getAttribute("results");
            if (results == null) {
                results = new ArrayList<>();
            }

            results.add(point);
            session.setAttribute("results", results);
            
            request.setAttribute("result", point);
            request.setAttribute("results", results);
            
            request.getRequestDispatcher("/WEB-INF/views/result.jsp").forward(request, response);
            
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Некорректный формат чисел");
            request.getRequestDispatcher("/WEB-INF/views/result.jsp").forward(request, response);
        } catch (Exception e) {
            request.setAttribute("error", "Ошибка сервера: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/result.jsp").forward(request, response);
        }
    }
}

