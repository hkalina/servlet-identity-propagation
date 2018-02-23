package jk.experiments.servlet.propagation.webfrontend;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Authenticator;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.security.PrivilegedActionException;

import javax.servlet.ServletException;
import javax.servlet.annotation.HttpConstraint;
import javax.servlet.annotation.ServletSecurity;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.wildfly.security.auth.client.AuthenticationConfiguration;
import org.wildfly.security.auth.client.AuthenticationContext;
import org.wildfly.security.auth.client.MatchRule;
import org.wildfly.security.auth.util.ElytronAuthenticator;
import org.wildfly.url.http.WildflyURLStreamHandlerFactory;

@WebServlet("/callingServlet")
@ServletSecurity(
        @HttpConstraint(rolesAllowed = {"Admin", "Adder", "Remover"})
)
public class CallingServlet extends HttpServlet {

    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.getOutputStream().println("Hello " + req.getRemoteUser() + "!");

        resp.getOutputStream().println("adder:");
        try {
            AuthenticationContext.empty().with(MatchRule.ALL,
                    AuthenticationConfiguration.empty().useName("adder@wildfly.org").usePassword("adder")
            ).runExceptionAction(() -> {
                URL url = new URL("http://localhost:8080/servlet-propagation-1.0-SNAPSHOT/securedServlet");
                URLConnection conn = url.openConnection();
                conn.connect();
                try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
                    resp.getOutputStream().println(br.readLine());
                    resp.getOutputStream().println(br.readLine());
                }
                return null;
            });
        } catch (PrivilegedActionException e) {
            throw new IOException(e);
        }

        resp.getOutputStream().println("remover:");

        try {
            AuthenticationContext.empty().with(MatchRule.ALL,
                    AuthenticationConfiguration.empty().useName("remover@wildfly.org").usePassword("remover")
            ).runExceptionAction(() -> {
                URL url = new URL("http://localhost:8080/servlet-propagation-1.0-SNAPSHOT/securedServlet");
                URLConnection conn = url.openConnection();
                conn.connect();
                try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
                    resp.getOutputStream().println(br.readLine());
                    resp.getOutputStream().println(br.readLine());
                }
                return null;
            });
        } catch (PrivilegedActionException e) {
            throw new IOException(e);
        }
    }

}
