package vn.tcl.timviec24h.service.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import vn.tcl.timviec24h.repository.RestResponse;

@RestControllerAdvice
public class GlobalException {
     @ExceptionHandler(value = IdInvalidException.class)
    public ResponseEntity<RestResponse<Object>> handleIdInvalidException(IdInvalidException ex) {
         RestResponse<Object> res = new RestResponse<Object>();
       res.setStatusCode(HttpStatus.BAD_REQUEST.value());
       res.setMessage("IdInvalidException");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
    }
}
