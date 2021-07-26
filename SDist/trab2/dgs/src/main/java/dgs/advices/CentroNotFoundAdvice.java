package dgs.advices;

import dgs.exceptions.CentroNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class CentroNotFoundAdvice {
    @ResponseBody
    @ExceptionHandler(CentroNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String centroNotFoundHandler(CentroNotFoundException ex) {
        return ex.getMessage();
    }
}
