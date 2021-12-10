#include <stdio.h>
#include <string.h>
#include <stdlib.h>

#include "queue.h"

#define MEMSIZE 200
#define Q_SIZE 10
#define QUANTUM 3


typedef struct processo {
	/**id do processo*/
	int pid;
	/**Momento de entrada no processo*/
	int entry;
	/**Indicador da instrução a ler*/
	int count;
	/**Número de instruções neste processo*/
	int num_inst;
	/**posição no array mem onde está a primeira instrução do processo*/
	int local_i;
	/**posição no array mem onde está a primeira variável do processo*/
	int local_v;
	int estado;
	int *instruction;
	/**Quantidade de var_Xs que este processo precisa*/
	int n_vars;
}processo;

/**Construtor para a estrutura processo*/
processo * novo_processo(int pid, int entry, int num_inst){
	processo *proc = malloc(sizeof(processo));
	proc->pid=pid;
	proc->entry=entry;
	proc->num_inst=num_inst;
	proc->estado=0;
	proc->count=0;
	proc->instruction = malloc(num_inst*2*sizeof(int));
	return proc;
}

/**Recebe o nome do ficheiro de input 
 * Altera os valores das variáveis passadas como argumento num_proc e num_inst
 * Retorna a informação presente no ficheiro em forma de lista de processos*/
processo **ler_ficheiro(char *file_name, int *num_proc, int *num_inst) {
	int entry;
	char *l = NULL;
	size_t len = 0;
	char instr[4];
	int valor, max=0;

	FILE *file = fopen(file_name, "r");
	while(getline(&l, &len, file) != -1) {
		sscanf(l,"%s %d", instr, &valor);
		if(strcmp(instr, "INI")==0) {
			(*num_proc)++;
		}
	}
	processo **procs = malloc(*num_proc*sizeof(processo));

	*num_proc=-1;
	rewind(file);
	while(getline(&l, &len, file) != -1) {
		sscanf(l,"%s %d", instr, &valor);
		if(strcmp(instr, "INI")==0) {
			if(*num_inst!=0){
				procs[*num_proc] = novo_processo(*num_proc, entry, *num_inst);
			}

			*num_inst=0;
			(*num_proc)++;
			entry = valor;
		} else {
			(*num_inst)++;
		}
	}
	procs[*num_proc] = novo_processo(*num_proc, entry, *num_inst);
	rewind(file);
	int j=-1;
	int i;
	while(getline(&l, &len, file) != -1) {
		sscanf(l,"%s %d", instr, &valor);
		if(strcmp(instr, "INI")==0) {
			i = 0;
			j++;
			if(max>=0){
				procs[j]->n_vars = max+1;
				max=0;
			}
		} else {
		       	if (strcmp(instr, "ZER") == 0) {
				procs[j]->instruction[i] = 0;
				i++;
			} else if (strcmp(instr, "CPY") == 0) {
				if(valor>max){
					max=valor;
				}
				procs[j]->instruction[i] = 1;
				i++;
			} else if (strcmp(instr, "DEC") == 0) {
				procs[j]->instruction[i] = 2;
				i++;
			} else if (strcmp(instr, "FRK") == 0) {
				procs[j]->instruction[i] = 3;
				i++;
			} else if (strcmp(instr, "JFW") == 0) {
				procs[j]->instruction[i] = 4;
				i++;
			} else if (strcmp(instr, "JBK") == 0) {
				procs[j]->instruction[i] = 5;
				i++;
			} else if (strcmp(instr, "DSK") == 0) {
				procs[j]->instruction[i] = 6;
				i++;
			} else if (strcmp(instr, "JIZ") == 0) {
				procs[j]->instruction[i] = 7;
				i++;
			} else if (strcmp(instr, "PRT") == 0) {
				procs[j]->instruction[i] = 8;
				i++;
			} else if (strcmp(instr, "HLT") == 0) {
				procs[j]->instruction[i] = 9;
				i++;
			}
			procs[j]->instruction[i] = valor;
			i++;
		}
	}
	return procs;
}

/**Imprime uma lista de processos*/
void print_procs(processo **procs, int size){
	for(int i=0; i<=size;i++){
		printf("Processo %d:\nM_entrada - %d\nN_vars - %d\nN_instr - %d\nInstr - { ",procs[i]->pid, procs[i]->entry, procs[i]->n_vars, procs[i]->num_inst);
		for(int j=0; j<procs[i]->num_inst*2; j++){
			printf("%d ",procs[i]->instruction[j]);
		}
		printf("}\n\n");
	}

}

/**Libertar espaço alocado pela lista de processos*/
void free_procs(processo **procs, int num_proc){
	for(int i=0; i<num_proc; i++){
		free(procs[i]->instruction);
		free(procs[i]);
	}
	free(procs);
}

int *inicializar_mem() {
	int *mem = malloc(MEMSIZE*sizeof(int));
	for(int i=0; i<MEMSIZE; i++) {
		mem[i] = -1;
	}
	return mem;
}

