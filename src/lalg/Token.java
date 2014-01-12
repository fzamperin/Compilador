package lalg;

import lalg.Tipos.tipo;
import lalg.Tipos.categoria;

public class Token {
	
	public String Nome;//Nome ou cadeia do objeto
	public int linha;//linha que o objeto est� no codigo fonte
	public int coluna;//coluna que o objeto est� no c�digo fonte
	public tipo Token;//ID ou n�mero
	public String Tipo; //int ou float
	public categoria Categoria;// var, par�metro, procedimento
	int escopo;
	
	public Token(int linha, int coluna) {
		this.Nome = "";
		this.linha = linha;
		this.coluna = coluna;
	}
}
