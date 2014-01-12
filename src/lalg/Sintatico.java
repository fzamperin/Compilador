package lalg;

import java.util.ArrayList;
import java.util.HashMap;

import lalg.Tipos.tipo;

public class Sintatico {

	private int aux = 0;//variável que percorre a lista de tokens
	private ArrayList<Token> TabelaDeTokens = new ArrayList<Token>();//Lista de tokens do léxico
	private HashMap <Integer,Token> TabelaDeSimbolos = new HashMap <Integer,Token>();//Tabela de Símbolos
	private Token contextoAuxiliar = new Token(0,0);//Variável para contextoAuxiliar na inserção na tabela de símbolos
	private boolean declaravar;//Variável flag para verificar se é declaração de variável ou uso de variável
	private int escopo = 0;
	
	public Sintatico(Tabelas t) {
		this.TabelaDeTokens = t.TabelaDeTokens;
	}

	void programa() {
		match("programa");
		if (this.TabelaDeTokens.get(aux).Token != Tipos.tipo.ID) {
			if(this.TabelaDeTokens.get(aux).Nome.contentEquals("e")
					|| this.TabelaDeTokens.get(aux).Nome.contentEquals("ou")) {
				System.out.println("Erro, o token 'e' e o token 'ou' são operadores relacionais!");
				System.exit(0);
			}
			System.out.println("Esperava identificador de programa");
			System.exit(0);
		}
		else {
			this.TabelaDeSimbolos.put(this.TabelaDeTokens.get(this.aux).hashCode(), this.TabelaDeTokens.get(this.aux));
		}
		this.aux++;
		corpo();
	}

	void corpo() {
		this.declaravar = true;
		declara_var();
		declara_proc();
		match("inicio");
		this.declaravar = false;
		comandos();
		match("fim");
	}

	void comandos() {
		String nome = this.TabelaDeTokens.get(this.aux).Nome;
		if(nome.contentEquals("leia") || nome.contentEquals("escreva") || 
				nome.contentEquals("se") || nome.contentEquals("enquanto") ||
				nome.contentEquals("para") || this.TabelaDeTokens.get(this.aux).Token == Tipos.tipo.ID) {
		comando();
		mais_comandos();
		}	
	}

	void declara_var() {
		if(this.TabelaDeTokens.get(aux).Nome.contentEquals("real")
				|| this.TabelaDeTokens.get(aux).Nome.contentEquals("inteiro")) {
			this.tipo_var();
			this.match(":");
			this.variaveis();
			this.match(";");
			this.declara_var();
		}
	}

	void variaveis() {
		if(this.declaravar == true) {//Trocar rotina para não pedir variáveis declaradas	
			if (this.TabelaDeTokens.get(aux).Token != Tipos.tipo.ID) {
				System.out.println("Erro, esperava identificador");
				System.exit(0);
			}
			else {
				if(this.TabelaDeSimbolos.containsKey(this.TabelaDeTokens.get(aux).Nome.hashCode())) {
					//Resolver aqui
					System.out.println("Identificador: " + this.TabelaDeTokens.get(aux).Nome + " já declarado" + " Linha: "+this.TabelaDeTokens.get(aux).linha + " Coluna: "+ (this.TabelaDeTokens.get(aux).coluna+1));
					System.exit(0);
				}
			}
		}
				else {
					contextoAuxiliar = this.TabelaDeTokens.get(aux);
					contextoAuxiliar.escopo = this.escopo;
					this.TabelaDeSimbolos.put(this.TabelaDeTokens.get(aux).Nome.hashCode(), contextoAuxiliar);
					contextoAuxiliar = new Token(0,0);
					contextoAuxiliar = this.TabelaDeSimbolos.get(this.TabelaDeTokens.get(this.aux).Nome.hashCode());
					System.out.println("Declarado: " + contextoAuxiliar.Nome + " " + "Tipo: " + contextoAuxiliar.Token + "Escopo: " + this.contextoAuxiliar.escopo);
					contextoAuxiliar = new Token(0,0);
				}
		if(this.declaravar == false) {
			if(!this.TabelaDeSimbolos.containsKey(this.TabelaDeTokens.get(this.aux).Nome.hashCode())) {
				System.out.println("Token: " + this.TabelaDeTokens.get(this.aux).Nome + " " + this.TabelaDeTokens.get(this.aux).Token + " não declarado");
				System.exit(0);
			}
		}
		this.aux++;
		mais_var();
	}

	void declara_proc() {
		if(this.TabelaDeTokens.get(aux).Nome.contentEquals("procedimento")) {
			dc_p();
			mais_declara_proc();
		}
	}

	void mais_declara_proc() {// Possui Lambda
		if(this.TabelaDeTokens.get(aux).Nome.contentEquals(";")) {
			this.aux++;
			declara_proc();
			return;
		}
		this.escopo--;
		return;
	}

