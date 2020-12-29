#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "paises.h"
#include "stu_htable.h"

//Método que trata a introdução de um novo estudante
//Argumentos:
//	id - Identificador do Estudante
//	pais - Código de 2 letras que identifica o país
//	stu_htable - Ficheiro onde se encontra a Hashtable dos estudantes
//	pais_htable - Hash table referente aos países
void introduzir(char *id, char *cod_pais, htable_stu_t* stu_htable, htable_pais_t* pais_htable)
{
	student_t *temp_stu = new_student(id, cod_pais, 'A');
	bool entrou = insert_stu(stu_htable, temp_stu);
	if(entrou)
	{
		pais_t* temp_pais = search(pais_htable, cod_pais);
		if(temp_pais == NULL)
		{
			insert_pais(pais_htable, cod_pais, 1, 0, 0);
		}
		else
			temp_pais->a++;
	}
	else
	{
		printf("+ estudante %s existe\n",id);
	}
	free(temp_stu);
}

//Método de remoção de um estudante
//Argumentos:
//	id - identificador do Estudante
//	stu_htable - Ficheiro onde se encontra a Hashtable dos estudantes
//	pais_htable - Hashtable referente aos países
void remover(char* id, htable_stu_t* stu_htable, htable_pais_t* pais_htable)
{
	char* cod_pais = remove_stu(stu_htable, id);
	if(strcmp(cod_pais, "ne") == 0)
		printf("+ estudante %s inexistente\n", id);
	else if(strcmp(cod_pais, "te") == 0)
		printf("+ estudante %s terminou\n", id);
	else if(strcmp(cod_pais, "ab") == 0)
		printf("+ estudante %s abandonou\n", id);
	else
	{
		pais_t* temp_pais = search(pais_htable, cod_pais);
		temp_pais->a--;
		if(temp_pais->a + temp_pais->t + temp_pais->d == 0)
			remove_pais(pais_htable, cod_pais);
		free(cod_pais);
	}
}

//Método de assinalar o abandono de um estudante
//Argumentos:
//	id - identificador do Estudante
//	stu_htable - Ficheiro onde se encontra a Hashtable dos estudantes
//	pais_htable - Hashtable referente aos países
void abandono(char* id, htable_stu_t* stu_htable, htable_pais_t* pais_htable)
{
	char* cod_pais = update_stu(stu_htable, id, 'D');
	if(strcmp(cod_pais, "ne") == 0)
		printf("+ estudante %s inexistente\n", id);
	else if(strcmp(cod_pais, "te") == 0)
		printf("+ estudante %s terminou\n", id);
	else if(strcmp(cod_pais, "ab") == 0)
		printf("+ estudante %s abandonou\n", id);
	else
	{
		pais_t* temp_pais = search(pais_htable, cod_pais);
		temp_pais->a--;
		temp_pais->d++;
		free(cod_pais);
	}
}

//Método de assinalar o termino de um estudante
//Argumentos:
//	id - identificador do Estudante
//	stu_htable - Ficheiro onde se encontra a Hashtable dos estudantes
//	pais_htable - Hashtable referente aos países
void termino(char* id, htable_stu_t* stu_htable, htable_pais_t* pais_htable)
{
	char* cod_pais = update_stu(stu_htable, id, 'T');
	if(strcmp(cod_pais, "ne") == 0)
		printf("+ estudante %s inexistente\n", id);
	else if(strcmp(cod_pais, "te") == 0)
		printf("+ estudante %s terminou\n", id);
	else if(strcmp(cod_pais, "ab") == 0)
		printf("+ estudante %s abandonou\n", id);
	else
	{
		pais_t* temp_pais = search(pais_htable, cod_pais);
		temp_pais->a--;
		temp_pais->t++;
		free(cod_pais);
	}
}

//procedimento para consultar o estado atual de um certo país
//recebe a hashtable dos países e o código do país do qual se quer informação
//pais_htable - hashtable referente aos países
//cod_pais - identificador do país a pesquisar
void consultar_pais(htable_pais_t* pais_htable, char* cod_pais )
{
	pais_t* temp_pais = search(pais_htable, cod_pais);
	if(temp_pais != NULL)
		printf("+ %s - correntes: %d, diplomados: %d, abandonaram: %d, total: %d\n", temp_pais->id, temp_pais->a, temp_pais->t, temp_pais->d, temp_pais->a+temp_pais->t+temp_pais->d);
	else
		printf("+ sem dados sobre %s\n", cod_pais);
}

int main()
{
	//Inicialização das hashtables
	htable_stu_t* stu_htable = new_stu_htable(HTABLE_CAP_STU);
	htable_pais_t* pais_htable = new_htable_pais(HTABLE_CAP_PAIS);

	char operation;
	char* pais = malloc(ID_PAIS);
	char* id = malloc(ID_STU);
	
	while(scanf(" %c", &operation)!=EOF)
	{
		switch(operation)
		{
			//Caso seja feita a introdução de um aluno
			case 'I':
				//leitura do código do aluno e do país
				scanf(" %s %s", id, pais);
				//Inserção do aluno no ficheiro e atualização dos dados do país referente 
				introduzir(id, pais, stu_htable, pais_htable);
				break;
			//Caso seja feita a remoção de um aluno
			case 'R':
				//leitura do código de aluno
				scanf(" %s", id);
				//Remoção do aluno do ficheiro e ataulizção dos dados do país referente
				remover(id, stu_htable, pais_htable);
				break;
			//Caso seja assinalado o termino de um aluno
			case 'T':
				//leitura do código do aluno
				scanf(" %s", id);
				//Atualização do aluno para terminado no ficheiro e ataulizção dos dados do país referente
				termino(id, stu_htable, pais_htable);
				break;
			//Caso seja assinalado o abandono de um aluno
			case 'A':
				//leitura do código do aluno
				scanf(" %s", id);
				//Atualização do aluno para abandono no ficheiro e ataulizção dos dados do país referente
				abandono(id, stu_htable, pais_htable);
				break;
			//Caso queremos verificar os dados de um país
			case 'P':
				//leitura do código do país
				scanf(" %s", pais);
				//Verificar na hash pelo país
				consultar_pais(pais_htable, pais);
				break;
			//Caso cheguemos ao fim da execução
			case 'X':
				//Libertação total da memoria principal apos o ficheiro dos países se atualizado
				free(id);
				free(pais);
				write_to_file(pais_htable);
				free_htable_pais(pais_htable);
				free_htable_stu(stu_htable);
				exit(0);
		}
	}
}
