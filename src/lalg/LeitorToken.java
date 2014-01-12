package lalg;

import java.io.BufferedReader;
import lalg.Tipos;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class LeitorToken {
	private File CodFonte;
	private FileReader fr;
	private BufferedReader br;
	private int linha = 0;
	private int cont = 0;
	public Tabelas t;
	public Token tok;
	
	public LeitorToken(Tabelas t) {
		this.t = t;
		t.CriarTabela();
	}
	

	// Métodos
	void OpenFile() throws FileNotFoundException {
		this.CodFonte = new File("D:\\Teste.txt");
		this.fr = new FileReader(CodFonte);
		this.br = new BufferedReader(fr);
	}

	char[] LerLinha(Tabelas t) throws IOException {
		if (this.br.ready()) {
			this.linha++;
			this.cont = 0;
			return (this.br.readLine().toCharArray());
		} else {
			return null;
		}
	}

	void SeparaTokens() throws IOException {
		char[] tokens;
		tokens = this.LerLinha(t);
		tok = new Token(linha, this.cont);
		while (tokens != null) {
			while (tokens != null && this.cont < tokens.length) {
				
				// Verificação de Palavras reservadas e ID
				if ((tokens[this.cont] >= 'a' && tokens[this.cont] <= 'z')
						|| (tokens[this.cont] >= 'A' && tokens[this.cont] <= 'Z')) {
					tok.Nome += tokens[this.cont];
					this.cont++;
					while ((this.cont < tokens.length)
							&& ((tokens[this.cont] >= 'a' && tokens[this.cont] <= 'z')
									|| (tokens[this.cont] >= 'A' && tokens[this.cont] <= 'Z') || (tokens[this.cont] >= '0' && tokens[this.cont] <= '9'))) {
						tok.Nome += tokens[this.cont];
						this.cont++;
					}
					if (this.t.Tabela.containsKey(tok.Nome)) {
						tok.linha = this.linha;
						tok.Token = Tipos.tipo.PR;
						t.TabelaDeTokens.add(tok);
					} else {
						tok.linha = this.linha;
						tok.Token = Tipos.tipo.ID;
						t.TabelaDeTokens.add(tok);
					}
					tok = new Token(linha, this.cont);
				}
				
				// Numeros (inteiros e reais)
				if (this.cont < tokens.length
						&& (tokens[this.cont] >= '1' && tokens[this.cont] <= '9')) {
					this.tok.Nome += tokens[this.cont];
					this.cont++;
					while ((this.cont < tokens.length)
							&& (tokens[this.cont] >= '0' && tokens[this.cont] <= '9')) {
						this.tok.Nome += tokens[this.cont];
						this.cont++;
					}
					if (this.cont < tokens.length && tokens[this.cont] == '.') {
						this.tok.Nome += tokens[this.cont];
						this.cont++;
						while ((this.cont < tokens.length) && tokens[this.cont] >= '0'
								&& tokens[this.cont] <= '9') {
							this.tok.Nome += tokens[this.cont];
							this.cont++;
						}
						tok.Token = Tipos.tipo.REAL;
						t.TabelaDeTokens.add(tok);
					} else {
						tok.Token = Tipos.tipo.INTEIRO;
						t.TabelaDeTokens.add(tok);
					}
					tok = new Token(linha, this.cont);
				}
				//Ponto e virgula
				if(this.cont < tokens.length && tokens[this.cont] == ';') {
					this.tok.Nome += tokens[this.cont];
					tok.Token = Tipos.tipo.PONTOVIRGULA;
					t.TabelaDeTokens.add(tok);
					tok = new Token(linha,this.cont);
				}
				//Abre parênteses
				if(this.cont < tokens.length && tokens[this.cont] == '(') {
					this.tok.Nome += tokens[this.cont];
					tok.Token = Tipos.tipo.PARENTESISABRE;
					t.TabelaDeTokens.add(tok);
					tok = new Token(linha, this.cont);
				}
				//Fecha Parênteses
				if(this.cont < tokens.length && tokens[this.cont] == ')') {
					this.tok.Nome += tokens[this.cont];
					tok.Token = Tipos.tipo.PARENTESISFECHA;
					t.TabelaDeTokens.add(tok);
					tok = new Token(linha, this.cont);
				}
				
				//Dois pontos
				if(this.cont < tokens.length && tokens[this.cont] == ':') {
					this.tok.Nome += tokens[this.cont];
					tok.Token = Tipos.tipo.DOISPONTOS;
					t.TabelaDeTokens.add(tok);
					tok = new Token(linha, this.cont);
				}
				
				//Virgula
				if(this.cont < tokens.length && tokens[this.cont] == ',') {
					this.tok.Nome += tokens[this.cont];
					tok.Token = Tipos.tipo.VIRGULA;
					t.TabelaDeTokens.add(tok);
					tok = new Token(linha, this.cont);
				}
				
				// Operador de Diferente
				if (this.cont < tokens.length && tokens[this.cont] == '#') {
					this.tok.Nome += tokens[this.cont];
					tok.Token = Tipos.tipo.OPDIFE;
					t.TabelaDeTokens.add(tok);
					tok = new Token(linha, this.cont);
				}
				// Operador de Soma
				if (this.cont < tokens.length && tokens[this.cont] == '+') {
					this.tok.Nome += tokens[this.cont];
					tok.Token = Tipos.tipo.SOMA;
					t.TabelaDeTokens.add(tok);
					tok = new Token(linha, this.cont);
				}
				// Operador de Subtração
				if (this.cont < tokens.length && tokens[this.cont] == '-') {
					this.tok.Nome += tokens[this.cont];
					tok.Token = Tipos.tipo.SUB;
					t.TabelaDeTokens.add(tok);
					tok = new Token(linha,this.cont);
				}
				// Operador de Multiplicação
				if (this.cont < tokens.length && tokens[this.cont] == '*') {
					this.tok.Nome += tokens[this.cont];
					tok.Token = Tipos.tipo.MUL;
					t.TabelaDeTokens.add(tok);
					tok = new Token(linha,this.cont);
				}
				// Operador Menor que e Menor ou igual que
				if (this.cont < tokens.length && tokens[this.cont] == '<') {
					this.tok.Nome += tokens[this.cont];
					this.cont++;
					if (this.cont < tokens.length && tokens[this.cont] == '=') {
						this.tok.Nome += tokens[this.cont];
						tok.Token = Tipos.tipo.MENORIGUAL;
						t.TabelaDeTokens.add(tok);
						this.cont++;
					}
					else {
						tok.Token = Tipos.tipo.OPMENOR;
						t.TabelaDeTokens.add(tok);
					}
					tok = new Token(linha, this.cont);
				}
				// Operador Maior que e Maior ou igual que
				if (this.cont < tokens.length && tokens[this.cont] == '>') {
					this.tok.Nome += tokens[this.cont];
					this.cont++;
					if (this.cont < tokens.length && tokens[this.cont] == '=') {
						this.tok.Nome += tokens[this.cont];
						tok.Token = Tipos.tipo.MAIORIGUAL;
						t.TabelaDeTokens.add(tok);
					}
					else {
						tok.Token = Tipos.tipo.OPMAIOR;
						t.TabelaDeTokens.add(tok);
					}
					tok = new Token(linha, this.cont);
				}
				// Assignação e igual que
				if (this.cont < tokens.length && tokens[this.cont] == '=') {
					this.tok.Nome += tokens[this.cont];
					this.cont++;
					if (this.cont < tokens.length && tokens[this.cont] == '=') {
						this.tok.Nome += tokens[this.cont];
						this.cont++;
						tok.Token = Tipos.tipo.OPIGUAL;
						t.TabelaDeTokens.add(tok);
					} else {
						tok.Token = Tipos.tipo.OPASSIG;
						t.TabelaDeTokens.add(tok);
					}
					tok = new Token(linha, this.cont);
				}
				
				// Comentário ou Operador de divisão
				if (this.cont < tokens.length && tokens[this.cont] == '/') {
					this.tok.Nome += tokens[this.cont];
					this.cont++;
					if (this.cont < tokens.length && tokens[this.cont] == '/') {
						break;
					}
					if (this.cont < tokens.length && tokens[this.cont] != '*') {
						t.TabelaDeTokens.add(tok);
						tok = new Token(linha,this.cont);
						this.cont--;
					} 
					else {
						while (tokens != null) {
							this.cont++;
							if (this.cont < tokens.length) {
								if (tokens[this.cont] == '*') {
									if (tokens[this.cont + 1] == '/')
										this.cont++;
										break;
								}
							} 
							else {
								tokens = this.LerLinha(t);
								this.cont = -1;
							}

							// if (this.cont == tokens.length) {
							// tokens = this.LerLinha(t);
							// this.cont = 0;
							// }
							// if (tokens[this.cont] == '*') {
							// if (this.cont < tokens.length && tokens[this.cont + 1] ==
							// '/')
							// break;
							// }

						}
					}
					
				}
				this.cont++;
			}
			tokens = this.LerLinha(t);
			tok = new Token(linha,this.cont);
		}
		this.br.close();
		this.fr.close();
	}
}