	void tipo_var() {
		if(this.TabelaDeTokens.get(aux).Nome.contentEquals("real")) {
			match("real");
			contextoAuxiliar.Tipo = "real";
		}
		else
			if(this.TabelaDeTokens.get(aux).Nome.contentEquals("inteiro")) {
				match("inteiro");
				contextoAuxiliar.Tipo = "inteiro";
			}
			else {
				System.out.println("Erro no tipo da variável");
				System.exit(0);
			}
	}

	// Possui lambda
	void mais_var() {
		if(this.TabelaDeTokens.get(aux).Nome.contentEquals(",")) {
			match(",");
			variaveis();
		}
		return;
	}

	void parametros() {
		this.declaravar = true;
		if(this.TabelaDeTokens.get(aux).Nome.contentEquals("(")) {
			match("(");
			lista_par();
			if(this.TabelaDeTokens.get(aux).Nome.contentEquals(")")) {
				this.aux++;
			}
			else {
				System.out.println("Esperava ')'");
				System.exit(0);
			}	
		}
	}

	void lista_par() {
		tipo_var();
		match(":");
		variaveis();
		mais_par();
	}

	void mais_par() {
		if(this.TabelaDeTokens.get(aux).Nome.contentEquals(";")) {
			match(";");
			lista_par();
			return;
		}
		return;
	}

	void corpo_p() {
		//Parte que o contextoAuxiliar.categoria tecebe os parametros
		declara_var();
		this.declaravar = false;
		match("inicio");
		comandos();
		match("fimprocedimento");
	}

	void dc_p() {
		match("procedimento");
		if (this.TabelaDeTokens.get(aux).Token != Tipos.tipo.ID) {
			System.out.println("Erro, esperava identificador do procedimento!");
			System.exit(0);
		}
		else {
			if(!this.TabelaDeSimbolos.containsKey(this.TabelaDeTokens.get(aux).Nome.hashCode())) {
				this.escopo++;
				contextoAuxiliar = this.TabelaDeTokens.get(aux);
				contextoAuxiliar.Categoria = Tipos.categoria.proc;
				contextoAuxiliar.Tipo = null;
				this.TabelaDeSimbolos.put(this.TabelaDeTokens.get(aux).Nome.hashCode(), contextoAuxiliar);
			}
			else {
				System.out.println("Identificador já declarado");
				System.exit(0);
			}
		}
		this.aux++;
		parametros();
		corpo_p();
	}

	void lista_arg() {
		if(this.TabelaDeTokens.get(aux).Nome.contentEquals("(")) {
			match("(");
			argumentos();
			match(")");
			return;
		}
		return;
	}

	void argumentos() {
		if(this.TabelaDeTokens.get(this.aux).Token == Tipos.tipo.ID) {
			this.aux++;
			mais_id();
		}
	}

	void mais_id() {
		if(this.TabelaDeTokens.get(aux).Nome.contentEquals(";")) {
			match(";");
			argumentos();
			return;
		}
		return;
	}

	void se_falso() {
		if(this.TabelaDeTokens.get(aux).Nome.contentEquals("senao")) {
			match("senao");
			comandos();
		}
		return;
	}

	void mais_comandos() {
		if(this.TabelaDeTokens.get(aux).Nome.contentEquals(";")) {
			match(";");
			comandos();
			return;
		}
		else
			System.out.println("Erro, esperava ; Linha: " + this.TabelaDeTokens.get(aux).linha + " Coluna: " + this.TabelaDeTokens.get(this.aux++).coluna);
			System.exit(0);
	}

	void comando() {
		String nome = this.TabelaDeTokens.get(aux).Nome;
		if (nome.contentEquals("leia")) {
			match("leia");
			match("(");
			variaveis();
			match(")");
			return;
		}
		if (nome.contentEquals("escreva")) {
			match("escreva");
			match("(");
			variaveis();
			match(")");
			return;
		}
		if (nome.contentEquals("enquanto")) {
			match("enquanto");
			condicao();
			match("faca");
			comandos();
			match("fimenquanto");
			return;
		}
		if (nome.contentEquals("se")) {
			match("se");
			condicao();
			match("entao");
			comandos();
			se_falso();
			match("fimse");
			return;
		}
		if (nome.contentEquals("para")) {
			match("para");
			if (this.TabelaDeTokens.get(aux).Token != Tipos.tipo.ID) {
				System.out.println("Erro, esperava uma variável");
				System.exit(0);
			}
			this.aux++;
			match("de");
			if (this.TabelaDeTokens.get(aux).Token != Tipos.tipo.INTEIRO) {
				System.out.println("Erro, esperava um número inteiro");
				System.exit(0);
			}
			this.aux++;
			match("ate");
			if (this.TabelaDeTokens.get(aux).Token != Tipos.tipo.INTEIRO) {
				System.out.println("Erro, esperava um número inteiro");
				System.exit(0);
			}
			this.aux++;
			match("faca");
			comandos();
			return;
		}
		if(this.TabelaDeTokens.get(aux).Token == Tipos.tipo.ID) {
			this.aux++;
			restold();
			return;
		}
		System.out.println("Erro em comando");
		System.exit(0);
	}

