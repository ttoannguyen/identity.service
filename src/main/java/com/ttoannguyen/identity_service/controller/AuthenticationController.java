package com.ttoannguyen.identity_service.controller;

import com.nimbusds.jose.JOSEException;
import com.ttoannguyen.identity_service.dto.ApiResponse;
import com.ttoannguyen.identity_service.dto.request.AuthenticationRequest;
import com.ttoannguyen.identity_service.dto.request.IntrospectRequest;
import com.ttoannguyen.identity_service.dto.response.AuthenticationResponse;
import com.ttoannguyen.identity_service.dto.response.IntrospectResponse;
import com.ttoannguyen.identity_service.service.AuthenticationService;
import java.text.ParseException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {
  AuthenticationService authenticationService;

  @PostMapping("/token")
  public ApiResponse<AuthenticationResponse> authenticate(
      @RequestBody AuthenticationRequest request) throws JOSEException {

    return ApiResponse.<AuthenticationResponse>builder()
        .result(authenticationService.authenticate(request))
        .build();
  }

  @PostMapping("/introspect")
  public ApiResponse<IntrospectResponse> authenticate(@RequestBody IntrospectRequest request)
      throws JOSEException, ParseException {

    return ApiResponse.<IntrospectResponse>builder()
        .result(authenticationService.introspect(request))
        .build();
  }
}
