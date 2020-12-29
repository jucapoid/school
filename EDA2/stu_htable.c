#include "stu_htable.h"
#include "hash.h"

//Inicializador da hashtable referente aos estudantes
//	capacity - número de posições totais na hashtable
htable_stu_t* new_stu_htable(int capacity)
{
	htable_stu_t* temp = malloc(sizeof(htable_stu_t));
	//afetação da variável capacidade utilizada no cálculo de hascodes
	temp->capacity = capacity;
	//chamada da função que inicia o ficheiro se este não existir
	start_stu_htable(temp);

	return temp;
}

//Construtor de estudante
//	id - identificador do estudante
//	cod_pais - identificador do país a que o estudante pertence
//	estado - estado do estudante: A = ativo, T = termino, D = abandono 
student_t* new_student(char* id, char* cod_pais, char estado)
{
	student_t* temp = malloc(sizeof(student_t));
	//set da memória a zeros para evitar erros de escrita no ficheiro por bytes não inicializados
	memset(temp, 0, sizeof(student_t));
	//afetação das variáveis do estudante a partir dos argumentos recebidos
	strcpy(temp->id, id);
	strcpy(temp->cod_pais, cod_pais);
	temp->estado = estado;

	return temp;
}

//método para fechar o ficheiro e libertar o espaço da estrutura
void free_htable_stu(htable_stu_t* table)
{
	fclose(table->htable);
	free(table);
}

//Método responsável por iniciar/abrir o ficheiro que contém a Hashtable dos estudantes
//Recebe como argumento único o nome do ficheiro que guarda a Hashtable e retorna o ficheiro aberto
//Preenche o ficheiro com 20 000 003 cópias de um estudante vazio
void start_stu_htable(htable_stu_t* table)
{
	//abertura do ficheiro em modo de leitura com permissão para escrita
	table->htable = fopen(STUDENTS, "r+");
	//se o ficheiro não existir
	if(table->htable == NULL)
	{
		//abertura do ficheiro em modo de escrita para permitir a sua criação
		table->htable = fopen(STUDENTS, "w+");
		//confirmar se foi possível criar o ficheiro
		if(table->htable == NULL)
		{
			perror("Erro: Impossível criar Ficheiro para tabela dos estudantes");
			exit(0);
		}
		//inicialização de um estudante vazio
		student_t* stu = new_student("", "", 'E');
		//escrever 20 000 003 vezes o estudante vazio no ficheiro
		for(int i=0; i < table->capacity; i++)
		{
			fwrite(stu, sizeof(student_t), 1, table->htable);
		}
		free(stu);
	}	
}

//Método para inserção de um novo aluno
//Retorna um booleano que indica se foi possível inserir
//Recebe como argumentos o ficheiro correspondente à hashtable e o estudante que se deseja inserir
bool insert_stu(htable_stu_t* table, student_t *stu)
{
	student_t* temp = malloc(sizeof(student_t));
	//col é o inteiro que vai ser incrementado para fazer o controlo de colisões com quadratic probing, pos é o hashcode correspondente ao identificador do estudante
	//index serve para quebrar ciclo caso o quadratic probing volte à posição inicial
	int col = 1, pos = hash(stu->id) % table->capacity, index = pos;
	//posicionar a cabeça de L/E do ficheiro na posiçáo calculada pela função hash, a contar a partir do inicio do ficheiro
	fseek(table->htable, pos*sizeof(student_t), SEEK_SET);
	//leitura de 11 bytes da cabeça de L/E do ficheiro para a variável temp
	fread(temp, sizeof(student_t), 1, table->htable);
	//ciclo para ler estudantes até encontrar uma posição vazia(E) ou removida(R)
	while(temp->estado != 'E')
	{
		
		if(strcmp(temp->id, stu->id) == 0 && temp->estado == 'R')
		{
			fseek(table->htable, pos*sizeof(student_t), SEEK_SET);
			fwrite(stu, sizeof(student_t), 1, table->htable);
			free(temp);
			return true;
		}
		//se encontrar um estudante com id igual ao que se deseja inserir libertar o temp e retornar falso
		if(strcmp(temp->id, stu->id) == 0)
		{
			free(temp);
			return false;
		}
		
		//avançar a posição para a próxima quadraticamente e confirmar se não é igual à inicial, somar o número de colisões
		pos = (pos + (col * col)) % table->capacity;
		if(pos == index)
		{
			free(temp);
			return false;
		}
		col++;
		//posicionar a cabeça de L/E na nova posição e ler o estudante dessa mesmo
		fseek(table->htable, pos*sizeof(student_t), SEEK_SET);
		fread(temp, sizeof(student_t), 1, table->htable);
	}
	//depois de confirmar que não havia estudantes com o mesmo id colocar a cabeça de L/E na posição vazia encontrada e escrever no ficheiro os conteudos do estudante
	fseek(table->htable, pos*sizeof(student_t), SEEK_SET);
	fwrite(stu, sizeof(student_t), 1, table->htable);
	free(temp);
	return true;
}

