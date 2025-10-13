package vn.tcl.timviec24h.controller;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import vn.tcl.timviec24h.domain.User;
import vn.tcl.timviec24h.domain.request.ReqLoginDTO;
import vn.tcl.timviec24h.domain.response.ResLoginDTO;
import vn.tcl.timviec24h.service.UserService;
import vn.tcl.timviec24h.util.SecurityUtil;
import vn.tcl.timviec24h.util.annotation.ApiMessage;
import vn.tcl.timviec24h.util.error.IdInvalidException;

@RestController
@RequestMapping("/api/v1")
public class AuthController {
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final SecurityUtil securityUtil;
    private final UserService userService;
    @Value("${timviec24h.jwt.refresh-token-validity-in-seconds}")
    private long refreshTokenExpiration;
    public AuthController(AuthenticationManagerBuilder authenticationManagerBuilder,SecurityUtil securityUtil,UserService userService) {
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.securityUtil = securityUtil;
        this.userService = userService;
    }
    @PostMapping("/auth/login")
    public ResponseEntity<ResLoginDTO> login(@Valid @RequestBody ReqLoginDTO reqLoginDTO){
        //Nạp input gồm username/password vào Security
        UsernamePasswordAuthenticationToken authenticationToken 
        = new UsernamePasswordAuthenticationToken(reqLoginDTO.getUsername(), reqLoginDTO.getPassword());

        //xác thực người dùng => cần viết hàm loadUserByUsername
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        // Create a token 
        //set thông tin đăng nhập vào context (Sau này cos thể dùng)
        SecurityContextHolder.getContext().setAuthentication(authentication);
        ResLoginDTO resLoginDTO = new ResLoginDTO();
        User currentUserDB = userService.getUserByUsername(reqLoginDTO.getUsername());
        if(currentUserDB!=null){
            ResLoginDTO.UserLogin userLogin = new ResLoginDTO.UserLogin(currentUserDB.getId(),currentUserDB.getEmail(),
                    currentUserDB.getName());
            resLoginDTO.setUser(userLogin);
        }
        String access_token = this.securityUtil.createAccessToken(authentication.getName(),resLoginDTO.getUser());
        resLoginDTO.setAccess_token(access_token);
        //create refresh token
        String refreshToken = securityUtil.createRefreshToken(reqLoginDTO.getUsername(),resLoginDTO);
        userService.updateRefreshToken(refreshToken, reqLoginDTO.getUsername());
        //set cookies
        ResponseCookie cookie = ResponseCookie.from("refresh_token",refreshToken)
                .httpOnly(true)
                .maxAge(refreshTokenExpiration)
                .secure(true)
                .path("/")
                .build();
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE,cookie.toString())
                .body(resLoginDTO);
    }
    @GetMapping("/auth/account")
    @ApiMessage("Fetch account")
    public ResponseEntity<ResLoginDTO.UserGetAccount> getAccount(){
        String email =  SecurityUtil.getCurrentUserLogin().isPresent() ? SecurityUtil.getCurrentUserLogin().get():"";
        User currentUserDB = userService.getUserByUsername(email);
        ResLoginDTO.UserLogin userLogin = new ResLoginDTO.UserLogin();
        ResLoginDTO.UserGetAccount userGetAccount = new ResLoginDTO.UserGetAccount();
        if(currentUserDB!=null){
           userLogin.setId(currentUserDB.getId());
           userLogin.setEmail(currentUserDB.getEmail());
           userLogin.setName(currentUserDB.getName());
            userGetAccount.setUser(userLogin);
        }
        return ResponseEntity.ok().body(userGetAccount);
    }
    @GetMapping("/auth/refresh")
    @ApiMessage("Refresh Token")
    public ResponseEntity<ResLoginDTO> getRefreshToken(
            @CookieValue(name = "refresh_token" ,defaultValue = "abc") String refresh_token
    ) throws IdInvalidException {
        if(refresh_token.equals("abc")){
            throw new IdInvalidException("Refresh Token không có ở cookies không tồn tại");
        }
        //check valid token
        Jwt decodedToken = securityUtil.checkValidRefreshToken(refresh_token);
        String email = decodedToken.getSubject();
        //check user by token + email
        User currentUser = userService.getUserByRefreshTokenAndEmail(refresh_token,email);
        if(currentUser==null){
            throw new IdInvalidException("Refresh Token không hợp lệ");
        }
        //issue new token/set refresh token as cookies

        ResLoginDTO resLoginDTO = new ResLoginDTO();
        User currentUserDB = userService.getUserByUsername(email);
        if(currentUserDB!=null){
            ResLoginDTO.UserLogin userLogin = new ResLoginDTO.UserLogin(currentUserDB.getId(),currentUserDB.getEmail(),
                    currentUserDB.getName());
            resLoginDTO.setUser(userLogin);
        }
        String access_token = this.securityUtil.createAccessToken(email,resLoginDTO.getUser());
        resLoginDTO.setAccess_token(access_token);
        //create refresh token
        String newRefreshToken = securityUtil.createRefreshToken(email,resLoginDTO);
        //update refreshToken
        userService.updateRefreshToken(newRefreshToken,email);
        //set cookies
        ResponseCookie cookie = ResponseCookie.from("refresh_token",newRefreshToken)
                .httpOnly(true)
                .maxAge(refreshTokenExpiration)
                .secure(true)
                .path("/")
                .build();
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE,cookie.toString())
                .body(resLoginDTO);
    }
    @PostMapping("/auth/logout")
    @ApiMessage("logout user")
    public ResponseEntity<Void> logout() throws IdInvalidException {
        String email = SecurityUtil.getCurrentUserLogin().isPresent() ? SecurityUtil.getCurrentUserLogin().get():"";
        if(email.equals("")){
            throw new IdInvalidException("Access Token khoong hợp lệ");
        }
        userService.updateRefreshToken("",email);
        ResponseCookie deleteCookie = ResponseCookie.from("refresh_token",null)
                .httpOnly(true)
                .maxAge(0)
                .secure(true)
                .path("/")
                .build();
        return  ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE,deleteCookie.toString()).body(null);
    }
}
