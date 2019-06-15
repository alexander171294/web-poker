package ar.com.tandilweb.ApiServer.filters.jwt;


import java.io.IOException;
import java.util.Date;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JWTValidFilter implements Filter {

	private static final Logger log = LoggerFactory.getLogger(JWTValidFilter.class);

	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub
		
	}

	class ExceptionInvalidJWT extends Exception {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

	}

	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
			throws IOException, ServletException {
		// TODO Auto-generated method stub
		log.debug("Filter Validate JWT");
		HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
		HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;
		if((Boolean) httpRequest.getAttribute("jwtTrusted")) {
			try {
				JWTUnpackedData uD = (JWTUnpackedData)httpRequest.getAttribute("jwtParsed");
				Date now = new Date();
				if(uD.getIssuedAt() == null || uD.getIssuedAt().after(now)) throw new ExceptionInvalidJWT();
				if(uD.getExpiration() == null || uD.getExpiration().before(now)) throw new ExceptionInvalidJWT();
				if(uD.getNotBefore() == null || uD.getNotBefore().after(now)) throw new ExceptionInvalidJWT();
				// chequear no repetición de hoy
				filterChain.doFilter(httpRequest, httpResponse);
			}catch(ExceptionInvalidJWT e){
				log.error("JWT Validation Exception", e);
				httpResponse.setStatus(400); // Bad request
			}
		} else {
			filterChain.doFilter(httpRequest, httpResponse);
		}
	}

	public void destroy() {
		// TODO Auto-generated method stub
		
	}


}