//Método de remover estudante
//Retorna o país ao qual o estudante removido pertencia ou retorna "ne" no caso do estudante não existir
//Recebe como argumentos o ficheiro correspondente à hashtable e o identificador do estudante a remover
char* remove_stu(htable_stu_t* table, char* id_stu)
{
	student_t* temp = malloc(sizeof(student_t));
	//col é o inteiro que vai ser incrementado para fazer o controlo de colisões com quadratic probing, pos é o hashcode correspondente ao identificador do estudante
	//index serve para quebrar ciclo caso o quadratic probing volte à posição inicial
	int col = 1, pos = hash(id_stu) % table->capacity, index = pos;
	//posicionar a cabeça de L/E do ficheiro na posiçáo calculada pela função hash, a contar a partir do inicio do ficheiro
	fseek(table->htable, pos*sizeof(student_t), SEEK_SET);
	//leitura de 11 bytes da cabeça de L/E do ficheiro para a variável temp
	fread(temp, sizeof(student_t), 1, table->htable);
	//ciclo para procurar estudantes em posições não vazias(E)
	while(temp->estado != 'E')
	{
		//confirmar se o estudante lido coincinde com o que se quer remover
		if(strcmp(temp->id, id_stu) == 0)
		{
			//fazer a verificação do estado (só ativos é que podem ser removidos)
			if(temp->estado == 'A')
			{
				//criação de um estudante com id e pais vazios e no estado assinalar que foi removido
				student_t* removed = new_student(temp->id, "", 'R');
				//posicionamento da cabeça de L/E na posição onde se encontra o estudante a remover e escrever os 11 bytes correspondentes ao estudante vazio
				fseek(table->htable, pos*sizeof(student_t), SEEK_SET);
				fwrite(removed, sizeof(student_t), 1, table->htable);
				//variável para retornar o país do estudante removido para se poder fazer a alteração na hashtable dos países
				char *pais = malloc(sizeof(temp->cod_pais));
				strcpy(pais, temp->cod_pais);
				free(temp);
				free(removed);
				return pais;
			}
			//se o estudante já tiver terminado o curso não remover e retornar "te" para se saber que o estudante terminou
			else if(temp->estado == 'T')
			{
				free(temp);
				return "te";
			}
			//se o estudante tiver abandonado o curso não remover e retornar "ab" para se saber que o estudante abandonou
			else if(temp->estado == 'D')
			{
				free(temp);
				return "ab";
			}
			//se o estudante já tiver removido não remover e retornar "ne" para se saber que o estudante não existe
			else if(temp->estado == 'R')
			{
				free(temp);
				return "ne";
			}
		}
		//avançar a posição para a próxima quadraticamente e confirmar se não é igual à inicial, somar o número de colisões
		pos = (pos + (col * col)) % table->capacity;
		if(pos == index)
		{
			//retornar a string "ne" para indicar que o estudante procurado não está no ficheiro
			free(temp);
			return "ne";
		}
		col++;
		//posicionar a cabeça de L/E na nova posição e ler o estudante dessa mesmo
		fseek(table->htable, pos*sizeof(student_t), SEEK_SET);
		fread(temp, sizeof(student_t), 1, table->htable);
	}
	free(temp);
	//retornar a string "ne" para indicar que o estudante procurado não está no ficheiro
	return "ne";
}

