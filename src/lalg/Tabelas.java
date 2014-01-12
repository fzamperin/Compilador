package lalg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class Tabelas {
	
	LinkedHashMap <String, String> Tabela = new LinkedHashMap  <String, String>(22);
	ArrayList <Token> TabelaDeTokens = new ArrayList<Token>();
	HashMap <Integer,String> TabelaDeSimbolos = new HashMap <Integer,String>();
	
	
	void MostraTabela() {
		System.out.println("Resultado do Analisador Léxico: ");
		for(int i=0; i < this.TabelaDeTokens.size();i++) {
			System.out.println(this.TabelaDeTokens.get(i).Nome + " " + " " + "Tipo: " + this.TabelaDeTokens.get(i).Token + " Linha: " + this.TabelaDeTokens.get(i).linha + " Coluna: " + this.TabelaDeTokens.get(i).coluna);
		}
	}
	
	void CriarTabela() {
		//Tabela de Palavras Reservadas
		Tabela.put("programa", "1");
		Tabela.put("inicio", "2");
		Tabela.put("fim", "3");
		Tabela.put("real", "4");
		Tabela.put("inteiro", "5");
		Tabela.put("procedimento", "6");
		Tabela.put("se", "7");
		Tabela.put("senao", "8");
		Tabela.put("fimse", "9");
		Tabela.put("enquanto", "10");
		Tabela.put("faca", "11");
		Tabela.put("fimenquanto", "12");
		Tabela.put("leia", "13");
		Tabela.put("escreva", "14");
		Tabela.put("para", "15");
		Tabela.put("de", "16");
		Tabela.put("ate", "17");
		Tabela.put("fimpara", "18");
		Tabela.put("e", "19");
		Tabela.put("ou", "20");
		Tabela.put("entao", "21");
		Tabela.put("fimprocedimento", "22");
	}
}