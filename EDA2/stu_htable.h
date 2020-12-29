#include <stdio.h>
#include <string.h>
#include <stdbool.h>
#include <stdlib.h>

#define ID_STU 7
#define ID_PAIS 3
#define HTABLE_CAP_STU 20000003
#define STUDENTS "students.txt"

//Definição de estudante
//	id - identificador unico de cada estudante
//	cod_pais - identificador unico do país ao qual o estudante pertence
//	estado - estado do estudante: A = ativo, T = termino, D = abandono, R = removido, E = vazio
typedef struct
{
	char id[ID_STU];
	char cod_pais[ID_PAIS];
	char estado;
}student_t;

typedef struct
{
	int capacity;
	FILE* htable;
}htable_stu_t;

htable_stu_t* new_stu_htable(int capacity);
student_t* new_student(char* id, char* cod_pais, char estado);
void free_htable_stu(htable_stu_t* table);
void start_stu_htable(htable_stu_t* table);
bool insert_stu(htable_stu_t* table, student_t *stu);
char* remove_stu(htable_stu_t* table, char* id_stu);
char* update_stu(htable_stu_t* table, char* id, char estado);
