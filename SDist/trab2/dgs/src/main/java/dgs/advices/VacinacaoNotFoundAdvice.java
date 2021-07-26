package dgs.advices;

import dgs.exceptions.CentroNotFoundException;
import dgs.exceptions.VacinacaoNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

public class VacinacaoNotFoundAdvice {
    @ResponseBody
    @ExceptionHandler(CentroNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String vacinacaoNotFoundHandler(VacinacaoNotFoundException ex) {
        return ex.getMessage();
    }

}
