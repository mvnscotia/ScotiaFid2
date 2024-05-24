package scotiaFid.sso.filter;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.bns.soa.client.utils.SecurityConstants;
import com.bns.xsd.types.security.ErrorType;
import com.bns.xsd.types.security.RolType;

public class SessionFilter implements Filter {

	private FilterConfig filterConfig = null;
	private String logon_User;
	

	public void init(FilterConfig fc) throws ServletException {
		this.filterConfig = fc;
		logon_User = filterConfig.getInitParameter(SecurityConstants.SSOWinUser);
		
		/*if (null == appCode || urlRedirect == null || null == urlRoles) {
			throw new ServletException(
					"Faltan parametros en la configuración del filtro");
		}

		userTest = filterConfig.getInitParameter("SSOUserTest");
		userDomain = filterConfig.getInitParameter("SSOUserDomain");*/

	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {

		if (request instanceof HttpServletRequest) {

			
			HttpServletRequest rq = (HttpServletRequest) request;
			@SuppressWarnings("unused")
			HttpServletResponse rs = (HttpServletResponse) response;
			HttpSession session = rq.getSession(false);

			if (session ==null) {
			//	System.out.println("Entro aqui");
				rs.sendRedirect(rq.getContextPath() + "/index.jsp");
				return;
			}else{ 
				if (null == session.getAttribute(SecurityConstants.SSOIDSesion)) {
					rs.sendRedirect(rq.getContextPath() + "/index.jsp");
					return;
				}
			}
		}
		chain.doFilter(request, response);
	}

	public void destroy() {
		this.filterConfig = null;
	}

}
