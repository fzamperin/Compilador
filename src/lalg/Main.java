package lalg;

import java.io.IOException;

public class Main {

	public static void main(String[] args) throws IOException   {
		Tabelas t = new Tabelas();
		LeitorToken Lexico = new LeitorToken(t);
		Sintatico sint = new Sintatico(t);
		
		Lexico.OpenFile();
		Lexico.SeparaTokens();
		t.MostraTabela();
		sint.programa();
	}
}