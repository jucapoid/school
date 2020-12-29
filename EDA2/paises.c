#include "paises.h"
#include "hash.h"

//função para criar um novo pais 
//recebe os dados do pais e retorna a struct atualizada
//id - char com o identificador do pais
//a - valor de estudantes ativos,
//t - valor de estudantes que terminaram,
//d - valor de estudantes que abandonaram,
pais_t* new_pais(char* id, int a, int t, int d)
{
	pais_t* temp = malloc(sizeof(pais_t));
	memset(temp, 0, sizeof(pais_t));
	strcpy(temp->id, id);
	temp->a = a;
	temp->t = t;
	temp->d = d;
	temp->next = NULL;
	return temp;
}

//função que cria e inicializa  a hastable dos paises
//recebe a capacidade maxima da hash e retorna a hastable
//capacity - capacidade da hastable
htable_pais_t *new_htable_pais(int capacity)
{
	//criação do ponteiro para a hashtable e alocação do espaço necessário
	htable_pais_t *temp = malloc(sizeof(htable_pais_t));
	//afetação da variável capacidade utilizada sempre que for necessário fazer o cálculo de um hashcode
	temp->capacity = capacity;
	//definição do array que guardará todas as posições da hashtable
	temp->paises = malloc(capacity * sizeof(pais_t));
	//inicializaçáo de cada posição a NULL
	for(int i = 0; i < capacity; i++)
	{
		temp->paises[i] = NULL;
	}
	//chamar a função que lê o ficheiro e introduz na hashtable todos os países anteriormente existentes
	check_file(temp);

	return temp;
}

//função utilizada na remoção de um país
//recebe o país a eliminar e retorna o país que estava na próxima posição da linked list das colisões
//ou retorna NULL caso não esteja nenhum país na próxima
pais_t* free_pais(pais_t* pais)
{
	pais_t* temp;
	temp = NULL;
	if(pais->next != NULL)
		temp = pais->next;
	free(pais);
	return temp;
}

//procedimento que inicializa o procedimento para atualizar o ficheiro com a mais recente hashtable
// e liberta a memoria da hastable apos esta ser atualizada no ficheiro
//table - hashtable referente aos países
void free_htable_pais(htable_pais_t *table)
{
	//chamada do procedimento encarregue de fazer dump da hashtable para o ficheiro
	write_to_file(table);
	
	for(int i=0; i<table->capacity; i++)
	{
		pais_t* cur = table->paises[i];
		if(cur != NULL)
		{
			while(cur->next != NULL)
			{
				pais_t* before = cur;
				cur = cur->next;
				free(before);
			}
			free(cur);
		}
	}
	free(table->paises);
	free(table);
}

//procedimento para inserir um pais novo na hastable
//recebe um pointer da hashtable e os dados do novo pais 
//table - hashtable referente aos países
//id - char com o identificador do pais
//a - valor de estudantes ativos,
//t - valor de estudantes que terminaram,
//d - valor de estudantes que abandonaram,
void insert_pais(htable_pais_t *table, char *id, int a, int t, int d)
{
	//afetação de um novo pais com os dado passados no argumento
	pais_t* temp = new_pais(id, a, t, d);
	//cálculo do hashcode correspondente ao id
	int i = hash(id) % table->capacity;

	pais_t* cur = table->paises[i];
	//caso a posição da hashtable não esteja preenchida, preencher com temp
	if(cur == NULL)
	{
		table->paises[i] = temp;
	}
	//se a primeira estiver preenchida iterar na lista até encontrar uma livre
	else
	{
		//iteração pela lista até que a próxima esteja vazia
		while(cur->next != NULL)
		{
			cur = cur->next;
		}
		//afetação da posição livre encontrada com temp
		cur->next = temp;
	}
}

