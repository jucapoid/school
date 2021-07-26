package dgs.exceptions;

public class CentroNotFoundException extends RuntimeException{
    public CentroNotFoundException(Long id) {
        super("Centro " + id + " não encontrado");
    }
}
