package lalg;

import lalg.Tipos.tipo;
import lalg.Tipos.categoria;

public class Token {
	
	public String Nome;//Nome ou cadeia do objeto
	public int linha;//linha que o objeto está no codigo fonte
	public int coluna;//coluna que o objeto está no código fonte
	public tipo Token;//ID ou número
	public String Tipo; //int ou float
	public categoria Categoria;// var, parâmetro, procedimento
	int escopo;
	
	public Token(int linha, int coluna) {
		this.Nome = "";
		this.linha = linha;
		this.coluna = coluna;
	}
}