	void restold() {
		if(this.TabelaDeTokens.get(this.aux).Nome.contentEquals("=")) {
			match("=");
			expressao();
		}
		else {
			if(this.TabelaDeTokens.get(this.aux).Nome.contentEquals("(")) {
				lista_arg();
			}
			else {
				System.out.println("Erro, em restoid");
				System.exit(0);
			}
		}
		
	}

	void condicao() {
		expressao();
		relacao();
		expressao();
	}

	void relacao() {
		if(this.TabelaDeTokens.get(aux).Nome.contentEquals("==")){
			match("==");
			return;
		}
		if(this.TabelaDeTokens.get(aux).Nome.contentEquals("#")){
			match("#");
			return;
		}
		if(this.TabelaDeTokens.get(aux).Nome.contentEquals(">=")) {
			match(">=");
			return;
		}
		if(this.TabelaDeTokens.get(aux).Nome.contentEquals("<=")) {
			match("<=");
			return;
		}
		if(this.TabelaDeTokens.get(aux).Nome.contentEquals("<")) {
			match("<");
			return;
		}
		if(this.TabelaDeTokens.get(aux).Nome.contentEquals(">")) {
			match(">");
			return;
		}
		if(this.TabelaDeTokens.get(aux).Nome.contentEquals("e")) {
			match("e");
			return;
		}
		if(this.TabelaDeTokens.get(aux).Nome.contentEquals("ou")){
			match("ou");
			return;
		}
		System.out.println("Erro de operador relacional");
		System.exit(0);
	}

	void expressao() {
		termo();
		outros_termos();
	}

	void op_un() {
		if(this.TabelaDeTokens.get(aux).Nome.contentEquals("+")) {
			match("+");
		}
		else
			if(this.TabelaDeTokens.get(aux).Nome.contentEquals("-")) {
				match("-");
			}
		return;
	}

	void outros_termos() {
		if(this.TabelaDeTokens.get(aux).Nome.contentEquals("+") 
			|| this.TabelaDeTokens.get(aux).Nome.contentEquals("-")){
			op_ad();
			termo();
			outros_termos();
		}
		return;
	}

	void op_ad() {
		if(this.TabelaDeTokens.get(aux).Nome.contentEquals("+")) {
			match("+");
		}
		else {
			if(this.TabelaDeTokens.get(aux).Nome.contentEquals("-")) {
				match("-");
			}
			else {
				System.out.println("Esperava uma soma ou subtração");
				System.exit(0);
			}
		}
		return;
	}

	void termo() {
		op_un();
		fator();
		mais_fatores();
		return;
	}

	void mais_fatores() {
		if(this.TabelaDeTokens.get(aux).Nome.contentEquals("*") ||
				this.TabelaDeTokens.get(aux).Nome.contentEquals("/")) {
			op_mul();
			fator();
			mais_fatores();
		}
		return;
	}

	void op_mul() {
		if(this.TabelaDeTokens.get(this.aux).Nome.contentEquals("*")) {
			match("*");
			return;
		}
		else {
			if(this.TabelaDeTokens.get(this.aux).Nome.contentEquals("/")) {
			match("/");
			return;
			}
			else {
				System.out.println("Erro, esperava operador de MUL ou DIV");
				System.exit(0);
			}
		}
	}

	void fator() {
		if (this.TabelaDeTokens.get(aux).Token == tipo.ID) {
			if(this.TabelaDeSimbolos.containsKey(this.TabelaDeTokens.get(aux).Nome.hashCode())) {
				this.aux++;
				return;
			}
			else {
				System.out.println("Erro em fator, identificador: " + this.TabelaDeTokens.get(this.aux).Nome + " não declarado");
				System.exit(0);
			}
		}
		else {
			if (this.TabelaDeTokens.get(aux).Token == tipo.REAL) {
				contextoAuxiliar = this.TabelaDeTokens.get(aux);
				this.TabelaDeSimbolos.put(this.TabelaDeTokens.get(aux).Nome.hashCode(), contextoAuxiliar);
				contextoAuxiliar = new Token(0,0);
				this.aux++;
				return;
			}
			else {
				if (this.TabelaDeTokens.get(aux).Token == tipo.INTEIRO) {
					contextoAuxiliar = this.TabelaDeTokens.get(aux);
					this.TabelaDeSimbolos.put(this.TabelaDeTokens.get(aux).Nome.hashCode(), contextoAuxiliar);
					contextoAuxiliar = new Token(0,0);
					this.aux++;
					return;
				}
				else {
					if(this.TabelaDeTokens.get(aux).Nome.contentEquals("(")) {
						match("(");
						expressao();
					}
					else {
						System.out.println("Erro, espeverava um fator " + this.TabelaDeTokens.get(this.aux).linha);
						System.exit(0);
					}
				}
			}
		}
	}
	
	private boolean match(String y) {
		if (!this.TabelaDeTokens.get(aux).Nome.contentEquals(y)) {
			System.out.println("Erro, esperava: " + y + " Linha: " + this.TabelaDeTokens.get(aux).linha + " Coluna: " + this.TabelaDeTokens.get(aux).coluna);
			System.exit(0);
		}
		this.aux++;
		return true;
	}
}