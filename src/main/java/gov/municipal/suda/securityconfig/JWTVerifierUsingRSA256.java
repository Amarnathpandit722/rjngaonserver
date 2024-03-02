package gov.municipal.suda.securityconfig;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.interfaces.RSAPublicKey;
import java.util.Base64;


@Component
@Slf4j
public class JWTVerifierUsingRSA256 {

    // please input your jwt token & public key here
    public static final String jwtToken = "";
    public static final String publicKey = "";
    private static final String SECRET_KEY="MY_NAME_IS_SUBHA";

    public Boolean isValidToken(String token) {
        try {
            buildJWTVerifier().verify(token.replace("Bearer ", ""));
            // if token is valid no exception will be thrown
            log.info("Valid TOKEN");
            return Boolean.TRUE;
        } catch (CertificateException e) {
            //if CertificateException comes from buildJWTVerifier()
            log.info("InValid TOKEN");
            e.printStackTrace();
            return Boolean.FALSE;
        } catch (JWTVerificationException e) {
            // if JWT Token in invalid
            log.info("InValid TOKEN");
            e.printStackTrace();
            return Boolean.FALSE;
        } catch (Exception e) {
            // If any other exception comes
            log.info("InValid TOKEN, Exception Occurred");
            e.printStackTrace();
            return Boolean.FALSE;
        }
    }


    private JWTVerifier buildJWTVerifier() throws CertificateException {
        var algo = Algorithm.RSA256(getRSAPublicKey(), null);
        return JWT.require(algo).build();
    }

    private RSAPublicKey getRSAPublicKey() throws CertificateException {
        var decode = Base64.getDecoder().decode(publicKey);
        var certificate = CertificateFactory.getInstance("X.509")
                .generateCertificate(new ByteArrayInputStream(decode));
        var publicKey = (RSAPublicKey) certificate.getPublicKey();
        return publicKey;
    }
    public boolean isValidToken1(String token) {
        try {
            Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
            return true;
        } catch (SignatureException e) {
            // Invalid signature
            return false;
        } catch (ExpiredJwtException e) {
            // Token has expired
            return false;
        } catch (Exception e) {
            // Other errors
            return false;
        }
    }

}
