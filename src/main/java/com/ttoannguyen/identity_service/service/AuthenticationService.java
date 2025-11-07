package com.ttoannguyen.identity_service.service;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JOSEObjectType;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.KeyLengthException;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.ttoannguyen.identity_service.dto.request.AuthenticationRequest;
import com.ttoannguyen.identity_service.dto.request.IntrospectRequest;
import com.ttoannguyen.identity_service.dto.response.AuthenticationResponse;
import com.ttoannguyen.identity_service.dto.response.IntrospectResponse;
import com.ttoannguyen.identity_service.entity.User;
import com.ttoannguyen.identity_service.exception.AppException;
import com.ttoannguyen.identity_service.exception.ErrorCode;
import com.ttoannguyen.identity_service.repository.UserRepository;
import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.StringJoiner;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AuthenticationService {
  UserRepository userRepository;
  PasswordEncoder passwordEncoder;

  @NonFinal
  @Value("${jwt.signerKey}")
  protected String SIGNER_KEY;

  public AuthenticationResponse authenticate(AuthenticationRequest request) throws JOSEException {
    User user =
        userRepository
            .findByUsername(request.getUsername())
            .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

    // PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
    boolean authenticated = passwordEncoder.matches(request.getPassword(), user.getPassword());
    if (!authenticated) {
      throw new AppException(ErrorCode.UNAUTHENTICATED);
    }

    String token = generateToken(user);
    return AuthenticationResponse.builder().authenticated(authenticated).token(token).build();
  }

  public IntrospectResponse introspect(IntrospectRequest request)
      throws JOSEException, ParseException {
    String token = request.getToken();

    JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());
    SignedJWT signedJWT = SignedJWT.parse(token);
    Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();
    boolean verified = signedJWT.verify(verifier);

    return IntrospectResponse.builder().valid(verified && expiryTime.after(new Date())).build();
  }

  private String generateToken(User user) throws JOSEException {
    JWSHeader jwsHeader =
        new JWSHeader.Builder(JWSAlgorithm.HS512).type(JOSEObjectType.JWT).build();
    JWTClaimsSet jwtClaimsSet =
        new JWTClaimsSet.Builder()
            .subject(user.getUsername())
            .issuer("ttoannguyen")
            .issueTime(new Date())
            .expirationTime(new Date(Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli()))
            .claim("scope", buildScope(user))
            .build();

    Payload payload = new Payload(jwtClaimsSet.toJSONObject());
    JWSObject jwsObject = new JWSObject(jwsHeader, payload);

    try {
      jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
      return jwsObject.serialize();
    } catch (KeyLengthException e) {
      log.error("Cannot generate token!", e);
      throw new RuntimeException(e);
    }
  }

  private String buildScope(User user) {
    StringJoiner stringJoiner = new StringJoiner(" ");
    if (!CollectionUtils.isEmpty(user.getRoles())) {
      user.getRoles().forEach(stringJoiner::add);
    }

    return stringJoiner.toString();
  }
}
