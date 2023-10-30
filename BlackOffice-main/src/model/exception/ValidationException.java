package model.exception;

import java.util.HashMap;
import java.util.Map;

public class ValidationException extends RuntimeException{
	private static final long serialVersionUID = 1L;
	
	private Map<String, String> erros = new HashMap<>();
	
	public ValidationException(String msg) {
		super(msg);
	}
	
	public Map<String, String> getErros(){
		return erros;
	}
	
	public void addErro (String campo, String mensaggemErro) {
		erros.put(campo, mensaggemErro);
	}

}
