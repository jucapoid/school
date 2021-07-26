package dgs.exceptions;

public class VacinacaoNotFoundException extends RuntimeException{
    public VacinacaoNotFoundException(Long id) {
        super("Vacinação " + id + " não encontrada");
    }
}