//Método de atualizar estudante, utilizado para alterar o estado em que o estudante se encontra
//Retorna o código referente ao país a que estudante pertence, ou um codigo que indica o estado do estudante:
//	"ne" se o estudante não existir,
//	"te" se existir e tiver terminado,
//	"ab" se existir e tiver abandonado.
char* update_stu(htable_stu_t* table, char* id, char estado)
{
	student_t* temp = malloc(sizeof(student_t));
	//col é o inteiro que vai ser incrementado para fazer o controlo de colisões com quadratic probing, pos é o hashcode correspondente ao identificador do estudante
	//index serve para quebrar ciclo caso o quadratic probing volte à posição inicial
	int col = 1, pos = hash(id) % table->capacity, index = pos;
	//posicionar a cabeça de L/E do ficheiro na posiçáo calculada pela função hash, a contar a partir do inicio do ficheiro
	fseek(table->htable, pos*sizeof(student_t), SEEK_SET);
	//leitura de 11 bytes da cabeça de L/E do ficheiro para a variável temp
	fread(temp, sizeof(student_t), 1, table->htable);
	//ciclo para procurar estudantes em posições não vazias(E)
	while(temp->estado != 'E')
	{
		//caso encontre os estudante com o identificador correto
		if(strcmp(temp->id, id) == 0)
		{
			//confirmar os estados visto que só se pode alterar um estudante que esteja ativo
			if(temp->estado == 'A')
			{
				//alterar o estado do estudante encontrado de 'A' para o que estiver no argumento estado
				temp->estado = estado;
				//posicionamento da cabeça de L/E na posição onde se encontra o estudante a alterar e escrever os 11 bytes correspondentes ao estudante alterado
				fseek(table->htable, pos*sizeof(student_t), SEEK_SET);
				fwrite(temp, sizeof(student_t), 1, table->htable);
				//variável para retornar o país do estudante removido para se poder fazer a alteração na hashtable dos países
				char *pais = malloc(sizeof(temp->cod_pais));
				strcpy(pais, temp->cod_pais);
				free(temp);
				return pais;
			}
			//se o estudante já tiver terminado o curso não alterar e retornar "te" para se saber que o estudante terminou
			else if(temp->estado == 'T')
			{
				free(temp);
				return "te";
			}
			//se o estudante já tiver abandonado o curso não alterar e retornar "ab" para se saber que o estudante abandonou
			else if(temp->estado == 'D')
			{
				free(temp);
				return "ab";
			}
			//se o estudante já tiver removido não alterar e retornar "ne" para se saber que o estudante não existe
			else if(temp->estado == 'R')
			{
				free(temp);
				return "ne";
			}
		}
		//avançar a posição para a próxima quadraticamente e confirmar se não é igual à inicial, somar o número de colisões
		pos = (pos + (col * col)) % table->capacity;
		if(pos == index)
		{
			//retornar a string "ne" para indicar que o estudante procurado não está no ficheiro
			free(temp);
			return "ne";
		}
		col++;
		//posicionar a cabeça de L/E na nova posição e ler o estudante dessa mesmo
		fseek(table->htable, pos*sizeof(student_t), SEEK_SET);
		fread(temp, sizeof(student_t), 1, table->htable);
	}
	free(temp);
	//retornar a string "ne" para indicar que o estudante procurado não está no ficheiro
	return "ne";
}
