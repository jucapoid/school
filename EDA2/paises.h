#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <stdbool.h>

#define HTABLE_CAP_PAIS 1361
#define L_ID_P 3
#define PAISES "paises.txt"

//struct para repesentar cada País
//	id - Código de 2 caracteres que identificam o país
//	a - Número de estudantes ativos
//	t - Número de estudantes que já terminaram o curso
//	d - Número de estudantes que abandonaram o curso
//	next - apontador para lidar com colisões
typedef struct pais
{
	char id[L_ID_P];
	int a, t, d;
	struct pais* next;
}pais_t;

//struct que guarda a hashtable
//	size - número de posições ocupadas
//	capacity - número de posicões total
//	paises - array com elementos do tipo pais
typedef struct
{
	int capacity;
	pais_t **paises;
}htable_pais_t;

pais_t* new_pais(char* id, int a, int t, int d);
htable_pais_t* new_htable_pais(int capacity);
pais_t* free_pais(pais_t* pais);
void free_htable_pais(htable_pais_t* table);
void insert_pais(htable_pais_t* table, char* id, int a, int t, int d);
void remove_pais(htable_pais_t* table, char* id);
pais_t* search(htable_pais_t* table, char* id);
void update(htable_pais_t* table, char* id, char escolha, int valor);
void check_file(htable_pais_t* table);
void write_to_file(htable_pais_t* table);