bool first_fit(int *mem, processo *proc){
	bool var=false,instr=false;
	for(int i=0;i<MEMSIZE;i++){
		if(var && instr){
			return true;
		}
		if(!instr){
			if(mem[i]==-1){
				for(int j=0;j<proc->num_inst*2-1;j++){
					if(mem[i+j]!=-1){
						break;
					} else if (j==proc->num_inst*2-2) {
						proc->local_i=i;
						instr=true;
						if(i+j+2>=MEMSIZE){
							i=0;
						} else {
							i=i+j+2;
						}
					}
				}
			}
		}
		if(!var){
			if(mem[i]==-1){
				for(int j=0;j<proc->n_vars;j++){
					if(mem[i+j]!=-1){
						break;
					} else if (j==proc->n_vars-1) {
						proc->local_v=i;
						var=true;
						if(i+j+1>=MEMSIZE){
							i=0;
						} else {
							i=i+j+1;
						}
					}
				}
			}
		}
	}
	return false;
}

void put_mem(int *mem, processo *proc){
	int n_i=proc->num_inst*2;

	for(int i=proc->local_i;i<proc->local_i+n_i;i++){
		mem[i]=proc->instruction[proc->count];
		proc->count++;
	}
	proc->count=0;
	for(int i=proc->local_v;i<proc->local_v+proc->n_vars;i++){
		mem[i]=-2;
	}
}

void out_mem(int *mem, processo *proc){
	int n_i=proc->num_inst*2;
	for(int i=proc->local_i;i<proc->local_i+n_i;i++){
		mem[i]=-1;
	}
	proc->local_i=-1;
	for(int i=proc->local_v;i<proc->local_v+proc->n_vars;i++){
		mem[i]=-1;
	}
	proc->local_v=-1;
}

void print_mem(int *mem){
	int s=0,e=0;
	for(int i=0;i<MEMSIZE;i++){
		if(mem[i]!=-1){
			e++;
			if(i+1==MEMSIZE || mem[i+1]==-1){
				printf("%d-%d ocupados\n", s,e-1);
				s=e;
			}
		}
		if(mem[i]==-1){
			e++;
			if(i+1==MEMSIZE || mem[i+1]!=-1){
				printf("%d-%d desocupados\n", s,e-1);
				s=e;
			}
		}
	}
	printf("\n");
}

/*void run_processes(int *mem, processo **procs,int num_proc){
	Queue *new = new_queue(Q_SIZE);
	Queue *ready = new_queue(Q_SIZE);
	Queue *run = new_queue(1);
	Queue *blocked = new_queue(Q_SIZE);
	Queue *exit = new_queue(Q_SIZE);
	int inst=0;
	int time=0;
	int p=0;
	bool r=true,b=true;
	bool sinal_r=false,sinal_b=false;

	while(time < 100){
		for(int i=0;i<num_proc;i++){
			if(procs[i]->estado==0 && time==procs[i]->entry){
				if(first_fit(mem, procs[i])){
					enqueue(new,procs[i]->pid);
					put_mem(mem, procs[i]);
					procs[i]->estado=1;
				}
	    		}
	    		else if((procs[i]->estado==1 && r==true)||procs[i]->estado==2){
				if(new->array[new->head]==procs[i]->pid){
					p=dequeue(new);
					enqueue(run,p);
				}
				else if(ready->array[ready->head]==procs[i]->pid){
					p=dequeue(ready);
					enqueue(run,p);
				}
				else if(blocked->array[blocked->head]==procs[i]->pid){
					p=dequeue(blocked);
					enqueue(run,p);
				}
				procs[i]->estado=2;
				r=false;
				inst++;
				else if(inst<QUANTUM){
					//resolve instrucao
					inst++;
					procs[i]->count+=2;
				} else {
					sinal_r=true;
					p=dequeue(run);
					enqueue(ready,p);
					procs[i]->estado=1;
					procs[i]->count+=2;
				}
	    		} else if(procs[i]->estado==1){
				if(new->array[new->head]==procs[i]->pid){
					p=dequeue(new);
					enqueue(ready,p);
				}
	    		}
	    		else if(procs[i]->estado==4){
				enqueue(exit,procs[i]->pid);
	    		}
	    		else if(procs[i]->estado==3){
				if(b==true && blocked->array[blocked->head]==procs[i]->pid){
					b=false;
					sinal_b=true;
					p=dequeue(blocked);
					enqueue(ready,p);
					procs[i]->estado=1;
				}
	    		}
		}
		if(sinal_b){
			b=true;
			sinal_b=false;
		}
		if(sinal_r){
			r=true;
			sinal_r=false;
		}
		time++;
		printf("NEW:	");
		queue_print(new);
		printf("	READY:	");
		queue_print(ready);
		printf("	RUN		");
		queue_print(run);
		printf("	BLOCKED		");
		queue_print(blocked);
		printf("	EXIT	");
		queue_print(exit);
		printf("\n");
	}
}*/

