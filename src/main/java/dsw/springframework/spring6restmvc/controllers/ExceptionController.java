package dsw.springframework.spring6restmvc.controllers;

import dsw.springframework.spring6restmvc.exceptions.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity handleNotFoundException(Exception ex){
        log.info(ex.getMessage());
        return ResponseEntity.notFound().build();
    }
}