//procedimento para remover um país da hashtable
//table - hashtable referente aos países
//id - identificador do país que se deseja remover
void remove_pais(htable_pais_t* table, char* id)
{
	//cálculo do hashcode correspondente ao id
	int i = hash(id) % table->capacity;

	pais_t* cur = table->paises[i];
	//caso o país seja encontrado na posição indicada da hashtable sem estar na lista de controlo de colisóes
	if(strcmp(cur->id, id) == 0)
	{
		//table->paises[i] fica a NULL se não existir nada na lista para colisões
		//ou passa a ser o país que estava na próxima posição da lista caso esta exista
		table->paises[i] = free_pais(table->paises[i]);
	}
	//passar a procurar na lista de colisões visto que não estava na cabeça
	else
	{
		//ciclo para iterar a linked list
		while(cur->next != NULL)
		{
			//se o país desejado estiver na próxima posição
			if(strcmp(cur->next->id, id) == 0)
			{
				//libertar a próxima posição e afetar o país (ou NULL) que estiver na próxima da próxima
				cur->next = free_pais(cur->next);
				return;
			}
			cur = cur->next;
		}
	}
}

//Função que retorna o pais que procuramos
//Recebe a hastable dos paises e o id do pais que pretendemos pesquisar
//table - hashtable referente aos países
//id - char com o identificador do pais
pais_t* search(htable_pais_t *table, char *id)
{
	//cálculo do hashcode correspondente ao id
	int i = hash(id) % table->capacity;

	pais_t* cur = table->paises[i];
	//caso a posição da hashtable estaja vazia retornar NULL
	if(cur == NULL)
	{
		return NULL;
	}
	//caso o país procurado seja o que se encontra na hashtable sem ser preciso iterar a lista de colisões
	else if(strcmp(cur->id, id) == 0)
	{
		return cur;
	}
	//procurar na lista de colisões
	else
	{
		//ciclo para iterar a lista
		while(cur->next != NULL)
		{
			cur = cur->next;
			//país encontrado na posição corrente retorno da mesma
			if(strcmp(cur->id, id) == 0)
			{
				return cur;
			}
		}
	}
	//se passou a lista até ao final e não encontrou retorna NULL
	return NULL;
}

//procedimento para atualizar a hastable no inicio do programa com os dados atuais dos paises no ficehiro paises.txt
//recebe um pointer da hashtable como argumento, abre o ficheiro e corre o ficheiro guardando os dados de cada pais na hastable
//table - hashtable referente aos países
void check_file(htable_pais_t* table)
{
	//abertura do ficheiro apenas em leitura
	FILE* fd = fopen(PAISES, "r");
	//pais temporário para armazenar cada país lido do ficheiro
	pais_t* temp = malloc(sizeof(pais_t));
	//se o ficheiro existir
	if(fd != NULL)
	{
		//ciclo que corre enquanto houver possibilidade de leitura de 24B
		while(fread(temp, sizeof(pais_t), 1, fd))
		{
			//inserção na hashtable o país lido do ficheiro
			insert_pais(table, temp->id, temp->a, temp->t, temp->d);
		}
		fclose(fd);
	}
	free(temp);
}


//procedimento para escrever os dados da hashtable no ficheiro .txt dos paises
//Recebe a hastable como argumento e escreve os dados dos paises no ficheiro
//table - hashtable referente aos países
void write_to_file(htable_pais_t* table)
{
	//abertura do ficheiro em modo de escrita (sobrepõe se já existir)
	FILE* fd = fopen(PAISES, "w");
	//confirmar que foi possível abrir o ficheiro
	if(fd == NULL)
	{
		perror("Erro: Impossível criar ficheiro");
		exit(0);
	}
	//correr a hashtable do inicio ao fim
	for(int i = 0; i < table->capacity; i++)
	{
		pais_t* cur = table->paises[i];
		//sempre que se encontrar um país escrever esse país e percorrer a lista de colisões escrevendo os países à medida que os encontra
		if(cur != NULL)
		{
			fwrite(cur, sizeof(pais_t), 1, fd);
			while(cur->next != NULL)
			{
				cur = cur->next;
				fwrite(cur, sizeof(pais_t), 1, fd);
			}
		}
	}
	fclose(fd);
}
