package jk.experiments.servlet.propagation.webfrontend;

import javax.servlet.ServletException;
import javax.servlet.annotation.HttpConstraint;
import javax.servlet.annotation.ServletSecurity;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/securedServlet")
@ServletSecurity(
        @HttpConstraint(rolesAllowed = {"Admin", "Adder", "Remover"})
)
public class SecuredServlet extends HttpServlet {

    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.getOutputStream().println("Hello " + req.getRemoteUser() + "!");

    }

}
