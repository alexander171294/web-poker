package ar.com.tandilweb.ApiServer.filters.jwt;

import java.io.IOException;

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

import io.jsonwebtoken.Claims;

public class JWTFilter implements Filter {

	private static final Logger log = LoggerFactory.getLogger(JWTFilter.class);
	
//	public UsersRepository repository;

	public void destroy() {

	}

	class ExceptionJWT extends Exception {
		
		private static final long serialVersionUID = 1L;

	}

	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
			throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
		HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;
		String jwToken = httpRequest.getHeader("Authorization");
		if(httpRequest.getMethod().equalsIgnoreCase("OPTIONS")) {
			httpResponse.setStatus(200); // forbidden
		} else if(jwToken != null) {
			String identity = httpRequest.getHeader("Identity");
			try {
				if(identity == null) throw new ExceptionJWT();
//				Users user = repository.findById(Long.parseLong(identity));
				Object user = null;
				if(user == null) throw new ExceptionJWT();
				//String key = new String(Base64.encodeBase64(user.getHashSignature().getBytes()));
				//Jws<Claims> jt = Jwts.parser().setSigningKey(key).parseClaimsJws(jwToken);
				Claims data = null; //jt.getBody();
				if(!identity.equals(data.getSubject())) throw new ExceptionJWT();
				JWTUnpackedData uD = new JWTUnpackedData();
				uD.setExpiration(data.getExpiration());
				uD.setId(data.getId());
				uD.setIssuedAt(data.getIssuedAt());
				uD.setIssuer(data.getIssuer());
				uD.setNotBefore(data.getNotBefore());
				uD.setSubject(data.getSubject());
				httpRequest.setAttribute("jwtParsed", uD);
				httpRequest.setAttribute("jwtTrusted", true);
				httpRequest.setAttribute("jwtUserOrigin", user);
				filterChain.doFilter(httpRequest, httpResponse);
			} catch (ExceptionJWT e) {
				httpResponse.setHeader("Authorization-Requested", "jwt.0.14.1");
				httpResponse.setStatus(400); // bad request
			}
		} else {
			httpResponse.setHeader("Authorization-Requested", "jwt.0.14.1");
			httpResponse.setStatus(403); // forbidden
		}
	}

	public void init(FilterConfig filterConfig) throws ServletException {
		log.debug("Filter JWT Initialized");
		//repository = (UsersRepository)WebApplicationContextUtils.getRequiredWebApplicationContext(filterConfig.getServletContext()).
		//	    getBean(UsersRepository.class);
	}

}
