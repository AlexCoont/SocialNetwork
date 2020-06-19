package project.handlerExceptions;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.util.MimeTypeUtils;
import project.dto.error.Error;
import project.dto.error.enums.ErrorDescriptionEnum;
import project.dto.error.enums.ErrorEnum;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest httpServletRequest,
                       HttpServletResponse httpServletResponse,
                       AccessDeniedException e) throws IOException, ServletException {
        Error error = new Error(ErrorEnum.UNAUTHORIZED.getError(), ErrorDescriptionEnum.UNAUTHORIZED.getError());
        httpServletResponse.setStatus(401);
        httpServletResponse.setContentType(MimeTypeUtils.APPLICATION_JSON_VALUE);
        httpServletResponse.getWriter().write(new ObjectMapper().writeValueAsString(error));
        httpServletResponse.getWriter().flush();
    }
}