int main(int argc, char *argv[]){
	int num_proc=0, num_inst=0;
	processo **procs = ler_ficheiro("input.txt", &num_proc, &num_inst);
	int *MEM = inicializar_mem();
	print_procs(procs,num_proc);
	printf("Num procs %d\n\n", num_proc);

	printf("Memória inicialmente\n");
	print_mem(MEM);

	printf("Colocar procs[0] na memória\n");
	if(first_fit(MEM, procs[0])){
		put_mem(MEM, procs[0]);
	}
	printf("instruções p[0] a começar em mem[%d] e terminar em mem[%d]\n",procs[0]->local_i,procs[0]->local_i+procs[0]->num_inst*2-1);
	printf("variáveis p[0] a começar em mem[%d] e terminar em mem[%d]\n",procs[0]->local_v,procs[0]->local_v+procs[0]->n_vars-1);
	print_mem(MEM);
	
	printf("Colocar procs[1] na memória\n");
	if(first_fit(MEM,procs[1])){
		put_mem(MEM, procs[1]);
	}
	printf("instruções p[1] a começar em mem[%d] e terminar em mem[%d]\n",procs[1]->local_i,procs[1]->local_i+procs[1]->num_inst*2-1);
	printf("variáveis p[1] a começar em mem[%d] e terminar em mem[%d]\n",procs[1]->local_v,procs[1]->local_v+procs[1]->n_vars-1);
	print_mem(MEM);

	printf("Retirar procs[0] da memória\n");
	out_mem(MEM, procs[0]);
	print_mem(MEM);

	printf("Colocar procs[2] na memória\n");
	if(first_fit(MEM, procs[2])){
		put_mem(MEM, procs[2]);
	}
	printf("instruções p[2] a começar em mem[%d] e terminar em mem[%d]\n",procs[2]->local_i,procs[2]->local_i+procs[2]->num_inst*2-1);
	printf("variáveis p[2] a começar em mem[%d] e terminar em mem[%d]\n",procs[2]->local_v,procs[2]->local_v+procs[2]->n_vars-1);
	print_mem(MEM);

	printf("Colocar procs[3] na memória\n");
	if(first_fit(MEM, procs[3])){
		put_mem(MEM, procs[3]);
	}
	printf("instruções p[3] a começar em mem[%d] e terminar em mem[%d]\n",procs[3]->local_i,procs[3]->local_i+procs[3]->num_inst*2-1);
	printf("variáveis p[3] a começar em mem[%d] e terminar em mem[%d]\n",procs[3]->local_v,procs[3]->local_v+procs[3]->n_vars-1);
	print_mem(MEM);

	printf("Colocar procs[4] na memória\n");
	if(first_fit(MEM, procs[4])){
		put_mem(MEM, procs[4]);
	}
	printf("instruções p[4] a começar em mem[%d] e terminar em mem[%d]\n",procs[4]->local_i,procs[4]->local_i+procs[4]->num_inst*2-1);
	printf("variáveis p[4] a começar em mem[%d] e terminar em mem[%d]\n",procs[4]->local_v,procs[4]->local_v+procs[4]->n_vars-1);
	print_mem(MEM);

	printf("Colocar procs[5] na memória\n");
	if(first_fit(MEM, procs[5])){
		put_mem(MEM, procs[5]);
	}
	printf("instruções p[5] a começar em mem[%d] e terminar em mem[%d]\n",procs[5]->local_i,procs[5]->local_i+procs[5]->num_inst*2-1);
	printf("variáveis p[5] a começar em mem[%d] e terminar em mem[%d]\n",procs[5]->local_v,procs[5]->local_v+procs[5]->n_vars-1);
	print_mem(MEM);

	printf("Colocar procs[6] na memória\n");
	if(first_fit(MEM, procs[6])){
		put_mem(MEM, procs[6]);
	}
	printf("instruções p[6] a começar em mem[%d] e terminar em mem[%d]\n",procs[6]->local_i,procs[6]->local_i+procs[6]->num_inst*2-1);
	printf("variáveis p[6] a começar em mem[%d] e terminar em mem[%d]\n",procs[6]->local_v,procs[6]->local_v+procs[6]->n_vars-1);
	print_mem(MEM);

	printf("Colocar procs[7] na memória\n");
	if(first_fit(MEM, procs[7])){
		put_mem(MEM, procs[7]);
	}
	printf("instruções p[7] a começar em mem[%d] e terminar em mem[%d]\n",procs[7]->local_i,procs[7]->local_i+procs[7]->num_inst*2-1);
	printf("variáveis p[7] a começar em mem[%d] e terminar em mem[%d]\n",procs[7]->local_v,procs[7]->local_v+procs[7]->n_vars-1);
	print_mem(MEM);

	printf("Introduzir o proc[0]\n");
	if(first_fit(MEM, procs[0])){
		put_mem(MEM, procs[0]);
	}
	printf("instruções p[0] a começar em mem[%d] e terminar em mem[%d]\n",procs[0]->local_i,procs[0]->local_i+procs[0]->num_inst*2-1);
	printf("variáveis p[0] a começar em mem[%d] e terminar em mem[%d]\n",procs[0]->local_v,procs[0]->local_v+procs[0]->n_vars-1);
	print_mem(MEM);

	return 0;
}
