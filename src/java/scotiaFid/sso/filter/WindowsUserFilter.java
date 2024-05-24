/**
 *  Created on  : 19 Mayo de 2010
 *  Author      : Angel Gutiérrez Servin
 *  Company     : CODES Consultoria y Desarrollo de Sistemas SA. de C.V.
 *  Place       : México D.F.
 *  Copyright  : Scotiabank México, All Rights Reserved.
 *  Description : This asks for the Windows user
 */
package scotiaFid.sso.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.codec.binary.Base64;

import com.bns.soa.client.utils.SecurityConstants;

/**
 * This asks for the Windows user through the required headers sent to the
 * browser. You also need to confirm that Integrated Windows Authentication is
 * enabled and the user and machine are within the domain
 *
 * @author Angel Gutierrez
 *
 */
public class WindowsUserFilter implements Filter {

	private static final String NTLM = "NTLM ";
        private static final String AUTHORIZATION = "Authorization";
        private static final String WWWAUTENTICATE = "WWW-Authenticate";
	@SuppressWarnings("unused")
	private FilterConfig filterConfig = null;
        
        private static String username;
        private static String winuser;
        

        @Override
	public void destroy() {
		this.filterConfig = null;
	}

        @Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {

		if (request instanceof HttpServletRequest) {
			HttpServletRequest rq = (HttpServletRequest) request;
			HttpServletResponse rs = (HttpServletResponse) response;

			HttpSession session = rq.getSession(false);

			String auth = rq.getHeader(AUTHORIZATION);
			if (auth == null) {
				rs.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				rs.setHeader(WWWAUTENTICATE, NTLM);
				return;
			}
			if (auth.startsWith(NTLM)) {
				byte[] msg = Base64.decodeBase64(auth.substring(5));

				int off = 0, length, offset;
				String s;
				if (msg[8] == 1) {
					//off = 18;

					byte z = 0;
					byte[] msg1 = { (byte) 'N', (byte) 'T', (byte) 'L',
							(byte) 'M', (byte) 'S', (byte) 'S', (byte) 'P', z,
							(byte) 2, z, z, z, z, z, z, z, (byte) 40, z, z, z,
							(byte) 1, (byte) 130, z, z, z, (byte) 2, (byte) 2,
							(byte) 2, z, z, z, z, //
							z, z, z, z, z, z, z, z };
					//
					rs.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
					rs.setHeader(WWWAUTENTICATE, NTLM.concat(Base64.encodeBase64String(msg1).trim()));
					return;
				} else if (msg[8] == 3) {
					off = 30;
					//length = msg[off + 17] * 256 + msg[off + 16]; CAVC
					//offset = msg[off + 19] * 256 + msg[off + 18]; CAVC
					//s = new String(msg, offset, length);
				} else {
					return;
				}

				length = msg[off + 1] * 256 + msg[off];
				offset = msg[off + 3] * 256 + msg[off + 2];
				String domain = new String(msg, offset, length);
				// out.println(s + " ");
				length = msg[off + 9] * 256 + msg[off + 8];
				offset = msg[off + 11] * 256 + msg[off + 10];
				s = new String(msg, offset, length);

				StringBuilder sbLogonUser = new StringBuilder();
				for (int i = 0; i < s.length(); i++) {
					char x = s.charAt(i);
					int k = x;
					if (k > 0) {
						sbLogonUser.append(x);
					}
				}

				StringBuilder sbDomain = new StringBuilder();
				for(int i = 0; i < domain.length(); i++) {
					char x = domain.charAt(i);
					int k = x;
					if(k > 0) {
						sbDomain.append(x);
					}
				}

				if (null == session) {
					session = rq.getSession(true);
				}
				username = sbLogonUser.toString();
				domain = sbDomain.toString();
                                winuser = domain.concat(SecurityConstants.DOMAIN_DELIMITER).concat(username);
                                
                                if(session.getAttribute(SecurityConstants.WIN_USER) == null){
                                    session.setAttribute(SecurityConstants.WIN_USER, winuser);
                                }
                                
				chain.doFilter(request, response);
			} else {
				rs.setStatus(HttpServletResponse.SC_FORBIDDEN);
			}
		} else {
			chain.doFilter(request, response);
		}
            }

	public void init(FilterConfig filterConfig) throws ServletException {
		this.filterConfig = filterConfig;
	}

}