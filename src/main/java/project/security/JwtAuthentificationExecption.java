package project.security;

import org.springframework.security.core.AuthenticationException;

public class JwtAuthentificationExecption extends AuthenticationException {

    public JwtAuthentificationExecption(String msg, Throwable t) {
        super(msg, t);
    }

    public JwtAuthentificationExecption(String msg) {
        super(msg);
    }
}
