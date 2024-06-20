package com.kropsz.github.msreview.jwt;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JwtUtils {

    public static String extractUserIdFromToken(String token, String pathToPrivateKeyFile) {
        try {
            RSAPrivateKey privateKey = loadPrivateKey(pathToPrivateKeyFile);
            String tokenWithoutBearer = token.replace("Bearer ", "");

            Jws<Claims> jws = Jwts.parser()
                    .setSigningKey(privateKey)
                    .parseClaimsJws(tokenWithoutBearer);

            return jws.getBody().getSubject();
        } catch (Exception e) {
            log.info("Erro ao extrair o ID do usuário do token: " + e.getMessage());
            return null;
        }
    }

    public static RSAPrivateKey loadPrivateKey(String pathToPrivateKeyFile) throws Exception {
        try {
            String key = new String(Files.readAllBytes(Paths.get(pathToPrivateKeyFile)));
            String privateKeyPEM = key
                    .replace("-----BEGIN PRIVATE KEY-----", "")
                    .replaceAll(System.lineSeparator(), "")
                    .replace("-----END PRIVATE KEY-----", "");

            byte[] decoded = Base64.getDecoder().decode(privateKeyPEM);

            PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(decoded);
            KeyFactory kf = KeyFactory.getInstance("RSA");

            PrivateKey privateKey = kf.generatePrivate(spec);
            log.info("Chave privada carregada com sucesso.");
            return (RSAPrivateKey) privateKey;

        } catch (IOException | NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return null;
    }
}